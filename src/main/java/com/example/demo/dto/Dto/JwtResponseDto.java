package com.example.demo.dto.Dto;

public class JwtResponseDto {
    private  String token;
    private final String type="Bearer";
    public JwtResponseDto(String token){
        this.token=token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }
}
