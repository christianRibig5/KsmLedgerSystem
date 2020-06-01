package com.ksmledger.frames;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import com.ksmledger.utils.ConnectionUtil;
import com.ksmledger.utils.LoggedInAdminUserId;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import com.jfoenix.controls.JFXHamburger;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DashboardController implements Initializable {


    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXHamburger hamburger;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private Label membersCount;

    @FXML
    private Label ledgerBalance;

    @FXML
    private Label adminCount;

    @FXML
    private Label loggedInUserName;



    private Connection connection=null;
    private PreparedStatement preparedStatement=null;
    private Statement statement=null;
    private ResultSet resultSet=null;
    private LoggedInAdminUserId loggedInAdminUserId=new LoggedInAdminUserId();

    public DashboardController(){
        connection= ConnectionUtil.connectDB();
    }

    private void ledgerBalance() {
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


    private void getMembersRowCount() {
        String sql="SELECT COUNT(*) AS rowcount FROM ksm_users";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            resultSet.next();
            int count = resultSet.getInt("rowcount") ;
            membersCount.setText(String.valueOf(count));
            resultSet.close() ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getAdminRowCount() {
        String sql="SELECT COUNT(*) AS rowcount FROM ksm_admin";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            resultSet.next();
            int count = resultSet.getInt("rowcount") ;
            adminCount.setText(String.valueOf(count));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            VBox box= FXMLLoader.load(getClass().getResource("drawerContent.fxml"));
            drawer.setSidePane(box);
            HamburgerBackArrowBasicTransition burgerTask = new HamburgerBackArrowBasicTransition(hamburger);
            burgerTask.setRate(-1);
            hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e)->{
                burgerTask.setRate(burgerTask.getRate()*-1);
                burgerTask.play();
                if(drawer.isOpened()){
                    drawer.close();
                } else{
                    drawer.open();
                }

            });

        }catch(IOException ex){
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE,null,ex);
        }
        getAdminRowCount();
        getMembersRowCount();
        ledgerBalance();
        setLoggedInUserName();
    }

    private void setLoggedInUserName() {
        String sql="SELECT ksm_users.id, ksm_users.firstname,ksm_users.lastname from ksm_users\n" +
                "INNER JOIN ksm_admin ON ksm_users.membership_id = ksm_admin.membership_id";

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                loggedInAdminUserId.setLoggedAdminUserId(resultSet.getInt(1));
                loggedInUserName.setText("Welcome Sir, "+resultSet.getString(2)+" "+resultSet.getString(3));
            }
            resultSet.close() ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
