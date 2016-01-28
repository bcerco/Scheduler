package ScheduleManager;

import java.io.File;

import Class.ClassNode;
import Class.ClassParser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ToolBarView extends ToolBar {
	private ClassParser parser;
	private FileChooser chooserImport = new FileChooser();

	private Button      btImport;
	private Button      btExport;

	private WeeklyScheduleCourseTracks tracks;

	public ToolBarView (Stage stage, WeeklyScheduleCourseTracks scheduleTracks) {
		this.tracks = scheduleTracks;

		chooserImport.setTitle("File Import");
		chooserImport.getExtensionFilters().addAll(
				new ExtensionFilter("Text CSV", "*.csv"));

		btImport = new Button("Import");
		btImport.setFocusTraversable(false);
		btImport.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	File checkFile = chooserImport.showOpenDialog(stage);

		    	if (checkFile != null && checkFile.exists()) {
		    		Main.spreadsheet = checkFile;
		    		String pathString = Main.spreadsheet.getAbsolutePath();
		        	parser = new ClassParser(pathString);
		        	parser.fillclassList();
		        	PopulateTracks();
		    	}
		    }
		});

		btExport = new Button("Export");
		btExport.setFocusTraversable(false);
		this.getItems().add(btImport);
		this.getItems().add(btExport);
	}

	public void PopulateTracks() {
	    for (int i = 0; i < 6; i++) {
	    	Pane tempPane = (Pane)tracks.getChildren().get(i+1 + 2);
	    	tempPane.getChildren().clear();
	    }

		if (Main.spreadsheet != null && Main.spreadsheet.exists()) {
			for (ClassNode cur : ClassParser.classList.values()) {
			    int[] sTimes = cur.getStartTime();
			    int[] eTimes = cur.getEndTime();
			    for (int i = 0; i < 6; i++) {
			    	if (sTimes[i] != 0 ) {
			    		Pane tempPane = (Pane)tracks.getChildren().get(i+1 + 2);
			    		String color = "";
			    		if (cur.getCourse().equals("MATH")) {
			    			color = "#FF7777";
			    		}
			    		else if (cur.getCourse().equals("CMPSC")) {
			    			color = "#77FF77";
			    		}
			    		else if (cur.getCourse().equals("COMP")) {
			    			color = "#7777FF";
			    		}
			    		else {
			    			color = "#000000";
			    		}

			    		double heightOfCell = (WeeklyScheduleCourseTracks.height)/(WeeklyScheduleView.endHour - WeeklyScheduleView.startHour);
			    		double pixelMinutes = (heightOfCell / 60);
			    		double positionOfClass = pixelMinutes * (sTimes[i] - (WeeklyScheduleView.startHour * 60));
			    		CompactCourseView curCourseView = new CompactCourseView(cur.getCourse(),  cur.getNumber(),  cur.getSection(), sTimes[i], eTimes[i], color);
			    		curCourseView.setTranslateY(positionOfClass+1);
			    		tempPane.getChildren().add(curCourseView);
			    	}
			    }
		    }
		}
	}
}
