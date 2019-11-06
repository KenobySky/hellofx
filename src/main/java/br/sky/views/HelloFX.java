package br.sky.views;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloFX extends Application {

    @Override
    public void start(Stage stage) {
        try {
            Parent root;

            root = FXMLLoader.load(getClass().getResource("/fxml/MainView.fxml"));

            Scene scene = new Scene(root);
            //scene.getStylesheets().add("/styles/MainSceneStyle.css");

            stage.setTitle("Fruit - JavaFX & Hibernate");
            stage.setScene(scene);
           

            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(HelloFX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        launch();
    }

}
