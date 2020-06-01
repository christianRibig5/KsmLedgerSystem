package com.ksmledger.frames;

import com.ksmledger.utils.ConnectionUtil;
import com.ksmledger.utils.LedgerTableModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class LedgerController implements Initializable {

    @FXML
    private ImageView backButton;

    @FXML
    private Label closeButton;

    @FXML
    private TableView<LedgerTableModel> table;

    @FXML
    private TableColumn<LedgerTableModel, String> col_id;

    @FXML
    private TableColumn<LedgerTableModel, String> col_membership_id;

    @FXML
    private TableColumn<?, ?> col_firstname;

    @FXML
    private TableColumn<?, ?> col_lastname;

    @FXML
    private TableColumn<LedgerTableModel, String> col_transaction_date;

    @FXML
    private TableColumn<LedgerTableModel, String> col_debit;

    @FXML
    private TableColumn<LedgerTableModel, String> col_credit;

    @FXML
    private TableColumn<LedgerTableModel, String> col_particulars;

    @FXML
    private Label ledgerBalance;

    ObservableList<LedgerTableModel> ledgerTableModel= FXCollections.observableArrayList();

    private Connection connection=null;
    private PreparedStatement preparedStatement=null;
    private ResultSet resultSet=null;
    private Statement statement=null;

    public LedgerController(){connection= ConnectionUtil.connectDB(); }

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
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String sql="SELECT ksm_ledgers.sn,ksm_ledgers.transaction_date," +
                "ksm_users.membership_id,ksm_users.firstname,ksm_users.lastname," +
                "ksm_ledgers.debit,ksm_ledgers.credit,ksm_ledgers.particulars " +
                "FROM ksm_ledgers " +
                "LEFT OUTER JOIN ksm_users " +
                "ON ksm_ledgers.user_id=ksm_users.id " +
                "ORDER BY ksm_ledgers.transaction_date DESC";
        try {
            resultSet=connection.createStatement().executeQuery(sql);
            while (resultSet.next()){
                ledgerTableModel.add(new LedgerTableModel(
                        resultSet.getString("sn"),
                        resultSet.getString("transaction_date"),
                        resultSet.getString("membership_id"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getString("debit"),
                        resultSet.getString("credit"),
                        resultSet.getString("particulars")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_transaction_date.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        col_membership_id.setCellValueFactory(new PropertyValueFactory<>("membershipId"));
        col_firstname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        col_lastname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        col_debit.setCellValueFactory(new PropertyValueFactory<>("debit"));
        col_credit.setCellValueFactory(new PropertyValueFactory<>("credit"));
        col_particulars.setCellValueFactory(new PropertyValueFactory<>("particulars"));
        table.setItems(ledgerTableModel);

        setLedgerBalance();



    }

    private void setLedgerBalance() {
        String sql="SELECT balance FROM ksm_vault WHERE id=1";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                double balance = resultSet.getDouble(1);
                NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "NG"));
                String moneyString = formatter.format(balance);
                ledgerBalance.setText(moneyString);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
