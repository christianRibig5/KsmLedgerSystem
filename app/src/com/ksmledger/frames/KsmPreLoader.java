package com.ksmledger.frames;

import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class KsmPreLoader extends Preloader {

    Stage preLoaderStage= new Stage();
    Scene scene;

    @Override
    public void init() throws Exception{
        Parent root1= FXMLLoader.load(getClass().getResource("splash_screen.fxml"));
        scene=new Scene(root1);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.preLoaderStage =primaryStage;
        preLoaderStage.initStyle(StageStyle.UNDECORATED);
        preLoaderStage.setScene(scene);
        preLoaderStage.show();
    }

    @Override
    public void handleApplicationNotification(Preloader.PreloaderNotification info) {
        if(info instanceof ProgressNotification){
            ProgressNotification pn= (ProgressNotification) info;
            FXMLDocumentController.label.setText("Loading "+(pn.getProgress())*100 + "%");
            System.out.println("value@: "+pn.getProgress());
            FXMLDocumentController.statProgressBar.setProgress(pn.getProgress());
//            if(((ProgressNotification) info).getProgress()==50.0){
//                FXMLDocumentController.welcome.setText("WELCOME");
//            }

        }
    }

    @Override
    public void handleStateChangeNotification(Preloader.StateChangeNotification info) {
        StateChangeNotification.Type type = info.getType();
        switch(type){
            case BEFORE_START:
                System.out.println("BEFORE START");
                preLoaderStage.hide();
                break;
        }
    }
}
