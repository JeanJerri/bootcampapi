package br.com.squadra.bootcampapi.service;

import br.com.squadra.bootcampapi.controller.dto.MunicipioDTO;
import br.com.squadra.bootcampapi.controller.mapper.MunicipioMapper;
import br.com.squadra.bootcampapi.model.Municipio;
import br.com.squadra.bootcampapi.model.Uf;
import br.com.squadra.bootcampapi.repository.MunicipioRepository;
import br.com.squadra.bootcampapi.repository.UfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MunicipioService {

    @Autowired
    private MunicipioRepository municipioRepository;

    @Autowired
    private UfRepository ufRepository;

    public Municipio salvar(MunicipioDTO municipioDto){

        //Validações da presença dos campos
        if (municipioDto.codigoUF() == null) {
            throw new IllegalArgumentException("Não foi possível salvar o Município no banco de dados. Motivo: o campo codigoUF é obrigatório.");
        }
        if (municipioDto.nome() == null || municipioDto.nome().isEmpty()) {
            throw new IllegalArgumentException("Não foi possível salvar o Município no banco de dados. Motivo: o campo nome é obrigatório.");
        }
        if (municipioDto.status() == null) {
            throw new IllegalArgumentException("Não foi possível salvar o Município no banco de dados. Motivo: o campo status é obrigatório.");
        }


        //Validações dos valores dos campos
        if ((String.valueOf(municipioDto.codigoMunicipio()).matches("^[0-9]+$"))) {
            throw new IllegalArgumentException("O campo codigoUF deve conter apenas números inteiros positivos.");
        }
        if (!(municipioDto.nome().matches("^[A-ZÀ-Ÿ ]+$")) || municipioDto.nome().trim().isEmpty()) {
            throw new IllegalArgumentException("O campo nome deve conter apenas letras em maiúsculo.");
        }
        if (municipioDto.status() != 1 && municipioDto.status() != 2) {
            throw new IllegalArgumentException("O campo status deve ser informado apenas os número 1 (ativado) ou 2 (desativado) .");
        }


        //Validação do tamanho dos valores dos campos
        if (municipioDto.codigoUF() > 999999999) {
            throw new IllegalArgumentException("O campo codigoUF deve ter no máximo 9 dígitos");
        }
        if (municipioDto.nome().length() > 60) {
            throw new IllegalArgumentException("O campo nome deve conter no máximo 60 letras");
        }


        //Validações de existência
        Uf uf = ufRepository.findByCodigoUF(municipioDto.codigoUF());
        if (uf == null) {
            throw new IllegalArgumentException("Não foi possível salvar o Município no banco de dados. Motivo: a UF não está cadastrada.");
        }


        //Validações de duplicidade
        List<Municipio> municipioRepetido = municipioRepository.findByUfAndNome(uf, municipioDto.nome());
        if (!municipioRepetido.isEmpty()) {
            throw new IllegalArgumentException("Não foi possível salvar o Município no banco de dados. Motivo: o nome e a UF já existe em um outro registro cadastrado no banco de dados.");
        }


        Municipio municipio = new Municipio();
        municipio.setUf(uf);
        municipio.setNome(municipioDto.nome());
        municipio.setStatus(municipioDto.status());

        return municipioRepository.save(municipio);
    }

    public Municipio alterar(MunicipioDTO novoMunicipio) {


        //Validações da presença dos campos
        if (novoMunicipio.codigoMunicipio() == null) {
            throw new IllegalArgumentException("Não foi possível alterar o Município no banco de dados. Motivo: o campo codigoMunicipio é obrigatório.");
        }
        if (novoMunicipio.codigoUF() == null) {
            throw new IllegalArgumentException("Não foi possível alterar o Município no banco de dados. Motivo: o campo codigoUF é obrigatório.");
        }
        if (novoMunicipio.nome() == null || novoMunicipio.nome().isEmpty()) {
            throw new IllegalArgumentException("Não foi possível alterar o Município no banco de dados. Motivo: o campo nome é obrigatório.");
        }
        if (novoMunicipio.status() == null) {
            throw new IllegalArgumentException("Não foi possível alterar o Município no banco de dados. Motivo: o campo status é obrigatório.");
        }


        //Validações dos valores dos campos
        if (!(String.valueOf(novoMunicipio.codigoMunicipio()).matches("^[0-9]+$"))) {
            throw new IllegalArgumentException("O campo codigoUF deve conter apenas números inteiros positivos.");
        }
        if (!(String.valueOf(novoMunicipio.codigoUF()).matches("^[0-9]+$"))) {
            throw new IllegalArgumentException("O campo codigoMunicipio deve conter apenas números inteiros positivos.");
        }
        if (!(novoMunicipio.nome().matches("^[A-ZÀ-Ÿ ]+$")) || novoMunicipio.nome().trim().isEmpty()) {
            throw new IllegalArgumentException("O campo nome deve conter apenas letras em maiúsculo.");
        }
        if (novoMunicipio.status() != 1 && novoMunicipio.status() != 2) {
            throw new IllegalArgumentException("O campo status deve ser informado apenas os número 1 (ativado) ou 2 (desativado).");
        }


        //Validação do tamanho dos valores dos campos
        if (novoMunicipio.codigoMunicipio() > 999999999) {
            throw new IllegalArgumentException("O campo codigoMunicipio deve ter no máximo 9 dígitos");
        }
        if (novoMunicipio.codigoUF() > 999999999) {
            throw new IllegalArgumentException("O campo codigoUF deve ter no máximo 9 dígitos");
        }
        if (novoMunicipio.nome().length() > 60) {
            throw new IllegalArgumentException("O campo nome deve conter no máximo 60 letras");
        }


        //Validações de existência
        Municipio antigoMunicipio = municipioRepository.findByCodigoMunicipio(novoMunicipio.codigoMunicipio());
        if (antigoMunicipio == null) {
            throw new IllegalArgumentException("Não foi possível alterar o Município no banco de dados. Motivo: o Município não está cadastrado.");
        }
        Uf uf = ufRepository.findByCodigoUF(novoMunicipio.codigoUF());
        if (uf.getCodigoUF() == null) {
            throw new IllegalArgumentException("Não foi possível alterar o Município no banco de dados. Motivo: a UF não está cadastrada.");
        }


        //Validações de duplicidade
        List<Municipio> municipioRepetido = municipioRepository.findByUfAndNome(uf, novoMunicipio.nome());
        if (
                !municipioRepetido.isEmpty() &&
                        !municipioRepetido.get(0).getCodigoMunicipio().equals(novoMunicipio.codigoMunicipio()) &&
                        municipioRepetido.get(0).getStatus().equals(novoMunicipio.status())
        ) {
            throw new IllegalArgumentException("Não foi possível alterar o Município no banco de dados. Motivo: o nome e a UF já existe em um outro registro cadastrado no banco de dados.");
        }


        //Validação de status ativado/desativado
        if (novoMunicipio.codigoUF().equals(antigoMunicipio.getUf().getCodigoUF()) &&
                novoMunicipio.nome().equals(antigoMunicipio.getNome()) &&
                novoMunicipio.status().equals(antigoMunicipio.getStatus())
        ) {
            if (novoMunicipio.status() == 1) {
                throw new IllegalArgumentException("Não foi possível alterar o status do Município no banco de dados. Motivo: o Município já está ativo.");
            } else {
                throw new IllegalArgumentException("Não foi possível alterar o status do Município no banco de dados. Motivo: o Município já está desativado.");
            }
        }


        antigoMunicipio.setUf(uf);
        antigoMunicipio.setNome(novoMunicipio.nome());
        antigoMunicipio.setStatus(novoMunicipio.status());

        return municipioRepository.save(antigoMunicipio);
    }

    public ResponseEntity<Object> pegar(List<Municipio> municipios, MunicipioDTO municipioDTO) {

        Municipio resultado = null;
        Uf uf = new Uf();
        uf.setCodigoUF(municipioDTO.codigoUF());

        //Sem parâmetros
        if (municipioDTO.status() == null && municipioDTO.codigoMunicipio() == null && municipioDTO.codigoUF() == null && municipioDTO.nome() == null) {
            return ResponseEntity.ok(municipioRepository.findAll().stream().map(MunicipioMapper::mapearParaMunicipio).toList());
        }


        //Validações dos valores dos campos
        if (municipioDTO.codigoUF() != null && !(String.valueOf(municipioDTO.codigoUF()).matches("^[0-9]+$"))) {
            throw new IllegalArgumentException("O campo codigoUF deve conter apenas números inteiros positivos.");
        }
        if (municipioDTO.nome() != null && (!(municipioDTO.nome().matches("^[A-ZÀ-Ÿ ]+$")) || municipioDTO.nome().trim().isEmpty())) {
            throw new IllegalArgumentException("O campo nome deve conter apenas letras em maiúsculo.");
        }
        if (municipioDTO.status() != null && municipioDTO.status() != 1 && municipioDTO.status() != 2) {
            throw new IllegalArgumentException("O campo status deve ser informado apenas os número 1 (ativado) ou 2 (desativado).");
        }


        //Validação do tamanho dos valores dos campos
        if (municipioDTO.codigoMunicipio() != null && municipioDTO.codigoMunicipio() > 999999999) {
            throw new IllegalArgumentException("O campo codigoMunicipio deve ter no máximo 9 dígitos");
        }
        if (municipioDTO.codigoUF() != null && municipioDTO.codigoUF() > 999999999) {
            throw new IllegalArgumentException("O campo codigoUF deve ter no máximo 9 dígitos");
        }
        if (municipioDTO.nome() != null && municipioDTO.nome().length() > 60) {
            throw new IllegalArgumentException("O campo nome deve conter no máximo 60 letras");
        }


        //Apenas 1 parâmetro
        if (municipioDTO.codigoMunicipio() != null) {
            if (!(String.valueOf(municipioDTO.codigoMunicipio()).matches("^[0-9]+$"))) {
                throw new IllegalArgumentException("O campo codigoMunicipio deve conter apenas números inteiros positivos.");
            }
            resultado = municipioRepository.findByCodigoMunicipio(municipioDTO.codigoMunicipio());
        }
        if (municipioDTO.codigoUF() != null && municipioDTO.codigoMunicipio() == null && municipioDTO.nome() == null && municipioDTO.status() == null) {
            municipios = municipioRepository.findByUf(uf);
            return ResponseEntity.ok(municipios.stream().map(MunicipioMapper::mapearParaMunicipio).toList());
        }
        if (municipioDTO.nome() != null && municipioDTO.codigoMunicipio() == null && municipioDTO.codigoUF() == null && municipioDTO.status() == null) {
            municipios = municipioRepository.findByNome(municipioDTO.nome());
            return ResponseEntity.ok(municipios.stream().map(MunicipioMapper::mapearParaMunicipio).toList());
        }
        if (municipioDTO.status() != null && municipioDTO.codigoMunicipio() == null && municipioDTO.codigoUF() == null && municipioDTO.nome() == null) {
            municipios = municipioRepository.findByStatus(municipioDTO.status());
            return ResponseEntity.ok(municipios.stream().map(MunicipioMapper::mapearParaMunicipio).toList());
        }


        //Parâmetros combinados
        if (municipioDTO.nome() != null && municipioDTO.codigoUF() != null && municipioDTO.status() != null && municipioDTO.codigoMunicipio() == null) {
            municipios = municipioRepository.findByUfAndNomeAndStatus(uf, municipioDTO.nome(), municipioDTO.status());
            return ResponseEntity.ok(municipios.stream().map(MunicipioMapper::mapearParaMunicipio).toList());
        }
        if (municipioDTO.nome() != null && municipioDTO.codigoUF() != null && municipioDTO.status() == null && municipioDTO.codigoMunicipio() == null) {
            municipios = municipioRepository.findByUfAndNome(uf, municipioDTO.nome());
            return ResponseEntity.ok(municipios.stream().map(MunicipioMapper::mapearParaMunicipio).toList());
        }
        if (municipioDTO.nome() == null && municipioDTO.codigoUF() != null && municipioDTO.status() != null && municipioDTO.codigoMunicipio() == null ) {
            municipios = municipioRepository.findByUfAndStatus(uf, municipioDTO.status());
            return ResponseEntity.ok(municipios.stream().map(MunicipioMapper::mapearParaMunicipio).toList());
        }
        if (municipioDTO.nome() != null && municipioDTO.codigoUF() == null && municipioDTO.status() != null && municipioDTO.codigoMunicipio() == null) {
            municipios = municipioRepository.findByNomeAndStatus(municipioDTO.nome(), municipioDTO.status());
            return ResponseEntity.ok(municipios.stream().map(MunicipioMapper::mapearParaMunicipio).toList());
        }


        // Verificação se os campos estão conforme o UF encontrado para pesquisas com mais de 1 parâmetro
        if (resultado == null) {
            return ResponseEntity.ok(municipios);
        } else {
            if (!municipioDTO.codigoMunicipio().equals(resultado.getCodigoMunicipio())) {
                return ResponseEntity.ok(municipios);
            }
            if (municipioDTO.codigoUF() != null && !municipioDTO.codigoUF().equals(resultado.getUf().getCodigoUF())) {
                return ResponseEntity.ok(municipios);
            }
            if (municipioDTO.nome() != null && !municipioDTO.nome().equals(resultado.getNome())) {
                return ResponseEntity.ok(municipios);
            }
            if (municipioDTO.status() != null && !municipioDTO.status().equals(resultado.getStatus())) {
                return ResponseEntity.ok(municipios);
            }
        }

        return ResponseEntity.ok(MunicipioMapper.mapearParaMunicipio(resultado));
    }
}
