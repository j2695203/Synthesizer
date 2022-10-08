package com.example.synthesizer;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class SynthesizeApplication extends Application {
    private AnchorPane mainCanvas_ = new AnchorPane();
    static ArrayList<AudioComponentWidget> widgets = new ArrayList<>();// 宣告在哪？
    @Override
    public void start(Stage stage) throws IOException {
        BorderPane root = new BorderPane();
        Scene scene = new Scene( root, 700, 400);

        stage.setTitle("Jinny's Synth");

        // CENTRAL PANEL
        mainCanvas_.setStyle("-fx-background-color: darkgray");
        // speaker
        Circle speakerCircle = new Circle(450,200,15);
        speakerCircle.setFill( Color.BLACK );
        // add to parent
        mainCanvas_.getChildren().add( speakerCircle );

        // RIGHT PANEL
        VBox rightPanel = new VBox();
        rightPanel.setPadding( new Insets(40) );
        rightPanel.setStyle("-fx-background-color: lightyellow");
        // sineWaveBtn
        Button sineWaveBtn = new Button("Create Sine Wave");
        sineWaveBtn.setOnAction( e -> createComponent (e, widgets));
        // add to Parent
        rightPanel.getChildren().add(sineWaveBtn);

        // BOTTOM PANEL
        HBox bottomPanel = new HBox();
        bottomPanel.setAlignment(Pos.CENTER);
        bottomPanel.setPadding(new Insets(10));
        // play button
        Button playBtn = new Button("Play");
        playBtn.setOnAction( e -> handlePlay(e) );

        // ADD TO PARENT
        bottomPanel.getChildren().add(playBtn);

        // add to BorderPane root
        root.setCenter( mainCanvas_ );
        root.setRight( rightPanel );
        root.setBottom(bottomPanel);

        // show on window
        stage.setScene(scene);
        stage.show();
    }

    private void handlePlay(ActionEvent e) {
        ///////////
    }

    private void createComponent(Object e, ArrayList<AudioComponentWidget> widgets) {
        System.out.println("in create component");
        AudioComponent sineWave = new SineWave(440);
        AudioComponentWidget acw = new AudioComponentWidget(sineWave, mainCanvas_);
        widgets.add(acw);
//////////////
    }


    public static void main(String[] args) {
        launch();
    }
}

