package sample;

import java.util.ArrayList;
import java.util.List;

public interface Observable {

    void remove(Observer observer);

    void add(Observer observer);

    void notifyObservers();
}
