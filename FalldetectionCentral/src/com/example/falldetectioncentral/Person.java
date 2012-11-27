package com.example.falldetectioncentral;

import java.io.Serializable;
import java.util.ArrayList;

public class Person implements Serializable {

	int id;
	String name;
	ArrayList<Message> messages = new ArrayList<Message>();
	
	public Person (int id, String name){
		this.id = id;
		this.name = name;
	} 
	
	public int getId(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public ArrayList<Message> getMessages(){
		return messages;
	}
	
	public void addMessage(Message message){
		messages.add(message);
	}
	
}
