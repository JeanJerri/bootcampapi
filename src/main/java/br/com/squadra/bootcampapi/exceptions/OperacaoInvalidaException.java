package br.com.squadra.bootcampapi.exceptions;

public class OperacaoInvalidaException extends IllegalArgumentException {

    public OperacaoInvalidaException(String mensagem) {
        super(mensagem);
    }
}
