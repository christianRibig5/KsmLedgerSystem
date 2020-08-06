package com.ksmledger.frames;

import com.ksmledger.utils.ConnectionUtil;
import com.ksmledger.utils.CurrentDate;
import com.ksmledger.utils.EmailPattern;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrationController implements Initializable {
    @FXML
    private ImageView backButton;

    @FXML
    private Label closeButton;

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

    @FXML
    private TextField outstandingDebt;

    private Connection connection=null;
    private PreparedStatement preparedStatement=null;
    private ResultSet resultSet=null;
    private int lastStoredUserID=0;

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
        state.clear();
        country.clear();
        dateDob.getEditor().clear();
        outstandingDebt.clear();
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
        checkIfMembershipIDAlreadyExist(membershipID.getText());

        if(phone.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter phone number");
            return;
        }
        checkIfPhoneNumberAlreadyExist(phone.getText());
        if(initiationDate.getEditor().getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter initiation date");
            return;
        }
        if(email.getText().isEmpty() || !EmailPattern.matches(email.getText())){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter valid email address");
            return;

        }
        checkIfEmailAlreadyExist(email.getText());
        if(outstandingDebt.getText()==null || outstandingDebt.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Outstanding Debt!",
                    "No outstanding debt?");
            outstandingDebt.setText("0");
            return;
        }

        String query="INSERT INTO ksm_users(firstname, middlename, lastname, membership_id," +
                "email, phone, initiation_date, address, city, state, country, avatar_path, role_id)" +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,null,2)";
        try {
            preparedStatement=connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
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
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            int generatedKey=0;
            if (resultSet.next()) {
                generatedKey = resultSet.getInt(1);
//                showAlert(Alert.AlertType.ERROR, owner, "Success!!",
//                        "Welcome "+firstName.getText()+" "+lastName.getText());
                infoBox("Registration successful for "+firstName.getText()+" "+lastName.getText(),null,"Registration Success!!");
            }
            lastStoredUserID=generatedKey;
            recordDebt();

        }catch (SQLException ex){
            Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null,ex);
            return;
        }

    }



    private void recordDebt() {
        String query="INSERT INTO ksm_dues(user_id, previous_outstanding, yearly_budget, ksm_hall_levy, other_levies, total_dues, total_dues_paid,unpaid_balance, created_at, updated_at)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,lastStoredUserID);
            preparedStatement.setDouble(2, Double.parseDouble(outstandingDebt.getText()));
            preparedStatement.setDouble(3,0.0);
            preparedStatement.setDouble(4,0.0);
            preparedStatement.setDouble(5,0.0);
            preparedStatement.setDouble(6,totalDues());
            preparedStatement.setDouble(7,0.0);
            preparedStatement.setDouble(8,totalUnpaidDues());
            preparedStatement.setString(9,CurrentDate.getCurrentDate());
            preparedStatement.setString(10,CurrentDate.getCurrentDate());
            preparedStatement.executeUpdate();
            clearText();
        }catch (SQLException ex){
            Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null,ex);
            return;
        }

    }

//    private int getUserID() {
//        return 0;
//    }

    private double totalUnpaidDues() {
        return totalDues();
    }

    private double totalDues() {
        double prevBal=Double.valueOf(outstandingDebt.getText());
        double yearlyBudget=0.0;
        double halLevy=0.0;
        double odaLevies=0.0;


        return (prevBal+yearlyBudget+halLevy+odaLevies);
    }

    private void checkIfMembershipIDAlreadyExist(String text) {
        String checkQuery= "SELECT * from ksm_users WHERE membership_id = '" + text + "'";
        try {
            preparedStatement=connection.prepareStatement(checkQuery);
            resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                infoBox("Sorry this membership id already exist",null,"failed");
            }
        } catch (SQLException e) {
            Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null,e);
        }
    }

    private void checkIfEmailAlreadyExist(String text) {
        //if(text!=null||!text.isEmpty()){
            String checkQuery= "SELECT * from ksm_users WHERE email = '" + text + "'";
            try {
                preparedStatement=connection.prepareStatement(checkQuery);
                resultSet=preparedStatement.executeQuery();
                if(resultSet.next()){
                    infoBox("Sorry this email has been taken",null,"failed");
                }
            } catch (SQLException e) {
                Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null,e);
            }
       // }
    }

    private void checkIfPhoneNumberAlreadyExist(String text) {
        if(text!=null||!text.isEmpty()){
            String checkQuery= "SELECT * from ksm_users WHERE phone = '" + text + "'";
            try {
                preparedStatement=connection.prepareStatement(checkQuery);
                resultSet=preparedStatement.executeQuery();
                if(resultSet.next()){
                    infoBox("Sorry this phone number has been taken",null,"failed");
                }
            } catch (SQLException e) {
                Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null,e);
            }
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
