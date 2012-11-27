package com.example.falldetectioncentral;

public class Elderly {

	private int id;
	private String name;
	private int phoneNumber;
	private int birthYear;
	private String adress, place;
	private int postNumber;
	private int status;

	
	public Elderly(int id, String name, int phoneNumber, int birthYear, String adress, String place, int postNumber, int status){
		this.setId(id);
		this.name = name;
		this.setPhoneNumber(phoneNumber);
		this.setBirthYear(birthYear);
		this.setAdress(adress);
		this.setStatus(status);
		this.setPlace(place);
		this.setPostNumber(postNumber);
	}
	
	
	public String getName(){
		return name;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getId() {
		return id;
	}


	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public int getPhoneNumber() {
		return phoneNumber;
	}


	public void setBirthYear(int birthYear) {
		this.birthYear = birthYear;
	}


	public int getBirthYear() {
		return birthYear;
	}


	public void setPostNumber(int postNumber) {
		this.postNumber = postNumber;
	}


	public int getPostNumber() {
		return postNumber;
	}


	public void setStatus(int status) {
		this.status = status;
	}


	public int getStatus() {
		return status;
	}


	public void setPlace(String place) {
		this.place = place;
	}


	public String getPlace() {
		return place;
	}


	public void setAdress(String adress) {
		this.adress = adress;
	}


	public String getAdress() {
		return adress;
	}
	
}
