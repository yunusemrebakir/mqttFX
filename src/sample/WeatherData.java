package sample;

import org.eclipse.paho.client.mqttv3.*;

import java.util.ArrayList;
import java.util.List;

public class WeatherData implements MqttCallback {

    String[] topics = {"sensors/outsideTemp", "sensors/insideTemp"};
    String broker = "tcp://192.168.0.222:1883";
    String clientId = "WeatherStation";

    List<Observer> insideTempObservers = new ArrayList<>();
    List<Observer> outsideTempObservers = new ArrayList<>();
    private double outsideTemp, insideTemp;


    public void removeInsideTempObservers(Observer observer) {
        this.insideTempObservers.remove(observer);
    }


    public void addInsideTempObservers(Observer observer) {
        this.insideTempObservers.add(observer);
    }

    public void notifyInsideTempObservers() {

        for (Observer o : insideTempObservers) {
            o.updateInsideTemp();
        }

    }


    public void removeOutsideTempObservers(Observer observer) {
        this.outsideTempObservers.remove(observer);
    }


    public void addOutsideTempObservers(Observer observer) {
        this.outsideTempObservers.add(observer);
    }

    public void notifyOutsideTempObservers() {

        for (Observer o : outsideTempObservers) {
            o.updateOutsideTemp();
        }

    }

    /*
    *
    * This class needs to connect to MQTT broker to update its fields
    * This method does that
    *
    */

    public void startMonitoring() {
        try {
            MqttClient sampleClient = new MqttClient(broker, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            sampleClient.setCallback(this);
            connOpts.setKeepAliveInterval(20 * 60);
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            sampleClient.subscribe(topics);

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }


    }


    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Connection lost.");
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        switch (s) {
            case "sensors/outsideTemp": {
                setOutsideTemp(Double.parseDouble(mqttMessage.toString()));
                notifyOutsideTempObservers();
                break;
            }
            case "sensors/insideTemp": {
                setInsideTemp(Double.parseDouble(mqttMessage.toString()));
                notifyInsideTempObservers();
                break;
            }

        }


    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    public void setOutsideTemp(double outsideTemp) {
        this.outsideTemp = outsideTemp;
    }

    public void setInsideTemp(double insideTemp) {
        this.insideTemp = insideTemp;
    }

    public double getOutsideTemp() {

        return this.outsideTemp;
    }

    public double getInsideTemp() {
        return this.insideTemp;
    }
}
