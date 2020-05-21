package com.ksmledger.frames;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
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

    @FXML
    void debitAction(ActionEvent event) {

    }

    @FXML
    void ledgerAction(ActionEvent event) {

    }

    @FXML
    void recordPaymentAction(ActionEvent event) {

    }

    @FXML
    void registerMemberAction(ActionEvent event) {
        try {
            Node node =(Node)event.getSource();
            Stage stage=(Stage)node.getScene().getWindow();
            stage.close();
            Scene scene= null;
            scene = new Scene(FXMLLoader.load(getClass().getResource("memberRegistration.fxml")));
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void viewMemberAction(ActionEvent event) {

    }

    @FXML
    void closeAction() {
        Stage mainStage = (Stage) exit.getScene().getWindow();
        mainStage.close();

    }



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
