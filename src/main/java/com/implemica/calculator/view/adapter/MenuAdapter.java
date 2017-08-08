package com.implemica.calculator.view.adapter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;

/**
 * Menu adapter
 *
 * @author Slavik Aleksey V.
 */
public class MenuAdapter {

    private static final String[] items = new String[] {
            "Standard",
            "Scientific",
            "Programmer",
            "Date Calculation",
            "Currency",
            "Volume",
            "Length",
            "Weight and Mass",
            "Temperature",
            "Energy",
            "Area",
            "Speed",
            "Time",
            "Power",
            "Data",
            "Pressure",
            "Angle"
    };

    public static ObservableList<String> init() {
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll(items);
        return list;
    }
}
