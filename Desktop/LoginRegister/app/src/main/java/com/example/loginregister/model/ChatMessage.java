package com.example.loginregister.model;

import java.util.Date;

public class ChatMessage {
    private String from;
    private String message;
    private long time;

    public ChatMessage() {
    }

    public ChatMessage(String from, String message, long time) {
        this.from = from;
        this.message = message;
        this.time = time;
    }

    public ChatMessage(String from, String message) {
        this.from = from;
        this.message = message;
        this.time = new Date().getTime();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "from='" + from + '\'' +
                ", message='" + message + '\'' +
                ", time=" + time +
                '}';
    }
}
