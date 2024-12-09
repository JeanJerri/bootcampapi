package br.com.squadra.bootcampapi.service;

import br.com.squadra.bootcampapi.controller.dto.BairroDTO;
import br.com.squadra.bootcampapi.controller.mapper.BairroMapper;
import br.com.squadra.bootcampapi.model.Bairro;
import br.com.squadra.bootcampapi.model.Municipio;
import br.com.squadra.bootcampapi.repository.BairroRepository;
import br.com.squadra.bootcampapi.repository.MunicipioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BairroService {

    @Autowired
    private BairroRepository bairroRepository;

    @Autowired
    private MunicipioRepository municipioRepository;

    public Bairro salvar(BairroDTO bairroDto){

        //Validações da presença dos campos
        if (bairroDto.codigoMunicipio() == null) {
            throw new IllegalArgumentException("Não foi possível salvar o Bairro no banco de dados. Motivo: o campo codigoMunicipio é obrigatório.");
        }
        if (bairroDto.nome() == null || bairroDto.nome().isEmpty()) {
            throw new IllegalArgumentException("Não foi possível salvar o Bairro no banco de dados. Motivo: o campo nome é obrigatório.");
        }
        if (bairroDto.status() == null) {
            throw new IllegalArgumentException("Não foi possível salvar o Bairro no banco de dados. Motivo: o campo status é obrigatório.");
        }


        //Validações dos valores dos campos
        if (!(String.valueOf(bairroDto.codigoMunicipio()).matches("^[0-9]+$"))) {
            throw new IllegalArgumentException("O campo codigoUF deve conter apenas números inteiros positivos.");
        }
        if (!(bairroDto.nome().matches("^[A-ZÀ-Ÿ0-9 ]+$")) || bairroDto.nome().trim().isEmpty()) {
            throw new IllegalArgumentException("O campo nome deve conter apenas letras em maiúsculo e números.");
        }
        if (bairroDto.status() != 1 && bairroDto.status() != 2) {
            throw new IllegalArgumentException("O campo status deve ser informado apenas os número 1 (ativado) ou 2 (desativado).");
        }


        //Validações do tamanho dos valores dos campos
        if (bairroDto.codigoMunicipio() > 999999999) {
            throw new IllegalArgumentException("O campo codigoMunicipio deve ter no máximo 9 dígitos");
        }
        if (bairroDto.nome().length() > 256) {
            throw new IllegalArgumentException("O campo nome deve conter no máximo 256 letras");
        }


        //Validações de existência
        Municipio municipio = municipioRepository.findByCodigoMunicipio(bairroDto.codigoMunicipio());
        if (municipio == null) {
            throw new IllegalArgumentException("Não foi possível salvar o Bairro no banco de dados. Motivo: o Município não está cadastrado.");
        }


        //Validações de duplicidade
        List<Bairro> bairroRepetido = bairroRepository.findByMunicipioAndNome(municipio, bairroDto.nome());
        if (!bairroRepetido.isEmpty()) {
            throw new IllegalArgumentException("Não foi possível salvar o Bairro no banco de dados. Motivo: o Nome e o Município já existe em um outro registro cadastro no banco de dados.");
        }


        Bairro bairro = new Bairro();
        bairro.setMunicipio(municipio);
        bairro.setNome(bairroDto.nome());
        bairro.setStatus(bairroDto.status());

        return bairroRepository.save(bairro);
    }

    public Bairro alterar(BairroDTO novoBairro) {

        //Validações da presença dos campos
        if (novoBairro.codigoBairro() == null) {
            throw new IllegalArgumentException("Não foi possível alterar o Bairro no banco de dados. Motivo: o campo codigoBairro é obrigatório.");
        }
        if (novoBairro.codigoMunicipio() == null) {
            throw new IllegalArgumentException("Não foi possível alterar o Bairro no banco de dados. Motivo: o campo codigoMunicipio é obrigatório.");
        }
        if (novoBairro.status() == null) {
            throw new IllegalArgumentException("Não foi possível alterar o Bairro no banco de dados. Motivo: o campo status é obrigatório.");
        }
        if (novoBairro.nome() == null || novoBairro.nome().isEmpty()) {
            throw new IllegalArgumentException("Não foi possível alterar o Bairro no banco de dados. Motivo: o campo nome é obrigatório.");
        }


        //Validações dos valores dos campos
        if (!(String.valueOf(novoBairro.codigoBairro()).matches("^[0-9]+$"))) {
            throw new IllegalArgumentException("O campo codigoBairro deve conter apenas números inteiros positivos.");
        }
        if (!(String.valueOf(novoBairro.codigoMunicipio()).matches("^[0-9]+$"))) {
            throw new IllegalArgumentException("O campo codigoMunicipio deve conter apenas números inteiros positivos.");
        }
        if (!(novoBairro.nome().matches("^[A-ZÀ-Ÿ0-9 ]+$")) || novoBairro.nome().trim().isEmpty()) {
            throw new IllegalArgumentException("O campo nome deve conter apenas letras em maiúsculo e números.");
        }
        if (novoBairro.status() != 1 && novoBairro.status() != 2) {
            throw new IllegalArgumentException("O campo status deve ser informado apenas os número 1 (ativado) ou 2 (desativado).");
        }


        //Validações do tamanho dos valores dos campos
        if (novoBairro.codigoBairro() > 999999999) {
            throw new IllegalArgumentException("O campo codigoBairro deve ter no máximo 9 dígitos");
        }
        if (novoBairro.codigoMunicipio() > 999999999) {
            throw new IllegalArgumentException("O campo codigoMunicipio deve ter no máximo 9 dígitos");
        }
        if (novoBairro.nome().length() > 256) {
            throw new IllegalArgumentException("O campo nome deve conter no máximo 256 letras");
        }


        //Validações de existência
        Bairro antigoBairro = bairroRepository.findByCodigoBairro(novoBairro.codigoBairro());
        if (antigoBairro == null) {
            throw new IllegalArgumentException("Não foi possível alterar o Bairro no banco de dados. Motivo: o Bairro não está cadastrado.");
        }
        Municipio municipio = municipioRepository.findByCodigoMunicipio(novoBairro.codigoMunicipio());
        if (municipio == null) {
            throw new IllegalArgumentException("Não foi possível alterar o Bairro no banco de dados. Motivo: o Município não está cadastrado.");
        }


        //Validações de duplicidade
        List<Bairro> bairroRepetido = bairroRepository.findByMunicipioAndNome(municipio, novoBairro.nome());
        if (
                !bairroRepetido.isEmpty() &&
                        !bairroRepetido.get(0).getCodigoBairro().equals(novoBairro.codigoBairro()) &&
                        bairroRepetido.get(0).getStatus().equals(novoBairro.status())
        ) {
            throw new IllegalArgumentException("Não foi possível alterar o Bairro no banco de dados. Motivo: o Nome e o Município já existe em um outro registro cadastro no banco de dados.");
        }


        //Validação de status ativado/desativado
        if (novoBairro.codigoMunicipio().equals(antigoBairro.getMunicipio().getCodigoMunicipio()) &&
                novoBairro.nome().equals(antigoBairro.getNome()) &&
                novoBairro.status().equals(antigoBairro.getStatus())
        ) {
            if (novoBairro.status() == 1) {
                throw new IllegalArgumentException("Não foi possível alterar o status do Bairro no banco de dados, pois o Bairro já está ativo.");
            } else {
                throw new IllegalArgumentException("Não foi possível alterar o status do Bairro no banco de dados, pois o Bairro já está inativo.");
            }
        }


        antigoBairro.setMunicipio(municipio);
        antigoBairro.setNome(novoBairro.nome());
        antigoBairro.setStatus(novoBairro.status());

        return bairroRepository.save(antigoBairro);
    }

    public ResponseEntity<Object> pegar(List<Bairro> bairros, BairroDTO bairroDto) {

        Bairro resultado = null;
        Municipio municipio = new Municipio();
        municipio.setCodigoMunicipio(bairroDto.codigoMunicipio());


        //Sem parâmetros
        if (bairroDto.status() == null && bairroDto.codigoBairro() == null && bairroDto.codigoMunicipio() == null && bairroDto.nome() == null) {
            return ResponseEntity.ok(bairroRepository.findAll().stream().map(BairroMapper::mapearParaBairro).toList());
        }


        //Validações dos valores dos campos
        if (bairroDto.codigoMunicipio() != null && !(String.valueOf(bairroDto.codigoMunicipio()).matches("^[0-9]+$"))) {
            throw new IllegalArgumentException("O campo codigoMunicipio deve conter apenas números inteiros positivos.");
        }
        if (bairroDto.nome() != null && (!(bairroDto.nome().matches("^[A-ZÀ-Ÿ0-9 ]+$")) || bairroDto.nome().trim().isEmpty())) {
            throw new IllegalArgumentException("O campo nome deve conter apenas letras em maiúsculo e números.");
        }
        if (bairroDto.status() != null && bairroDto.status() != 1 && bairroDto.status() != 2) {
            throw new IllegalArgumentException("O campo status deve ser informado apenas os número 1 (ativado) ou 2 (desativado).");
        }


        //Validações do tamanho dos valores dos campos
        if (bairroDto.codigoBairro() != null && bairroDto.codigoBairro() > 999999999) {
            throw new IllegalArgumentException("O campo codigoBairro deve ter no máximo 9 dígitos");
        }
        if (bairroDto.codigoMunicipio() != null && bairroDto.codigoMunicipio() > 999999999) {
            throw new IllegalArgumentException("O campo codigoMunicipio deve ter no máximo 9 dígitos");
        }
        if (bairroDto.nome() != null && bairroDto.nome().length() > 256) {
            throw new IllegalArgumentException("O campo nome deve conter no máximo 256 letras");
        }


        //Apenas 1 parâmetro
        if (bairroDto.codigoBairro() != null) {
            if (!(String.valueOf(bairroDto.codigoBairro()).matches("^[0-9]+$"))) {
                throw new IllegalArgumentException("O campo codigoBairro deve conter apenas números inteiros positivos.");
            }
            resultado = bairroRepository.findByCodigoBairro(bairroDto.codigoBairro());
        }
        if (bairroDto.codigoMunicipio() != null && bairroDto.codigoBairro() == null && bairroDto.nome() == null && bairroDto.status() == null) {
            bairros = bairroRepository.findByMunicipio(municipio);
            return ResponseEntity.ok(bairros.stream().map(BairroMapper::mapearParaBairro).toList());
        }
        if (bairroDto.nome() != null && bairroDto.codigoBairro() == null && bairroDto.codigoMunicipio() == null && bairroDto.status() == null) {
            bairros = bairroRepository.findByNome(bairroDto.nome());
            return ResponseEntity.ok(bairros.stream().map(BairroMapper::mapearParaBairro).toList());
        }
        if (bairroDto.status() != null && bairroDto.codigoMunicipio() == null && bairroDto.nome() == null && bairroDto.codigoBairro() == null) {
            bairros = bairroRepository.findByStatus(bairroDto.status());
            return ResponseEntity.ok(bairros.stream().map(BairroMapper::mapearParaBairro).toList());
        }


        //Parâmetros combinados
        if (bairroDto.nome() != null && bairroDto.codigoMunicipio() != null && bairroDto.status() != null && bairroDto.codigoBairro() == null) {
            bairros = bairroRepository.findByMunicipioAndNomeAndStatus(municipio, bairroDto.nome(), bairroDto.status());
            return ResponseEntity.ok(bairros.stream().map(BairroMapper::mapearParaBairro).toList());
        }
        if (bairroDto.nome() != null && bairroDto.codigoMunicipio() != null && bairroDto.status() == null && bairroDto.codigoBairro() == null) {
            bairros = bairroRepository.findByMunicipioAndNome(municipio, bairroDto.nome());
            return ResponseEntity.ok(bairros.stream().map(BairroMapper::mapearParaBairro).toList());
        }
        if (bairroDto.nome() == null && bairroDto.codigoMunicipio() != null && bairroDto.status() != null && bairroDto.codigoBairro() == null ) {
            bairros = bairroRepository.findByMunicipioAndStatus(municipio, bairroDto.status());
            return ResponseEntity.ok(bairros.stream().map(BairroMapper::mapearParaBairro).toList());
        }
        if (bairroDto.nome() != null && bairroDto.codigoMunicipio() == null && bairroDto.status() != null && bairroDto.codigoBairro() == null) {
            bairros = bairroRepository.findByNomeAndStatus(bairroDto.nome(), bairroDto.status());
            return ResponseEntity.ok(bairros.stream().map(BairroMapper::mapearParaBairro).toList());
        }


        // Verificação se os campos estão conforme o UF encontrado para pesquisas com mais de 1 parâmetro
        if (resultado == null) {
            return ResponseEntity.ok(bairros);
        } else {
            if (!bairroDto.codigoBairro().equals(resultado.getCodigoBairro())) {
                return ResponseEntity.ok(bairros);
            }
            if (bairroDto.codigoMunicipio() != null && !bairroDto.codigoMunicipio().equals(resultado.getMunicipio().getCodigoMunicipio())) {
                return ResponseEntity.ok(bairros);
            }
            if (bairroDto.nome() != null && !bairroDto.nome().equals(resultado.getNome())) {
                return ResponseEntity.ok(bairros);
            }
            if (bairroDto.status() != null && !bairroDto.status().equals(resultado.getStatus())) {
                return ResponseEntity.ok(bairros);
            }
        }

        return ResponseEntity.ok(BairroMapper.mapearParaBairro(resultado));
    }
}
