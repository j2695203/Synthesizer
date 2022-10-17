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

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.ArrayList;

public class SynthesizeApplication extends Application {
    private AnchorPane mainCanvas_ = new AnchorPane();
    static ArrayList<AudioComponentWidgetBase> widgets = new ArrayList<>();// 宣告在哪？
    static ArrayList<AudioComponentWidgetBase> connectedWidgets = new ArrayList<>();
    static Circle speakerCircle;
    @Override
    public void start(Stage stage) throws IOException {
        BorderPane root = new BorderPane();
        Scene scene = new Scene( root, 700, 400);

        stage.setTitle("Jinny's Synth");

        // CENTRAL PANEL
        mainCanvas_.setStyle("-fx-background-color: darkgray");
        // speaker
        speakerCircle = new Circle(450,200,15);
        speakerCircle.setFill( Color.BLACK );
        // add to parent
        mainCanvas_.getChildren().add( speakerCircle );

        // RIGHT PANEL
        VBox rightPanel = new VBox();
        rightPanel.setPadding( new Insets(40) );
        rightPanel.setStyle("-fx-background-color: lightyellow");
        // sineWaveBtn
        Button sineWaveBtn = new Button("Create Sine Wave");
        sineWaveBtn.setOnAction( e -> createSineWaveComponent(e, widgets));
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
        // when no connected widgets
        if( connectedWidgets.size() == 0 ){
            return;
        }
        // when there are connected widgets
        else{

            Clip c = null; // Note, this is different from our AudioClip class.
            AudioListener listener = new AudioListener(c);
            try {
                c = AudioSystem.getClip();
                // This is the format that we're following, 44.1 KHz mono audio, 16 bits per sample.
                AudioFormat format16 = new AudioFormat( 44100, 16, 1, true, false );

                // put connectedWidgets into mixer
                Mixer mixer = new Mixer();
                for( AudioComponentWidgetBase w: connectedWidgets ){
                    mixer.connectInput( w.getAc_() );
                }
                // Create each component
//            AudioComponent gen1 = new SineWave(440);
//        AudioComponent gen2 = new SineWave(500);
//        AudioComponent gen3 = new LinearRamp(50,2000);
//        VFSineWave vfSineWave = new VFSineWave();

                // Mixer, Processor
//            Mixer mixer = new Mixer();
//            mixer.connectInput(gen1);
//        mixer.connectInput(gen2);
//        vfSineWave.connectInput(gen3);

                // Filter
//            Filter volume = new Filter(0.5);
//            volume.connectInput(mixer);
//        volume.connectInput(vfSineWave);

                // Data to be played
                byte[] playData = mixer.getClip().getData();
//            byte[] playData = volume.getClip().getData();

                // Play method by library
                c.open( format16, playData, 0, playData.length ); // Reads data from our byte array to play it.

                System.out.println( "About to play..." );
                c.start(); // Plays it.
                c.loop( 1 ); // Plays it 2 more times if desired, so 6 seconds total

                // Makes sure the program doesn't quit before the sound plays.
//            while( c.getFramePosition() < playData.length || c.isActive() || c.isRunning() ){
//                // Do nothing while we wait for the note to play.
//            }
                c.addLineListener(listener);

                System.out.println( "Done." );
//            c.close();

            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }

        }


    }

    private void createSineWaveComponent(Object e, ArrayList<AudioComponentWidgetBase> widgets) {
        System.out.println("in create sine wave component");
        AudioComponent sineWave = new SineWave(440);
        AudioComponentWidgetBase acw = new SineWaveWidget(sineWave, mainCanvas_);
        widgets.add(acw);
    }


    public static void main(String[] args) {
        launch();
    }
}

