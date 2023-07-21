package com.stinkytooters.stinkytootersbot.api.discord;

public class InvalidCommandFormatException extends RuntimeException {

    public InvalidCommandFormatException(String message) {
        super(message);
    }

}
