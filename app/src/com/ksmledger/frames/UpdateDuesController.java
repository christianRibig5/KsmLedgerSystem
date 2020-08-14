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

public class UpdateDuesController implements Initializable{

    @FXML
    private ImageView backButton;

    @FXML
    private Label closeButton;

    @FXML
    private TextField membershipID;

    @FXML
    private Button updateButton;

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

    @FXML
    private TextField searchedMemberID;
    @FXML
    private TextField burialLevy;

    @FXML
    private TextField marriageLevy;

    @FXML
    private Label totalDues;

    @FXML
    private Label paidDues;

    @FXML
    private Label unpaidBalance;

    @FXML
    private Button findButton;
    private Connection connection=null;
    private PreparedStatement preparedStatement=null;
    private LoggedInAdminUserId loggedInAdminUserId =new LoggedInAdminUserId();
    private ResultSet resultSet=null;
    private Statement statement=null;

    private double latestTotalDues =0;
    double totalPaidDues=0;
    public UpdateDuesController(){
        connection = ConnectionUtil.connectDB();
    }

    @FXML
    void FindAction() {
        Window owner = findButton.getScene().getWindow();

        if(searchedMemberID.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter the membership ID of the member to search");
            return;
        }

        try {
            String sql="SELECT * FROM ksm_dues WHERE user_id = '"+getUserID()+"'";
            preparedStatement=connection.prepareStatement(sql);
            resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                outstandingBalance.setText(resultSet.getString("previous_outstanding"));
                yearlyBudget.setText(resultSet.getString("yearly_budget"));
                hallLevy.setText(resultSet.getString("ksm_hall_levy"));
                otherLevy.setText(resultSet.getString("other_levies"));
                createdAt.getEditor().setText(resultSet.getString("created_at"));
                burialLevy.setText(resultSet.getString("burial_levy"));
                marriageLevy.setText(resultSet.getString("marriage_levy"));
                totalDues.setText("Dues: "+resultSet.getString("total_dues"));
                paidDues.setText("Paid: "+resultSet.getString("total_dues_paid"));
                unpaidBalance.setText("Unpaid: "+resultSet.getString("unpaid_balance"));
                membershipID.setText(searchedMemberID.getText());


            }else{
                showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                        "Sorry this membership id is not registered in this system");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private int getUserID() {
        String checkQuery= "SELECT id  FROM ksm_users WHERE membership_id = '" +searchedMemberID.getText() + "'";
        int id=0;
        try {
            preparedStatement=connection.prepareStatement(checkQuery);
            resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                id=resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }

    private void showAlert(Alert.AlertType error, Window owner, String title, String message) {
        Alert alert = new Alert(error);
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
    void updateAction() {
        Window owner = updateButton.getScene().getWindow();
        if(searchedMemberID.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter the membership ID of the member to search");
            return;
        }

        String sql="UPDATE ksm_dues" +
                    " SET previous_outstanding = ?,yearly_budget = ?,burial_levy=?,marriage_levy=?, ksm_hall_levy = ?," +
                    "other_levies=? WHERE user_id = '"+getUserID()+"'";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,outstandingBalance.getText());
            preparedStatement.setString(2,yearlyBudget.getText());
            preparedStatement.setString(3,burialLevy.getText());
            preparedStatement.setString(4,marriageLevy.getText());
            preparedStatement.setString(5,hallLevy.getText());
            preparedStatement.setString(6,otherLevy.getText());
            preparedStatement.executeUpdate();
            validateUnpaidDues();
            showAlert(Alert.AlertType.CONFIRMATION, owner,
                    "Dues Update", "Data update was successful!");
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    private void validateUnpaidDues() {
        double unpaidBalance = getTotalDues()-getPaidDues();
        updateUnpaidBalance(unpaidBalance,createdAt.getEditor().getText());

    }


    private double getTotalDues() {

        double prevBal=getOutstandingB();
        double yearlyBudg=getBudget();
        double halLevy=getHallLvey();
        double burialLevey=getBurialLvey();
        double marriageLevey=getMariageLvey();
        double odaLevies=getOtherLevy();
        double totalDues =(prevBal+yearlyBudg+halLevy+burialLevey+ marriageLevey+odaLevies);
        updateTotalDues(totalDues);
        return totalDues;

    }

    private void updateTotalDues(double totalDues) {
        String sql="UPDATE ksm_dues" +
                " SET total_dues =? WHERE user_id = '"+getUserID()+"'";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1,totalDues);
            preparedStatement.executeUpdate();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    private double getMariageLvey() {
        double marriagelevy=0;
        String sql="SELECT marriage_levy FROM ksm_dues WHERE user_id='"+getUserID()+"' ";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                marriagelevy = resultSet.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return marriagelevy;
    }

    private double getBurialLvey() {
        double buriallevy=0;
        String sql="SELECT burial_levy FROM ksm_dues WHERE user_id='"+getUserID()+"' ";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                buriallevy = resultSet.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buriallevy;
    }

    private double getOtherLevy() {
        double otherlevy=0;
        String sql="SELECT other_levies FROM ksm_dues WHERE user_id='"+getUserID()+"' ";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                otherlevy = resultSet.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return otherlevy;
    }

    private double getHallLvey() {
        double halllevy=0;
        String sql="SELECT ksm_hall_levy FROM ksm_dues WHERE user_id='"+getUserID()+"' ";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                halllevy = resultSet.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return halllevy;
    }

    private double getBudget() {
        double budget=0;
        String sql="SELECT yearly_budget FROM ksm_dues WHERE user_id='"+getUserID()+"' ";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                budget = resultSet.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return budget;
    }

    private double getOutstandingB() {
        double outstandings=0;
        String sql="SELECT previous_outstanding FROM ksm_dues WHERE user_id='"+getUserID()+"' ";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                outstandings = resultSet.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return outstandings;
    }

    private void updateUnpaidBalance(double unpaidBalance, String updateTime) {
        String sql="UPDATE ksm_dues" +
                " SET unpaid_balance =?,updated_at =? WHERE user_id = '"+getUserID()+"'";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1,unpaidBalance);
            preparedStatement.setString(2,updateTime);
            preparedStatement.executeUpdate();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    private double getPaidDues() {
        double paidDues=0;
        String sql="SELECT total_dues_paid FROM ksm_dues WHERE user_id='"+getUserID()+"' ";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                paidDues = resultSet.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paidDues;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        membershipID.setEditable(false);
        outstandingBalance.setEditable(false);
        yearlyBudget.setEditable(false);
        burialLevy.setEditable(false);
        marriageLevy.setEditable(false);
        hallLevy.setEditable(false);
    }
}
