package br.com.squadra.bootcampapi.exceptions;

public class CampoInvalidoException extends RuntimeException {

    public CampoInvalidoException(String mensagem) {
        super(mensagem);
    }
}
