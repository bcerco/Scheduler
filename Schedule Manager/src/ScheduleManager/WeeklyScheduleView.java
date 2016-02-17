package ScheduleManager;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class WeeklyScheduleView extends GridPane {

	private String[]          days = {
		"Sunday",
		"Monday",
		"Tuesday",
		"Wednesday",
		"Thursday",
		"Friday",
		"Saturday"
	};

	public  static int        startHour    = 8;
	public  static int        endHour      = 22;
	private int               showSunday   = 1;
	private int               showSaturday = 1;
	private int               numRows;
	private int               numCols;

	private ColumnConstraints ccColDefault;
	private RowConstraints    rcRowDefault;
	private ColumnConstraints ccColGrow;
	private RowConstraints    rcRowGrow;

	public WeeklyScheduleView(int sHour, int eHour) {

		WeeklyScheduleView.startHour = sHour;
		WeeklyScheduleView.endHour   = eHour;

		numRows = (endHour - startHour);
		numCols = 5 + showSunday + showSaturday;

		BuildConstraints();
		BuildWeeklyView();
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
		ccColGrow.setHalignment(HPos.CENTER);
		rcRowGrow.setVgrow(Priority.ALWAYS);
		rcRowGrow.setPrefHeight(10);
	}

	private void BuildWeeklyView() {
		Label tempLabel;
		StackPane tempStackPane;
		Insets labelPadding = new Insets(5);

		this.getColumnConstraints().add(ccColDefault);
		this.getRowConstraints().add(rcRowDefault);

		tempStackPane = new StackPane();
		tempStackPane.getStyleClass().add("ScheduleHeaderCell");
		this.add(tempStackPane, 0, 0);

		for (int i = 0; i < numCols; i++) {
			tempLabel = new Label(days[i]);
			tempStackPane = new StackPane();
			tempStackPane.getStyleClass().add("ScheduleHeaderCell");
			this.getColumnConstraints().add(ccColGrow);
			tempLabel.getStyleClass().add("ScheduleHeaderText");
			tempLabel.setPadding(labelPadding);
			tempStackPane.getChildren().add(tempLabel);
			this.add(tempStackPane, i+1, 0);
		}

		for (int i = 0; i < numRows; i++) {
			tempLabel = new Label();
			tempStackPane = new StackPane();
			tempStackPane.getStyleClass().add("ScheduleHeaderCell");
			this.getRowConstraints().add(rcRowGrow);
			if ((i + startHour) < 12) {
				tempLabel = new Label(Integer.toString(i + startHour) + ":00AM");
			}
			else if ((i + startHour) == 12) {
				tempLabel = new Label(Integer.toString(i + startHour) + ":00PM");
			}
			else {
				tempLabel = new Label(Integer.toString(i + startHour - 12) + ":00PM");
			}
			tempLabel.getStyleClass().add("ScheduleHeaderText");
			tempLabel.setPadding(labelPadding);
			tempStackPane.getChildren().add(tempLabel);
			System.out.println(tempStackPane.getPrefWidth());
			this.add(tempStackPane, 0, i+1);
		}

		VBox tempVBox;

		for (int i = 0; i < numCols; i++) {
			for (int j = 0; j < numRows; j++) {
				tempVBox = new VBox();
				tempVBox.getStyleClass().add("ScheduleCell");
				this.add(tempVBox, i+1, j+1);
			}
		}
	}
}
