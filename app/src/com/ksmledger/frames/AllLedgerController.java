package com.ksmledger.frames;

import com.ksmledger.utils.AllLedgerTableModel;
import java.sql.*;

import com.ksmledger.utils.CurrencyConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import com.ksmledger.utils.ConnectionUtil;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AllLedgerController implements Initializable {
    @FXML
    private ImageView backButton;

    @FXML
    private Label closeButton;

    @FXML
    private TableView<AllLedgerTableModel> table;

    @FXML
    private TableColumn<AllLedgerTableModel, String> col_id;

    @FXML
    private TableColumn<AllLedgerTableModel, String> col_firstname;

    @FXML
    private TableColumn<AllLedgerTableModel, String> col_lastname;

    @FXML
    private TableColumn<AllLedgerTableModel, String> col_outstanding;

    @FXML
    private TableColumn<AllLedgerTableModel, String> col_budget;

    @FXML
    private TableColumn<AllLedgerTableModel, String> col_hall_levy;

    @FXML
    private TableColumn<AllLedgerTableModel, String> col_other_levy;

    @FXML
    private TableColumn<AllLedgerTableModel, String> col_total_dues;

    @FXML
    private TableColumn<AllLedgerTableModel, String> col_total_paid_dues;

    @FXML
    private TableColumn<AllLedgerTableModel, String> col_total_unpaid_balance;

    @FXML
    private Label totalOutstandings;

    @FXML
    private Label totalBudget;

    @FXML
    private Label totalHallLevy;

    @FXML
    private Label totalOtherLevy;

    @FXML
    private Label totalDuesOwed;

    @FXML
    private Label totalDuesPaid;

    @FXML
    private Label totalUnpaidDues;

    CurrencyConverter currencyConverter=new CurrencyConverter();

    ObservableList<AllLedgerTableModel> allLedgerTableModel= FXCollections.observableArrayList();
    private Connection connection=null;
    private PreparedStatement preparedStatement=null;
    private ResultSet resultSet=null;
    private Statement statement=null;

    public AllLedgerController(){connection= ConnectionUtil.connectDB(); }



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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        String sql="SELECT ksm_dues.id,ksm_users.firstname," +
                "ksm_users.lastname,ksm_dues.previous_outstanding,ksm_dues.yearly_budget," +
                "ksm_dues.ksm_hall_levy,ksm_dues.other_levies,ksm_dues.total_dues,ksm_dues.total_dues_paid,ksm_dues.unpaid_balance " +
                "FROM ksm_dues " +
                "LEFT OUTER JOIN ksm_users " +
                "ON ksm_dues.user_id=ksm_users.id ";
        try {
            resultSet=connection.createStatement().executeQuery(sql);
            while (resultSet.next()){
                allLedgerTableModel.add(new AllLedgerTableModel(
                        resultSet.getString("id"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getString("previous_outstanding"),
                        resultSet.getString("yearly_budget"),
                        resultSet.getString("ksm_hall_levy"),
                        resultSet.getString("other_levies"),
                        resultSet.getString("total_dues"),
                        resultSet.getString("total_dues_paid"),
                        resultSet.getString("unpaid_balance")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_firstname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        col_lastname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        col_outstanding.setCellValueFactory(new PropertyValueFactory<>("previousOutstanding"));
        col_budget.setCellValueFactory(new PropertyValueFactory<>("yearlyBudget"));
        col_hall_levy.setCellValueFactory(new PropertyValueFactory<>("hallLevy"));
        col_other_levy.setCellValueFactory(new PropertyValueFactory<>("credit"));
        col_total_dues.setCellValueFactory(new PropertyValueFactory<>("totalDues"));
        col_total_paid_dues.setCellValueFactory(new PropertyValueFactory<>("totalDuesPaid"));
        col_total_unpaid_balance.setCellValueFactory(new PropertyValueFactory<>("totalUnpaidDues"));
        table.setItems(allLedgerTableModel);
        setTotalOutstandings();
        setTotalBudget();
        setTotalHallLevy();
        setTotalOtherLevy();
        setTotalDuesOwed();
        setTotalDuesPaid();
        setTotalUnpaidDues();


    }

    private void setTotalUnpaidDues() {
        double sum=0;
        String sql="SELECT SUM(unpaid_balance) FROM ksm_dues";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                double unpaidBalance = resultSet.getDouble(1);
                sum = sum + unpaidBalance;
            }

            totalUnpaidDues.setText(CurrencyConverter.ngn(sum));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void setTotalDuesPaid() {
        double sum=0;
        String sql="SELECT SUM(total_dues_paid) FROM ksm_dues";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                double duesPaid = resultSet.getDouble(1);
                sum = sum + duesPaid ;
            }
            totalDuesPaid.setText(CurrencyConverter.ngn(sum));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setTotalDuesOwed() {
        double sum=0;
        String sql="SELECT SUM(total_dues) FROM ksm_dues";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                double duesOwed = resultSet.getDouble(1);
                sum = sum + duesOwed ;
            }
            totalDuesOwed.setText(CurrencyConverter.ngn(sum));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setTotalOtherLevy() {
        double sum=0;
        String sql="SELECT SUM(other_levies) FROM ksm_dues";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                double otherLvey = resultSet.getDouble(1);
                sum = sum + otherLvey ;
            }
            totalOtherLevy.setText(CurrencyConverter.ngn(sum));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setTotalBudget() {
        double sum=0;
        String sql="SELECT SUM(yearly_budget) FROM ksm_dues";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                double yearlyBudget = resultSet.getDouble(1);
                sum = sum + yearlyBudget ;
            }
            totalBudget.setText(CurrencyConverter.ngn(sum));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setTotalHallLevy() {
        double sum=0;
        String sql="SELECT SUM(ksm_hall_levy) FROM ksm_dues";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                double hallLevy = resultSet.getDouble(1);
                sum = sum + hallLevy ;
            }
            totalHallLevy.setText("NGN"+sum);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setTotalOutstandings() {
        double sum=0;
        String sql="SELECT SUM(previous_outstanding) FROM ksm_dues";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                double previousOutstanding = resultSet.getDouble(1);
                sum = sum + previousOutstanding ;
            }
            totalOutstandings.setText(CurrencyConverter.ngn(sum));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
