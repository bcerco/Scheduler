package ScheduleManager;

import Class.ClassParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class CreateEditCourseDialog {
	CompactCourseView courseView = null;

	boolean[] dayCreateDeleteCheck;

	public CreateEditCourseDialog(CompactCourseView ccv) {
		this.courseView = ccv;
		this.dayCreateDeleteCheck = new boolean[8];

		for (int i = 1; i < 8; i++) {
			this.dayCreateDeleteCheck[i] = false;
		}

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

	    HBox[]      dayTimeRowArray      = new HBox[8];
	    Label[]     dayLabelsArray       = new Label[8];
	    TextField[] dayStartHoursArray   = new TextField[8];
	    TextField[] dayStartMinutesArray = new TextField[8];
	    ComboBox[]	dayStartAMPM		 = new ComboBox[8];
	    TextField[] dayEndHoursArray     = new TextField[8];
	    TextField[] dayEndMinutesArray   = new TextField[8];
	    ComboBox[]	dayEndAMPM			 = new ComboBox[8];
	    Label[]     timeSeparatorArray   = new Label[8];

	    VBox createEditMainBox = new VBox();
	    HBox nameFieldRow      = new HBox();
	    HBox idFieldRow        = new HBox();
	    HBox professorFieldRow = new HBox();
	    HBox roomFieldRow      = new HBox();
	    HBox seatsFieldRow     = new HBox();
	    HBox creditsFieldRow   = new HBox();
	    HBox buttonRow		   = new HBox();
	    buttonRow.alignmentProperty().set(Pos.BOTTOM_RIGHT);
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
	    Label roomLabel = new Label("Room");
	    roomLabel.setMinWidth(125);
	    roomLabel.setMaxWidth(125);
	    roomLabel.alignmentProperty().set(Pos.CENTER);
	    TextField roomField = new TextField();
	    roomField.setPromptText("Room");
	    Label seatsLabel = new Label("Seats");
	    seatsLabel.setMinWidth(125);
	    seatsLabel.setMaxWidth(125);
	    seatsLabel.alignmentProperty().set(Pos.CENTER);
	    TextField seatsSoftField = new TextField();
	    seatsSoftField.setPromptText("Soft");
	    TextField seatsHardField = new TextField();
	    seatsHardField.setPromptText("Hard");
	    Label creditsLabel = new Label("Credits");
	    creditsLabel.setMinWidth(125);
	    creditsLabel.setMaxWidth(125);
	    creditsLabel.alignmentProperty().set(Pos.CENTER);
	    TextField creditsField = new TextField();
	    creditsField.setPromptText("Credits");
	    Button saveButton = new Button("Save");
	    saveButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				boolean deleteThisCourse = true;
				for (int i = 1; i < 8; i++) {
					if (!dayStartHoursArray[i].getText().toString().equals("") ||
						!dayStartMinutesArray[i].getText().toString().equals("")) {
						deleteThisCourse = false;
						break;
					}
				}

				if (deleteThisCourse == true) {
					Pane parent = (Pane) CreateEditCourseDialog.this.courseView.getParent();
					parent.getChildren().remove(CreateEditCourseDialog.this.courseView);
					for (CompactCourseView cv: CreateEditCourseDialog.this.courseView.sameCourses) {
						cv.sameCourses.remove(CreateEditCourseDialog.this.courseView);
					}

					for (CompactCourseView cv: CreateEditCourseDialog.this.courseView.sameCourses) {
						parent = (Pane) cv.getParent();
						parent.getChildren().remove(cv);

						for (CompactCourseView cv2: cv.sameCourses) {
							cv2.sameCourses.remove(cv);
						}
					}

					ClassParser.classList.get(CreateEditCourseDialog.this.courseView.getCid()).removeClass();

					createEditStage.close();
					event.consume();
					return;
				}
				else {
					CompactCourseView ccvDelete = null;
					boolean deleteOwnCourse = false;
					for (int i = 1; i < 8; i++) {
						if (CreateEditCourseDialog.this.dayCreateDeleteCheck[i] == true) {
							if (dayStartHoursArray[i].getText().toString().equals("") ||
								dayStartMinutesArray[i].getText().toString().equals("")) {
								// TODO: DELETE THE COURSE ON Course.day == i+2!

								for (CompactCourseView cv : CreateEditCourseDialog.this.courseView.sameCourses) {
									if (cv.getDay() == i - 2) {
										ccvDelete = cv;
									}
								}

								if (CreateEditCourseDialog.this.courseView.getDay() == i - 2) {
									deleteOwnCourse = true;
									Pane parent = (Pane) CreateEditCourseDialog.this.courseView.getParent();
									parent.getChildren().remove(CreateEditCourseDialog.this.courseView);

									for (CompactCourseView ccv : CreateEditCourseDialog.this.courseView.sameCourses) {
										ccv.sameCourses.remove(ccvDelete);
									}
								}
								else {
									Pane parent = (Pane) ccvDelete.getParent();
									parent.getChildren().remove(ccvDelete);

									for (CompactCourseView ccv : ccvDelete.sameCourses) {
										ccv.sameCourses.remove(ccvDelete);
									}
								}
							}
						}
						else {
							if (!dayStartHoursArray[i].getText().toString().equals("") &&
								!dayStartMinutesArray[i].getText().toString().equals("")) {
								// TODO: CREATE THE COURSE ON Course.day == i+2!
							}
						}
					}
					if (deleteOwnCourse) {
						ClassParser.classList.get(CreateEditCourseDialog.this.courseView.getCid()).startTime[CreateEditCourseDialog.this.courseView.getDay()] = 0;
						ClassParser.classList.get(CreateEditCourseDialog.this.courseView.getCid()).endTime[CreateEditCourseDialog.this.courseView.getDay()] = 0;
					}
					else {
						if (ccvDelete != null) {
							ClassParser.classList.get(ccvDelete.getCid()).startTime[ccvDelete.getDay()] = 0;
							ClassParser.classList.get(ccvDelete.getCid()).endTime[ccvDelete.getDay()] = 0;
						}
					}
				}

				String currentId = CreateEditCourseDialog.this.courseView.getCid();
				ClassParser.classList.get(currentId).setTitle(nameField.getText());

				currentId = CreateEditCourseDialog.this.courseView.getCid();
				ClassParser.classList.get(currentId).setCourse(courseField.getText());
				CreateEditCourseDialog.this.courseView.setCourse(courseField.getText());
				CreateEditCourseDialog.this.courseView.setCid(courseField.getText() +
						CreateEditCourseDialog.this.courseView.getNumber() +
						Short.toString(CreateEditCourseDialog.this.courseView.getSection()));

				currentId = CreateEditCourseDialog.this.courseView.getCid();
				ClassParser.classList.get(currentId).setNumber(numberField.getText());
				CreateEditCourseDialog.this.courseView.setNumber(numberField.getText());
				CreateEditCourseDialog.this.courseView.setCid(CreateEditCourseDialog.this.courseView.getCourse() +
						numberField.getText() +
						Short.toString(CreateEditCourseDialog.this.courseView.getSection()));

				currentId = CreateEditCourseDialog.this.courseView.getCid();
				ClassParser.classList.get(currentId).setSection(Short.parseShort(sectionField.getText()));
				CreateEditCourseDialog.this.courseView.setSection(Short.parseShort(sectionField.getText()));
				CreateEditCourseDialog.this.courseView.setCid(CreateEditCourseDialog.this.courseView.getCourse() +
						CreateEditCourseDialog.this.courseView.getNumber() +
						sectionField.getText());

				currentId = CreateEditCourseDialog.this.courseView.getCid();

				// Times here
				String isStartPM = dayStartAMPM[CreateEditCourseDialog.this.courseView.getDay() + 2].getValue().toString();
				int startOffset = 0;
				if (isStartPM.equals("PM") && !dayStartHoursArray[CreateEditCourseDialog.this.courseView.getDay() + 2].getText().toString().equals("12")) {
					startOffset = 12 * 60;
				}
				if (!dayStartHoursArray[CreateEditCourseDialog.this.courseView.getDay() + 2].getText().toString().equals("") &&
					!dayStartMinutesArray[CreateEditCourseDialog.this.courseView.getDay() + 2].getText().toString().equals("")) {
					ClassParser.classList.get(CreateEditCourseDialog.this.courseView.getCid())
						.startTime[CreateEditCourseDialog.this.courseView.getDay()] =
						(Integer.parseInt(dayStartHoursArray[CreateEditCourseDialog.this.courseView.getDay() + 2].getText()) * 60) +
						(Integer.parseInt(dayStartMinutesArray[CreateEditCourseDialog.this.courseView.getDay() + 2].getText())) +
						startOffset;
					CreateEditCourseDialog.this.courseView.setStart(Integer.toString((Integer.parseInt(dayStartHoursArray[CreateEditCourseDialog.this.courseView.getDay() + 2].getText()) * 60) +
							(Integer.parseInt(dayStartMinutesArray[CreateEditCourseDialog.this.courseView.getDay() + 2].getText())) +
							startOffset));
					CreateEditCourseDialog.this.courseView.setStartTime((Integer.parseInt(dayStartHoursArray[CreateEditCourseDialog.this.courseView.getDay() + 2].getText()) * 60) +
							(Integer.parseInt(dayStartMinutesArray[CreateEditCourseDialog.this.courseView.getDay() + 2].getText())) +
							startOffset);
				}

				String isEndPM = dayEndAMPM[CreateEditCourseDialog.this.courseView.getDay() + 2].getValue().toString();
				int endOffset = 0;
				if (isEndPM.equals("PM") && !dayEndHoursArray[CreateEditCourseDialog.this.courseView.getDay() + 2].getText().toString().equals("12")) {
					endOffset = 12 * 60;
				}
				if (!dayStartHoursArray[CreateEditCourseDialog.this.courseView.getDay() + 2].getText().toString().equals("") &&
					!dayStartMinutesArray[CreateEditCourseDialog.this.courseView.getDay() + 2].getText().toString().equals("")) {
					ClassParser.classList.get(CreateEditCourseDialog.this.courseView.getCid())
						.endTime[CreateEditCourseDialog.this.courseView.getDay()] =
						(Integer.parseInt(dayEndHoursArray[CreateEditCourseDialog.this.courseView.getDay() + 2].getText()) * 60) +
						(Integer.parseInt(dayEndMinutesArray[CreateEditCourseDialog.this.courseView.getDay() + 2].getText())) +
						endOffset;
					CreateEditCourseDialog.this.courseView.setEnd(Integer.toString((Integer.parseInt(dayEndHoursArray[CreateEditCourseDialog.this.courseView.getDay() + 2].getText()) * 60) +
							(Integer.parseInt(dayEndMinutesArray[CreateEditCourseDialog.this.courseView.getDay() + 2].getText())) +
							endOffset));
					CreateEditCourseDialog.this.courseView.setEndTime((Integer.parseInt(dayEndHoursArray[CreateEditCourseDialog.this.courseView.getDay() + 2].getText()) * 60) +
							(Integer.parseInt(dayEndMinutesArray[CreateEditCourseDialog.this.courseView.getDay() + 2].getText())) +
							endOffset);

					CreateEditCourseDialog.this.courseView.calculateDisplayTime();
				}

				//System.out.println(ClassParser.classList.get(CreateEditCourseDialog.this.courseView.getCid()).startTime[CreateEditCourseDialog.this.courseView.getDay()]);
				//System.out.println(ClassParser.classList.get(CreateEditCourseDialog.this.courseView.getCid()).endTime[CreateEditCourseDialog.this.courseView.getDay()]);

				ClassParser.classList.get(currentId).setInstructor(professorField.getText());
				ClassParser.classList.get(currentId).setRoom(roomField.getText());
				ClassParser.classList.get(currentId).setSoft(Short.parseShort(seatsSoftField.getText()));
				ClassParser.classList.get(currentId).setHard(Short.parseShort(seatsHardField.getText()));
				ClassParser.classList.get(currentId).setCredit(Float.parseFloat(creditsField.getText()));

				// Update visual
				CreateEditCourseDialog.this.courseView.courseLabel.setText(courseField.getText());
				CreateEditCourseDialog.this.courseView.numberLabel.setText(numberField.getText());
				CreateEditCourseDialog.this.courseView.sectionLabel.setText(sectionField.getText());

				String courseColor = ToolBarView.colorMap.get(CreateEditCourseDialog.this.courseView.getCourse());
				if (courseColor != null) {
					CreateEditCourseDialog.this.courseView.setStyle("-fx-background-color: " + courseColor);
				}
				else {
					CreateEditCourseDialog.this.courseView.setStyle("-fx-background-color: #000000");
				}

				double heightOfCell = (WeeklyScheduleCourseTracks.height)/(WeeklyScheduleView.endHour - WeeklyScheduleView.startHour);
	    		double pixelMinutes = (heightOfCell / 60);
	    		double positionOfClass = pixelMinutes * (CreateEditCourseDialog.this.courseView.getStartTime() - (WeeklyScheduleView.startHour * 60));
	    		CreateEditCourseDialog.this.courseView.setTranslateY(positionOfClass);
	    		double courseHeight = (CreateEditCourseDialog.this.courseView.getEndTime() - CreateEditCourseDialog.this.courseView.getStartTime()) * pixelMinutes;
	    		CreateEditCourseDialog.this.courseView.setMinHeight(courseHeight);
	    		CreateEditCourseDialog.this.courseView.setMaxHeight(courseHeight);

				for (CompactCourseView ccv : CreateEditCourseDialog.this.courseView.sameCourses) {
					ccv.setCourse(courseField.getText());
					ccv.setCid(courseField.getText() +
							ccv.getNumber() +
							Short.toString(ccv.getSection()));

					ccv.setNumber(numberField.getText());
					ccv.setCid(ccv.getCourse() +
							numberField.getText() +
							Short.toString(ccv.getSection()));

					ccv.setSection(Short.parseShort(sectionField.getText()));
					ccv.setCid(ccv.getCourse() +
							ccv.getNumber() +
							sectionField.getText());

					isStartPM = dayStartAMPM[ccv.getDay() + 2].getValue().toString();
					startOffset = 0;
					if (isStartPM.equals("PM") && !dayStartHoursArray[ccv.getDay() + 2].getText().toString().equals("12")) {
						startOffset = 12 * 60;
					}
					if (!dayStartHoursArray[ccv.getDay() + 2].getText().toString().equals("") &&
						!dayStartMinutesArray[ccv.getDay() + 2].getText().toString().equals("")) {
						ClassParser.classList.get(CreateEditCourseDialog.this.courseView.getCid())
							.startTime[ccv.getDay()] =
							(Integer.parseInt(dayStartHoursArray[ccv.getDay() + 2].getText()) * 60) +
							(Integer.parseInt(dayStartMinutesArray[ccv.getDay() + 2].getText())) +
							startOffset;
						ccv.setStart(Integer.toString((Integer.parseInt(dayStartHoursArray[ccv.getDay() + 2].getText()) * 60) +
								(Integer.parseInt(dayStartMinutesArray[ccv.getDay() + 2].getText())) +
								startOffset));
						ccv.setStartTime((Integer.parseInt(dayStartHoursArray[ccv.getDay() + 2].getText()) * 60) +
								(Integer.parseInt(dayStartMinutesArray[ccv.getDay() + 2].getText())) +
								startOffset);
					}

					isEndPM = dayEndAMPM[ccv.getDay() + 2].getValue().toString();
					endOffset = 0;
					if (isEndPM.equals("PM") && !dayEndHoursArray[ccv.getDay() + 2].getText().toString().equals("12")) {
						endOffset = 12 * 60;
					}
					if (!dayStartHoursArray[ccv.getDay() + 2].getText().toString().equals("") &&
						!dayStartMinutesArray[ccv.getDay() + 2].getText().toString().equals("")) {
						ClassParser.classList.get(CreateEditCourseDialog.this.courseView.getCid())
							.endTime[ccv.getDay()] =
							(Integer.parseInt(dayEndHoursArray[ccv.getDay() + 2].getText()) * 60) +
							(Integer.parseInt(dayEndMinutesArray[ccv.getDay() + 2].getText())) +
							endOffset;
						ccv.setEnd(Integer.toString((Integer.parseInt(dayEndHoursArray[ccv.getDay() + 2].getText()) * 60) +
								(Integer.parseInt(dayEndMinutesArray[ccv.getDay() + 2].getText())) +
								endOffset));
						ccv.setEndTime((Integer.parseInt(dayEndHoursArray[ccv.getDay() + 2].getText()) * 60) +
								(Integer.parseInt(dayEndMinutesArray[ccv.getDay() + 2].getText())) +
								endOffset);
					}

					ccv.calculateDisplayTime();

					ccv.courseLabel.setText(courseField.getText());
					ccv.numberLabel.setText(numberField.getText());
					ccv.sectionLabel.setText(sectionField.getText());

					courseColor = ToolBarView.colorMap.get(CreateEditCourseDialog.this.courseView.getCourse());
					if (courseColor != null) {
						ccv.setStyle("-fx-background-color: " + courseColor);
					}
					else {
						ccv.setStyle("-fx-background-color: #000000");
					}

					positionOfClass = pixelMinutes * (ccv.getStartTime() - (WeeklyScheduleView.startHour * 60));
		    		ccv.setTranslateY(positionOfClass);
		    		courseHeight = (ccv.getEndTime() - ccv.getStartTime()) * pixelMinutes;
		    		ccv.setMinHeight(courseHeight);
		    		ccv.setMaxHeight(courseHeight);

					//ccv.setStyle("-fx-background-color: " + ToolBarView.colorMap.get(ccv.getCourse()));
				}

				createEditStage.close();
				event.consume();
			}
	    });

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
	    roomFieldRow.getChildren().add(roomLabel);
	    roomFieldRow.getChildren().add(roomField);
	    seatsFieldRow.getChildren().add(seatsLabel);
	    seatsFieldRow.getChildren().add(seatsSoftField);
	    seatsFieldRow.getChildren().add(seatsHardField);
	    creditsFieldRow.getChildren().add(creditsLabel);
	    creditsFieldRow.getChildren().add(creditsField);
	    buttonRow.getChildren().add(saveButton);
	    createEditMainBox.getChildren().add(nameFieldRow);
	    createEditMainBox.getChildren().add(idFieldRow);
	    for (int i = 1; i < 8; i++) {
	    	createEditMainBox.getChildren().add(dayTimeRowArray[i]);
	    }
	    createEditMainBox.getChildren().add(professorFieldRow);
	    createEditMainBox.getChildren().add(roomFieldRow);
	    createEditMainBox.getChildren().add(seatsFieldRow);
	    createEditMainBox.getChildren().add(creditsFieldRow);
	    createEditMainBox.getChildren().add(buttonRow);
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

	    	this.dayCreateDeleteCheck[this.courseView.getDay() + 2] = true;

	    	nameField.setText(ClassParser.classList.get(this.courseView.getCid()).getTitle());
	    	professorField.setText(ClassParser.classList.get(this.courseView.getCid()).getInstructor());
	    	roomField.setText(ClassParser.classList.get(this.courseView.getCid()).getRoom());
	    	seatsSoftField.setText(Short.toString(ClassParser.classList.get(this.courseView.getCid()).getSoft()));
	    	seatsHardField.setText(Short.toString(ClassParser.classList.get(this.courseView.getCid()).getHard()));
	    	creditsField.setText(Float.toString(ClassParser.classList.get(this.courseView.getCid()).getCredit()));

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

		    	this.dayCreateDeleteCheck[ccv.getDay() + 2] = true;
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

	    HBox.setHgrow(roomLabel, Priority.NEVER);
	    HBox.setHgrow(roomField, Priority.ALWAYS);
	    VBox.setVgrow(roomFieldRow, Priority.NEVER);

	    HBox.setHgrow(seatsLabel, Priority.NEVER);
	    HBox.setHgrow(seatsSoftField, Priority.ALWAYS);
	    HBox.setHgrow(seatsHardField, Priority.ALWAYS);
	    VBox.setVgrow(seatsFieldRow, Priority.NEVER);

	    HBox.setHgrow(creditsLabel, Priority.NEVER);
	    HBox.setHgrow(creditsField, Priority.ALWAYS);
	    VBox.setVgrow(creditsFieldRow, Priority.NEVER);

	    HBox.setHgrow(saveButton, Priority.NEVER);
	    VBox.setVgrow(buttonRow, Priority.ALWAYS);

	    createEditStage.show();
	    createEditStage.setResizable(false);
	}
}
