package com.ksmledger.frames;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;



import java.net.URL;
import java.util.ResourceBundle;


public class FXMLDocumentController implements Initializable {
    @FXML
    private Label progress;
    public static Label label;

    @FXML
    private ProgressBar progressbar;
    public static ProgressBar progressBar;

    @FXML
    private Label lblWelcome;
    public  static Label welcome;





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        label=progress;
        progressBar=progressbar;
        welcome=lblWelcome;
    }

}
