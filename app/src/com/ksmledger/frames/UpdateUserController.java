package com.ksmledger.frames;

import javafx.fxml.Initializable;
import com.ksmledger.utils.ConnectionUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class UpdateUserController implements Initializable {
    @FXML
    private ImageView backButton;

    @FXML
    private Label closeButton;

    @FXML
    private Button findButton;

    @FXML
    private TextField firstName;

    @FXML
    private TextField membershipID;

    @FXML
    private TextField city;

    @FXML
    private TextField middleName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField email;

    @FXML
    private TextField phone;

    @FXML
    private DatePicker dateDob;

    @FXML
    private TextArea address;

    @FXML
    private Button updateButton;

    @FXML
    private DatePicker initiationDate;

    @FXML
    private TextField state;

    @FXML
    private TextField country;

    @FXML
    private TextField searchedMemberID;

    private Connection connection=null;
    private PreparedStatement preparedStatement=null;
    private ResultSet resultSet=null;
    private Statement statement=null;

    public UpdateUserController(){
        connection = ConnectionUtil.connectDB();
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
    void findAction() {
        Window owner = findButton.getScene().getWindow();

        if(searchedMemberID.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter the membership ID of the member to search");
            return;
        }

        try {
            String sql="SELECT * FROM ksm_users WHERE membership_id=?";
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,searchedMemberID.getText());
            resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                firstName.setText(resultSet.getString("firstname"));
                lastName.setText(resultSet.getString("lastname"));
                middleName.setText(resultSet.getString("middlename"));
                membershipID.setText(resultSet.getString("membership_id"));
                email.setText(resultSet.getString("email"));
                phone.setText(resultSet.getString("phone"));
                city.setText(resultSet.getString("city"));
                dateDob.getEditor().setText(resultSet.getString("dob"));
                address.setText(resultSet.getString("address"));
                initiationDate.getEditor().setText(resultSet.getString("initiation_date"));
                state.setText(resultSet.getString("state"));
                country.setText(resultSet.getString("country"));
            }else{
                showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                        "Sorry this membership id is not registered in this system");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    void updateAction() {
        Window owner = updateButton.getScene().getWindow();
        if(searchedMemberID.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter the membership ID of the member to search");
            return;
        }
        String sql="UPDATE ksm_users" +
                " SET firstname =?,lastname =?,middlename=?, email=?," +
                "phone=?,city=?,dob=?,address=?,initiation_date=?,state=?,country=? " +
                "WHERE membership_id = '"+membershipID.getText()+"'";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,firstName.getText());
            preparedStatement.setString(2,lastName.getText());
            preparedStatement.setString(3,middleName.getText());
            preparedStatement.setString(4,email.getText());
            preparedStatement.setString(5,phone.getText());
            preparedStatement.setString(6,city.getText());
            preparedStatement.setString(7,dateDob.getEditor().getText());
            preparedStatement.setString(8,address.getText());
            preparedStatement.setString(9,initiationDate.getEditor().getText());
            preparedStatement.setString(10,state.getText());
            preparedStatement.setString(11,country.getText());
            preparedStatement.executeUpdate();
            showAlert(Alert.AlertType.CONFIRMATION, owner,
                    "User Update", "Data update was successful!");
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
