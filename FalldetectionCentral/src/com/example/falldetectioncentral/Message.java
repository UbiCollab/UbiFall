package com.example.falldetectioncentral;

import java.io.Serializable;

public class Message implements Serializable{
	private String message;
	private int type;
	private String from;
	
	public Message(String message, int type, String from){
		this.message = message;
		this.type = type;
		this.from = from;
	}
	
	public String getMessage(){
		return message;
	}
	
	public int getType(){
		return type;
	}
	
	public String getFrom(){
		return from;
	}
}