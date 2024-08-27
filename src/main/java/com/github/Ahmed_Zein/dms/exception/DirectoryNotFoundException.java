package com.github.Ahmed_Zein.dms.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DirectoryNotFoundException extends Exception {
    public DirectoryNotFoundException(String message) {
        super(message);
    }
}
