package ScheduleManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import Class.ClassNode;
import Class.ClassParser;
import Class.Conflict;
import Class.Filter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ToolBarView extends ToolBar {
	private ClassParser parser;
	private FileChooser chooserImport = new FileChooser();

	private Button      btImport;
	private Button      btExport;
	public static TextField	tfFilterEdit;

	public static Filter      filter;
	public static Conflict    conflict;

	public static WeeklyScheduleCourseTracks tracks;

	public ToolBarView (Stage stage, WeeklyScheduleCourseTracks scheduleTracks) {
		ToolBarView.tracks = scheduleTracks;

		ToolBarView.filter = new Filter();

		ToolBarView.conflict = new Conflict("conflicts.txt");

		chooserImport.setTitle("File Import");
		chooserImport.getExtensionFilters().addAll(
				new ExtensionFilter("Text CSV", "*.csv"));

		btImport = new Button("Import");
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

		HBox filterBox = new HBox();
		tfFilterEdit = new TextField();
		tfFilterEdit.setPromptText("Enter Filter");
		tfFilterEdit.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					HashSet<String> tempHashSet = filter.search(tfFilterEdit.getText());
					for (int i = 0; i < 6; i++) {
				    	Pane tempPane = (Pane)tracks.getChildren().get(i+1 + 2);
				    	for (Node cur: tempPane.getChildren()) {
				    		CompactCourseView tempCourse = (CompactCourseView)cur;
				    		if (tfFilterEdit.getText().equals("")) {
				    			tempCourse.setVisible(true);
				    		}
				    		else if (tempHashSet.contains(tempCourse.getCid())) {
				    			tempCourse.setVisible(true);
				    		}
				    		else {
				    			tempCourse.setVisible(false);
				    		}
				    	}
				    }
				}
			}

		});

		btExport = new Button("Export");
		btExport.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	File checkFile = chooserImport.showSaveDialog(stage);
		    	if (checkFile != null) {
		    		parser.exportClassList(checkFile.getAbsolutePath());
		    	}
		    }
		});

		this.getItems().add(btImport);
		this.getItems().add(btExport);
		filterBox.getChildren().add(tfFilterEdit);
		this.getItems().add(filterBox);
		HBox.setHgrow(filterBox, Priority.ALWAYS);
		HBox.setHgrow(tfFilterEdit, Priority.ALWAYS);
	}

	public void PopulateTracks() {
	    for (int i = 0; i < 6; i++) {
	    	Pane tempPane = (Pane)tracks.getChildren().get(i+1 + 2);
	    	tempPane.getChildren().clear();
	    }

		if (Main.spreadsheet != null && Main.spreadsheet.exists()) {
			ArrayList<CompactCourseView> workingSet = new ArrayList<CompactCourseView>();
			String workingCourseId = "";
			String currentCourseId = "";
			for (ClassNode cur : ClassParser.classList.values()) {
			    int[] sTimes = cur.getStartTime();
			    int[] eTimes = cur.getEndTime();
			    for (int i = 0; i < 6; i++) {
			    	if (sTimes[i] != 0 ) {
			    		Pane tempPane = (Pane)tracks.getChildren().get(i + 1 + 2);
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
			    		CompactCourseView curCourseView = new CompactCourseView(cur.getCourse(),  cur.getNumber(),  cur.getSection(), i, sTimes[i], eTimes[i], color);

			    		currentCourseId = cur.getCourse() + cur.getNumber() + cur.getSection();
			    		if (!currentCourseId.equals(workingCourseId)) {
			    			workingCourseId = currentCourseId;
			    			workingSet.clear();
			    			workingSet.add(curCourseView);
			    		}
			    		else {
			    			for (CompactCourseView cv: workingSet) {
			    				cv.sameCourses.add(curCourseView);
			    				curCourseView.sameCourses.add(cv);
			    			}
			    			workingSet.add(curCourseView);
			    		}

			    		curCourseView.setTranslateY(positionOfClass);
			    		tempPane.getChildren().add(curCourseView);
			    	}
			    }
		    }
		}

		if (tfFilterEdit != null && !tfFilterEdit.getText().equals("")) {
	        HashSet<String> tempHashSet = ToolBarView.filter.search(ToolBarView.tfFilterEdit.getText());
			for (int i = 0; i < 6; i++) {
		    	Pane tempPane = (Pane)ToolBarView.tracks.getChildren().get(i+1 + 2);
		    	for (Node cur: tempPane.getChildren()) {
		    		CompactCourseView tempCourse = (CompactCourseView)cur;
		    		if (ToolBarView.tfFilterEdit.getText().equals("")) {
		    			tempCourse.setVisible(true);
		    		}
		    		else if (tempHashSet.contains(tempCourse.getCid())) {
		    			tempCourse.setVisible(true);
		    		}
		    		else {
		    			tempCourse.setVisible(false);
		    		}
		    	}
		    }
		}
	}
}
