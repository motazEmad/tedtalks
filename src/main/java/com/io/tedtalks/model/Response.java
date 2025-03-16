package com.io.tedtalks.model;

import lombok.Data;

@Data
public class Response<T> {
    private T data;
    private String error;

    public Response(T data) {
        this.data = data;
    }

    public Response(T data, String error) {
        this.data = data;
        this.error = error;
    }
}
