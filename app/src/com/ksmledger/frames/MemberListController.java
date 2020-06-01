package com.ksmledger.frames;

import com.ksmledger.utils.ConnectionUtil;
import com.ksmledger.utils.UserTableModel;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MemberListController implements Initializable {
    @FXML
    private ImageView backButton;

    @FXML
    private Label closeButton;

    @FXML
    private TableView<UserTableModel> table;

    @FXML
    private TableColumn<UserTableModel, String> col_id;

    @FXML
    private TableColumn<UserTableModel, String> col_firstname;

    @FXML
    private TableColumn<UserTableModel, String> col_lastname;

    @FXML
    private TableColumn<UserTableModel, String> col_membership_id;

    @FXML
    private TableColumn<UserTableModel, String> col_email;

    @FXML
    private TableColumn<UserTableModel, String> col_phone;



    @FXML
    private TableColumn<UserTableModel, String> col_init_date;

    ObservableList<UserTableModel> userTableModel = FXCollections.observableArrayList();

    private Connection connection=null;
    private PreparedStatement preparedStatement=null;
    private ResultSet resultSet=null;

    public MemberListController(){ connection= ConnectionUtil.connectDB(); }

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

        String sql="SELECT id, firstname, lastname, membership_id, email, phone, initiation_date FROM ksm_users";
        try {
            resultSet=connection.createStatement().executeQuery(sql);
            while (resultSet.next()){
                userTableModel.add(new UserTableModel(
                        resultSet.getString("id"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getString("membership_id"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("initiation_date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_firstname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        col_lastname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        col_membership_id.setCellValueFactory(new PropertyValueFactory<>("membershipId"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        col_init_date.setCellValueFactory(new PropertyValueFactory<>("initiationDate"));

        table.setItems(userTableModel);
    }
}
