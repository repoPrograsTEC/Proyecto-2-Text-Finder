package Interfaz;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String [] arg){
        launch(arg);
    }
    @Override
    public void start(Stage primaryStage) throws  Exception{
        primaryStage.setTitle("Text Finder");
        VBox vbox=new VBox();
        Scene scene = new Scene(vbox, 1100, 700);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
