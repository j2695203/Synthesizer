package com.example.synthesizer;

import javafx.event.ActionEvent;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;


public class SineWaveWidget extends AudioComponentWidgetBase {
    SineWaveWidget(AudioComponent ac, AnchorPane parent){
        super(parent);
        super.ac_ = ac;

        Label title = new Label("Sine Wave (440 Hz)");  // should be modified with slider
        Slider slider = new Slider(220,880,440);
        slider.setOnMouseDragged( e -> handleSlider(e, slider, title) ); // modify title and frequency
        super.setEachWidget( title, slider );
        super.addToParent();
    }


}
