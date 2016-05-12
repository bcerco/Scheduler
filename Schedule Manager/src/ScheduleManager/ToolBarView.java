package ScheduleManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
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
import javafx.scene.control.ColorPicker;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

// ToolBarView
// Manages display and functionality of the toolbar at the top.
public class ToolBarView extends ToolBar {
	public static ClassParser parser;
	private FileChooser chooserImport = new FileChooser();
	private FileChooser chooserCredits = new FileChooser();
	private DirectoryChooser directoryChooser = new DirectoryChooser();
	public static HashMap<String, String> colorMap = new HashMap<String, String>();

	private Button      btImport;
	private Button      btExport;
	public static Button      btConflict;
	private Button      btCredits;
	private Button		btColors;
	private Button		btPaths;
	public static TextField	tfFilterEdit;

	private BorderPane  conflictRoot;
	ListView<String>    conflictList = new ListView<String>();
	private boolean		conflictsVisible;

	private BorderPane  creditsRoot;
	ListView<String>    creditsList = new ListView<String>();
	ListView<String>    creditsUnderloadList;
	ListView<String>    creditsOverloadList;

	public static Filter      filter;
	public static Conflict    conflict;

	private File initialDirectory;

	ColorPicker   colorPicker = new ColorPicker();

	private File checkFile; // Only used for checking purposes! NEVER reference elsewhere!


	public static WeeklyScheduleCourseTracks tracks;

