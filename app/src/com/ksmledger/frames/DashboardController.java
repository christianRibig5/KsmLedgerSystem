package com.ksmledger.frames;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import com.jfoenix.controls.JFXHamburger;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DashboardController implements Initializable {


    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXHamburger hamburger;

    @FXML
    private JFXDrawer drawer;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            VBox box= FXMLLoader.load(getClass().getResource("drawerContent.fxml"));
            drawer.setSidePane(box);
            HamburgerBackArrowBasicTransition burgerTask = new HamburgerBackArrowBasicTransition(hamburger);
            burgerTask.setRate(-1);
            hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e)->{
                burgerTask.setRate(burgerTask.getRate()*-1);
                burgerTask.play();
                if(drawer.isOpened()){
                    drawer.close();
                } else{
                    drawer.open();
                }

            });

        }catch(IOException ex){
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
}
