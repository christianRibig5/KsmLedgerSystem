package com.ksmledger.frames;

import com.ksmledger.utils.ConnectionUtil;
import com.ksmledger.utils.LoggedInAdminUserId;
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

public class DuesController implements Initializable {

    @FXML
    private ImageView backButton;

    @FXML
    private Label closeButton;

    @FXML
    private TextField membershipID;

    @FXML
    private Button saveButton;

 

    @FXML
    private DatePicker createdAt;

    @FXML
    private TextField outstandingBalance;

    @FXML
    private TextField yearlyBudget;

    @FXML
    private TextField hallLevy;

    @FXML
    private TextField otherLevy;

    private Connection connection=null;
    private PreparedStatement preparedStatement=null;
    private ResultSet resultSet=null;
    private LoggedInAdminUserId loggedInAdminUserId =new LoggedInAdminUserId();
    private Statement statement=null;

    public DuesController(){
        connection= ConnectionUtil.connectDB();
    }

    @FXML
    void saveAction() {

        Window owner = saveButton.getScene().getWindow();
        if(membershipID.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your membership Id");
            return;
        }
        checkIfMembershipIDAlreadyExist(membershipID.getText());

        if(outstandingBalance.getText()==null|| outstandingBalance.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter the previous outstanding");
            return;
        }
        if(yearlyBudget.getText()==null || yearlyBudget.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter yearly budget");
            return;
        } if(hallLevy.getText()==null || hallLevy.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Hall Levy!",
                    "");
            hallLevy.setText("0");
            return;
        }
        if(otherLevy.getText()==null || otherLevy.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Other Levy!",
                    "No other levy?");
            otherLevy.setText("0");
            return;
        }


        if(createdAt.getEditor().getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter date");
            return;
        }
        if(!checkIfMemberDuesAlreadyCreated(getUserID(membershipID.getText()))){

            String query="INSERT INTO ksm_dues(user_id,previous_outstanding,yearly_budget,ksm_hall_levy,other_levies,total_dues,total_dues_paid,unpaid_balance, created_at, updated_at)" +
                    "VALUES('"+getUserID(membershipID.getText())+"',?,?,?,?,'"+totalDues()+"',0.0,'"+totalUnpaidDues()+"',?,?)";
            try {
                preparedStatement=connection.prepareStatement(query);
                preparedStatement.setDouble(1, Double.parseDouble(outstandingBalance.getText()));
                preparedStatement.setDouble(2, Double.parseDouble(yearlyBudget.getText()));
                preparedStatement.setDouble(3, Double.parseDouble(hallLevy.getText()));
                preparedStatement.setDouble(4, Double.parseDouble(otherLevy.getText()));
                preparedStatement.setString(5,createdAt.getEditor().getText());
                preparedStatement.setString(6,createdAt.getEditor().getText());
                preparedStatement.executeUpdate();
                showAlert(Alert.AlertType.CONFIRMATION, owner, "Dues saved Successfully!", "Thank you!");
                clearText();
            }catch (SQLException ex){
                Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null,ex);
                return;
            }
        }


    }

    private boolean checkIfMemberDuesAlreadyCreated(int userId) {
        boolean duesIsCreated=false;
        String checkQuery= "SELECT * from ksm_dues WHERE user_id = '" +userId+ "'";
        try {
            preparedStatement=connection.prepareStatement(checkQuery);
            resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                infoBox("Sorry dues for this member already created",null,"failed");
                duesIsCreated=true;
            }
        } catch (SQLException e) {
            Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null,e);
        }
        return duesIsCreated;
    }

    private double totalUnpaidDues() {
        return totalDues();
    }

    private double totalDues() {
        double prevBal=Double.valueOf(outstandingBalance.getText().toString());
        double yearlyBudg=Double.valueOf(yearlyBudget.getText().toString());
        double halLevy=Double.valueOf(hallLevy.getText().toString());
        double odaLevies=Double.valueOf(otherLevy.getText().toString());;
//        if(!outstandingBalance.getText().isEmpty()) {
//            prevBal=Double.valueOf(outstandingBalance.getText().toString());
//        }
//        if(!yearlyBudget.getText().isEmpty()){
//            yearlyBudg=Double.valueOf(yearlyBudget.getText().toString());
//        }
//       if(!hallLevy.getText().isEmpty()){
//           halLevy=Double.valueOf(hallLevy.getText().toString());
//       }
//       if(!otherLevy.getText().isEmpty()){
//           odaLevies=Double.valueOf(otherLevy.getText().toString());
//       }

        return (prevBal+yearlyBudg+halLevy+odaLevies);
    }



    private int getUserID(String text) {
        String checkQuery= "SELECT id  from ksm_users WHERE membership_id = '" + text + "'";
        int id=0;
        try {
            preparedStatement=connection.prepareStatement(checkQuery);
            resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                id=resultSet.getInt(1);
                loggedInAdminUserId.setLoggedAdminUserId(id);
            }
        } catch (SQLException e) {
            Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null,e);
        }

        return id;
    }

    private void checkIfMembershipIDAlreadyExist(String text) {
        String checkQuery= "SELECT * from ksm_users WHERE membership_id = '" + text + "'";
        try {
            preparedStatement=connection.prepareStatement(checkQuery);
            resultSet=preparedStatement.executeQuery();
            if(!resultSet.next()){
                infoBox("Sorry this membership_id is invalid",null,"failed");
            }
        } catch (SQLException e) {
            Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null,e);
        }
    }

    private static  void infoBox(String infoMessage, String headerText, String title){
        Alert alert =new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setHeaderText(headerText);
        alert.setTitle(title);
        alert.showAndWait();
    }

    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }


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
        membershipID.clear();
        outstandingBalance.clear();
        yearlyBudget.clear();
        hallLevy.clear();
        otherLevy.clear();
        createdAt.getEditor().clear();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
