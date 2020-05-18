package com.ksmledger.frames;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class DrawerContentController implements Initializable {


    @FXML
    private Button registerMember;
    @FXML
    private Button recordPayment;
    @FXML
    private Button viewMembers;

    @FXML
    private Button ksmLedger;
    @FXML
    private Button debitLedger;
    @FXML
    private Button exit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        registerMember.setAlignment(Pos.BASELINE_LEFT);
        recordPayment.setAlignment(Pos.BASELINE_LEFT);
        viewMembers.setAlignment(Pos.BASELINE_LEFT);
        ksmLedger.setAlignment(Pos.BASELINE_LEFT);
        debitLedger.setAlignment(Pos.BASELINE_LEFT);
        exit.setAlignment(Pos.BASELINE_LEFT);
    }
}
