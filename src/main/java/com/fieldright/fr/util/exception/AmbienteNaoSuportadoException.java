package com.fieldright.fr.util.exception;

public class AmbienteNaoSuportadoException extends RuntimeException {

    public AmbienteNaoSuportadoException() {
        super("Não foi possivel realizar a operação solicitada. Confere se o ambiente for test ou localhost e tente novamente");
    }
}
