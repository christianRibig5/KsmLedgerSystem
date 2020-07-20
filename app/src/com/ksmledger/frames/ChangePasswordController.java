package com.ksmledger.frames;

import com.ksmledger.utils.ConnectionUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ChangePasswordController implements Initializable {

    @FXML
    private TextField txtMembershipID;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnUpdatePassword;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private Label closeButton;
    private Connection connection=null;
    private PreparedStatement preparedStatement=null;
    private ResultSet resultSet=null;
    public ChangePasswordController(){
        connection= ConnectionUtil.connectDB();
    }

    @FXML
    void closeAction() {
        try {
            Stage stage=(Stage)closeButton.getScene().getWindow();
            stage.close();
            Scene scene= new Scene(FXMLLoader.load(getClass().getResource("login.fxml")));
            stage.setScene(scene);
            stage.show();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    void updatePasswordAction() {
        Window owner = btnUpdatePassword.getScene().getWindow();
        if(txtMembershipID.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter the membership ID of the member to search");
            return;
        }
        if(txtPassword.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter new password");
            return;
        }
        if(confirmPassword.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please retype the  password");
            return;
        }

        String sql="UPDATE ksm_admin SET password = ? WHERE membership_id = '"+txtMembershipID.getText()+"'";
        String password= txtPassword.getText();
        String confirmPass=confirmPassword.getText();
        if(!password.equals(confirmPass)){
            infoBox("Password did not match",null,"failure");
        }else{
            try {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1,txtPassword.getText());
                preparedStatement.executeUpdate();
                infoBox("Password change was successful",null,"Success");
                clearText();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private void showAlert(Alert.AlertType error, Window owner, String title, String message) {
        Alert alert = new Alert(error);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    private void clearText() {
        txtMembershipID.clear();
        txtPassword.clear();
        confirmPassword.clear();
    }

    private void infoBox(String message, String header, String title) {
        Alert alert =new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(message);
        alert.setHeaderText(header);
        alert.setTitle(title);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
