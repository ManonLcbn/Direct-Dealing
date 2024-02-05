package eu.telecomnancy.base;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MatCat {

    private final SimpleIntegerProperty id = new SimpleIntegerProperty();
    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleIntegerProperty suggestedCost = new SimpleIntegerProperty();
	

	public final SimpleIntegerProperty idProperty() {
		return this.id;
	}
	
	public final int getId() {
		return this.idProperty().get();
	}
	
	public final void setId(final int id) {
		this.idProperty().set(id);
	}
	
	public final SimpleStringProperty nameProperty() {
		return this.name;
	}

	public final String getName() {
		return this.nameProperty().get();
	}

	public final void setName(final String name) {
		this.nameProperty().set(name);
	}
	
	public final SimpleIntegerProperty suggestedCostProperty() {
		return this.suggestedCost;
	}
	
	public final int getSuggestedCostd() {
		return this.suggestedCostProperty().get();
	}
	

	public final void setSuggestedCost(final int suggestedCostd) {
		this.suggestedCostProperty().set(suggestedCostd);
	}

	
	@Override
	public String toString() {
		return this.name.get();
	}
	
	public MatCat( int id, String name, int suggestedCost ) {
        this.id.set(id);
        this.name.set(name);
        this.suggestedCost.set(suggestedCost);
	}

	
}
