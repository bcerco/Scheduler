package ScheduleManager;

import java.util.ArrayList;

import Class.ClassParser;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
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
	private int    day;
	private int    startTime;
	private int    endTime;

	private boolean isRightClicked;
	private boolean isLocked;

	Label startTimeLabel = new Label();
	Label endTimeLabel = new Label();

	public ArrayList<CompactCourseView> sameCourses;

	public CompactCourseView(String course, String number, short section, int day, int sTime, int eTime, String color) {
		this.sameCourses = new ArrayList<CompactCourseView>();

		this.isDragging = false;
		this.isLocked   = true;
		this.getStyleClass().add("CompactCourse");
		this.setStyle("-fx-background-color: " + color);
		this.cid      = course + number + section;
		this.course  = course;
		this.number  = number;
		this.section = section;

		this.startTime = sTime;
		this.endTime = eTime;
		this.day = day;

		isRightClicked = false;

		this.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				double halfWidth = CompactCourseView.this.getWidth() / 2;
				double halfHeight = CompactCourseView.this.getHeight() / 2;

				if (event.getButton() == MouseButton.SECONDARY) {
					return;
				}

				if (event.getY() < (CompactCourseView.this.getHeight() - 5) && CompactCourseView.this.isDragging == false) {
					if (event.getSceneY() + halfHeight > Main.appHeight || event.getSceneX() + halfWidth > Main.appWidth
							|| event.getSceneX() - halfWidth < 75 || event.getSceneY() - halfHeight < Main.minAppHeight)
						return;

					CompactCourseView.this.setLayoutX(CompactCourseView.this.getLayoutX() + event.getX() - halfWidth);
					CompactCourseView.this.setLayoutY(CompactCourseView.this.getLayoutY() + event.getY() - halfHeight);

					double heightOfCell = (WeeklyScheduleCourseTracks.height)/(WeeklyScheduleView.endHour - WeeklyScheduleView.startHour);
		    		double pixelMinutes = (heightOfCell / 60);
		    		CompactCourseView.this.startTime = (int)((CompactCourseView.this.getLayoutY() + CompactCourseView.this.getTranslateY())/pixelMinutes)+(WeeklyScheduleView.startHour * 60);
		    		CompactCourseView.this.endTime = (int)(CompactCourseView.this.startTime + (CompactCourseView.this.getHeight()/pixelMinutes));

		    		CompactCourseView.this.calculateDisplayTime();

					if (isLocked) {
						for (CompactCourseView cv: sameCourses) {
							cv.setLayoutX(cv.getLayoutX() + event.getX() - halfWidth);
							cv.setLayoutY(cv.getLayoutY() + event.getY() - halfHeight);

				    		cv.startTime = (int)((cv.getLayoutY() + cv.getTranslateY())/pixelMinutes)+(WeeklyScheduleView.startHour * 60);
				    		cv.endTime = (int)(cv.startTime + (cv.getHeight()/pixelMinutes));
				    		cv.calculateDisplayTime();
						}
					}

				}
				else {
					CompactCourseView.this.isDragging = true;
					if (event.getSceneY() > Main.appHeight || event.getY() < CompactCourseView.this.getPrefHeight())
						return;
					CompactCourseView.this.setMinHeight(Math.round(event.getY()));
					CompactCourseView.this.setMaxHeight(Math.round(event.getY()));

					double heightOfCell = (WeeklyScheduleCourseTracks.height)/(WeeklyScheduleView.endHour - WeeklyScheduleView.startHour);
		    		double pixelMinutes = (heightOfCell / 60);
					CompactCourseView.this.startTime = (int)((CompactCourseView.this.getLayoutY() + CompactCourseView.this.getTranslateY())/pixelMinutes)+(WeeklyScheduleView.startHour * 60);
		    		CompactCourseView.this.endTime = (int)(CompactCourseView.this.startTime + (CompactCourseView.this.getHeight()/pixelMinutes));

		    		CompactCourseView.this.calculateDisplayTime();

					if (isLocked) {
						for (CompactCourseView cv: sameCourses) {
							cv.setMinHeight(Math.round(event.getY()));
							cv.setMaxHeight(Math.round(event.getY()));

							cv.startTime = (int)((cv.getLayoutY() + cv.getTranslateY())/pixelMinutes)+(WeeklyScheduleView.startHour * 60);
				    		cv.endTime = (int)(cv.startTime + (cv.getHeight()/pixelMinutes));
							cv.calculateDisplayTime();
						}
					}
				}

				event.consume();
			}

		});

		this.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				if (event.getButton() == MouseButton.SECONDARY && isRightClicked == false) {
					isRightClicked = true;
					isLocked = !isLocked;
					System.out.println(isLocked);
					return;
				}

				event.consume();
			}

		});

		this.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				CompactCourseView.this.isRightClicked = false;
				CompactCourseView.this.isDragging = false;

				ClassParser.classList.get(cid).startTime[day] = startTime;
				ClassParser.classList.get(cid).endTime[day] = endTime;

				for (CompactCourseView cv: sameCourses) {
					ClassParser.classList.get(cv.cid).startTime[cv.day] = cv.startTime;
					ClassParser.classList.get(cv.cid).endTime[cv.day] = cv.endTime;
				}

				System.out.println(ToolBarView.conflict.professorCheck(ClassParser.classList.get(cid).getInstructor()));
				//ToolBarView.conflict.timeCheck(cid);

				event.consume();
			}

		});

		calculateDisplayTime();

		double heightOfCell = (WeeklyScheduleCourseTracks.height)/(WeeklyScheduleView.endHour - WeeklyScheduleView.startHour);
		double pixelMinutes = (heightOfCell / 60);
		double heightOfClass = pixelMinutes * (endTime - startTime);

		this.setPrefWidth(65);
		this.setPrefHeight(Math.round(heightOfClass));

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
		startTimeLabel = new Label(this.start);
		startTimeLabel.getStyleClass().add("CompactCourseText");
		startTimeInformation.getChildren().addAll(startLabel, startTimeLabel);

		HBox endTimeInformation = new HBox();
		Label endLabel = new Label("E: ");
		endLabel.getStyleClass().add("CompactCourseText");
		endTimeLabel = new Label(this.end);
		endTimeLabel.getStyleClass().add("CompactCourseText");
		endTimeInformation.getChildren().addAll(endLabel, endTimeLabel);

		this.getChildren().addAll(information, startTimeInformation, endTimeInformation);
	}

	public String getCid() {
		return cid;
	}

	public void calculateDisplayTime() {
		int    startHour = (int)Math.floor(startTime/60);
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
		int    tempMinute = (int)Math.floor(startTime%60);
		String startMinute = "";
		if (tempMinute < 10) {
			startMinute = "0" + Integer.toString(tempMinute);
		}
		else {
			startMinute = Integer.toString(tempMinute);
		}

		int    endHour = (int)Math.floor(endTime/60);
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
		tempMinute = (int)Math.floor(endTime%60);
		String endMinute = "";
		if (tempMinute < 10) {
			endMinute = "0" + Integer.toString(tempMinute);
		}
		else {
			endMinute = Integer.toString(tempMinute);
		}

		this.start = Integer.toString(startHour) + ":" + startMinute + startAMPM;
		this.end = Integer.toString(endHour) + ":" + endMinute + endAMPM;

		this.startTimeLabel.setText(this.start);
		this.endTimeLabel.setText(this.end);
	}
}
