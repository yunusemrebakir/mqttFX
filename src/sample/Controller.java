package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

public class Controller implements Observer {
    WeatherData weatherData;

    public Controller(WeatherData weatherData) {
        this.weatherData = weatherData;
    }


    private double initialX;
    private double initialY;

    @FXML
    private BorderPane borderPane;
    @FXML
    private VBox vBox;
    @FXML
    Label lblOutsideTemp;
    @FXML
    Label lblInsideTemp;

    @FXML
    public void btnExitClicked() {
        Platform.exit();
    }


    /*
     * Because we don't use windows borders stage(window) can't be dragged
     * this method handles it
     */
    @FXML
    public void dragWindows() {

        borderPane.setOnMousePressed(e -> {
            initialX = borderPane.getScene().getWindow().getX() - e.getScreenX();
            initialY = borderPane.getScene().getWindow().getY() - e.getScreenY();
        });

        borderPane.setOnMouseDragged(e -> {
            borderPane.getScene().getWindow().setX(e.getScreenX() + initialX);
            borderPane.getScene().getWindow().setY(e.getScreenY() + initialY);
        });

    }

    @FXML
    public void btnMaximizeClicked() {

        Stage stage = (Stage) borderPane.getScene().getWindow();

        if (stage.isFullScreen()) {
            stage.setFullScreen(false);

            /*
            *
            * getWidth() method is not returning correct value.
            * Maybe, because of the fullscreen animation(which requires some time to complete)
            * it doesn't return correct width.
            * To overcome this problem, getWidth() method is called after some time(after animation finished and
            * stage is eventually fullscreen)
            * See bug: https://bugs.openjdk.java.net/browse/JDK-8089167
            *
            * */

            Timer timer = new Timer(true);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        double scaleAmount = (vBox.getWidth() * 1.5) / (stage.getWidth());
                        vBox.setScaleX(scaleAmount);
                        vBox.setScaleY(scaleAmount);

                    });
                }
            }, 100);

        } else {
            stage.setFullScreen(true);
            //  stage.setWidth(600);
            //  stage.setHeight(400);
            Timer timer = new Timer(true);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                                System.out.println("stage.getWidth() after delay " + stage.getWidth());
                                double scaleAmount = (stage.getWidth()) / (vBox.getWidth() * 1.5);
                                vBox.setScaleX(scaleAmount);
                                vBox.setScaleY(scaleAmount);
                            }
                    );
                }
            }, 100);
        }


    }


    @Override
    public void updateOutsideTemp() {
        Platform.runLater(() -> {
                    System.out.println("Outside Temp " +this.weatherData.getOutsideTemp() + "");
                    lblOutsideTemp.setText(this.weatherData.getOutsideTemp() + "");
                }
        );

    }

    @Override
    public void updateInsideTemp() {
        Platform.runLater(() -> {
                    System.out.println("Inside Temp " +this.weatherData.getInsideTemp() + "");
                    lblInsideTemp.setText(this.weatherData.getInsideTemp() + "");
                }
        );

    }

}
