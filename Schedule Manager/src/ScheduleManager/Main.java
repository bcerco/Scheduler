package ScheduleManager;

import java.io.File;
import java.io.IOException;

import Class.ClassParser;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;


public class Main extends Application {
	public static File conflictsFile = null;
	public static File spreadsheet = null;

	public static double height;
	public static double appHeight;
	public static double appWidth;
	public static double minAppWidth;
	public static double minAppHeight;
	ToolBarView toolBarView;

	BorderPane settingsRoot;


	@Override
	public void start(Stage primaryStage) {
		if (!(new File("SMConfig").exists())) {
			File pathsFile = new File("SMConfig");
			pathsFile.mkdirs();
		}

		if (!(new File("SMConfig/conflicts.txt").exists())) {
			File pathsFile = new File("SMConfig/conflicts.txt");
			try {
				pathsFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (!(new File("SMConfig/colors.txt").exists())) {
			File pathsFile = new File("SMConfig/colors.txt");
			try {
				pathsFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

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
			//System.out.println(WeeklyScheduleCourseTracks.height);
			root.setCenter(schedulePane);
			root.setTop(toolBarView);
			//System.out.println(weeklyTracks.getPrefHeight());
			scene.widthProperty().addListener(new ChangeListener<Number>() {
			    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
			        schedule.setPrefWidth((double)newSceneWidth);
			        weeklySchedule.setPrefWidth((double)newSceneWidth);
			        weeklyTracks.setPrefWidth((double)newSceneWidth);
			        Label leftLabel = (Label)weeklyTracks.getChildren().get(0);
			        WeeklyScheduleCourseTracks.width = ((double)newSceneWidth - leftLabel.getWidth()) / 7;
			        WeeklyScheduleCourseTracks.leftOffset = leftLabel.getWidth();
			        //System.out.println(WeeklyScheduleCourseTracks.width);
			        appWidth = (double)newSceneWidth;
			        //System.out.println(weeklyTracks.getPrefHeight());
			    }
			});
			scene.heightProperty().addListener(new ChangeListener<Number>() {
			    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
			        schedule.setPrefHeight((double)newSceneHeight);
			        weeklySchedule.setPrefHeight((double)newSceneHeight - toolBarView.getHeight());
			        weeklyTracks.setPrefHeight((double)newSceneHeight - toolBarView.getHeight());
			        Label topLabel = (Label)weeklyTracks.getChildren().get(1);
			        WeeklyScheduleCourseTracks.height = (double)newSceneHeight - toolBarView.getHeight() - topLabel.getHeight();
			        appHeight = (double)newSceneHeight;
			        minAppHeight = 0 + toolBarView.getHeight() + topLabel.getHeight();
			        //System.out.println(WeeklyScheduleCourseTracks.height);
			        toolBarView.PopulateTracks(); // Only populating in height may be what causes graphical bugs on Mac
			        							  // But updating in both width AND height causes performance issues.
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
