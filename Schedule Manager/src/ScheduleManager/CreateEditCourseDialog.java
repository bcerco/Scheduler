package ScheduleManager;

import Class.ClassParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class CreateEditCourseDialog {
	CompactCourseView courseView = null;

	public CreateEditCourseDialog(CompactCourseView ccv) {
		this.courseView = ccv;
		createDialog();
	}

	public void createDialog() {
		ObservableList<String> optionsAMPM =
	    	    FXCollections.observableArrayList(
	    	        "AM",
	    	        "PM"
	    	    );

		Stage createEditStage = new Stage();
    	BorderPane createEditRoot = new BorderPane();
	    Scene createEditScene = new Scene(createEditRoot,640,480);
	    createEditScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	    createEditStage.setScene(createEditScene);
	    if (courseView != null) {
	    	createEditStage.setTitle("Edit Dialog");
	    }
	    else {
	    	createEditStage.setTitle("Create Dialog");
	    }
	    createEditStage.initModality(Modality.WINDOW_MODAL);
	    createEditStage.initOwner(Main.mainStage);

	    createEditStage.setOnCloseRequest((new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				createEditStage.close();
				event.consume();
			}
	    }));

	    VBox createEditMainBox = new VBox();
	    HBox idFieldRow        = new HBox();
	    HBox timeFieldRow      = new HBox();
	    HBox professorFieldRow = new HBox();
	    Label idLabel          = new Label("Course ID");
	    idLabel.setMinWidth(150);
	    idLabel.setMaxWidth(150);
	    idLabel.alignmentProperty().set(Pos.CENTER);
	    TextField courseField  = new TextField();
	    TextField numberField  = new TextField();
	    TextField sectionField = new TextField();
	    courseField.setPromptText("Course Department");
	    numberField.setPromptText("Course Number");
	    sectionField.setPromptText("Course Section");
	    Label timeLabel        = new Label("Course Time");
	    timeLabel.setMinWidth(150);
	    timeLabel.setMaxWidth(150);
	    timeLabel.alignmentProperty().set(Pos.CENTER);
	    TextField startHourField    = new TextField();
	    TextField startMinuteField  = new TextField();
	    ComboBox<String>  startAMPMField    = new ComboBox<String>(optionsAMPM);
	    startAMPMField.setMinWidth(75);
	    startAMPMField.setMaxWidth(75);
	    startHourField.setPromptText("Start Hour");
	    startMinuteField.setPromptText("Start Minute");
	    Label toLabel          = new Label("to");
	    toLabel.setMinWidth(20);
	    toLabel.setMaxWidth(20);
	    toLabel.alignmentProperty().set(Pos.CENTER);
	    TextField endHourField      = new TextField();
	    TextField endMinuteField    = new TextField();
	    ComboBox<String>  endAMPMField    = new ComboBox<String>(optionsAMPM);
	    endAMPMField.setMinWidth(75);
	    endAMPMField.setMaxWidth(75);
	    endHourField.setPromptText("End Hour");
	    endMinuteField.setPromptText("End Minute");
	    Label professorLabel = new Label("Professor");
	    professorLabel.setMinWidth(150);
	    professorLabel.setMaxWidth(150);
	    professorLabel.alignmentProperty().set(Pos.CENTER);
	    TextField professorField = new TextField();
	    professorField.setPromptText("Professor");

	    idFieldRow.getChildren().add(idLabel);
	    idFieldRow.getChildren().add(courseField);
	    idFieldRow.getChildren().add(numberField);
	    idFieldRow.getChildren().add(sectionField);
	    timeFieldRow.getChildren().add(timeLabel);
	    timeFieldRow.getChildren().add(startHourField);
	    timeFieldRow.getChildren().add(startMinuteField);
	    timeFieldRow.getChildren().add(startAMPMField);
	    timeFieldRow.getChildren().add(toLabel);
	    timeFieldRow.getChildren().add(endHourField);
	    timeFieldRow.getChildren().add(endMinuteField);
	    timeFieldRow.getChildren().add(endAMPMField);
	    professorFieldRow.getChildren().add(professorLabel);
	    professorFieldRow.getChildren().add(professorField);
	    createEditMainBox.getChildren().add(idFieldRow);
	    createEditMainBox.getChildren().add(timeFieldRow);
	    createEditMainBox.getChildren().add(professorFieldRow);
	    createEditRoot.setCenter(createEditMainBox);

	    if (this.courseView != null) {
	    	courseField.setText(this.courseView.getCourse());
	    	numberField.setText(this.courseView.getNumber());
	    	sectionField.setText(Short.toString(this.courseView.getSection()));
	    	int startHour = this.courseView.getStartTime() / 60;
	    	int startMinute = this.courseView.getStartTime() % 60;
	    	if (startHour > 12) {
	    		startHour = startHour - 12;
	    		startAMPMField.setValue("PM");
	    	}
	    	else if (startHour == 12) {
	    		startAMPMField.setValue("PM");
	    	}
	    	else {
	    		startAMPMField.setValue("AM");
	    	}
	    	startHourField.setText(Integer.toString(startHour));
	    	startMinuteField.setText(Integer.toString(startMinute));
	    	int endHour = this.courseView.getEndTime() / 60;
	    	int endMinute = this.courseView.getEndTime() % 60;
	    	if (endHour > 12) {
	    		endHour = endHour - 12;
	    		endAMPMField.setValue("PM");
	    	}
	    	else if (endHour == 12) {
	    		endAMPMField.setValue("PM");
	    	}
	    	else {
	    		endAMPMField.setValue("AM");
	    	}
	    	endHourField.setText(Integer.toString(endHour));
	    	endMinuteField.setText(Integer.toString(endMinute));
	    	professorField.setText(ClassParser.classList.get(this.courseView.getCid()).getInstructor());
	    }

	    HBox.setHgrow(idLabel, Priority.NEVER);
	    HBox.setHgrow(courseField, Priority.ALWAYS);
	    HBox.setHgrow(numberField, Priority.ALWAYS);
	    HBox.setHgrow(sectionField, Priority.ALWAYS);
	    VBox.setVgrow(idFieldRow, Priority.NEVER);

	    HBox.setHgrow(timeLabel, Priority.NEVER);
	    HBox.setHgrow(startHourField, Priority.ALWAYS);
	    HBox.setHgrow(startMinuteField, Priority.ALWAYS);
	    HBox.setHgrow(toLabel, Priority.NEVER);
	    HBox.setHgrow(endHourField, Priority.ALWAYS);
	    HBox.setHgrow(endMinuteField, Priority.ALWAYS);
	    VBox.setVgrow(timeFieldRow, Priority.NEVER);

	    HBox.setHgrow(professorLabel, Priority.NEVER);
	    HBox.setHgrow(professorField, Priority.ALWAYS);
	    VBox.setVgrow(professorFieldRow, Priority.NEVER);

	    createEditStage.show();
	    createEditStage.setResizable(false);
	}
}