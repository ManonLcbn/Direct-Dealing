package eu.telecomnancy.base;

import java.time.*;

import javafx.beans.property.*;
import javafx.scene.image.Image;

public class Ad extends Annonce{
	

	private int id;
	private SimpleBooleanProperty isRequest;
	private int userId;
	private boolean isMaterial;
	private SimpleStringProperty title;
	private SimpleIntegerProperty cost;
	private SimpleStringProperty description;
	private SimpleIntegerProperty categoryId;
	private SimpleBooleanProperty isAvailable;
	
	private SimpleStringProperty localization;
	private SimpleStringProperty comments;
	private String picture;
    private ObjectProperty<LocalDate> startDate;

	private SimpleIntegerProperty durationInDay;
	private SimpleBooleanProperty isRepetitive;
	private ObjectProperty<LocalDate> endDate;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public final SimpleBooleanProperty isRequestProperty() {
		return this.isRequest;
	}
	

	public final boolean isIsRequest() {
		return this.isRequestProperty().get();
	}
	

	public final void setIsRequest(final boolean isRequest) {
		this.isRequestProperty().set(isRequest);
	}

	public final int getUserId() {
		return this.userId;
	}
	
	public final void setUserId(final int userId) {
		this.userId = userId;
	}
	
	public boolean isMaterial() {
		return isMaterial;
	}

	public void setMaterial(boolean isMaterial) {
		this.isMaterial = isMaterial;
	}

	public final SimpleStringProperty titleProperty() {
		return this.title;
	}
	

	public final String getTitle() {
		return this.titleProperty().get();
	}
	

	public final void setTitle(final String title) {
		this.titleProperty().set(title);
	}
	

	public final SimpleIntegerProperty costProperty() {
		return this.cost;
	}
	

	public final int getCost() {
		return this.costProperty().get();
	}
	

	public final void setCost(final int cost) {
		this.costProperty().set(cost);
	}
	

	public final SimpleStringProperty descriptionProperty() {
		return this.description;
	}
	

	public final String getDescription() {
		return this.descriptionProperty().get();
	}
	

	public final void setDescription(final String description) {
		this.descriptionProperty().set(description);
	}
	

	public final SimpleIntegerProperty categoryIdProperty() {
		return this.categoryId;
	}
	

	public final int getCategoryId() {
		return this.categoryIdProperty().get();
	}
	

	public final void setCategoryId(final int categoryId) {
		this.categoryIdProperty().set(categoryId);
	}
	

	public final SimpleBooleanProperty isAvailableProperty() {
		return this.isAvailable;
	}
	

	public final boolean isIsAvailable() {
		return this.isAvailableProperty().get();
	}
	

	public final void setIsAvailable(final boolean isAvailable) {
		this.isAvailableProperty().set(isAvailable);
	}
	
	public final SimpleStringProperty localizationProperty() {
		return this.localization;
	}
	
	public final String getGeolocalization() {
		return this.localizationProperty().get();
	}
	
	public final void setGeolocalization(final String geolocalization) {
		this.localizationProperty().set(geolocalization);
	}
	
	public final SimpleStringProperty commentsProperty() {
		return this.comments;
	}
	
	public final String getComments() {
		return this.commentsProperty().get();
	}
	
	public final void setComments(final String comments) {
		this.commentsProperty().set(comments);
	}
	
	public final SimpleIntegerProperty durationInDayProperty() {
		return this.durationInDay;
	}
	
	public final int getDurationInDay() {
		return this.durationInDayProperty().get();
	}
	
	public final void setDurationInDay(final int durationInDay) {
		this.durationInDayProperty().set(durationInDay);
	}
	
	public final SimpleBooleanProperty isRepetitiveProperty() {
		return this.isRepetitive;
	}
	
	public final boolean isIsRepetitive() {
		return this.isRepetitiveProperty().get();
	}
	
	public final void setIsRepetitive(final boolean isRepetitive) {
		this.isRepetitiveProperty().set(isRepetitive);
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
	
	public final ObjectProperty<LocalDate> startDateProperty() {
		return this.startDate;
	}
	
	public final LocalDate getStartDate() {
		return this.startDateProperty().get();
	}
	
	public final void setStartDate(final LocalDate startDate) {
		this.startDateProperty().set(startDate);
	}

	public final String getPicture() {return picture;}

	public final void setPicture(final String picture) {this.picture = picture;}
	
	public Ad( int id, int uId, boolean isMaterial, boolean isRequest, String title, int cost,
			String description, int categoryId, boolean isAvailable, String localization, String comments,
			LocalDate startDate, int duration, boolean isRepetitive, LocalDate endDate, String picture )
	{
		this.id = id;
		this.userId = uId;
		this.isMaterial = isMaterial;
		this.isRequest = new SimpleBooleanProperty(this, "isRequest", isRequest);
		this.title = new SimpleStringProperty(this, "title", title);
		this.cost = new SimpleIntegerProperty(this, "cost", cost);
		this.description = new SimpleStringProperty(this, "description", description);
		this.categoryId = new SimpleIntegerProperty(this, "categoryId", categoryId);
		this.isAvailable = new SimpleBooleanProperty(this, "isAvailable", isAvailable);
		this.localization = new SimpleStringProperty(this, "Localization", localization);
		this.comments = new SimpleStringProperty(this, "comments", comments);
		this.startDate = new SimpleObjectProperty<>(this, "startDate", startDate);
		this.durationInDay = new SimpleIntegerProperty(this, "durationInDay", duration);
		this.isRepetitive = new SimpleBooleanProperty(this, "isRepetitive", isRepetitive);
		this.endDate = new SimpleObjectProperty<>(this, "endDate", endDate);
		this.picture = picture;
	}
	
	public Ad( int uId, boolean isMaterial )
	{
		this(0,uId,isMaterial,false,"",0,"",0,true,"","",LocalDate.now(),1,false, null, "DefaultPicture.jpg");
	}
}
