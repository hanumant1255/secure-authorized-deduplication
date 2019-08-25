package com.hybrid.cloud.dao;
public class FileAlreadyExistException extends Exception {

    public FileAlreadyExistException(String message) {
        super(message);
    }

}