package eu.telecomnancy.test;

import java.io.IOException;

import javafx.scene.layout.GridPane;

public interface GenericView  {
	
	public GridPane loadPage( int usrId, int selectedAdId, AppController appCtrl ) throws IOException;
	
}