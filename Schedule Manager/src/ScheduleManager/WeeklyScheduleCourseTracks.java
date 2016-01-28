package ScheduleManager;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class WeeklyScheduleCourseTracks extends GridPane {

	public static double height;

	private ColumnConstraints ccColDefault;
	private RowConstraints    rcRowDefault;
	private ColumnConstraints ccColGrow;
	private RowConstraints    rcRowGrow;

	public WeeklyScheduleCourseTracks(int sHour, int eHour) {
		BuildConstraints();
		BuildWeeklyViewClassTracks();
	}

	private void BuildConstraints() {
		ccColDefault = new ColumnConstraints();
		rcRowDefault = new RowConstraints();
		ccColDefault.setHgrow(Priority.NEVER);
		ccColDefault.setHalignment(HPos.CENTER);
		rcRowDefault.setVgrow(Priority.NEVER);

		ccColGrow    = new ColumnConstraints();
		rcRowGrow    = new RowConstraints();
		ccColGrow.setHgrow(Priority.ALWAYS);
		ccColGrow.setPrefWidth(10);
		rcRowGrow.setVgrow(Priority.ALWAYS);
		rcRowGrow.setPrefHeight(10);
	}

	private void BuildWeeklyViewClassTracks() {
		this.getColumnConstraints().add(ccColDefault);
		this.getRowConstraints().add(rcRowDefault);
		Label tempLabel = new Label("12:00PM");
		tempLabel.setPadding(new Insets(5));
		tempLabel.getStyleClass().add("TrackHeaderText");
		this.add(tempLabel, 0, 1);

		tempLabel = new Label("Wednesday");
		tempLabel.setPadding(new Insets(5));
		tempLabel.getStyleClass().add("TrackHeaderText");
		this.add(tempLabel, 1, 0);

		Pane tempPane = new Pane();
		for (int i = 0; i < 7; i++) {
			if (i == 0) {
				this.getRowConstraints().add(rcRowGrow);
			}
			this.getColumnConstraints().add(ccColGrow);
			tempPane = new Pane();
			this.add(tempPane, i+1, 1);
		}
	}
}
