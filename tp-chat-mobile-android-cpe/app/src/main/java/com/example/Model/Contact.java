package com.example.Model;

public class Contact {
    public String uuid;
    public String login;
    public String latestMsg;

    public Contact(String uuid, String login, String message){
        this.uuid = uuid;
        this.login = login;
        this.latestMsg = message;
    }
}
