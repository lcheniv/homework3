package sample;

//////////////////////////////////////////////////
/// NAME: LAWRENCE CHEN                        ///
/// ASSIGNMENT: HW3, RANGE, CONTAINER, WINDOWS ///
//              AND DIALOG                     ///
/// PROFESSOR: DAVID BOLDING                   ///
/// DUE DATE: OCTOBER 5th before class         ///
//////////////////////////////////////////////////

/*
    Outline // Students will create a simple GUI for a transcoding system.

    Transcoding Widget should include the following components :

        -  On one line, a read-only text-field and a button that says “Get Source”; we will call these the “source
            text-field” and “source button”, respectively. [CHECK]

        -  On another line, a read-only text-field and a button that says “Get Target” (the “target textfield” and
            “target button”) [CHECK]

        -  On another line, a slider, with a range from 1 to 9 (the “quality slider”). [CHECK]

        -  On another line, a single button that says “Start”. [CHECK]

        -  On the last line, a progress bar and a button that says “Step”. [CHECK]

        -  Each row should take up the full width of the widget [CHECK]

        -  Any excess height allocated to the widget should be left blank beneath the last row [CHECK]

        -  The buttons should remain at their natural size, and the other components (the text-fields, sliders and
           progress-bars) should grow horizontally to take up the extra space [CHECK]

        -  (JavaFX reminder: you can get the progress bar to grow horizontally by setting its preferred size to some
           arbitrarily large number [OK]

        -  Any extra vertical space should be left unused at the bottom of the widget [CHECK]

        -  When the program starts, the source and target text-fields should be blank [CHECK]

        -   If the user clicks the source button, an open-file dialog should be displayed; if the user picks a file,
            then that file’s path should be displayed in the source text-field (if they do not select a file,
            the contents of the source text-filed should not be altered) [CHECK]

            -   Likewise, when the user clicks target button, a save filedialog should be displayed, and the path
                selected should be displayed in the target text-field (if the user selected a file). [CHECK]

        // WHEN THE USER CLICKS THE START BUTTON
         if either source or target fields are empty, display an error (with an error dialog);

         if both the source and target text-fields have values, you should record the current source,
         target and quality, and then set the progress-bar to 0% progress (and to normal, not indeterminate mode). [CHECK]


        // WHEN THE USER CLICKS THE STEP BUTTON
         if valid settings haven’t been stored, display an error (with an error dialog); [CHECK]

         if the progress-bar is at less than 100%, [CHECK]
            advance it by 10%;

         if the progress-bar is at 100%,
            set it to indeterminate mode and display a message to the user that their encoding is
            complete. (using a message dialog); [CHECK]

         if the progress-bar is in indeterminate mode, [CHECK]
            do nothing.

        // THE MAIN INTERFACE
         The Main Interface In your main window, you should have a single button in the
         bottom-right corner that says “Add Transcode”, and a tab-manager that takes up all of the remaining space.

         When the “Add Transcode” button is clicked, you should add a new tab to the tab-manager; this tab should
         contain a single scroller, and that scroller should contain a single TranscodeWidget. (You can set the title
         of the tab to anything you’d like.)

 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.awt.*;
import java.awt.ScrollPane;
import java.io.File;

import static javafx.scene.paint.Color.LIGHTSTEELBLUE;

public class TranscoderWidget extends Application {

    double increment = 0;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        // TITLE OF WINDOW
        primaryStage.setTitle("Homework 3 : Transcoder Widget");

        // BLANK SPACE WIDGET - SUPER CLASS OF ALL CLASSES
        Region r = new Region();

        // TO SET UP LAYOUT IN A GRID WHICH IS PLACED IN THE BORDERPANE CLASS
        GridPane root = new GridPane();


        // CREATING REGIONS TO CREATE SPACING AND SEPERATIONS
        root.add(r, 1, 0);
        root.setHgrow(r, Priority.ALWAYS);


        // A READ-ONLY TEXT FIELD TO DISPLAY SOURCE DIRECTORY
        TextField fromDirectoryField = new TextField();
        root.add(fromDirectoryField, 0,0);
        fromDirectoryField.setEditable(false);


        // GET SOURCE BUTTON
        Button sourceButton = new Button("Get Source");
        root.add(sourceButton, 2, 0);
        sourceButton.setOnAction(

                event -> {

                    // FILE CHOOSER CLASS TO ACCESS FILE DIRECTORY
                    // THIS OPENS UP THE DIRECTORY FOR USER TO FIND THEIR SOURCE FILE
                    FileChooser chooser = new FileChooser();
                    chooser.setTitle("Find your source file . . .");
                    chooser.getExtensionFilters().addAll(
                            new FileChooser.ExtensionFilter("Source Files", "*.java", "*.py"),
                            new FileChooser.ExtensionFilter("All Files", "*"));

                    // THIS PRINTS THE SOURCE FILE INTO THE TEXT-FIELD TO DISPLAY THEIR DIRECTORY
                    File selectedFile = chooser.showOpenDialog(null);

                    if(selectedFile != null) {

                       fromDirectoryField.setText(selectedFile.getAbsolutePath());

                    } else {

                        // CONTENTS OF FIELD IS NOT ALTERED, NOTHING HAPPENS
                        fromDirectoryField.setText("");
                    }
                }
        );


        // A READ-ONLY TEXT FIELD TO DISPLAY TARGET DIRECTORY
        TextField targetDirectoryField = new TextField();
        root.add(targetDirectoryField, 0,1);
        targetDirectoryField.setEditable(false);


        // TARGET BUTTON
        Button targetButton = new Button("Target");
        root.add(targetButton, 1, 1);
        targetButton.setPrefWidth(73);
        targetButton.setOnAction((EventHandler<ActionEvent>) event -> {

            // ALLOWS USER TO SEARCH FOR TARGET DESTINATION IN FILE EXPLORER
            DirectoryChooser destination = new DirectoryChooser();
            File selectedDirectory =  destination.showDialog(primaryStage);

            if(selectedDirectory == null){

                // CONTENTS OF FIELD IS NOT ALTERED, NOTHING HAPPENS
                targetDirectoryField.setText("");

            } else {

                // PRINTS OUT DIRECTORY PATH TO TARGET DIRECTORY TEXT FIELD
                targetDirectoryField.setText(selectedDirectory.getAbsolutePath());

            }
        });


        // 1 - 9 VALUE QUALITY SLIDER
        Slider qualitySlider = new Slider();
        root.add(qualitySlider, 0,2);
        qualitySlider.setMin(1);
        qualitySlider.setMax(9);
        qualitySlider.setShowTickLabels(true);
        qualitySlider.setShowTickMarks(true);
        qualitySlider.setMinorTickCount(1);
        qualitySlider.setMajorTickUnit(4);
        qualitySlider.setBlockIncrement(1);

        // PROGRESS BAR
        ProgressBar progressBar = new ProgressBar();
        root.add(progressBar, 0, 3);
        progressBar.setProgress(0);

        // VARIABLES USED FOR START AND STEP BUTTONS
        final String[] source = new String[1];
        final String[] destination = new String[1];


        // START BUTTON
        Button startButton = new Button("Start");
        root.add(startButton, 2,2);
        startButton.setPrefWidth(73);
        startButton.setOnAction(

                event -> {


                    // IF SOURCE FOLDER OR DESTINATION FOLDER ARE NOT DEFINED, PRINT ERROR
                    // ELSE SAVE THE SOURCE AND DESTINATION
                    if(targetDirectoryField.getText().equals("") || fromDirectoryField.getText().equals("")) {

                        Alert complete = new Alert(Alert.AlertType.WARNING);
                        complete.setTitle("Error");
                        complete.setContentText("Error : Please check if you have chosen a source and destination folder.");
                        complete.showAndWait();

                    } else {

                        System.out.println("Source and target directory saved.");
                        System.out.println("Progress bar successfully set to 0.");
                        source[0] = fromDirectoryField.getText();
                        destination[0] = targetDirectoryField.getText();
                        progressBar.setProgress(0);

                    }



                } // END OF EVENT

        );

        // STEP BUTTON
        Button stepButton = new Button("Step");
        root.add(stepButton, 2, 3);
        stepButton.setPrefWidth(73);
        stepButton.setOnAction(

                event -> {

                    double size = 10.0;

                    // IF SOURCE FOLDER OR DESTINATION FOLDER ARE NOT DEFINED, PRINT ERROR
                    if(targetDirectoryField.getText().equals("") || fromDirectoryField.getText().equals("")){

                        Alert complete = new Alert(Alert.AlertType.WARNING);
                        complete.setTitle("Error");
                        complete.setContentText("Error : Please check if you have chosen a source and destination folder.");
                        complete.showAndWait();

                    }

                    // IF PROGRESS BAR IS EQUAL TO 100, SET INDETERMINATE & SHOW WINDOW DIALOG
                    if(increment == 1.0) {

                        System.out.println("Progress bar equal to 100. Setting to indeterminate.");
                        progressBar.isIndeterminate();

                        Alert complete = new Alert(Alert.AlertType.WARNING);
                        complete.setTitle("Complete.");
                        complete.setContentText("The process is complete.");
                        complete.showAndWait();

                    } else {

                        System.out.println("Progress bar successfully incremented by 10%");
                        progressBar.setProgress(increment);
                        increment = (double) (increment + 0.1);

                    }



                } // END OF EVENT

        );

        TabPane tabPane = new TabPane();
        Tab tab = new Tab();
        tab.setText("new tab");
        tab.setContent(new Rectangle(200, 200, Color.LIGHTSTEELBLUE));
        tabPane.getTabs().add(tab);

        ScrollBar scrollBar = new ScrollBar();
        tab.setContent(scrollBar);
        //scrollBar.setContextMenu();


        // SET THE HGROW TO ALWAYS TO HAVE IT BE RESPONSIVE AND SCALE OUT TO THE WIDTH OF THE WINDOW
        HBox.setHgrow(fromDirectoryField, Priority.ALWAYS);
        HBox.setHgrow(targetDirectoryField, Priority.ALWAYS);
        HBox.setHgrow(qualitySlider, Priority.ALWAYS);
        HBox.setHgrow(progressBar, Priority.ALWAYS);

        // CONTAINED WTIHIN HBOXS TO LAYOUT THE WIDGETS
        HBox hBoxTop = new HBox();
        hBoxTop.setSpacing(10);
        hBoxTop.setPadding(new Insets(10));
        hBoxTop.getChildren().addAll(fromDirectoryField, sourceButton);

        HBox hBoxCenter = new HBox();
        hBoxCenter.setSpacing(10);
        hBoxCenter.setPadding(new Insets(10));
        hBoxCenter.getChildren().addAll(targetDirectoryField, targetButton);

        HBox hBoxBottom = new HBox();
        hBoxBottom.setSpacing(10);
        hBoxBottom.setPadding(new Insets(10));
        hBoxBottom.getChildren().addAll(qualitySlider, startButton, progressBar, stepButton);

        // SETS UP LAYOUT BASED ON TOP, CENTER, LEFT, RIGHT, BOTTOM
        BorderPane pane = new BorderPane();
        pane.setTop(hBoxTop);
        pane.setCenter(hBoxCenter);
        pane.setBottom(hBoxBottom);

        primaryStage.setScene(new Scene(pane, 500, 400));
        primaryStage.show();

    }

    // METHOD USED TO OPEN A NEW WINDOW ABOVE THE PARENT
//    private Window getOwnerWindow() {
//
//        Scene parentScene = this.getScene();
//        if(parentScene != null){
//
//            return parentScene.getWindow();
//
//        }
//
//        return null;
//
//    }

}