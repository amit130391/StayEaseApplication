package com.crio.Stayease.Exception;

public class HotelPresentInDatabaseException extends RuntimeException{
    public HotelPresentInDatabaseException(String message){
        super(message);
    }
}
