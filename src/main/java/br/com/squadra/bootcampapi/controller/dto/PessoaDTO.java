package br.com.squadra.bootcampapi.controller.dto;

import java.util.List;

public record PessoaDTO(
        Long codigoPessoa,
        String nome,
        String sobrenome,
        Integer idade,
        String login,
        String senha,
        Integer status,
        List<EnderecoDTO> enderecos
) {}
