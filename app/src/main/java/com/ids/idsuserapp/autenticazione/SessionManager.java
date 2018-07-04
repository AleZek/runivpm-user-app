package com.ids.idsuserapp.autenticazione;

public interface SessionManager {
    String getBearer() throws Exception;

    String getAccessToken() throws Exception;
}
