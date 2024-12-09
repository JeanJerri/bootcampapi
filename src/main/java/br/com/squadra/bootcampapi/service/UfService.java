package br.com.squadra.bootcampapi.service;

import br.com.squadra.bootcampapi.controller.dto.UfDTO;
import br.com.squadra.bootcampapi.model.Uf;
import br.com.squadra.bootcampapi.repository.UfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UfService {

    @Autowired
    private UfRepository ufRepository;

    public Uf salvar(UfDTO ufDto){

        //Validações da presença dos campos
        if (ufDto.nome() == null || ufDto.nome().isEmpty()) {
            throw new IllegalArgumentException("Não foi possível salvar o UF no banco de dados. Motivo: o campo nome é obrigatório.");
        }
        if (ufDto.sigla() == null || ufDto.sigla().trim().isEmpty()) {
            throw new IllegalArgumentException("Não foi possível salvar o UF no banco de dados. Motivo: o campo sigla é obrigatório.");
        }
        if (ufDto.status() == null) {
            throw new IllegalArgumentException("Não foi possível salvar o UF no banco de dados. Motivo: o campo status é obrigatório.");
        }


        //Validações dos valores dos campos
        if (!(ufDto.nome().matches("^[A-ZÀ-Ÿ ]+$")) || ufDto.nome().trim().isEmpty()) {
            throw new IllegalArgumentException("O campo nome deve conter apenas letras em maiúsculo.");
        }
        if (!(ufDto.sigla().matches("^[A-Z]{2}$"))) {
            throw new IllegalArgumentException("O campo sigla deve conter apenas 2 letras, sem acentos e em maiúsculo.");
        }
        if (ufDto.status() != 1 && ufDto.status() != 2) {
            throw new IllegalArgumentException("O campo status deve ser informado apenas os número 1 (ativado) ou 2 (desativado).");
        }


        //Validações do tamanho dos valores dos campos
        if (ufDto.sigla().length() > 2) {
            throw new IllegalArgumentException("O campo sigla deve conter 2 letras");
        }
        if (ufDto.nome().length() > 60) {
            throw new IllegalArgumentException("O campo nome deve conter no máximo 60 letras");
        }


        //Validações de duplicidade
        if (ufRepository.findByNome(ufDto.nome()) != null) {
            throw new IllegalArgumentException("Não foi possível salvar o UF no banco de dados. Motivo: o nome já existe em outro registro cadastrado no banco de dados.");
        }
        if (ufRepository.findBySigla(ufDto.sigla()) != null) {
            throw new IllegalArgumentException("Não foi possível salvar o UF no banco de dados. Motivo: a sigla já existe em outro registro cadastrado no banco de dados.");
        }


        Uf uf = new Uf();
        uf.setNome(ufDto.nome());
        uf.setSigla(ufDto.sigla());
        uf.setStatus(ufDto.status());

        return ufRepository.save(uf);
    }

    public Uf alterar(UfDTO novaUf){

        //Validações da presença dos campos
        if (novaUf.codigoUF() == null) {
            throw new IllegalArgumentException("Não foi possível alterar o UF no banco de dados. Motivo: o campo codigoUF é obrigatório.");
        }
        if (novaUf.nome() == null || novaUf.nome().isEmpty()) {
            throw new IllegalArgumentException("Não foi possível alterar o UF no banco de dados. Motivo: o campo nome é obrigatório.");
        }
        if (novaUf.sigla() == null || novaUf.sigla().trim().isEmpty()) {
            throw new IllegalArgumentException("Não foi possível alterar o UF no banco de dados. Motivo: o campo sigla é obrigatório.");
        }
        if (novaUf.status() == null) {
            throw new IllegalArgumentException("Não foi possível alterar o UF no banco de dados. Motivo: o campo status é obrigatório.");
        }


        //Validações dos valores dos campos
        if (!(String.valueOf(novaUf.codigoUF()).matches("^[0-9]+$"))) {
            throw new IllegalArgumentException("O campo codigoUF deve conter apenas números inteiros positivos.");
        }
        if (!(novaUf.nome().matches("^[A-ZÀ-Ÿ ]+$")) || novaUf.nome().trim().isEmpty()) {
            throw new IllegalArgumentException("O campo nome deve conter apenas letras em maiúsculo.");
        }
        if (!(novaUf.sigla().matches("^[A-Z]{2}$"))) {
            throw new IllegalArgumentException("O campo sigla deve conter apenas 2 letras, sem acentos e em maiúsculo.");
        }
        if (novaUf.status() != 1 && novaUf.status() != 2) {
            throw new IllegalArgumentException("O campo status deve ser informado apenas os número 1 (ativado) ou 2 (desativado).");
        }


        //Validações do tamanho dos valores dos campos
        if (novaUf.codigoUF() > 999999999) {
            throw new IllegalArgumentException("O campo codigoUF deve ter no máximo 9 dígitos");
        }
        if (novaUf.sigla().length() > 2) {
            throw new IllegalArgumentException("O campo sigla deve conter 2 letras");
        }
        if (novaUf.nome().length() > 60) {
            throw new IllegalArgumentException("O campo nome deve conter no máximo 60 letras");
        }


        //Validações de existência
        Uf antigaUf = ufRepository.findByCodigoUF(novaUf.codigoUF());
        if (antigaUf == null) {
            throw new IllegalArgumentException("Não foi possível alterar UF no banco de dados. Motivo: a UF não está cadastrada no banco de dados.");
        }


        //Validações de duplicidade
        Uf ufRepetida = ufRepository.findBySigla(novaUf.sigla());
        if (ufRepetida!=null && !(ufRepetida.getCodigoUF().equals(antigaUf.getCodigoUF()))) {
            throw new IllegalArgumentException("Não foi possível alterar o UF no banco de dados. Motivo: a sigla já existe.");
        }
        ufRepetida = ufRepository.findByNome(novaUf.nome());
        if (ufRepetida!=null && !(ufRepetida.getCodigoUF().equals(antigaUf.getCodigoUF()))) {
            throw new IllegalArgumentException("Não foi possível alterar o UF no banco de dados. Motivo: o nome já existe.");
        }


        //Validação de status ativado/desativado
        if (novaUf.sigla().equals(antigaUf.getSigla()) && novaUf.nome().equals(antigaUf.getNome()) && novaUf.status().equals(antigaUf.getStatus())) {
            if (novaUf.status() == 1) {
                throw new IllegalArgumentException("Não foi possível alterar o UF no banco de dados. Motivo: a UF já está ativa.");
            } else {
                throw new IllegalArgumentException("Não foi possível alterar o UF no banco de dados. Motivo: a UF já está desativada.");
            }
        }


        antigaUf.setSigla(novaUf.sigla());
        antigaUf.setNome(novaUf.nome());
        antigaUf.setStatus(novaUf.status());

        return ufRepository.save(antigaUf);
    }

    public ResponseEntity<Object> pegar(List<Uf> ufs, UfDTO ufDto) {

        Uf resultado = null;

        //Sem parâmetros
        if (ufDto.status() == null && ufDto.codigoUF() == null && ufDto.sigla() == null && ufDto.nome() == null) {
            return ResponseEntity.ok(ufRepository.findAll());
        }


        //Validações dos valores dos campos
        if (ufDto.codigoUF() != null && !(String.valueOf(ufDto.codigoUF()).matches("^[0-9]+$"))) {
            throw new IllegalArgumentException("O campo códigoUF deve conter apenas números inteiros positivos.");
        }
        if (ufDto.status() != null && ufDto.status() != 1 && ufDto.status() != 2) {
            throw new IllegalArgumentException("O campo status deve ser informado apenas os número 1 (ativado) ou 2 (desativado).");
        }
        if (ufDto.nome() != null && (!(ufDto.nome().matches("^[A-ZÀ-Ÿ ]+$")) || ufDto.nome().trim().isEmpty())) {
            throw new IllegalArgumentException("O campo nome deve conter apenas letras em maiúsculo.");
        }
        if (ufDto.sigla() != null && ((!(ufDto.sigla().matches("^[A-Z]{2}$")) || ufDto.sigla().trim().isEmpty()))) {
            throw new IllegalArgumentException("O campo sigla deve conter apenas 2 letras, sem acentos e em maiúsculo.");
        }


        //Validações do tamanho dos valores dos campos
        if (ufDto.codigoUF() != null && ufDto.codigoUF() > 999999999) {
            throw new IllegalArgumentException("O campo codigoUF deve ter no máximo 9 dígitos");
        }
        if (ufDto.sigla() != null && ufDto.sigla().length() > 2) {
            throw new IllegalArgumentException("O campo sigla deve conter 2 letras");
        }
        if (ufDto.nome() != null && ufDto.nome().length() > 60) {
            throw new IllegalArgumentException("O campo nome deve conter no máximo 60 letras");
        }


        //Apenas 1 parâmetro
        if (ufDto.status() != null && ufDto.codigoUF() == null && ufDto.sigla() == null && ufDto.nome() == null) {
            return ResponseEntity.ok(ufRepository.findByStatus(ufDto.status()));
        }
        if (ufDto.codigoUF() != null) {
            resultado = ufRepository.findByCodigoUF(ufDto.codigoUF());
        }
        if (ufDto.nome() != null && resultado == null) {
            resultado = ufRepository.findByNome(ufDto.nome());
        }
        if (ufDto.sigla() != null && resultado == null) {
            resultado = ufRepository.findBySigla(ufDto.sigla());
        }


        // Verificação se os campos estão conforme o UF encontrado para pesquisas com mais de 1 parâmetro
        if (resultado == null) {
            return ResponseEntity.ok(ufs);
        } else {
            if (ufDto.codigoUF() != null && !ufDto.codigoUF().equals(resultado.getCodigoUF())) {
                return ResponseEntity.ok(ufs);
            }
            if (ufDto.sigla() != null && !ufDto.sigla().equals(resultado.getSigla())) {
                return ResponseEntity.ok(ufs);
            }
            if (ufDto.nome()!= null && !ufDto.nome().equals(resultado.getNome())) {
                return ResponseEntity.ok(ufs);
            }
            if (ufDto.status() != null && !ufDto.status().equals(resultado.getStatus())) {
                return ResponseEntity.ok(ufs);
            }
        }

        return ResponseEntity.ok(resultado);
    }

    public void deletar(Long codigoUF) {

        //Validações da presença dos campos
        if (codigoUF == null) {
            throw new IllegalArgumentException("Não foi possível deletar o UF no banco de dados. Motivo: o campo codigoUF é obrigatório.");
        }

        //Validações dos valores dos campos
        if (!(String.valueOf(codigoUF).matches("^[0-9]+$"))) {
            throw new IllegalArgumentException("O campo codigoUF deve conter apenas números inteiros positivos.");
        }

        //Validações do tamanho dos valores dos campos
        if (codigoUF > 999999999) {
            throw new IllegalArgumentException("O campo codigoUF deve ter no máximo 9 dígitos");
        }

        //Validações de existência
        Uf uf = ufRepository.findByCodigoUF(codigoUF);
        if (uf == null) {
            throw new IllegalArgumentException("Não foi possível deletar o UF no banco de dados. Motivo: o UF não está cadastrado no banco de dados.");
        }

        ufRepository.delete(uf);
    }
}
