package br.com.squadra.bootcampapi.controller.dto;

public record MunicipioPessoaDTO(Long codigoMunicipio, Long codigoUF, String nome, Integer status, UfDTO uf) {
}
