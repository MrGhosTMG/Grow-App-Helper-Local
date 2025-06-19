package org.jdta.growapp.Models;

import org.jdta.growapp.Views.View;

public class Model {
    private static Model model;
    private final View view;

    private Model() {
        this.view = new View();
    }
    public static synchronized Model getInstance() {
        if (model == null) {
            model= new Model();
        }
        return model;
    }

    public View getView() {
        return view;
    }
}
