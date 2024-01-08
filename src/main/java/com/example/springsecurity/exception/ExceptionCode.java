package com.example.springsecurity.exception;

import lombok.Getter;

public enum ExceptionCode {

    //Security
    NO_ACCESS_TOKEN(403, "해당 토큰에 권한 정보가 존재하지 않습니다.");

    
    @Getter
    private final int status;

    @Getter
    private final String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
