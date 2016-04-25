package ScheduleManager;

import Class.ClassParser;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WeeklyScheduleCourseTracks extends GridPane {

	public static double height;
	public static double width;
	public static double leftOffset;

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
			tempPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (event.getButton() == MouseButton.SECONDARY && CompactCourseView.classPopupVisible == false) {
						showPopupDialog(event.getScreenX(), event.getScreenY());
						return;
					}
					event.consume();
				}
		    });
			this.add(tempPane, i+1, 1);
		}
	}

	public void showPopupDialog(double x, double y) {
		// Popup dialog when right-clicking on a classview
    	Stage popupStage = new Stage(StageStyle.UNDECORATED);
    	BorderPane popupRoot = new BorderPane();
	    Scene popupScene = new Scene(popupRoot,100,25);
	    popupScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	    popupStage.setScene(popupScene);
	    popupStage.setTitle("Popup Dialog");
	    popupStage.initModality(Modality.WINDOW_MODAL);
	    popupStage.initOwner(Main.mainStage);

	    popupStage.setX(x - 50);
	    popupStage.setY(y - 12);

	    /*popupStage.setOnCloseRequest((new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				popupStage.close();
				event.consume();
			}

	    }));*/

	    VBox  mainBox      = new VBox();
	    HBox  closeRow      = new HBox();
	    Label closeButton   = new Label("Close");

	    closeRow.getChildren().add(closeButton);
	    closeRow.alignmentProperty().set(Pos.CENTER);

	    closeRow.getStyleClass().add("ScheduleCell");

	    closeRow.setOnMouseEntered(new EventHandler<MouseEvent> () {
			@Override
			public void handle(MouseEvent event) {
				closeRow.getStyleClass().remove("PopupButton");
				closeRow.getStyleClass().add("PopupButtonHighlight");
				event.consume();
			}
	    });
	    closeRow.setOnMouseExited(new EventHandler<MouseEvent> () {
			@Override
			public void handle(MouseEvent event) {
				closeRow.getStyleClass().remove("PopupButtonHighlight");
				closeRow.getStyleClass().add("PopupButton");
				event.consume();
			}
	    });
	    closeRow.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				popupStage.close();
				event.consume();
			}
	    });

	    mainBox.getChildren().add(closeRow);

	    HBox.setHgrow(closeButton, Priority.ALWAYS);

	    VBox.setVgrow(closeRow, Priority.ALWAYS);

	    popupRoot.setCenter(mainBox);

	    /*popupScene.setOnMouseExited(new EventHandler<MouseEvent> () {
			@Override
			public void handle(MouseEvent event) {
				popupStage.close();
				event.consume();
			}
	    });*/

	    popupStage.show();
	    popupStage.setResizable(false);
	}
}
