package com.example.synthesizer;

import javafx.event.ActionEvent;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class AudioComponentWidgetBase extends Pane {
    protected AudioComponent ac_;
    protected AnchorPane parent_; // = mainCanvas
    protected Label title_;
    protected Slider slider_;
    private double mouseStartDragX_, mouseStartDragY_, widgetStartDragX_, widgetStartDragY_;
    private Line line_ = null;
    private Circle outputDot;
    AudioComponentWidgetBase(AudioComponent ac, AnchorPane parent){
        ac_ = ac;
        parent_ = parent;

        // widget frame
        HBox baseLayout = new HBox();
        baseLayout.setStyle("-fx-border-color: black; -fx-border-image-width: 8; -fx-background-color: bisque");

        // LEFT PANEL

        // CENTRAL PANEL
        VBox centralPanel = new VBox();

        // 應放SineWaveWidget
        title_ = new Label("Sine Wave (440 Hz)");  // should be modified with slider
        slider_ = new Slider(220,880,440);
        slider_.setOnMouseDragged( e -> handleSlider(e, slider_, title_) ); // modify title and frequency


        // drag the widget to move
        centralPanel.setOnMousePressed( e -> startDrag( e ) );
        centralPanel.setOnMouseDragged( e -> handleDrag( e, outputDot ) );
        // add to parent
        centralPanel.getChildren().add(title_);
        centralPanel.getChildren().add(slider_);
        baseLayout.getChildren().add(centralPanel);


        // RIGHT PANEL
        VBox rightPanel = new VBox();
        // items
        Button closeBtn = new Button("X");
        closeBtn.setOnAction( e -> closeWidget(e) );
        outputDot = new Circle(10);
        outputDot.setFill( Color.CHOCOLATE);
        // draw a line when connected
        outputDot.setOnMousePressed( e -> startConnection( e, outputDot ) ); // cancel an existed line or draw a new line
        outputDot.setOnMouseDragged( e -> moveConnection( e ) ); // draw a line to the dragging mouse's position
        outputDot.setOnMouseReleased( e -> endConnection( e ) ); // delete the line or draw a connection line to speaker
        // add to parent
        rightPanel.getChildren().add(closeBtn);
        rightPanel.getChildren().add(outputDot);
        baseLayout.getChildren().add(rightPanel);

        // ADJUST LAYOUT
        centralPanel.setAlignment(Pos.CENTER);
        centralPanel.setPadding( new Insets(5) );
        centralPanel.setSpacing( 5 );
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

    private void startDrag(MouseEvent e) { // save the original position
        mouseStartDragX_ = e.getSceneX();
        mouseStartDragY_ = e.getSceneY();
        widgetStartDragX_ = this.getLayoutX();
        widgetStartDragY_ = this.getLayoutY();
    }

    private void handleDrag(MouseEvent e, Circle outputDot) {
        // modify widget's position
        double mouseDelX = e.getSceneX() - mouseStartDragX_;
        double mouseDelY = e.getSceneY() - mouseStartDragY_;
        this.relocate(widgetStartDragX_ + mouseDelX, widgetStartDragY_ + mouseDelY);
        // modify line's position
        if ( line_ != null){
            Bounds parentBounds = parent_.getBoundsInParent(); // this code doesn't have parent bound issue
            Bounds bounds = this.outputDot.localToScene( this.outputDot.getBoundsInLocal() );
            line_.setStartX( bounds.getCenterX() - parentBounds.getMinX() );
            line_.setStartY( bounds.getCenterY() - parentBounds.getMinY() );
        }
    }

    private void closeWidget(ActionEvent e) {
        parent_.getChildren().remove(this);
        parent_.getChildren().remove( line_ );
        line_ = null;
        SynthesizeApplication.widgets.remove(this); // 可刪？
        SynthesizeApplication.connectedWidgets.remove(this);
        System.out.println("num widgets: " + SynthesizeApplication.widgets.size());
    }

    private void startConnection(MouseEvent e, Circle outputDot) { // remove or create a line
        if ( line_ != null ){
            parent_.getChildren().remove(line_);
            SynthesizeApplication.connectedWidgets.remove(this );
        }else{
            Bounds parentBounds = parent_.getBoundsInParent(); // this code doesn't have parent bound issue
            Bounds bounds = outputDot.localToScene( outputDot.getBoundsInLocal() );
            line_ = new Line();
            line_.setStrokeWidth( 4 );
            line_.setStartX( bounds.getCenterX() - parentBounds.getMinX() );
            line_.setStartY( bounds.getCenterY() - parentBounds.getMinY() );
            line_.setEndX( e.getSceneX() );
            line_.setEndY( e.getSceneY() );
            parent_.getChildren().add( line_ );
        }
    }

    private void moveConnection(MouseEvent e ) {
        Bounds parentBounds = parent_.getBoundsInParent(); // this code doesn't have parent bound issue
        line_.setEndX( e.getSceneX() - parentBounds.getMinX() );
        line_.setEndY( e.getSceneY() - parentBounds.getMinY() );
    }

    private void endConnection(MouseEvent e ) {
        Circle speaker = SynthesizeApplication.speakerCircle;
        Bounds speakerBounds = speaker.localToScene( speaker.getBoundsInLocal() );
        double distance = Math.sqrt( Math.pow( speakerBounds.getCenterX() - e.getSceneX(), 2.0 ) + Math.pow( speakerBounds.getCenterY() - e.getSceneY(), 2.0 ) );

        if( distance < 13 ){
            line_.setEndX( speakerBounds.getCenterX() );
            line_.setEndY( speakerBounds.getCenterY() );
            SynthesizeApplication.connectedWidgets.add( this );
        }else{
            parent_.getChildren().remove( line_ );
            line_ = null;
        }
    }

    public AudioComponent getAc_() {
        return ac_;
    }

    // 應放SineWaveWidget
    private void handleSlider(MouseEvent e, Slider slider, Label title) {
        int value = (int) slider.getValue();
        // modify title
        title.setText("Sine Wave (" + value + " Hz)");
        // modify frequency
        ac_ = new SineWave(value); // 只能新增新的？不能調原本的？
    }


}
