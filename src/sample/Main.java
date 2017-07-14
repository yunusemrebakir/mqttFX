package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;



public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        WeatherData weatherData = new WeatherData();
        weatherData.startMonitoring();
        Controller controller = new Controller(weatherData);
        weatherData.addOutsideTempObservers(controller);
        weatherData.addInsideTempObservers(controller);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("transparent.fxml"));

        /*
        * It is necessary to remove fx:controller property from the fxml file and set the controller from here
        * Because we are creating a new Controller which is different from the one stated in fx:controller
        * By setting controller from here we are saying, use this new controller to control your fields, because
        * we are controlling fields using this controller
        * If this is not done, it gives a cryptic null exception
        * */
        fxmlLoader.setController(controller);
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));

        //Remove window borders
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();



    }



    public static void main(String[] args) {
        launch(args);
    }



}
