/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.japs.frontend.bodyfitgym.response;

/**
 *
 * @author jhonathan.penaloza_p
 */
public class ServiceResponse<T> {
    
    private String message;
    
    private int code;
    
    private boolean status;
    
    private T data;

    public ServiceResponse(String message, int code, boolean status, T data) {
        this.message = message;
        this.code = code;
        this.status = status;
        this.data = data;
    }

    public ServiceResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ServiceResponse{" + "message=" + message + ", code=" + code + ", status=" + status + ", data=" + data + '}';
    }

}
