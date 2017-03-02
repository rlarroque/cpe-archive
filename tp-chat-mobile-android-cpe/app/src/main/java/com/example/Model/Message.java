package com.example.Model;

import java.util.ArrayList;

public class Message {

    public String uuid;
    public String login;
    public String message;
    public String image;
    public ArrayList<Attachments> attachments = new ArrayList<>();


    public Message(String uuid, String author, String message){
        this.login = author;
        this.message = message;
        this.uuid = uuid;
    }

    public void SetImage(String url){
       this.image = url;
    }

    public void SetAttachments(String[] attach){
        attachments.add(new Attachments(attach[0],attach[1]));
    }
}