	// Constructor
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
					if (in != null && !in.equals("") && !in.equals("\n")) {
						colorsPair = in.split(";");
						colorMap.put(colorsPair[0], colorsPair[1]);
					}
				}
			}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}

		BufferedReader pathsFile;
		in        = "";
		String[] currentPath;
		try {
			pathsFile = new BufferedReader(new FileReader("SMConfig/paths.txt"));
			if (pathsFile != null) {
				while ((in = pathsFile.readLine()) != null) {
					if (in != null && !in.equals("") && !in.equals("\n")) {
						currentPath = in.split(";");
						switch (currentPath[0]) {
						case "CSVPATH":
							initialDirectory = new File(currentPath[1]);
							break;
						}
					}
				}
			}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}

		if (initialDirectory != null) {
			chooserImport.setInitialDirectory(initialDirectory);
			directoryChooser.setInitialDirectory(initialDirectory);
		}

		chooserImport.getExtensionFilters().addAll(
				new ExtensionFilter("Text CSV", "*.csv"));
		chooserCredits.getExtensionFilters().addAll(
				new ExtensionFilter("Text TXT", "*.txt"));

		btImport = new Button("Import");
		btImport.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	chooserImport.setTitle("File Import");
		    	File tempFile = chooserImport.showOpenDialog(stage);

		    	if (tempFile != null) {
		    		ToolBarView.this.checkFile = tempFile;
		    	}

		    	if (checkFile != null && checkFile.exists() && tempFile != null) {
		    		Main.spreadsheet = checkFile;
		    		String pathString = Main.spreadsheet.getAbsolutePath();
		        	parser = new ClassParser(pathString);
		        	parser.alternateFillclassList();

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

				    HBox conflictCommandPanel = new HBox();
				    VBox    conflictPane     = new VBox();

				    TabPane conflictTabPane  = new TabPane();
				    Tab     conflictTab      = new Tab("Conflicts");
				    Tab     conflictEditTab  = new Tab("Editor");

				    // TODO
				    VBox             conflictEditPane = new VBox();
				    HBox             conflictEditRow  = new HBox();

				    String conflictFileContents = "";

				    ListView<String> localConflictList = new ListView<String>();

				    Button			 conflictSave     = new Button("Add");
				    Button			 conflictDelete   = new Button("Remove");

				    btnIgnoreConflict = new Button("Ignore");
				    btnIgnoreConflict.setOnAction(new EventHandler<ActionEvent>() {
				    	String conflictFileContents2 = "";
					    @Override public void handle(ActionEvent e) {
					    	//int selectedIndex = conflictList.getSelectionModel().getSelectedIndex();
					    	//conflictList.getItems().remove(selectedIndex);

					    	String selectedItem = conflictList.getSelectionModel().getSelectedItem();
					    	if (selectedItem == null || selectedItem.split(" ")[0].equals("time") || selectedItem.split(" ")[0].equals("credit") ||
					    		selectedItem.split(" ")[0].equals("WARNING:"))
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
							    conflictPane.getChildren().clear();
							    conflictPane.getChildren().add(conflictCommandPanel);
							    conflictPane.getChildren().add(conflictList);
						    }

					    	// Turn conflict button red
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

							    if (conflictStringList != null && !conflictStringList.equals("")) {
							    	ToolBarView.btConflict.getStyleClass().remove("ConflictPresentButton");
							    	ToolBarView.btConflict.getStyleClass().add("ConflictPresentButton");
							    }
							    else {
							    	ToolBarView.btConflict.getStyleClass().remove("ConflictPresentButton");
							    	//ToolBarView.btConflict.getStyleClass().add("ConflictPresentButton");
							    }

							    // Populate conflict file list
							    String   in        = "";
								BufferedReader tempConfFile;
								try {
									tempConfFile = new BufferedReader(new FileReader("SMConfig/conflicts.txt"));
									if (tempConfFile != null) {
										while ((in = tempConfFile.readLine()) != null) {
											conflictFileContents2 += in + "\n";
										}
									}
								}
								catch (IOException ioe) {
									ioe.printStackTrace();
								}

							    ObservableList<String> items = FXCollections.observableArrayList(conflictFileContents2.split("\n"));

							    localConflictList.setItems(items);

							    conflictEditPane.getChildren().clear();
							    conflictEditPane.getChildren().add(conflictEditRow);
							    conflictEditPane.getChildren().add(localConflictList);
							    conflictEditPane.getChildren().add(conflictDelete);
							}
					    }
					});

				    conflictList.setStyle("-fx-font-family: 'monospace';");

				    conflictCommandPanel.getChildren().add(btnIgnoreConflict);

				    conflictPane.getChildren().add(conflictCommandPanel);
				    conflictPane.getChildren().add(conflictList);

				    ObservableList<String> conflictTypes =
				    	    FXCollections.observableArrayList(
				    	        "Time",
				    	        "Credit"
				    	    );
				    ComboBox<String> conflictType     = new ComboBox<String>(conflictTypes);
				    TextField        conflictParam1   = new TextField();
				    TextField        conflictParam2   = new TextField();
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

				    conflictSave.setOnMouseClicked(new EventHandler<MouseEvent> () {
						@Override
						public void handle(MouseEvent event) {
							String item = conflictType.getSelectionModel().getSelectedItem();
							String conf = "";
							if (item.equals("Time")) {
								if (!conflictParam1.getText().toString().equals("") &&
									!conflictParam2.getText().toString().equals("")) {
									conf += "time;" + conflictParam1.getText().toString() + ";" + conflictParam2.getText().toString();
									boolean shouldAdd = true;
									for (String c : localConflictList.getItems()) {
										String[] ca = c.split(";");
										if ((ca[1].equals(conflictParam1.getText().toString()) && ca[2].equals(conflictParam2.getText().toString())) ||
											(ca[1].equals(conflictParam2.getText().toString()) && ca[2].equals(conflictParam1.getText().toString()))) {
											shouldAdd = false;
										}
									}
									if (shouldAdd) {
										conflict.appendConflict(conf);
										conflict.fillConflict();
									}
								}
							}
							else if (item.equals("Credit")) {
								if (!conflictParam1.getText().toString().equals("") &&
									!conflictParam2.getText().toString().equals("")) {
									conf += "credit;" + conflictParam1.getText().toString() + ";" + conflictParam2.getText().toString();
									boolean shouldAdd = true;
									for (String c : localConflictList.getItems()) {
										String[] ca = c.split(";");
										if (ca[1].equals(conflictParam1.getText().toString())) {
											shouldAdd = false;
										}
									}
									if (shouldAdd) {
										conflict.appendConflict(conf);
										conflict.fillConflict();
									}
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


				    conflictDelete.setOnMouseClicked(new EventHandler<MouseEvent> () {
						@Override
						public void handle(MouseEvent event) {
							localConflictList.getItems().remove(localConflictList.getSelectionModel().getSelectedItem());
							String toFile = "";
							for (String item : localConflictList.getItems()) {
								toFile += item + "\n";
							}
							// TODO: Rewrite conflict file
							PrintWriter tempConfFile;
							try {
								tempConfFile = new PrintWriter("SMConfig/conflicts.txt");
								if (tempConfFile != null) {
									tempConfFile.print(toFile);
									tempConfFile.close();
								}
							}
							catch (IOException ioe) {
								ioe.printStackTrace();
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
							    conflictPane.getChildren().clear();
							    conflictPane.getChildren().add(conflictCommandPanel);
							    conflictPane.getChildren().add(conflictList);
					    	}
							event.consume();
						}
				    });
				    //System.out.println(conflictType.getSelectionModel().getSelectedItem();

				    conflictTabPane.getTabs().add(conflictTab);
				    conflictTabPane.getTabs().add(conflictEditTab);

				    conflictTab.setContent(conflictPane);
				    conflictEditTab.setContent(conflictEditPane);

				    // Populate conflict file list
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
				    conflictEditPane.getChildren().add(conflictDelete);

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

			    Label creditsHeader = new Label(" Instructor          Current             Expected");
			    creditsHeader.setStyle("-fx-font-family: 'monospace';");
			    Label underloadHeader = new Label(" Instructor          Current             Expected");
			    underloadHeader.setStyle("-fx-font-family: 'monospace';");
			    Label overloadHeader = new Label(" Instructor          Current             Expected");
			    overloadHeader.setStyle("-fx-font-family: 'monospace';");

			    TabPane creditsTabPane      = new TabPane();
			    Tab     creditsTab          = new Tab("Credits");
			    VBox    creditsBox          = new VBox();
			    VBox    creditsUnderloadBox = new VBox();
			    VBox    creditsOverloadBox  = new VBox();
			    VBox    creditsEditBox      = new VBox();
			    HBox    creditsEditRow      = new HBox();
			    Tab		creditsUnderloadTab = new Tab("Underload");
			    Tab     creditsOverloadTab  = new Tab("Overload");
			    Tab     creditsEditTab      = new Tab("Editor");
			    Button  creditsExport          = new Button("Export");
			    Button  creditsUnderloadExport = new Button("Export");
			    Button  creditsOverloadExport  = new Button("Export");
			    Button  creditsEditRemove      = new Button("Remove");
			    Button  creditsEditSave        = new Button("Save");

			    ObservableList<String> conflictTypes =
			    	    FXCollections.observableArrayList(
			    	        "Time",
			    	        "Credit"
			    	    );
			    ComboBox<String> conflictType     = new ComboBox<String>(conflictTypes);
			    TextField        conflictParam1   = new TextField();
			    TextField        conflictParam2   = new TextField();
			    creditsEditRow.getChildren().add(conflictType);
			    creditsEditRow.getChildren().add(conflictParam1);
			    creditsEditRow.getChildren().add(conflictParam2);
			    creditsEditRow.getChildren().add(creditsEditSave);

			    creditsList = new ListView<String>();
			    if (ClassParser.instructorCredit != null) {
			    	String creditStringList = ClassParser.exportInstructorCredits(conflict.creditNum);
			    	ObservableList<String> items = FXCollections.observableArrayList(creditStringList.split("\n"));
			    	creditsList.setItems(items);
			    }
			    creditsList.setStyle("-fx-font-family: 'monospace';");

			    creditsUnderloadList = new ListView<String>();
			    if (ClassParser.instructorCredit != null) {
			    	String creditStringList = ClassParser.generateUnderloadList(conflict.creditNum);
			    	ObservableList<String> items = FXCollections.observableArrayList(creditStringList.split("\n"));
			    	creditsUnderloadList.setItems(items);
			    }
			    creditsUnderloadList.setStyle("-fx-font-family: 'monospace';");

			    creditsOverloadList = new ListView<String>();
			    if (ClassParser.instructorCredit != null) {
			    	String creditStringList = ClassParser.generateOverloadList(conflict.creditNum);
			    	ObservableList<String> items = FXCollections.observableArrayList(creditStringList.split("\n"));
			    	creditsOverloadList.setItems(items);
			    }
			    creditsOverloadList.setStyle("-fx-font-family: 'monospace';");

			    creditsExport.setOnAction(new EventHandler<ActionEvent>() {
				    @Override public void handle(ActionEvent e) {
				    	chooserImport.setTitle("File Export");
				    	File checkFile = chooserCredits.showSaveDialog(stage);
				    	if (checkFile != null) {
				    		String filePath = checkFile.getAbsolutePath().replaceAll("\\.txt", "");
				    		// Rewrite color file
		    				PrintWriter tempConfFile;
							try {
								tempConfFile = new PrintWriter(filePath + ".txt");
								if (tempConfFile != null) {
									tempConfFile.println(creditsHeader.getText().toString().replaceFirst("[ ]", ""));
									tempConfFile.println(ClassParser.exportInstructorCredits(conflict.creditNum));
									tempConfFile.close();
								}
							}
							catch (IOException ioe) {
								ioe.printStackTrace();
							}
				    	}
				    }
			    });

			    creditsUnderloadExport.setOnAction(new EventHandler<ActionEvent>() {
				    @Override public void handle(ActionEvent e) {
				    	chooserImport.setTitle("File Export");
				    	File checkFile = chooserCredits.showSaveDialog(stage);
				    	if (checkFile != null) {
				    		String filePath = checkFile.getAbsolutePath().replaceAll("\\.txt", "");
				    		// Rewrite color file
		    				PrintWriter tempConfFile;
							try {
								tempConfFile = new PrintWriter(filePath + ".txt");
								if (tempConfFile != null) {
									tempConfFile.println(creditsHeader.getText().toString().replaceFirst("[ ]", ""));
									tempConfFile.println(ClassParser.generateUnderloadList(conflict.creditNum));
									tempConfFile.close();
								}
							}
							catch (IOException ioe) {
								ioe.printStackTrace();
							}
				    	}
				    }
			    });

			    creditsOverloadExport.setOnAction(new EventHandler<ActionEvent>() {
				    @Override public void handle(ActionEvent e) {
				    	chooserImport.setTitle("File Export");
				    	File checkFile = chooserCredits.showSaveDialog(stage);
				    	if (checkFile != null) {
				    		String filePath = checkFile.getAbsolutePath().replaceAll("\\.txt", "");
				    		// Rewrite color file
		    				PrintWriter tempConfFile;
							try {
								tempConfFile = new PrintWriter(filePath + ".txt");
								if (tempConfFile != null) {
									tempConfFile.println(creditsHeader.getText().toString().replaceFirst("[ ]", ""));
									tempConfFile.println(ClassParser.generateOverloadList(conflict.creditNum));
									tempConfFile.close();
								}
							}
							catch (IOException ioe) {
								ioe.printStackTrace();
							}
				    	}
				    }
			    });

			    // Populate conflict file list
			    ListView<String> localConflictList = new ListView<String>();
			    String   conflictFileContents = "";
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

			    HBox.setHgrow(conflictParam1, Priority.ALWAYS);
			    HBox.setHgrow(conflictParam2, Priority.ALWAYS);

			    creditsEditRemove.setOnMouseClicked(new EventHandler<MouseEvent> () {
					@Override
					public void handle(MouseEvent event) {
						localConflictList.getItems().remove(localConflictList.getSelectionModel().getSelectedItem());
						String toFile = "";
						for (String item : localConflictList.getItems()) {
							toFile += item + "\n";
						}
						// TODO: Rewrite conflict file
						PrintWriter tempConfFile;
						try {
							tempConfFile = new PrintWriter("SMConfig/conflicts.txt");
							if (tempConfFile != null) {
								tempConfFile.print(toFile);
								tempConfFile.close();
							}
						}
						catch (IOException ioe) {
							ioe.printStackTrace();
						}

						conflict = new Conflict("SMConfig/conflicts.txt");

						creditsList = new ListView<String>();
					    if (ClassParser.instructorCredit != null) {
					    	String creditStringList = ClassParser.exportInstructorCredits(conflict.creditNum);
					    	ObservableList<String> items = FXCollections.observableArrayList(creditStringList.split("\n"));
					    	creditsList.setItems(items);
					    }
					    creditsList.setStyle("-fx-font-family: 'monospace';");

					    creditsUnderloadList = new ListView<String>();
					    if (ClassParser.instructorCredit != null) {
					    	String creditStringList = ClassParser.generateUnderloadList(conflict.creditNum);
					    	ObservableList<String> items = FXCollections.observableArrayList(creditStringList.split("\n"));
					    	creditsUnderloadList.setItems(items);
					    }
					    creditsUnderloadList.setStyle("-fx-font-family: 'monospace';");

					    creditsOverloadList = new ListView<String>();
					    if (ClassParser.instructorCredit != null) {
					    	String creditStringList = ClassParser.generateOverloadList(conflict.creditNum);
					    	ObservableList<String> items = FXCollections.observableArrayList(creditStringList.split("\n"));
					    	creditsOverloadList.setItems(items);
					    }
					    creditsOverloadList.setStyle("-fx-font-family: 'monospace';");

					    creditsBox.getChildren().clear();
					    creditsBox.getChildren().add(creditsHeader);
					    creditsBox.getChildren().add(creditsList);
					    creditsBox.getChildren().add(creditsExport);
					    creditsUnderloadBox.getChildren().clear();
					    creditsUnderloadBox.getChildren().add(underloadHeader);
					    creditsUnderloadBox.getChildren().add(creditsUnderloadList);
					    creditsUnderloadBox.getChildren().add(creditsUnderloadExport);
					    creditsOverloadBox.getChildren().clear();
					    creditsOverloadBox.getChildren().add(overloadHeader);
					    creditsOverloadBox.getChildren().add(creditsOverloadList);
					    creditsOverloadBox.getChildren().add(creditsOverloadExport);

						event.consume();
					}
			    });

			    creditsEditSave.setOnMouseClicked(new EventHandler<MouseEvent> () {
					@Override
					public void handle(MouseEvent event) {
						String item = conflictType.getSelectionModel().getSelectedItem();
						String conf = "";
						if (item.equals("Time")) {
							if (!conflictParam1.getText().toString().equals("") &&
								!conflictParam2.getText().toString().equals("")) {
								conf += "time;" + conflictParam1.getText().toString() + ";" + conflictParam2.getText().toString();
								boolean shouldAdd = true;
								for (String c : localConflictList.getItems()) {
									String[] ca = c.split(";");
									if ((ca[1].equals(conflictParam1.getText().toString()) && ca[2].equals(conflictParam2.getText().toString())) ||
										(ca[1].equals(conflictParam2.getText().toString()) && ca[2].equals(conflictParam1.getText().toString()))) {
										shouldAdd = false;
									}
								}
								if (shouldAdd) {
									conflict.appendConflict(conf);
									conflict.fillConflict();
								}
							}
						}
						else if (item.equals("Credit")) {
							if (!conflictParam1.getText().toString().equals("") &&
								!conflictParam2.getText().toString().equals("")) {
								conf += "credit;" + conflictParam1.getText().toString() + ";" + conflictParam2.getText().toString();
								boolean shouldAdd = true;
								for (String c : localConflictList.getItems()) {
									String[] ca = c.split(";");
									if (ca[1].equals(conflictParam1.getText().toString())) {
										shouldAdd = false;
									}
								}
								if (shouldAdd) {
									conflict.appendConflict(conf);
									conflict.fillConflict();
								}
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

					    conflict = new Conflict("SMConfig/conflicts.txt");

						creditsList = new ListView<String>();
					    if (ClassParser.instructorCredit != null) {
					    	String creditStringList = ClassParser.exportInstructorCredits(conflict.creditNum);
					    	ObservableList<String> items2 = FXCollections.observableArrayList(creditStringList.split("\n"));
					    	creditsList.setItems(items2);
					    }
					    creditsList.setStyle("-fx-font-family: 'monospace';");

					    creditsUnderloadList = new ListView<String>();
					    if (ClassParser.instructorCredit != null) {
					    	String creditStringList = ClassParser.generateUnderloadList(conflict.creditNum);
					    	ObservableList<String> items2 = FXCollections.observableArrayList(creditStringList.split("\n"));
					    	creditsUnderloadList.setItems(items2);
					    }
					    creditsUnderloadList.setStyle("-fx-font-family: 'monospace';");

					    creditsOverloadList = new ListView<String>();
					    if (ClassParser.instructorCredit != null) {
					    	String creditStringList = ClassParser.generateOverloadList(conflict.creditNum);
					    	ObservableList<String> items2 = FXCollections.observableArrayList(creditStringList.split("\n"));
					    	creditsOverloadList.setItems(items2);
					    }
					    creditsOverloadList.setStyle("-fx-font-family: 'monospace';");

					    creditsBox.getChildren().clear();
					    creditsBox.getChildren().add(creditsHeader);
					    creditsBox.getChildren().add(creditsList);
					    creditsBox.getChildren().add(creditsExport);
					    creditsUnderloadBox.getChildren().clear();
					    creditsUnderloadBox.getChildren().add(underloadHeader);
					    creditsUnderloadBox.getChildren().add(creditsUnderloadList);
					    creditsUnderloadBox.getChildren().add(creditsUnderloadExport);
					    creditsOverloadBox.getChildren().clear();
					    creditsOverloadBox.getChildren().add(overloadHeader);
					    creditsOverloadBox.getChildren().add(creditsOverloadList);
					    creditsOverloadBox.getChildren().add(creditsOverloadExport);

						event.consume();
					}
			    });

			    creditsBox.getChildren().add(creditsHeader);
			    creditsBox.getChildren().add(creditsList);
			    creditsBox.getChildren().add(creditsExport);
			    creditsTab.setContent(creditsBox);
			    creditsTabPane.getTabs().add(creditsTab);
			    creditsUnderloadBox.getChildren().add(underloadHeader);
			    creditsUnderloadBox.getChildren().add(creditsUnderloadList);
			    creditsUnderloadBox.getChildren().add(creditsUnderloadExport);
			    creditsUnderloadTab.setContent(creditsUnderloadBox);
			    creditsTabPane.getTabs().add(creditsUnderloadTab);
			    creditsOverloadBox.getChildren().add(overloadHeader);
			    creditsOverloadBox.getChildren().add(creditsOverloadList);
			    creditsOverloadBox.getChildren().add(creditsOverloadExport);
			    creditsOverloadTab.setContent(creditsOverloadBox);
			    creditsTabPane.getTabs().add(creditsOverloadTab);
			    // TODO: START  Add tab contents here!
			    creditsEditBox.getChildren().add(creditsEditRow);
			    creditsEditBox.getChildren().add(localConflictList);
			    creditsEditBox.getChildren().add(creditsEditRemove);
			    // TODO: FINISH Add tab contents here!
			    creditsEditTab.setContent(creditsEditBox);
			    creditsTabPane.getTabs().add(creditsEditTab);
			    ToolBarView.this.creditsRoot.setCenter(creditsTabPane);

			    creditsStage.show();
		    }
		});

		// TODO: Colors
		btColors = new Button("Colors");
		btColors.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	Stage colorsStage = new Stage();
		    	BorderPane colorsRoot = new BorderPane();
			    Scene creditsScene = new Scene(colorsRoot,500,400);
			    colorsStage.setScene(creditsScene);
			    colorsStage.setTitle("Course Colors");
			    colorsStage.initModality(Modality.WINDOW_MODAL);
			    colorsStage.initOwner(
			        ((Node)e.getSource()).getScene().getWindow() );

			    HBox colorPanelBox = new HBox();
			    HBox colorChooserBox = new HBox();
			    VBox colorListBox = new VBox();
			    VBox colorParameterBox = new VBox();

			    TextField courseField = new TextField();
			    courseField.setPromptText("Course");
			    TextField colorField = new TextField();
			    colorField.setPromptText("Color");
			    colorField.setEditable(false);
			    Button saveColorsButton = new Button("Save");
			    Button deleteColorsButton = new Button("Remove");

			    ListView<String> colorListing = new ListView<String>();

			    colorPicker.setOnAction(new EventHandler<ActionEvent>() {
			    	@Override public void handle(ActionEvent e) {
			    		String tempColor = colorPicker.getValue().toString().toUpperCase();
			    		String color = "";
			    		for (int i = 2; i < tempColor.length() - 2; i++) {
			    			color += tempColor.charAt(i);
			    		}
			    		colorField.setText("#" + color);
			    	}
		    	});

			    saveColorsButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						boolean shouldAdd = true;
						String toFile = "";

						colorListing.getSelectionModel().clearSelection();

			    		if (!courseField.getText().toString().equals("") && !colorField.getText().toString().equals("")) {
			    			if (colorMap.containsKey(courseField.getText().toString())) {
			    				shouldAdd = false;
			    			}

			    			colorMap.put(courseField.getText().toString(), colorField.getText().toString());

			    			for (int i = 0; i < colorListing.getItems().size(); i++) {
			    				String tempColor = colorListing.getItems().get(i);
			    				String[] colorMapping = tempColor.split(";");
			    				if (colorMapping[0].equals(courseField.getText().toString()) || tempColor.equals("") || tempColor.equals("\n")) {
			    					colorListing.getItems().remove(i);
			    				}
			    			}
			    			colorListing.getItems().add(courseField.getText().toString() + ";" + colorField.getText().toString());

			    			for (String i : colorListing.getItems()) {
			    				toFile += i + "\n";
			    			}

		    				// Rewrite color file
		    				PrintWriter tempConfFile;
							try {
								tempConfFile = new PrintWriter("SMConfig/colors.txt");
								if (tempConfFile != null) {
									tempConfFile.println(toFile);
									tempConfFile.close();
								}
							}
							catch (IOException ioe) {
								ioe.printStackTrace();
							}
			    		}

			    		for (int i = 0; i < 6; i++) {
				    		Pane tempPane = (Pane)tracks.getChildren().get(i + 1 + 2);
				    		for (Node child : tempPane.getChildren()) {
				    			CompactCourseView curCourse = (CompactCourseView) child;
				    			if (colorMap.containsKey(curCourse.getCourse())) {
				    				curCourse.setStyle("-fx-background-color: " + colorMap.get(curCourse.getCourse()));
				    			}
				    			else {
				    				curCourse.setStyle("-fx-background-color: #777777");
				    			}
				    		}
					    }
					}
			    });

			    deleteColorsButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						String toFile = "";
						String item = colorListing.getSelectionModel().getSelectedItem();
						String[] itemArray = item.split(";");

						if (!item.equals("") && item != null) {
							colorMap.remove(itemArray[0]);
							colorListing.getItems().remove(item);

							for (String i : colorListing.getItems()) {
			    				toFile += i + "\n";
			    			}

							// Rewrite color file
		    				PrintWriter tempConfFile;
							try {
								tempConfFile = new PrintWriter("SMConfig/colors.txt");
								if (tempConfFile != null) {
									tempConfFile.println(toFile);
									tempConfFile.close();
								}
							}
							catch (IOException ioe) {
								ioe.printStackTrace();
							}
						}

						for (int i = 0; i < 6; i++) {
				    		Pane tempPane = (Pane)tracks.getChildren().get(i + 1 + 2);
				    		for (Node child : tempPane.getChildren()) {
				    			CompactCourseView curCourse = (CompactCourseView) child;
				    			if (colorMap.containsKey(curCourse.getCourse())) {
				    				curCourse.setStyle("-fx-background-color: " + colorMap.get(curCourse.getCourse()));
				    			}
				    			else {
				    				curCourse.setStyle("-fx-background-color: #777777");
				    			}
				    		}
					    }
					}
			    });

			    colorsStage.setOnCloseRequest((new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent event) {
						colorsStage.close();
						event.consume();
					}

			    }));

			    BufferedReader tempColorsFile;

				String   in        = "";
				String   colList   = "";
				try {
					tempColorsFile = new BufferedReader(new FileReader("SMConfig/colors.txt"));
					if (tempColorsFile != null) {
						while ((in = tempColorsFile.readLine()) != null) {
							colList += in + "\n";
						}
					}
				}
				catch (IOException ioe) {
					ioe.printStackTrace();
				}

				ObservableList<String> items = FXCollections.observableArrayList(colList.split("\n"));

				colorListing.setItems(items);
				colorListing.setStyle("-fx-font-family: 'monospace';");

				colorListing.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
				    @Override
				    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				        // Your action here
				    	if (newValue != null) {
					        String[] selectedValues = newValue.split(";");
					        colorMap.put(selectedValues[0], selectedValues[1]);
					        courseField.setText(selectedValues[0]);
					        colorField.setText(selectedValues[1]);
				    	}
				    }
				});

				colorChooserBox.getChildren().add(colorField);
				colorChooserBox.getChildren().add(colorPicker);
				colorParameterBox.getChildren().add(courseField);
				colorParameterBox.getChildren().add(colorChooserBox);
				colorParameterBox.getChildren().add(saveColorsButton);
				colorListBox.getChildren().add(colorListing);
				colorListBox.getChildren().add(deleteColorsButton);
				colorPanelBox.getChildren().add(colorListBox);
				colorPanelBox.getChildren().add(colorParameterBox);
				colorsRoot.setCenter(colorPanelBox);

				HBox.setHgrow(colorParameterBox, Priority.ALWAYS);
				HBox.setHgrow(colorField, Priority.ALWAYS);

			    colorsStage.show();
			    colorsStage.setResizable(false);
		    }
		});

		btPaths = new Button("Paths");
		btPaths.setOnAction(new EventHandler<ActionEvent>() {
			String toFile = "";

		    @Override public void handle(ActionEvent e) {
		    	Stage pathsStage = new Stage();
		    	BorderPane pathsRoot = new BorderPane();
			    Scene pathsScene = new Scene(pathsRoot,800,400);
			    pathsStage.setScene(pathsScene);
			    pathsStage.setTitle("Paths");
			    pathsStage.initModality(Modality.WINDOW_MODAL);
			    pathsStage.initOwner(
			        ((Node)e.getSource()).getScene().getWindow() );

			    VBox      pathsBox        = new VBox();
			    HBox      csvDirectoryBox = new HBox();
			    HBox      actionBox		  = new HBox();
			    Label     csvDirectoryLabel = new Label("Default CSV Directory");
			    TextField csvDirectoryField = new TextField();
			    csvDirectoryField.setPromptText("Directory Path");
			    csvDirectoryField.setEditable(false);
			    Button    selectCsvDirectory = new Button("...");
			    Button	  saveDirectories = new Button("Save");

			    if (initialDirectory != null) {
			    	csvDirectoryField.setText(initialDirectory.getAbsolutePath());
			    }

			    selectCsvDirectory.setOnAction(new EventHandler<ActionEvent>() {
				    @Override public void handle(ActionEvent e) {
				    	chooserImport.setTitle("File Import");
						chooserImport.getExtensionFilters().addAll(
								new ExtensionFilter("Text CSV", "*.csv"));
				    	File tempDir = directoryChooser.showDialog(pathsStage);
				    	if (tempDir != null) {
				    		csvDirectoryField.setText(tempDir.getAbsolutePath());
				    	}
				    	e.consume();
				    }
			    });

			    pathsStage.setOnCloseRequest((new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent event) {
						conflictsVisible = false;
						pathsStage.close();
						event.consume();
					}
			    }));

			    saveDirectories.setOnAction(new EventHandler<ActionEvent>() {
				    @Override public void handle(ActionEvent e) {
				    	toFile = "CSVPATH;" + csvDirectoryField.getText().toString() + "\n";
				    	PrintWriter tempConfFile;
						try {
							tempConfFile = new PrintWriter("SMConfig/paths.txt");
							if (tempConfFile != null) {
								tempConfFile.print(toFile);
								tempConfFile.close();

								initialDirectory = new File(csvDirectoryField.getText().toString());

								if (initialDirectory != null) {
									chooserImport.setInitialDirectory(initialDirectory);
									directoryChooser.setInitialDirectory(initialDirectory);
								}
							}
						}
						catch (IOException ioe) {
							ioe.printStackTrace();
						}
				    	e.consume();
				    }
			    });

			    VBox.setVgrow(csvDirectoryBox, Priority.ALWAYS);
			    HBox.setHgrow(csvDirectoryLabel, Priority.NEVER);
			    HBox.setHgrow(csvDirectoryField, Priority.ALWAYS);

			    csvDirectoryBox.getChildren().add(csvDirectoryLabel);
			    csvDirectoryBox.getChildren().add(csvDirectoryField);
			    csvDirectoryBox.getChildren().add(selectCsvDirectory);
			    actionBox.getChildren().add(saveDirectories);
			    pathsBox.getChildren().add(csvDirectoryBox);
			    pathsBox.getChildren().add(actionBox);
			    pathsRoot.setCenter(pathsBox);

			    pathsStage.show();
		    }
		});

		this.getItems().add(btImport);
		this.getItems().add(btExport);
		this.getItems().add(btConflict);
		this.getItems().add(btCredits);
		this.getItems().add(btColors);
		this.getItems().add(btPaths);
		filterBox.getChildren().add(tfFilterEdit);
		this.getItems().add(filterBox);
		HBox.setHgrow(filterBox, Priority.ALWAYS);
		HBox.setHgrow(tfFilterEdit, Priority.ALWAYS);
	}

	// Build the tracks with classes based on classListing in ClassParser
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

		// Turn conflict button red
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

		    if (conflictStringList != null && !conflictStringList.equals("")) {
		    	ToolBarView.btConflict.getStyleClass().remove("ConflictPresentButton");
		    	ToolBarView.btConflict.getStyleClass().add("ConflictPresentButton");
		    }
		    else {
		    	ToolBarView.btConflict.getStyleClass().remove("ConflictPresentButton");
		    	//ToolBarView.btConflict.getStyleClass().add("ConflictPresentButton");
		    }
		}
	}
	public void drawClass(ClassNode cur){
		ArrayList<CompactCourseView> workingSet = new ArrayList<CompactCourseView>();
		String workingCourseId = "";
		String currentCourseId = "";
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
