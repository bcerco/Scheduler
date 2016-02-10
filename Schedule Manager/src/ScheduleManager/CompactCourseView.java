package ScheduleManager;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/*
 * DRAG AND DROP IS GLITCHY RIGHT NOW
 * Only happens when crossing columns
 * When a classview moves into another column, physically move it into that column's track.
 * Otherwise the track blocks drag and drop of elements under it (any class obstructed by the pane)
 */

/*
 * Dragging gets lost when the mouse leaves the class view area. Make this work no matter what!
 */

public class CompactCourseView extends VBox {
	private String cid;

	private String course;
	private String number;
	private String divider = "-";
	private short  section;
	private String start;
	private String end;
	private boolean isDragging;

	public CompactCourseView(String course, String number, short section, int sTime, int eTime, String color) {
		this.isDragging = false;
		this.getStyleClass().add("CompactCourse");
		this.setStyle("-fx-background-color: " + color);
		this.cid      = course + number + section;
		this.course  = course;
		this.number  = number;
		this.section = section;

		this.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				if (event.getY() < (CompactCourseView.this.getHeight() - 5) && CompactCourseView.this.isDragging == false) {
					double halfWidth = CompactCourseView.this.getWidth() / 2;
					double halfHeight = CompactCourseView.this.getHeight() / 2;

					CompactCourseView.this.setLayoutX(CompactCourseView.this.getLayoutX() + event.getX() - halfWidth);
					CompactCourseView.this.setLayoutY(CompactCourseView.this.getLayoutY() + event.getY() - halfHeight);
				}
				else {
					CompactCourseView.this.isDragging = true;
					CompactCourseView.this.setMinHeight(event.getY());
					CompactCourseView.this.setMaxHeight(event.getY());
				}

				event.consume();
			}

		});

		this.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				CompactCourseView.this.isDragging = false;

				event.consume();
			}

		});

		int    startHour = (int)Math.floor(sTime/60);
		String startAMPM = "";
		if (startHour > 12) {
			startHour = startHour - 12;
			startAMPM = "PM";
		}
		else if (startHour == 12) {
			startAMPM = "PM";
		}
		else {
			startAMPM = "AM";
		}
		int    tempMinute = (int)Math.floor(sTime%60);
		String startMinute = "";
		if (tempMinute < 10) {
			startMinute = "0" + Integer.toString(tempMinute);
		}
		else {
			startMinute = Integer.toString(tempMinute);
		}

		int    endHour = (int)Math.floor(eTime/60);
		String endAMPM = "";
		if (endHour > 12) {
			endHour = endHour - 12;
			endAMPM = "PM";
		}
		else if (endHour == 12) {
			endAMPM = "PM";
		}
		else {
			endAMPM = "AM";
		}
		tempMinute = (int)Math.floor(eTime%60);
		String endMinute = "";
		if (tempMinute < 10) {
			endMinute = "0" + Integer.toString(tempMinute);
		}
		else {
			endMinute = Integer.toString(tempMinute);
		}

		this.start = Integer.toString(startHour) + ":" + startMinute + startAMPM;
		this.end = Integer.toString(endHour) + ":" + endMinute + endAMPM;

		double heightOfCell = (WeeklyScheduleCourseTracks.height)/(WeeklyScheduleView.endHour - WeeklyScheduleView.startHour);
		double pixelMinutes = (heightOfCell / 60);
		double heightOfClass = pixelMinutes * (eTime - sTime);

		this.setPrefWidth(65);
		this.setPrefHeight(heightOfClass);

		BuildCompactCourseView();
	}

	private void BuildCompactCourseView() {
		HBox information = new HBox();
		information.setAlignment(Pos.CENTER);
		Label courseLabel = new Label(this.course);
		courseLabel.getStyleClass().add("CompactCourseText");
		Label numberLabel = new Label(this.number);
		numberLabel.getStyleClass().add("CompactCourseText");
		Label dividerLabel = new Label(this.divider);
		dividerLabel.getStyleClass().add("CompactCourseText");
		Label sectionLabel = new Label(Short.toString(this.section));
		sectionLabel.getStyleClass().add("CompactCourseText");
		information.getChildren().addAll(courseLabel, numberLabel, dividerLabel, sectionLabel);

		HBox startTimeInformation = new HBox();
		Label startLabel = new Label("S: ");
		startLabel.getStyleClass().add("CompactCourseText");
		Label startTimeLabel = new Label(this.start);
		startTimeLabel.getStyleClass().add("CompactCourseText");
		startTimeInformation.getChildren().addAll(startLabel, startTimeLabel);

		HBox endTimeInformation = new HBox();
		Label endLabel = new Label("E: ");
		endLabel.getStyleClass().add("CompactCourseText");
		Label endTimeLabel = new Label(this.end);
		endTimeLabel.getStyleClass().add("CompactCourseText");
		endTimeInformation.getChildren().addAll(endLabel, endTimeLabel);

		this.getChildren().addAll(information, startTimeInformation, endTimeInformation);
	}

	public String getCid() {
		return cid;
	}
}
