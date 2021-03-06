package com.ksmledger.frames;

import javafx.application.Application;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class KsmMainApp extends Application {
    private static  final int COUNT_LIMIT=10;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene= new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();

    }

    @Override
    public void init() throws Exception {
        for(int i=1;i<=COUNT_LIMIT;i++){
            double progress=(double) i/10;
            System.out.println("progress :"+progress);
            notifyPreloader(new Preloader.ProgressNotification(progress));
            Thread.sleep(500);
        }
    }


    public static void main(String[] args) {
        System.setProperty("javafx.preloader", KsmPreLoader.class.getCanonicalName());
        Application.launch(KsmMainApp.class, args);
    }
}
