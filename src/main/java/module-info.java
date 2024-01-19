module eu.telecomnancy.test {
	requires javafx.controls;
	requires transitive java.sql;
	requires transitive javafx.graphics;
	requires javafx.fxml;
	requires java.desktop;
	requires transitive javafx.base;
	
	opens eu.telecomnancy.test to javafx.graphics, javafx.fxml, javafx.base;
	opens eu.telecomnancy.test.base to javafx.fxml;

	exports eu.telecomnancy.test;
	exports eu.telecomnancy.test.base;
}