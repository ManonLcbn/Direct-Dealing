module eu.telecomnancy {
	requires javafx.controls;
	requires transitive java.sql;
	requires transitive javafx.graphics;
	requires javafx.fxml;
	requires java.desktop;
	requires transitive javafx.base;
	
	opens eu.telecomnancy to javafx.graphics, javafx.fxml, javafx.base;
	opens eu.telecomnancy.base to javafx.fxml;

	exports eu.telecomnancy;
	exports eu.telecomnancy.base;
}