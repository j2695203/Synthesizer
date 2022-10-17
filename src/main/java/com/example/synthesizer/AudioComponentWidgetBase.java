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
    private HBox baseLayout_;
    private VBox leftPanel_;
    private VBox centralPanel_;
    private VBox rightPanel_;
    protected Label title_;
    protected Slider slider_;
    private double mouseStartDragX_, mouseStartDragY_, widgetStartDragX_, widgetStartDragY_;
    private Line line_ = null;
    private Circle outputDot;
    private Circle inputDot;
    AudioComponentWidgetBase(AnchorPane parent){
        parent_ = parent;

        // widget frame
        baseLayout_ = new HBox();
        baseLayout_.setStyle("-fx-border-color: black; -fx-border-image-width: 8; -fx-background-color: bisque");

        // LEFT PANEL
        leftPanel_= new VBox();
        inputDot = new Circle(10);
        inputDot.setFill( Color.GRAY);
        // add to parent
        leftPanel_.getChildren().add(inputDot);

        // CENTRAL PANEL
        centralPanel_ = new VBox();

        // drag the widget to move
        centralPanel_.setOnMousePressed(e -> startDrag( e ) );
        centralPanel_.setOnMouseDragged(e -> handleDrag( e, outputDot ) );


        // RIGHT PANEL
        rightPanel_ = new VBox();
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
        rightPanel_.getChildren().add(closeBtn);
        rightPanel_.getChildren().add(outputDot);


        // ADJUST LAYOUT
        centralPanel_.setAlignment(Pos.CENTER);
        centralPanel_.setPadding( new Insets(5) );
        centralPanel_.setSpacing( 5 );
        rightPanel_.setAlignment(Pos.CENTER);
        rightPanel_.setPadding( new Insets(5) );
        rightPanel_.setSpacing( 5 );
        // adjust widget's location, not overlap when creating
        this.setLayoutX(50+10*SynthesizeApplication.widgets.size());
        this.setLayoutY(100+10*SynthesizeApplication.widgets.size());

    }



    protected void addToParent(){
        // add to parent
        centralPanel_.getChildren().add(title_);
        centralPanel_.getChildren().add(slider_);
        baseLayout_.getChildren().add(leftPanel_);
        baseLayout_.getChildren().add(centralPanel_);
        baseLayout_.getChildren().add(rightPanel_);
        // ADD TO PARENT
        this.getChildren().add(baseLayout_);
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

    protected void setEachWidget( Label title, Slider slider){
        title_ = title;
        slider_ = slider;
//        title_ = new Label("Sine Wave (440 Hz)");  // should be modified with slider
//        slider_ = new Slider(220,880,440);
//        slider_.setOnMouseDragged( e -> handleSlider(e, slider_, title_) ); // modify title and frequency
    }

    // slider for sine wave
    protected void handleSlider(MouseEvent e, Slider slider, Label title) {
        int value = (int) slider.getValue();

        // modify title
        title.setText("Sine Wave (" + value + " Hz)");
        // modify frequency
        ac_ = new SineWave(value); // 只能新增新的？不能調原本的？
    }

    // slider for others ( without changing title )
    protected void handleSlider(MouseEvent e, Slider slider) {
        int value = (int) slider.getValue();
    }


}
