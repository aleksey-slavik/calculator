package com.implemica.calculator.view.adapter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Menu adapter
 *
 * @author Slavik Aleksey V.
 */
public class MenuAdapter {

    /**
     * Menu items
     */
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

    /**
     * Setup menu list items
     *
     * @return  list with menu items
     */
    public static ObservableList<String> init() {
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll(items);
        return list;
    }
}
