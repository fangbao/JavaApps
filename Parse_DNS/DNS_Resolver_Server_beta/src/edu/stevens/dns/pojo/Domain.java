package edu.stevens.dns.pojo;

public class Domain {
	
	private String name;
	private String IP_Address;
	private int inquireTimes;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIP_Address() {
		return IP_Address;
	}
	public void setIP_Address(String address) {
		IP_Address = address;
	}
	public int getInquireTimes() {
		return inquireTimes;
	}
	public void setInquireTimes(int inquireTimes) {
		this.inquireTimes = inquireTimes;
	}
	
	
}
