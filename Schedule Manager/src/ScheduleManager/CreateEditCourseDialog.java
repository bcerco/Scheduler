package ScheduleManager;

import Class.ClassParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
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
	    HBox nameFieldRow      = new HBox();
	    HBox idFieldRow        = new HBox();
	    HBox professorFieldRow = new HBox();
	    Label nameLabel		   = new Label("Course Title");
	    nameLabel.setMinWidth(125);
	    nameLabel.setMaxWidth(125);
	    nameLabel.alignmentProperty().set(Pos.CENTER);
	    TextField nameField	   = new TextField();
	    Label idLabel          = new Label("Course ID");
	    idLabel.setMinWidth(125);
	    idLabel.setMaxWidth(125);
	    idLabel.alignmentProperty().set(Pos.CENTER);
	    TextField courseField  = new TextField();
	    TextField numberField  = new TextField();
	    TextField sectionField = new TextField();
	    courseField.setPromptText("Course Department");
	    numberField.setPromptText("Course Number");
	    sectionField.setPromptText("Course Section");
	    Label professorLabel = new Label("Professor");
	    professorLabel.setMinWidth(125);
	    professorLabel.setMaxWidth(125);
	    professorLabel.alignmentProperty().set(Pos.CENTER);
	    TextField professorField = new TextField();
	    professorField.setPromptText("Professor");

	    HBox[]      dayTimeRowArray      = new HBox[8];
	    Label[]     dayLabelsArray       = new Label[8];
	    TextField[] dayStartHoursArray   = new TextField[8];
	    TextField[] dayStartMinutesArray = new TextField[8];
	    ComboBox[]	dayStartAMPM		 = new ComboBox[8];
	    TextField[] dayEndHoursArray     = new TextField[8];
	    TextField[] dayEndMinutesArray   = new TextField[8];
	    ComboBox[]	dayEndAMPM			 = new ComboBox[8];
	    Label[]     timeSeparatorArray   = new Label[8];

	    for (int i = 1; i < 8; i++) {
	    	dayTimeRowArray[i]      = new HBox();
	    	dayLabelsArray[i]       = new Label();
	    	dayStartHoursArray[i]   = new TextField();
    		dayStartMinutesArray[i] = new TextField();
    		dayStartAMPM[i]			= new ComboBox(optionsAMPM);
    		timeSeparatorArray[i]   = new Label("to");
    		dayEndHoursArray[i]     = new TextField();
    		dayEndMinutesArray[i]   = new TextField();
    		dayEndAMPM[i]			= new ComboBox(optionsAMPM);


	    	switch(i) {
	    	case 1:
	    		dayLabelsArray[i].setText("Sunday");
	    		break;
	    	case 2:
	    		dayLabelsArray[i].setText("Monday");
	    		break;
	    	case 3:
	    		dayLabelsArray[i].setText("Tuesday");
	    		break;
	    	case 4:
	    		dayLabelsArray[i].setText("Wednesday");
	    		break;
	    	case 5:
	    		dayLabelsArray[i].setText("Thursday");
	    		break;
	    	case 6:
	    		dayLabelsArray[i].setText("Friday");
	    		break;
	    	case 7:
	    		dayLabelsArray[i].setText("Saturday");
	    		break;
	    	}

	    	dayStartHoursArray[i].setPromptText("Hour");
    		dayStartMinutesArray[i].setPromptText("Minute");
    		dayEndHoursArray[i].setPromptText("Hour");
    		dayEndMinutesArray[i].setPromptText("Minute");

	    	dayTimeRowArray[i].getChildren().add(dayLabelsArray[i]);
    		dayTimeRowArray[i].getChildren().add(dayStartHoursArray[i]);
    		dayTimeRowArray[i].getChildren().add(dayStartMinutesArray[i]);
    		dayTimeRowArray[i].getChildren().add(dayStartAMPM[i]);
    		dayStartAMPM[i].setMinWidth(75);
    		dayStartAMPM[i].setMaxWidth(75);
    		dayTimeRowArray[i].getChildren().add(timeSeparatorArray[i]);
    		timeSeparatorArray[i].setMinWidth(50);
    	    timeSeparatorArray[i].setMaxWidth(50);
    	    timeSeparatorArray[i].alignmentProperty().set(Pos.CENTER);
    		dayTimeRowArray[i].getChildren().add(dayEndHoursArray[i]);
    		dayTimeRowArray[i].getChildren().add(dayEndMinutesArray[i]);
    		dayTimeRowArray[i].getChildren().add(dayEndAMPM[i]);
    		dayEndAMPM[i].setMinWidth(75);
    		dayEndAMPM[i].setMaxWidth(75);

    		dayLabelsArray[i].setMinWidth(125);
    		dayLabelsArray[i].setMaxWidth(125);
    		dayLabelsArray[i].alignmentProperty().set(Pos.CENTER);

    		HBox.setHgrow(dayLabelsArray[i], Priority.NEVER);
    	    HBox.setHgrow(dayStartHoursArray[i], Priority.ALWAYS);
    	    HBox.setHgrow(dayStartMinutesArray[i], Priority.ALWAYS);
    	    HBox.setHgrow(dayEndHoursArray[i], Priority.ALWAYS);
    	    VBox.setVgrow(dayEndMinutesArray[i], Priority.NEVER);
	    }

	    nameFieldRow.getChildren().add(nameLabel);
	    nameFieldRow.getChildren().add(nameField);
	    idFieldRow.getChildren().add(idLabel);
	    idFieldRow.getChildren().add(courseField);
	    idFieldRow.getChildren().add(numberField);
	    idFieldRow.getChildren().add(sectionField);
	    professorFieldRow.getChildren().add(professorLabel);
	    professorFieldRow.getChildren().add(professorField);
	    createEditMainBox.getChildren().add(nameFieldRow);
	    createEditMainBox.getChildren().add(idFieldRow);
	    for (int i = 1; i < 8; i++) {
	    	createEditMainBox.getChildren().add(dayTimeRowArray[i]);
	    }
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
	    		dayStartAMPM[this.courseView.getDay() + 2].setValue("PM");
	    	}
	    	else if (startHour == 12) {
	    		dayStartAMPM[this.courseView.getDay() + 2].setValue("PM");
	    	}
	    	else {
	    		dayStartAMPM[this.courseView.getDay() + 2].setValue("AM");
	    	}
	    	dayStartHoursArray[this.courseView.getDay() + 2].setText(Integer.toString(startHour));
	    	dayStartMinutesArray[this.courseView.getDay() + 2].setText(Integer.toString(startMinute));

	    	int endHour = this.courseView.getEndTime() / 60;
	    	int endMinute = this.courseView.getEndTime() % 60;
	    	if (endHour > 12) {
	    		endHour = endHour - 12;
	    		dayEndAMPM[this.courseView.getDay() + 2].setValue("PM");
	    	}
	    	else if (endHour == 12) {
	    		dayEndAMPM[this.courseView.getDay() + 2].setValue("PM");
	    	}
	    	else {
	    		dayEndAMPM[this.courseView.getDay() + 2].setValue("AM");
	    	}
	    	dayEndHoursArray[this.courseView.getDay() + 2].setText(Integer.toString(endHour));
	    	dayEndMinutesArray[this.courseView.getDay() + 2].setText(Integer.toString(endMinute));

	    	nameField.setText(ClassParser.classList.get(this.courseView.getCid()).getTitle());
	    	professorField.setText(ClassParser.classList.get(this.courseView.getCid()).getInstructor());

	    	for (CompactCourseView ccv : this.courseView.sameCourses) {
	    		startHour = ccv.getStartTime() / 60;
		    	startMinute = ccv.getStartTime() % 60;
		    	if (startHour > 12) {
		    		startHour = startHour - 12;
		    		dayStartAMPM[ccv.getDay() + 2].setValue("PM");
		    	}
		    	else if (startHour == 12) {
		    		dayStartAMPM[ccv.getDay() + 2].setValue("PM");
		    	}
		    	else {
		    		dayStartAMPM[ccv.getDay() + 2].setValue("AM");
		    	}
	    		dayStartHoursArray[ccv.getDay() + 2].setText(Integer.toString(startHour));
		    	dayStartMinutesArray[ccv.getDay() + 2].setText(Integer.toString(startMinute));

		    	endHour = ccv.getEndTime() / 60;
		    	endMinute = ccv.getEndTime() % 60;
		    	if (endHour > 12) {
		    		endHour = endHour - 12;
		    		dayEndAMPM[ccv.getDay() + 2].setValue("PM");
		    	}
		    	else if (endHour == 12) {
		    		dayEndAMPM[ccv.getDay() + 2].setValue("PM");
		    	}
		    	else {
		    		dayEndAMPM[ccv.getDay() + 2].setValue("AM");
		    	}
		    	dayEndHoursArray[ccv.getDay() + 2].setText(Integer.toString(endHour));
		    	dayEndMinutesArray[ccv.getDay() + 2].setText(Integer.toString(endMinute));
	    	}
	    }

	    HBox.setHgrow(nameLabel, Priority.NEVER);
	    HBox.setHgrow(nameField, Priority.ALWAYS);
	    VBox.setVgrow(nameFieldRow, Priority.NEVER);

	    HBox.setHgrow(idLabel, Priority.NEVER);
	    HBox.setHgrow(courseField, Priority.ALWAYS);
	    HBox.setHgrow(numberField, Priority.ALWAYS);
	    HBox.setHgrow(sectionField, Priority.ALWAYS);
	    VBox.setVgrow(idFieldRow, Priority.NEVER);

	    HBox.setHgrow(professorLabel, Priority.NEVER);
	    HBox.setHgrow(professorField, Priority.ALWAYS);
	    VBox.setVgrow(professorFieldRow, Priority.NEVER);

	    createEditStage.show();
	    createEditStage.setResizable(false);
	}
}
