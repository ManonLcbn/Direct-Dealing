package eu.telecomnancy.base;

import javafx.beans.property.*;

public class User {

	private int id;
	private StringProperty name;
	private StringProperty email;
	private StringProperty password;
	private BooleanProperty isDisable;
	private StringProperty availability;
	private IntegerProperty famount;
	private String Picture;
	private Integer admin;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email.get();
	}

	public void setEmail(String email) {
		this.email.set(email);
	}
	
    public StringProperty emailProperty() {
        return email;
    }

	public String getPassword() {
		return password.get();
	}

	public void setPassword(String password) {
		this.password.set(password);
	}

    public StringProperty passwordProperty() {
        return password;
    }

	public boolean isDisable() {
		return (isDisable.get());
	}

	public void setActive(boolean isDisable) {
		this.isDisable.set(isDisable);
	}
	
    public BooleanProperty isDisableProperty() {
        return isDisable;
    }

	public String getAvailability() {
		return availability.get();
	}

	public void setAvailability(char availability[]) {
		this.availability.set(String.valueOf(availability));
	}

    public StringProperty availabilityProperty() {
        return availability;
    }

	public int getFamount() {
		return famount.get();
	}

	public void setFamount(int fAmount) {
		famount.set(fAmount);
	}

    public IntegerProperty famountProperty() {
        return famount;
    }

	public String getName() {
		return name.get();
	}
	
	public void setName( String name ) {
		this.name.set(name);
	}
	
    public StringProperty nameProperty() {
        return name;
    }

	public String getPicture() {return Picture;}

	public void setPicture(String picture) {this.Picture = picture;}

	public Integer getAdmin() {return admin;}

	public void setAdmin(Integer admin) {this.admin = admin;}
	
	public User( int id, String name, String email, String password, boolean isActive, String Availability,
			int f_amount, String picture, int admin)
	{
		this.id = id;
		this.name = new SimpleStringProperty(this, "name", name);
		this.email = new SimpleStringProperty(this, "email", email);
		this.password = new SimpleStringProperty(this, "password", password);
		this.isDisable = new SimpleBooleanProperty(this, "isDisable", isActive);
		this.availability = new SimpleStringProperty(this, "availability", Availability);
		this.famount = new SimpleIntegerProperty(this, "famount", f_amount);
		this.Picture = picture;
		this.admin = admin;
	}
	
	public User()
	{
		this(0,"","","",false,"xxxxxxxxxxxx",150, "UserPicture.jpg", 0);
	}
}
