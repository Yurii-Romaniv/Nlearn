package com.example.nlearn.exception;


public class TestNotAllowedException extends RuntimeException {
    public TestNotAllowedException() {
        super("this test is not allowed for you, or you have run out of attempts");
    }
}