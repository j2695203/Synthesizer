package com.example.synthesizer;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

//// 缺拉線//////////////

public class AudioComponentWidget extends Pane {
//    AudioComponent ac_;
    AnchorPane parent_; // = mainCanvas
    AudioComponentWidget(AudioComponent ac, AnchorPane parent){
//        ac_ = ac;
        parent_ = parent;
        // widget frame
        HBox baseLayout = new HBox();
        baseLayout.setStyle("-fx-border-color: black; -fx-border-image-width: 8; -fx-background-color: bisque");

        // LEFT PANEL
        VBox leftPanel = new VBox();
        // items
        Label title = new Label("Sine Wave (440 Hz)");  // should be modified with slider
        Slider slider = new Slider(220,880,440);
        slider.setOnMouseDragged( e -> handleSlider(e, slider, title) ); // modify title and frequency
        // add to parent
        leftPanel.getChildren().add(title);
        leftPanel.getChildren().add(slider);
        baseLayout.getChildren().add(leftPanel);

        // RIGHT PANEL
        VBox rightPanel = new VBox();
        // items
        Button closeBtn = new Button("X");
        closeBtn.setOnAction( e -> closeWidget(e) );
        Circle outputDot = new Circle(10);
        outputDot.setFill( Color.CHOCOLATE);
        // add to parent
        rightPanel.getChildren().add(closeBtn);
        rightPanel.getChildren().add(outputDot);
        baseLayout.getChildren().add(rightPanel);

        // ADJUST LAYOUT
        leftPanel.setAlignment(Pos.CENTER);
        leftPanel.setPadding( new Insets(5) );
        leftPanel.setSpacing( 5 );
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setPadding( new Insets(5) );
        rightPanel.setSpacing( 5 );

        // adjust widget's location, not overlap when creating
        this.setLayoutX(50+10*SynthesizeApplication.widgets.size());
        this.setLayoutY(100+10*SynthesizeApplication.widgets.size());

        // ADD TO PARENT
        this.getChildren().add(baseLayout);
        parent_.getChildren().add( this );
    }

    private void handleSlider(MouseEvent e, Slider slider, Label title) {
        int value = (int) slider.getValue();
        title.setText("Sine Wave (" + value + " Hz)");
        // 改變音頻/////////////////////
    }

    private void closeWidget(ActionEvent e) {
        parent_.getChildren().remove(this);
        SynthesizeApplication.widgets.remove(this);
        System.out.println("num widgets: " + SynthesizeApplication.widgets.size());
    }
}
