package com.ksmledger.frames;



import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;


import java.net.URL;
import java.util.ResourceBundle;


public class FXMLDocumentController implements Initializable {
    @FXML
    private Label progress;
    public static Label label;

    @FXML
    private ProgressBar progressbar;
    public static ProgressBar progressBar;

    @FXML private Label closeButton;

    @FXML
    public void closeAction(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        label=progress;
        progressBar=progressbar;
    }

}
