import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class KSMLedgerBook extends Application {
    public static boolean isFlashScreenLoaded = false;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("AdminLogin.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Splash Screen");
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
