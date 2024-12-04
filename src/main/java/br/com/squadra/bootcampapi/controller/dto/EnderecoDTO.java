package br.com.squadra.bootcampapi.controller.dto;

public record EnderecoDTO(
        Long codigoEndereco,
        Long codigoPessoa,
        Long codigoBairro,
        String nomeRua,
        String numero,
        String complemento,
        String cep,
        BairroPessoaDTO bairro) {}
