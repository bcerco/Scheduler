package ScheduleManager;

import java.io.File;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;


public class Main extends Application {
	public static File spreadsheet = null;

	public static double height;

	ToolBarView toolBarView;

	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setMinWidth(1024);
			primaryStage.setMinHeight(768);
			primaryStage.setTitle("Penn State Schedule Manager");
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,800,600);

			GridPane schedulePane = new GridPane();
			ColumnConstraints ccColGrow;
			RowConstraints    rcRowGrow;
			ccColGrow    = new ColumnConstraints();
			rcRowGrow    = new RowConstraints();
			ccColGrow.setHgrow(Priority.ALWAYS);
			rcRowGrow.setVgrow(Priority.ALWAYS);
			schedulePane.getColumnConstraints().add(ccColGrow);
			schedulePane.getRowConstraints().add(rcRowGrow);

			WeeklyScheduleView weeklySchedule = new WeeklyScheduleView(8,22);
			AnchorPane schedule = new AnchorPane();
			schedule.getChildren().add(weeklySchedule);
			schedulePane.add(schedule, 0, 0);

			WeeklyScheduleCourseTracks weeklyTracks = new WeeklyScheduleCourseTracks(8, 22);
			AnchorPane scheduleTracks = new AnchorPane();
			scheduleTracks.getChildren().add(weeklyTracks);
			schedulePane.add(scheduleTracks, 0, 0);

			toolBarView = new ToolBarView(primaryStage, weeklyTracks);

			root.setCenter(schedulePane);
			root.setTop(toolBarView);

			scene.widthProperty().addListener(new ChangeListener<Number>() {
			    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
			        schedule.setPrefWidth((double)newSceneWidth);
			        weeklySchedule.setPrefWidth((double)newSceneWidth);
			        weeklyTracks.setPrefWidth((double)newSceneWidth);
			    }
			});
			scene.heightProperty().addListener(new ChangeListener<Number>() {
			    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
			        schedule.setPrefHeight((double)newSceneHeight);
			        weeklySchedule.setPrefHeight((double)newSceneHeight - toolBarView.getHeight());
			        weeklyTracks.setPrefHeight((double)newSceneHeight - toolBarView.getHeight());
			        Label topLabel = (Label)weeklyTracks.getChildren().get(1);
			        WeeklyScheduleCourseTracks.height = (double)newSceneHeight - toolBarView.getHeight() - topLabel.getHeight();
			        toolBarView.PopulateTracks();
			    }
			});
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
