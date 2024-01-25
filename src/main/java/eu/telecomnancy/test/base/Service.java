package eu.telecomnancy.test.base;

import javafx.beans.property.*;
import java.time.*;


public class Service extends Annonce {
	

	private int id;
    private int userId;
	private SimpleBooleanProperty isRequest;
	private SimpleStringProperty title;
	private SimpleStringProperty description;
	private SimpleIntegerProperty categoryId;
    private SimpleBooleanProperty isAvailable;

	
	private SimpleStringProperty localization;
	private SimpleStringProperty comments;
    private SimpleBooleanProperty isPonctual;
    private ObjectProperty<LocalDate> startDate;
    private ObjectProperty<LocalDate> endDate;


	private SimpleStringProperty day;
	private SimpleIntegerProperty hour;
	private SimpleIntegerProperty minutes;

    private SimpleIntegerProperty cost;

    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public final int getUserId() {
		return this.userId;
	}
	public final void setUserId(final int userId) {
		this.userId = userId;
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

    public final SimpleStringProperty titleProperty() {
		return this.title;
	}
	public final String getTitle() {
		return this.titleProperty().get();
	}
	public final void setTitle(final String title) {
		this.titleProperty().set(title);
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

    public final SimpleBooleanProperty isPonctualProperty() {
		return this.isPonctual;
	} 
	public final boolean isIsPonctual() {
        return this.isPonctualProperty().get();
    }
    public final void setIsPonctual(final boolean isPonctual) {
		this.isPonctualProperty().set(isPonctual);
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

    public final SimpleStringProperty dayProperty() {
		return this.day;
	}
	public final String getDay() {
		return this.dayProperty().get();
	}
	public final void setDay(final String day) {
		this.dayProperty().set(day);
	}

    public final SimpleIntegerProperty hourProperty() {
		return this.hour;
	}
	public final int getHour() {
		return this.hourProperty().get();
	}
	public final void setHour(final int hour) {
		this.hourProperty().set(hour);
	}

    public final SimpleIntegerProperty minutesProperty() {
		return this.minutes;
	}
	public final int getMinutes() {
		return this.minutesProperty().get();
	}
	public final void setMinutes(final int minutes) {
		this.minutesProperty().set(minutes);
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

	
	public Service( int id, int uId, boolean isRequest, String title,
			String description, int categoryId, boolean isAvailable, String localization, String comments, boolean isPonctual,
			LocalDate startDate, LocalDate endDate, String day, int hour,int minutes, int cost )
	{
		this.id = id;
		this.userId = uId;
		this.isRequest = new SimpleBooleanProperty(this, "isRequest", isRequest);
		this.title = new SimpleStringProperty(this, "title", title);
		this.description = new SimpleStringProperty(this, "description", description);
		this.categoryId = new SimpleIntegerProperty(this, "categoryId", categoryId);
        this.isAvailable = new SimpleBooleanProperty(this, "isAvailable", isAvailable);
		this.localization = new SimpleStringProperty(this, "Localization", localization);
		this.comments = new SimpleStringProperty(this, "comments", comments);
        this.isPonctual = new SimpleBooleanProperty(this, "isPonctual", isPonctual);
		this.startDate = new SimpleObjectProperty<>(this, "startDate", startDate);
        this.endDate = new SimpleObjectProperty<>(this, "endDate", endDate);
		this.day = new SimpleStringProperty(this, "day", day);
		this.hour = new SimpleIntegerProperty(this, "hour", hour);
		this.minutes = new SimpleIntegerProperty(this, "minutes", minutes);
        this.cost = new SimpleIntegerProperty(this, "cost", cost);

	}
	
	public Service( int uId)
	{
		this(0,uId,false,"","",0,true,"","",true,LocalDate.now(),null,"lundi",0, 0, 0);
	}

    
}

