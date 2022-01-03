package com.idolcollector.idolcollector.advice.exception;

public class CMemberNotFoundException extends RuntimeException {

    public CMemberNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public CMemberNotFoundException(String msg) {
        super(msg);
    }

    public CMemberNotFoundException() {
        super();
    }
}
