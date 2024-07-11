package com.sparta.kanbanboard.common.exception.customexception.globalexception;

public class GlobalRejectedException extends RuntimeException{
    public GlobalRejectedException(String message) {
        super(message);
    }
}
