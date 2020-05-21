package com.ksmledger.frames;



import com.ksmledger.utils.ConnectionUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class LoginController implements Initializable {
    @FXML
    private Label progress;
    public static Label label;

    @FXML
    private ProgressBar progressbar;
    public static ProgressBar progressBar;

    @FXML private Label closeButton;

    @FXML
    private TextField txtMembershipID;

    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label membersCount;

    @FXML
    private Label ledgerBalance;

    @FXML
    private Label adminCount;


    private Connection connection=null;
    private PreparedStatement preparedStatement=null;
    private ResultSet resultSet=null;

    @FXML
    public void closeAction(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public LoginController(){
        connection= ConnectionUtil.connectDB();
    }

    public void getAdminCount(){

        try {
            String sql="SELECT COUNT(*) FROM ksm_admin";
            preparedStatement=connection.prepareStatement(sql);
            resultSet=preparedStatement.executeQuery();
            adminCount.setText(resultSet.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void loginAction(ActionEvent event){
        String membershipID= txtMembershipID.getText();
        String password= txtPassword.getText();
        String sql="SELECT * FROM ksm_admin WHERE membership_id=? AND password =?";

        try {
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,membershipID);
            preparedStatement.setString(2,password);
            resultSet=preparedStatement.executeQuery();
            if(!resultSet.next()){
                infoBox("Incorrect membership id and password password combination",null,"Failed");
            }else {
                infoBox("Login was successful",null,"Success");
                Node node =(Node)event.getSource();
                Stage stage=(Stage)node.getScene().getWindow();
                stage.close();
                Scene scene= new Scene(FXMLLoader.load(getClass().getResource("dashboard.fxml")));
                stage.setScene(scene);
                stage.show();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

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
        label=progress;
        progressBar=progressbar;

    }

}
