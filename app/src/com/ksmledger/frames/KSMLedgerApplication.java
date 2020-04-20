package com.ksmledger.frames;

import javafx.application.Application;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class KSMLedgerApplication extends Application {
    private static  final int COUNT_LIMIT=10;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Scene scene= new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setWidth(1000);
        primaryStage.setHeight(700);
        primaryStage.show();

    }

    @Override
    public void init() throws Exception {
        for(int i=1;i<=COUNT_LIMIT;i++){
            double progress=(double) i/10;
            System.out.println("progress :"+progress);
            notifyPreloader(new Preloader.ProgressNotification(progress));
            Thread.sleep(1000);
        }
    }


    public static void main(String[] args) {
        System.setProperty("javafx.preloader", KsmPreLoader.class.getCanonicalName());
        Application.launch(KSMLedgerApplication.class, args);
    }
}
