package ScheduleManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import Class.ClassNode;
import Class.ClassParser;
import Class.Conflict;
import Class.Filter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ToolBarView extends ToolBar {
	public static ClassParser parser;
	private FileChooser chooserImport = new FileChooser();
	public static HashMap<String, String> colorMap = new HashMap<String, String>();

	private Button      btImport;
	private Button      btExport;
	private Button      btConflict;
	private Button      btCredits;
	public static TextField	tfFilterEdit;

	private BorderPane  conflictRoot;
	ListView<String>    conflictList;
	private boolean		conflictsVisible;

	private BorderPane  creditsRoot;
	ListView<String>    creditsList;

	public static Filter      filter;
	public static Conflict    conflict;

	private File checkFile; // Only used for checking purposes! NEVER reference elsewhere!


	public static WeeklyScheduleCourseTracks tracks;

	public ToolBarView (Stage stage, WeeklyScheduleCourseTracks scheduleTracks) {
		conflictsVisible = false;

		ToolBarView.tracks = scheduleTracks;

		ToolBarView.filter = new Filter();

		ToolBarView.conflict = new Conflict("SMConfig/conflicts.txt");

		BufferedReader colorsFile;

		String   in        = "";
		String[] colorsPair;
		try {
			colorsFile = new BufferedReader(new FileReader("SMConfig/colors.txt"));
			if (colorsFile != null) {
				while ((in = colorsFile.readLine()) != null) {
					colorsPair = in.split(";");
					colorMap.put(colorsPair[0], colorsPair[1]);
				}
			}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}

		btImport = new Button("Import");
		btImport.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	chooserImport.setTitle("File Import");
				chooserImport.getExtensionFilters().addAll(
						new ExtensionFilter("Text CSV", "*.csv"));
		    	File tempFile = chooserImport.showOpenDialog(stage);

		    	if (tempFile != null) {
		    		ToolBarView.this.checkFile = tempFile;
		    	}

		    	if (checkFile != null && checkFile.exists() && tempFile != null) {
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
					if (ToolBarView.this.checkFile != null && !tfFilterEdit.getText().equals("")){
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
					else {
						for (int i = 0; i < 6; i++) {
							Pane tempPane = (Pane)tracks.getChildren().get(i+1 + 2);
							for (Node cur: tempPane.getChildren()) {
								CompactCourseView tempCourse = (CompactCourseView)cur;
								tempCourse.setVisible(true);
							}
						}
					}
				}
			}

		});

		btExport = new Button("Export");
		btExport.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	chooserImport.setTitle("File Export");
				chooserImport.getExtensionFilters().addAll(
						new ExtensionFilter("Text CSV", "*.csv"));
		    	File checkFile = chooserImport.showSaveDialog(stage);
		    	if (checkFile != null) {
		    		String filePath = checkFile.getAbsolutePath().replaceAll("\\.csv", "");
		    		parser.exportClassList(filePath + ".csv");
		    	}
		    }
		});

		btConflict = new Button("Conflicts");
		btConflict.setOnAction(new EventHandler<ActionEvent>() {
			Button btnIgnoreConflict;

		    @Override public void handle(ActionEvent e) {
		    	if (!conflictsVisible) {
		    		conflict = new Conflict("SMConfig/conflicts.txt");
			    	Stage conflictStage = new Stage();
				    ToolBarView.this.conflictRoot = new BorderPane();
				    Scene conflictScene = new Scene(ToolBarView.this.conflictRoot,800,400);
				    conflictStage.setScene(conflictScene);
				    conflictStage.setTitle("Conflicts");
				    conflictStage.initModality(Modality.WINDOW_MODAL);
				    conflictStage.initOwner(
				        ((Node)e.getSource()).getScene().getWindow() );

				    conflictStage.setOnCloseRequest((new EventHandler<WindowEvent>() {
						@Override
						public void handle(WindowEvent event) {
							conflictsVisible = false;
							conflictStage.close();
							event.consume();
						}

				    }));

				    if (!(ClassParser.classList == null)) {
					    String conflictStringList = "";
					    HashSet<String> visited = new HashSet<String>();
					    for (String cur: ClassParser.classList.keySet()){
					    	String conflictResult = ToolBarView.conflict.timeCheck(cur);
					    	if (conflictResult != null){
					    		String[] conflictResultArray = conflictResult.split("\n");
					    		for (int i = 0; i < conflictResultArray.length; i++) {
					    			if (!conflictResultArray[i].equals("null")) {
						    			String [] tmp = conflictResultArray[i].split(" ");
						    			String c = tmp[1]; c = c.replace(".", "");
						    			String c2 = tmp[4]; c2 = c2.replace(".", "");
					    				if (!visited.contains(c2)){
					    					conflictStringList += conflictResultArray[i];
					    					conflictStringList += ";";
					    				}
					    			}
						    	}
					    	}
					    	visited.add(cur);
					    }

					    for (String inst : ClassParser.instructorList.keySet()) {
					    	//String conflictResult = ToolBarView.conflict.professorCheck(ClassParser.classList.get(id).getInstructor());
					    	String conflictResult = ToolBarView.conflict.professorCheck(inst);
					    	//String conflictCreditResult = ToolBarView.conflict.creditCheck(inst);
					    	//if (conflictCreditResult != null)
					    		//conflictStringList += conflictCreditResult.replace("\n","") + ";";
					    	if (conflictResult != null && !conflictResult.equals("null")) {
					    		String[] conflictResultArray = conflictResult.split("\n");
					    		for (int i = 0; i < conflictResultArray.length; i++) {
					    			if (!conflictResultArray[i].equals("null")) {
						    			conflictStringList += conflictResultArray[i];
							    		conflictStringList += ";";
					    			}
						    	}
					    	}
					    }

					    ObservableList<String> items = FXCollections.observableArrayList(conflictStringList.split(";"));

					    conflictList = new ListView<String>();
					    conflictList.setItems(items);
				    }

				    btnIgnoreConflict = new Button("Ignore");
				    btnIgnoreConflict.setOnAction(new EventHandler<ActionEvent>() {
					    @Override public void handle(ActionEvent e) {
					    	//int selectedIndex = conflictList.getSelectionModel().getSelectedIndex();
					    	//conflictList.getItems().remove(selectedIndex);

					    	String selectedItem = conflictList.getSelectionModel().getSelectedItem();
					    	if (selectedItem == null)
					    		return;

					    	String[] selectedItemArray = selectedItem.split(" ");
					    	String conflictType = selectedItemArray[0]; // For ignoring purposes
					    	String classId1     = selectedItemArray[6]; // For ignoring purposes
					    	classId1 = classId1.replace(".", "");
					    	String classId2     = selectedItemArray[8]; // For ignoring purposes
					    	classId2 = classId2.replace(".", "");

					    	switch (conflictType) {
					    	case "PROFESSOR:":
					    		conflict.appendConflict("ignore;time;" + classId1 + ";" + classId2);
					    		break;
					    	}

					    	conflict = new Conflict("SMConfig/conflicts.txt");
					    	if (!(ClassParser.classList == null)) {
							    String conflictStringList = "";
							    HashSet<String> visited = new HashSet<String>();
							    for (String cur: ClassParser.classList.keySet()){
							    	String conflictResult = ToolBarView.conflict.timeCheck(cur);
							    	if (conflictResult != null){
							    		String[] conflictResultArray = conflictResult.split("\n");
							    		for (int i = 0; i < conflictResultArray.length; i++) {
							    			if (!conflictResultArray[i].equals("null")) {
								    			String [] tmp = conflictResultArray[i].split(" ");
								    			String c = tmp[1]; c = c.replace(".", "");
								    			String c2 = tmp[4]; c2 = c2.replace(".", "");
							    				if (!visited.contains(c2)){
							    					conflictStringList += conflictResultArray[i];
							    					conflictStringList += ";";
							    				}
							    			}
								    	}
							    	}
							    	visited.add(cur);
							    }
							    for (String inst : ClassParser.instructorList.keySet()) {
							    	//String conflictResult = ToolBarView.conflict.professorCheck(ClassParser.classList.get(id).getInstructor());
							    	String conflictResult = ToolBarView.conflict.professorCheck(inst);
							    	String conflictCreditResult = ToolBarView.conflict.creditCheck(inst);
							    	if (conflictCreditResult != null)
							    		conflictStringList += conflictCreditResult.replace("\n","") + ";";
							    	if (conflictResult != null && !conflictResult.equals("null")) {
							    		String[] conflictResultArray = conflictResult.split("\n");
							    		for (int i = 0; i < conflictResultArray.length; i++) {
							    			if (!conflictResultArray[i].equals("null")) {
								    			conflictStringList += conflictResultArray[i];
									    		conflictStringList += ";";
							    			}
								    	}
							    	}
							    }

							    ObservableList<String> items = FXCollections.observableArrayList(conflictStringList.split(";"));

							    conflictList = new ListView<String>();
							    conflictList.setItems(items);
							    ToolBarView.this.conflictRoot.setCenter(conflictList);
						    }
					    }
					});

				    HBox conflictCommandPanel = new HBox();

				    conflictList.setStyle("-fx-font-family: 'monospace';");

				    conflictCommandPanel.getChildren().add(btnIgnoreConflict);

				    TabPane conflictTabPane  = new TabPane();
				    Tab     conflictTab      = new Tab("Conflicts");
				    Tab     conflictEditTab  = new Tab("Editor");

				    VBox    conflictPane     = new VBox();
				    conflictPane.getChildren().add(conflictCommandPanel);
				    conflictPane.getChildren().add(conflictList);

				    VBox             conflictEditPane = new VBox();
				    HBox             conflictEditRow  = new HBox();
				    ObservableList<String> conflictTypes =
				    	    FXCollections.observableArrayList(
				    	        "Time",
				    	        "Credit"
				    	    );
				    ComboBox<String> conflictType     = new ComboBox<String>(conflictTypes);
				    TextField        conflictParam1   = new TextField();
				    TextField        conflictParam2   = new TextField();
				    Button			 conflictSave     = new Button("Save Conflict");
				    conflictEditRow.getChildren().add(conflictType);
				    conflictEditRow.getChildren().add(conflictParam1);
				    conflictEditRow.getChildren().add(conflictParam2);
				    conflictEditRow.getChildren().add(conflictSave);
				    conflictEditPane.getChildren().add(conflictEditRow);

				    // conflictType.getSelectionModel().getSelectedItem();
				    conflictType.valueProperty().addListener(new ChangeListener<String>() {
						@Override
						public void changed(ObservableValue<? extends String> observable, String oldValue,
								String newValue) {
							if (newValue.equals("Time")) {
								conflictParam1.setPromptText("Class");
								conflictParam2.setPromptText("Class(es)");
							}
							else if (newValue.equals("Credit")) {
								conflictParam1.setPromptText("Professor");
								conflictParam2.setPromptText("Credits");
							}
						}
				    });

				    ListView<String> localConflictList = new ListView<String>();
				    conflictSave.setOnMouseClicked(new EventHandler<MouseEvent> () {
						@Override
						public void handle(MouseEvent event) {
							String item = conflictType.getSelectionModel().getSelectedItem();
							String conf = "";
							if (item.equals("Time")) {
								if (!conflictParam1.getText().toString().equals("") &&
									!conflictParam2.getText().toString().equals("")) {
									conf += "time;" + conflictParam1.getText().toString() + ";" + conflictParam2.getText().toString();
									conflict.appendConflict(conf);
									conflict.fillConflict();
								}
							}
							else if (item.equals("Credit")) {
								if (!conflictParam1.getText().toString().equals("") &&
									!conflictParam2.getText().toString().equals("")) {
									conf += "credit;" + conflictParam1.getText().toString() + ";" + conflictParam2.getText().toString();
									conflict.appendConflict(conf);
									conflict.fillConflict();
								}
							}

							String conflictFileContents = "";

						    String   in        = "";
							BufferedReader tempConfFile;
							try {
								tempConfFile = new BufferedReader(new FileReader("SMConfig/conflicts.txt"));
								if (tempConfFile != null) {
									while ((in = tempConfFile.readLine()) != null) {
										conflictFileContents += in + "\n";
									}
								}
							}
							catch (IOException ioe) {
								ioe.printStackTrace();
							}

						    ObservableList<String> items = FXCollections.observableArrayList(conflictFileContents.split("\n"));

						    localConflictList.setItems(items);

							event.consume();
						}
				    });
				    //System.out.println(conflictType.getSelectionModel().getSelectedItem();

				    conflictTabPane.getTabs().add(conflictTab);
				    conflictTabPane.getTabs().add(conflictEditTab);

				    conflictTab.setContent(conflictPane);
				    conflictEditTab.setContent(conflictEditPane);

				    String conflictFileContents = "";

				    String   in        = "";
					BufferedReader tempConfFile;
					try {
						tempConfFile = new BufferedReader(new FileReader("SMConfig/conflicts.txt"));
						if (tempConfFile != null) {
							while ((in = tempConfFile.readLine()) != null) {
								conflictFileContents += in + "\n";
							}
						}
					}
					catch (IOException ioe) {
						ioe.printStackTrace();
					}

				    ObservableList<String> items = FXCollections.observableArrayList(conflictFileContents.split("\n"));

				    localConflictList.setItems(items);

				    conflictEditPane.getChildren().add(localConflictList);

				    //ToolBarView.this.conflictRoot.setCenter(conflictList);
				    //ToolBarView.this.conflictRoot.setTop(conflictCommandPanel);
				    ToolBarView.this.conflictRoot.setCenter(conflictTabPane);

				    HBox.setHgrow(conflictParam1, Priority.ALWAYS);
				    HBox.setHgrow(conflictParam2, Priority.ALWAYS);

				    conflictStage.show();
				    conflictsVisible = true;
		    	}
		    }
		});

		btCredits = new Button("Credits");
		btCredits.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	Stage creditsStage = new Stage();
			    ToolBarView.this.creditsRoot = new BorderPane();
			    Scene creditsScene = new Scene(ToolBarView.this.creditsRoot,800,400);
			    creditsStage.setScene(creditsScene);
			    creditsStage.setTitle("Credits");
			    creditsStage.initModality(Modality.WINDOW_MODAL);
			    creditsStage.initOwner(
			        ((Node)e.getSource()).getScene().getWindow() );

			    creditsStage.setOnCloseRequest((new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent event) {
						creditsStage.close();
						event.consume();
					}

			    }));

			    Label header = new Label(" Instructor          Current             Expected");
			    header.setStyle("-fx-font-family: 'monospace';");

			    conflictList = new ListView<String>();
			    if (ClassParser.instructorCredit != null) {
			    	String creditStringList = ClassParser.exportInstructorCredits(conflict.creditNum);
			    	ObservableList<String> items = FXCollections.observableArrayList(creditStringList.split("\n"));
			    	conflictList.setItems(items);
			    }

			    conflictList.setStyle("-fx-font-family: 'monospace';");

			    ToolBarView.this.creditsRoot.setTop(header);
			    ToolBarView.this.creditsRoot.setCenter(conflictList);

			    creditsStage.show();
		    }
		});

		this.getItems().add(btImport);
		this.getItems().add(btExport);
		this.getItems().add(btConflict);
		this.getItems().add(btCredits);
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

			    		if (colorMap.containsKey(cur.getCourse())) {
			    			color = colorMap.get(cur.getCourse());
			    		}
			    		else {
			    			color = "#777777";
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
			    		curCourseView.setMinWidth(WeeklyScheduleCourseTracks.width - 6);
			    		curCourseView.setMaxWidth(WeeklyScheduleCourseTracks.width - 6);
			    		curCourseView.setLayoutX(3);
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
