package com.elspepegoteras.server.dto;

public class JoinPartidaDTO {
    private Long idPartida;
    private String token;
    private long idUsuari;

    public JoinPartidaDTO(Long idPartida, String token, long idUsuari) {
        this.idPartida = idPartida;
        this.token = token;
        this.idUsuari = idUsuari;
    }

    public Long getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(Long idPartida) {
        this.idPartida = idPartida;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getIdUsuari() {
        return idUsuari;
    }

    public void setIdUsuari(long idUsuari) {
        this.idUsuari = idUsuari;
    }
}
