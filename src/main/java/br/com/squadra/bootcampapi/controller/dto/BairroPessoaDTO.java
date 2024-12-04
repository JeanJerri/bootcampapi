package br.com.squadra.bootcampapi.controller.dto;

public record BairroPessoaDTO(Long codigoBairro, Long codigoMunicipio, String nome, Integer status, MunicipioPessoaDTO municipio
) {}