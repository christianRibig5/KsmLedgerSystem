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

public class RecordPaymentController implements Initializable {
    @FXML
    private ImageView backButton;

    @FXML
    private Label closeButton;

    @FXML
    private TextField membershipID;

    @FXML
    private Button creditButton;

    @FXML
    private DatePicker paymentDate;

    @FXML
    private TextField creditAmount;

    @FXML
    private ComboBox<String> paymentParticulars;

    public RecordPaymentController(){
        connection= ConnectionUtil.connectDB();
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

    private Connection connection=null;
    private PreparedStatement preparedStatement=null;
    private ResultSet resultSet=null;
    private LoggedInAdminUserId loggedInAdminUserId =new LoggedInAdminUserId();
    private Statement statement=null;

    @FXML
    void resetAllAction() {
        clearText();
    }

    @FXML
    void creditAction() {
        Window owner = creditButton.getScene().getWindow();
        if(membershipID.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your membership Id of the member that made this payment");
            return;
        }
        checkIfMembershipIDAlreadyExist(membershipID.getText());
        if(creditAmount.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter the credit amount");
            return;
        }
        if(paymentParticulars.getSelectionModel().getSelectedItem()==null){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please choose reason for payment");
            return;
        }
        if(paymentDate.getEditor().getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter date this payment was made");
            return;
        }
        String query="INSERT INTO ksm_ledgers(user_id, transaction_date, debit, credit, particulars)" +
                "VALUES('"+getUserID(membershipID.getText())+"',?,0.00, ?,?)";
        try {
            preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,paymentDate.getEditor().getText());
            preparedStatement.setString(2, creditAmount.getText());
            preparedStatement.setString(3,paymentParticulars.getSelectionModel().getSelectedItem());
            preparedStatement.executeUpdate();
            showAlert(Alert.AlertType.CONFIRMATION, owner,
                    "Account credit Successful!", "Thank you!");
            validateVault();
            clearText();
        }catch (SQLException ex){
            Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null,ex);
            return;
        }

    }

    private void validateVault() {
        double debit=getAllDebits();
        double credit =getAllCredits();
        double balance=credit-debit;
        updateVault(balance);
    }

    private void updateVault(double vaultBalance) {
        String sql="UPDATE ksm_vault" +
                " SET balance =? WHERE id = 1";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1,vaultBalance );
            preparedStatement.executeUpdate();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    private double getAllCredits() {
        double sum=0;
        String sql="SELECT SUM(credit) FROM ksm_ledgers";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                double credit = resultSet.getDouble(1);
                sum = sum + credit;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sum;
    }

    private double getAllDebits() {
        double sum=0;
        String sql="SELECT SUM(debit) FROM ksm_ledgers";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                double debit = resultSet.getDouble(1);
                sum = sum + debit;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sum;
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

    private void clearText() {
        membershipID.clear();
        creditAmount.clear();
        paymentDate.getEditor().clear();
        paymentParticulars.getSelectionModel().clearSelection();
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
        paymentParticulars.getItems().addAll("Monthly Dues", "Levy", "Fine");
    }
}
