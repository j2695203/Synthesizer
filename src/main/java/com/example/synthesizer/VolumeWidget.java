package com.example.synthesizer;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;

public class VolumeWidget extends AudioComponentWidgetBase {
    public VolumeWidget(AnchorPane parent) {
        super(parent);
        Label title = new Label("Volume Widget");
        Slider slider = new Slider(0.0,5.0,1.0);
        slider.setOnMouseDragged( e -> handleSlider(e, slider) );
        super.setEachWidget( title, slider );
        super.addToParent();
    }
}
