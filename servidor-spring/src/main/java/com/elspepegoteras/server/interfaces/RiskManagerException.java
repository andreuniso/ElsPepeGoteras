package com.elspepegoteras.server.interfaces;

public class RiskManagerException extends Exception {
    public RiskManagerException(String message) {
        super(message);
    }

    public RiskManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
