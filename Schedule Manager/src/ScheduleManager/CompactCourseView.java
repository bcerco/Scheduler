package ScheduleManager;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CompactCourseView extends VBox {
	private String course;
	private String number;
	private String divider = "-";
	private short  section;

	public CompactCourseView(String course, String number, short section, int sTime, int eTime, String color) {
		this.getStyleClass().add("CompactCourse");
		this.setStyle("-fx-background-color: " + color);
		this.course  = course;
		this.number  = number;
		this.section = section;

		this.setPrefHeight(eTime - sTime); // Base on track heights

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

		information.getChildren().add(courseLabel);
		information.getChildren().add(numberLabel);
		information.getChildren().add(dividerLabel);
		information.getChildren().add(sectionLabel);
		this.getChildren().add(information);
	}
}
