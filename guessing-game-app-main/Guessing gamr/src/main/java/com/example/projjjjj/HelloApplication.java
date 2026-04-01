package com.example.projjjjj;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage)  {
try{
Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
Scene scene= new Scene(root);
stage.setScene(scene);
stage.show();
} catch (Exception e) {
e.printStackTrace();
}


}
public static void main ( String[] args) {
launch(args);
}
}
