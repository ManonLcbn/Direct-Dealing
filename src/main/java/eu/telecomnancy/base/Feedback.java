package eu.telecomnancy.base;

import java.time.LocalDate;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Feedback {

	int id;
	int userId;
	private SimpleStringProperty username;
	int adId;
	
	LocalDate createdDate;
	
	private SimpleIntegerProperty note;
	private SimpleStringProperty comments;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getAdId() {
		return adId;
	}
	public void setAdId(int adId) {
		this.adId = adId;
	}
	public final SimpleStringProperty usernameProperty() {
		return this.username;
	}
	
	public final String getUsername() {
		return this.usernameProperty().get();
	}
	
	public final void setUsername(final String username) {
		this.usernameProperty().set(username);
	}
	public LocalDate getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}
	public final SimpleIntegerProperty noteProperty() {
		return this.note;
	}
	
	public final int getNote() {
		return this.noteProperty().get();
	}
	
	public final void setNote(final int note) {
		this.noteProperty().set(note);
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
	
	public Feedback(int ID, int userId, String username, int adId, LocalDate date, int note, String comments )
	{
		this.id = ID;
		this.userId = userId;
		this.username = new SimpleStringProperty(this, "username", username);
		this.adId = adId;
		this.createdDate = date;
		this.note = new SimpleIntegerProperty(this, "note", note);
		this.comments = new SimpleStringProperty(this, "comments", comments);
	}
	
	public Feedback()
	{
		this(0,0,"Inconnu",0,LocalDate.now(),1,"");
	}

}
