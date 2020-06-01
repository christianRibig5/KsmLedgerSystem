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

public class DebitLedgerController implements Initializable {
    @FXML
    private ImageView backButton;

    @FXML
    private Label closeButton;

    @FXML
    private Button debitButton;

    @FXML
    private DatePicker withdrawalDate;

    @FXML
    private TextField debitAmount;

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
    private Statement statement=null;

    public DebitLedgerController(){connection= ConnectionUtil.connectDB(); }
    @FXML
    void debitAction() {
        Window owner = debitButton.getScene().getWindow();

        if(debitAmount.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter the debit amount");
            return;
        }

        if(withdrawalDate.getEditor().getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter date");
            return;
        }
        String query="INSERT INTO ksm_ledgers(user_id, transaction_date, debit, credit, particulars)" +
                "VALUES('"+LoggedInAdminUserId.getUserId()+"', ?,?,0.00,'KSM Project')";
        try {
            preparedStatement=connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,withdrawalDate.getEditor().getText());
            preparedStatement.setString(2, debitAmount.getText());
            preparedStatement.executeUpdate();
            showAlert(Alert.AlertType.CONFIRMATION, owner,
                    "Your debit was Successful!", "Thank you!");
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

    @FXML
    void resetAllAction() { clearText(); }

    private void clearText() {
        debitAmount.clear();
        withdrawalDate.getEditor().clear();
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
