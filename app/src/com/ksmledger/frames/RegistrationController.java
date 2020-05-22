package com.ksmledger.frames;

import com.ksmledger.utils.ConnectionUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable {
    @FXML
    private ImageView backButton;

    @FXML
    private Label closeButton;
    @FXML
    private Button resetAllButton;

    @FXML
    private Button saveButton;
    @FXML
    private TextField firstName;

    @FXML
    private TextField membershipID;

    @FXML
    private DatePicker initiationDate;

    @FXML
    private TextField city=null;

    @FXML
    private TextField middleName=null;

    @FXML
    private TextField lastName;

    @FXML
    private TextField email=null;

    @FXML
    private TextField phone;

    @FXML
    private DatePicker dateDob=null;

    @FXML
    private TextArea address=null;

    @FXML
    private TextField state=null;

    @FXML
    private TextField country=null;

    private Connection connection=null;
    private PreparedStatement preparedStatement=null;
    private ResultSet resultSet=null;

    public RegistrationController(){ connection= ConnectionUtil.connectDB();}

    @FXML
    void backAction() {
        try {
            Stage stage=(Stage)backButton.getScene().getWindow();
            stage.close();
            Scene scene= new Scene(FXMLLoader.load(getClass().getResource("dashboard.fxml")));
            stage.setScene(scene);
            stage.show();
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

    @FXML
    void closeAction() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }


    @FXML
    void resetAllAction() {
            clearText();
    }

    private void clearText() {
        firstName.clear();
        middleName.clear();
        lastName.clear();
        membershipID.clear();
        email.clear();
        phone.clear();
        initiationDate.getEditor().clear();
        address.clear();
        city.clear();
        state.setPromptText("State");
        country.setPromptText("Country");
        dateDob.getEditor().clear();

    }
    @FXML
    void saveAction() {
        Window owner = saveButton.getScene().getWindow();
        if(firstName.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter firstName");
            return;
        }
        if(lastName.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter lastName");
            return;
        }
        if(membershipID.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your membership ID");
            return;
        }
        if(phone.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter phone number");
            return;
        }

        if(initiationDate.getEditor().getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter initiation date");
            return;
        }
        checkIfDataAlreadyExist(membershipID.getText());


        String query="INSERT INTO ksm_users(firstname, middlename, lastname, membership_id," +
                "email, phone, initiation_date, address, city, state, country, avatar_path, role_id)" +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,null,2)";
        try {
            preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,firstName.getText());
            preparedStatement.setString(2,middleName.getText());
            preparedStatement.setString(3,lastName.getText());
            preparedStatement.setString(4,membershipID.getText());
            preparedStatement.setString(5,email.getText());
            preparedStatement.setString(6,phone.getText());
            preparedStatement.setString(7,initiationDate.getEditor().getText());
            preparedStatement.setString(8,address.getText());
            preparedStatement.setString(9,city.getText());
            preparedStatement.setString(10,state.getText());
            preparedStatement.setString(11,country.getText());
            preparedStatement.executeUpdate();
            showAlert(Alert.AlertType.CONFIRMATION, owner,
                    "Registration Successful!",
                    "Welcome " + firstName.getText()+" "+lastName.getText());
            clearText();
        }catch (SQLException ex){
            ex.printStackTrace();
        }


    }

    private void checkIfDataAlreadyExist(String text) {
       String checkQuery= "SELECT * from ksm_users WHERE membership_id = '" + text + "'";
        try {
            preparedStatement=connection.prepareStatement(checkQuery);
            resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                infoBox("membership id already exist",null,"failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
    private static  void infoBox(String infoMessage, String headerText, String title){
        Alert alert =new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setHeaderText(headerText);
        alert.setTitle(title);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
