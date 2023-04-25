package com.cat.digital.reco.utils.rest;

public class RecoResponse {
    public int status;
    public String body;

    RecoResponse(int status, String body) {
        this.status = status;
        this.body = body;
    }
}
