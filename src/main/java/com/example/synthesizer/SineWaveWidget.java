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
        super(ac, parent);

        // CENTRAL PANEL

        // items
        title_ = new Label("Sine Wave (440 Hz)");  // should be modified with slider
        slider_ = new Slider(220,880,440);
        slider_.setOnMouseDragged( e -> handleSlider(e, slider_, title_) ); // modify title and frequency

    }


    private void handleSlider(MouseEvent e, Slider slider, Label title) {
        int value = (int) slider.getValue();
        // modify title
        title.setText("Sine Wave (" + value + " Hz)");
        // modify frequency
        ac_ = new SineWave(value); // 只能新增新的？不能調原本的？
    }


}
