package br.com.fiap.msobras.exception;

public class RecursoNaoEncontradoException extends RuntimeException {
    public RecursoNaoEncontradoException(String recurso, Long id) {
        super(recurso + " com id " + id + " nao encontrado(a)");
    }
}
