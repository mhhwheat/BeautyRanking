package org.wheat.ranking.entity;

public class Location {
	private double lat=0.0 ;
	private double lng=0.0;
	private String locationMessage;
	public double getLat(){
		return this.lat;
	}
	public void setLat(double lat){
		this.lat=lat;
	}
	public double getLng(){
		return this.lng;
	}
	public void setLng(double lng){
		this.lng=lng;
	}
	public String getLocationMessage(){
		return this.locationMessage;
	}
	public void setLocationMessage(String locationMessage){
		this.locationMessage=locationMessage;
	}

}
