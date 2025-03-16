package com.io.tedtalks.exception;

public class CSVFileFormatException extends RuntimeException {

    public CSVFileFormatException(String message) {
        super(message);
    }

    public CSVFileFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
