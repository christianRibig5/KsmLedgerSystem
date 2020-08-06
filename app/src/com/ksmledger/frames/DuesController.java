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
import java.util.ArrayList;

public class DuesController implements Initializable {

    @FXML
    private ImageView backButton;

    @FXML
    private Label closeButton;



    @FXML
    private Button saveButton;

 

    @FXML
    private DatePicker createdAt;

    @FXML
    private TextField duesAmount;

    @FXML
    private ComboBox<String> duesType;

    private ArrayList<Integer>allUserIDs=new ArrayList<>();



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

        if(duesType.getSelectionModel().getSelectedItem()==null){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please select dues type");
            return;
        }
        if(duesAmount.getText()==null || duesAmount.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter amount");
            return;
        }

        if(createdAt.getEditor().getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter date");
            return;
        }
        String sql="";
        if(duesType.getSelectionModel().getSelectedItem()=="Yearly Budget"){
            sql="UPDATE ksm_dues SET yearly_budget = ?, created_at=?";
        }else if(duesType.getSelectionModel().getSelectedItem()=="Hall Levy"){
            sql="UPDATE ksm_dues SET ksm_hall_levy = ?, created_at=?";
        }

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,duesAmount.getText());
            preparedStatement.setString(2,createdAt.getEditor().getText());
            preparedStatement.executeUpdate();
            //int[] userIds=new int[6];
            validateUnpaidDues(allUserIDs);
            showAlert(Alert.AlertType.CONFIRMATION, owner,
                    ""+duesType.getSelectionModel().getSelectedItem()+"!", "Data update was successful!");
            clearText();
        }catch(SQLException ex){
            ex.printStackTrace();
        }



    }

    private void validateUnpaidDues(ArrayList<Integer> userIds) {
        for(int i=0;i<userIds.size();i++){
            double unpaidBalance = getTotalDues(userIds.get(i))-getPaidDues(userIds.get(i));
            updateUnpaidBalance(unpaidBalance,userIds.get(i));
        }

    }

    private double getPaidDues(int id) {
        double paidDues=0;
        String sql="SELECT total_dues_paid FROM ksm_dues WHERE user_id='"+id+"' ";
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


    private double getTotalDues(int id) {
        double totalDues=0;
        double outstBalance=getOutstandingB(id);
        double budget=getBudget(id);
        double hallLevey=getHallLvey(id);
        double otherLevey=getOtherLevy(id);
        totalDues=(outstBalance+budget+hallLevey+otherLevey);
        updateTotalDues(totalDues,id);
        return totalDues;
    }

    private double getOtherLevy(int id) {
        double otherlevy=0;
        String sql="SELECT other_levies FROM ksm_dues WHERE user_id='"+id+"' ";
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

    private double getHallLvey(int id) {
        double halllevy=0;
        String sql="SELECT ksm_hall_levy FROM ksm_dues WHERE user_id='"+id+"' ";
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

    private double getBudget(int id) {
        double budget=0;
        String sql="SELECT yearly_budget FROM ksm_dues WHERE user_id='"+id+"' ";
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

    private double getOutstandingB(int id) {
        double outstandings=0;
        String sql="SELECT previous_outstanding FROM ksm_dues WHERE user_id='"+id+"' ";
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

    private void updateTotalDues(double dues,int id) {
        String sql="UPDATE ksm_dues" +
                " SET total_dues =? WHERE user_id = '"+id+"'";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1,dues);
            preparedStatement.executeUpdate();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }



    private void updateUnpaidBalance(double unpaidBalance, int id) {
        String sql="UPDATE ksm_dues" +
                " SET unpaid_balance =? WHERE user_id = '"+id+"'";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1,unpaidBalance);
            preparedStatement.executeUpdate();
        }catch(SQLException ex){
            ex.printStackTrace();
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
        duesAmount.clear();
        duesType.getEditor().clear();
        createdAt.getEditor().clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        duesType.getItems().addAll("Yearly Budget","Hall Levy");
        String sql="SELECT user_id FROM ksm_dues ";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                allUserIDs.add(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
