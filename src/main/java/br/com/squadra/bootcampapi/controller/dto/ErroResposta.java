package br.com.squadra.bootcampapi.controller.dto;

import org.springframework.http.HttpStatus;

public record ErroResposta(String mensagem, int status) {

    public static ErroResposta respostaPadrao(String mensagem) {
        return new ErroResposta(mensagem, HttpStatus.NOT_FOUND.value());
    }
}
