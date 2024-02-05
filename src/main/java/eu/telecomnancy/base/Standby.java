package eu.telecomnancy.base;

import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Standby {
	
	private int ID;
	private int userID;
	private int adID;
	private ObjectProperty<LocalDate> startDate;
    private ObjectProperty<LocalDate> endDate;
    
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public int getAdID() {
		return adID;
	}
	public void setAdID(int adID) {
		this.adID = adID;
	}
	
	public final ObjectProperty<LocalDate> startDateProperty() {
		return this.startDate;
	}
	
	public final LocalDate getStartDate() {
		return this.startDateProperty().get();
	}
	
	public final void setStartDate(final LocalDate startDate) {
		this.startDateProperty().set(startDate);
	}
	
	public final ObjectProperty<LocalDate> endDateProperty() {
		return this.endDate;
	}
	
	public final LocalDate getEndDate() {
		return this.endDateProperty().get();
	}
	
	public final void setEndDate(final LocalDate endDate) {
		this.endDateProperty().set(endDate);
	}
	
	public Standby( int id, int userId, int adId, LocalDate startDate, LocalDate endDate)
	{
		this.ID = id;
		this.userID = userId;
		this.adID = adId;
		
		this.startDate = new SimpleObjectProperty<>(this, "startDate", startDate);
		this.endDate = new SimpleObjectProperty<>(this, "endDate", endDate);
	}
	
	public Standby( int userId, int adId, LocalDate startDate )
	{
		this.ID = 0;
		this.userID = userId;
		this.adID = adId;
		
		this.startDate = new SimpleObjectProperty<>(this, "startDate", startDate);
		this.endDate = new SimpleObjectProperty<>(this, "endDate", null);
	}


}
