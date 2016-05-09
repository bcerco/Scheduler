package ScheduleManager;

import java.util.ArrayList;
import java.util.HashSet;

import Class.ClassParser;
import Class.Conflict;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/*
 * Dragging gets lost when the mouse leaves the class view area. Make this work no matter what!
 */

/*
 * Use WeeklyScheduleCourseTracks.leftOffset in checking if classes cross track boundaries
 */

public class CompactCourseView extends VBox {
	private String cid;

	private String course;
	private String number;
	private String divider = "-";
	private short  section;
	private String start;
	private String end;
	private boolean isDragging;
	private int    day;
	public int    startTime;
	public int    endTime;

	public Label courseLabel;
	public Label numberLabel;
	public Label sectionLabel;
	public Label lockLabel;


	private int    track;

	private boolean isRightClicked;
	private boolean isLocked;
	public  static boolean classPopupVisible = false;

	Label startTimeLabel = new Label();
	Label endTimeLabel = new Label();

	public ArrayList<CompactCourseView> sameCourses;

	public CompactCourseView(String course, String number, short section, int day, int sTime, int eTime, String color) {
		this.sameCourses = new ArrayList<CompactCourseView>();

		this.isDragging = false;
		this.isLocked   = true;
		this.getStyleClass().add("CompactCourse");
		this.setStyle("-fx-background-color: " + color);
		this.cid      = course + number + section;
		this.course  = course;
		this.number  = number;
		this.section = section;

		this.startTime = sTime;
		this.endTime = eTime;
		this.day = day;

		isRightClicked = false;

		this.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				double halfWidth = CompactCourseView.this.getWidth() / 2;
				double halfHeight = CompactCourseView.this.getHeight() / 2;

				if (event.getButton() == MouseButton.SECONDARY) {
					return;
				}

				if (event.getY() < (CompactCourseView.this.getHeight() - 5) && CompactCourseView.this.isDragging == false) {
					if (event.getSceneY() + halfHeight > Main.appHeight || event.getSceneX() + halfWidth > Main.appWidth
							|| event.getSceneX() - halfWidth < 200 || event.getSceneY() - halfHeight < Main.minAppHeight)
						return;
					for (CompactCourseView cv: CompactCourseView.this.sameCourses){
						int trackDelta = cv.day - CompactCourseView.this.day;
						double cheightOfCell = (WeeklyScheduleCourseTracks.height) / (WeeklyScheduleView.endHour - WeeklyScheduleView.startHour);
			    		double cpixelMinutes = (cheightOfCell / 60);
			    		if (event.getSceneY() +halfHeight + ((cpixelMinutes * cv.endTime) - (cpixelMinutes * CompactCourseView.this.endTime)) > Main.appHeight ||
			    				event.getSceneY() - halfHeight - Math.abs((cpixelMinutes * cv.startTime) - (cpixelMinutes * CompactCourseView.this.startTime)) < Main.minAppHeight)
			    			return;
						//System.out.println(trackDelta);
						double deltaWidth = 0;
						if (trackDelta > 0){
							switch(trackDelta){
							case 1:
								deltaWidth = (3 * halfWidth);
								break;
							case 2:
								deltaWidth = (5 * halfWidth);
								break;
							case 3:
								deltaWidth = (7 * halfWidth);
								break;
							case 4:
								deltaWidth = (9 * halfWidth);
								break;
							default:
								break;
							}
							if (event.getSceneX() + deltaWidth > Main.appWidth)
								return;
						}
						else{
							switch(trackDelta){
							case -1:
								deltaWidth = (3 * halfWidth);
								break;
							case -2:
								deltaWidth = (5 * halfWidth);
								break;
							case -3:
								deltaWidth = (7 * halfWidth);
								break;
							case -4:
								deltaWidth = (9 * halfWidth);
								break;
							default:
								break;
							}
							if (event.getSceneX() - deltaWidth < 202)
								return;
						}

					}
					CompactCourseView.this.setLayoutX(CompactCourseView.this.getLayoutX() + event.getX() - halfWidth);
					CompactCourseView.this.setLayoutY(CompactCourseView.this.getLayoutY() + event.getY() - halfHeight);

					double heightOfCell = (WeeklyScheduleCourseTracks.height) / (WeeklyScheduleView.endHour - WeeklyScheduleView.startHour);
		    		double pixelMinutes = (heightOfCell / 60);
		    		CompactCourseView.this.startTime = (int)((CompactCourseView.this.getLayoutY() + CompactCourseView.this.getTranslateY()) / pixelMinutes) + (WeeklyScheduleView.startHour * 60);
		    		CompactCourseView.this.endTime = (int)(CompactCourseView.this.startTime + (CompactCourseView.this.getHeight() / pixelMinutes));

		    		CompactCourseView.this.calculateDisplayTime();

		    		// Calculate Schedule Track here
		    		// -1 = Move one track left
		    		//  1 = Move one track right
		    		CompactCourseView.this.track = (int)Math.floor((CompactCourseView.this.getLayoutX() + CompactCourseView.this.getTranslateX() + halfWidth) / WeeklyScheduleCourseTracks.width);
		    		//System.out.println(track);
		    		//System.out.println(CompactCourseView.this.day + 3);
		    		//System.out.println(CompactCourseView.this.day + track + 3);

					if (isLocked) {
						//System.out.println(sameCourses.size());
						for (CompactCourseView cv: sameCourses) {

							cv.setLayoutX(cv.getLayoutX() + event.getX() - halfWidth);
							cv.setLayoutY(cv.getLayoutY() + event.getY() - halfHeight);

				    		cv.startTime = (int)((cv.getLayoutY() + cv.getTranslateY()) / pixelMinutes) + (WeeklyScheduleView.startHour * 60);
							//cv.startTime = (int)((cv.getLayoutY() + event.getY()) / pixelMinutes) + (WeeklyScheduleView.startHour * 60);
				    		cv.endTime = (int)(cv.startTime + (cv.getHeight() / pixelMinutes));
				    		//System.out.println("Here - "+cv.startTime + ":" + cv.endTime);
				    		cv.calculateDisplayTime();
						}
						//System.out.println();
					}

				}
				else {
					CompactCourseView.this.isDragging = true;
					if (event.getSceneY() > Main.appHeight || event.getY() < CompactCourseView.this.getPrefHeight())
						return;
					CompactCourseView.this.setMinHeight(Math.round(event.getY()));
					CompactCourseView.this.setMaxHeight(Math.round(event.getY()));

					double heightOfCell = (WeeklyScheduleCourseTracks.height)/(WeeklyScheduleView.endHour - WeeklyScheduleView.startHour);
		    		double pixelMinutes = (heightOfCell / 60);
					CompactCourseView.this.startTime = (int)((CompactCourseView.this.getLayoutY() + CompactCourseView.this.getTranslateY())/pixelMinutes)+(WeeklyScheduleView.startHour * 60);
		    		CompactCourseView.this.endTime = (int)(CompactCourseView.this.startTime + (CompactCourseView.this.getHeight()/pixelMinutes));

		    		CompactCourseView.this.calculateDisplayTime();

					if (isLocked) {
						for (CompactCourseView cv: sameCourses) {
							cv.setMinHeight(Math.round(CompactCourseView.this.getMinHeight()));
							cv.setMaxHeight(Math.round(CompactCourseView.this.getMaxHeight()));

							cv.startTime = (int)((cv.getLayoutY() + cv.getTranslateY())/pixelMinutes)+(WeeklyScheduleView.startHour * 60);
				    		cv.endTime = (int)(cv.startTime + (cv.getHeight()/pixelMinutes));
				    		//System.out.println("Here2 - "+cv.startTime + ":" + cv.endTime);
							cv.calculateDisplayTime();
						}
					}
				}

				event.consume();
			}

		});

		this.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.SECONDARY && isRightClicked == false) {
					isRightClicked = true;
					classPopupVisible = true;

					showPopupDialog(event.getScreenX(), event.getScreenY());

					return;
				}

				event.consume();
			}

		});

		this.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				CompactCourseView.this.isRightClicked = false;
				CompactCourseView.this.isDragging = false;

				if (event.getButton() == MouseButton.PRIMARY) {
					//ClassParser.classList.get(cid).startTime[CompactCourseView.this.day] = startTime;
					//ClassParser.classList.get(cid).endTime[CompactCourseView.this.day] = endTime;
					ClassParser.classList.get(CompactCourseView.this.cid).savePrevTime();

					if ((CompactCourseView.this.day + 3) != ((CompactCourseView.this.day + CompactCourseView.this.track + 3))) {
		    			Pane fromTrackPane = (Pane)ToolBarView.tracks.getChildren().get(CompactCourseView.this.day + 3);
		    			Pane toTrackPane = (Pane)ToolBarView.tracks.getChildren().get(CompactCourseView.this.day + CompactCourseView.this.track + 3);

		    			boolean coursePresent = false;

		    			if (!CompactCourseView.this.isLocked) {
			    			Object[] courseArray = toTrackPane.getChildren().toArray();
							for (int i = 0; i < courseArray.length; i++) {
								CompactCourseView curCourse = (CompactCourseView)courseArray[i];
								if (CompactCourseView.this.cid.equals(curCourse.cid)) {
									coursePresent = true;
								}
							}
		    			}

						if (coursePresent == false) {
							fromTrackPane.getChildren().remove(CompactCourseView.this);
							toTrackPane.getChildren().add(CompactCourseView.this);

							ClassParser.classList.get(cid).startTime[CompactCourseView.this.day] = 0;
							ClassParser.classList.get(cid).endTime[CompactCourseView.this.day] = 0;
							CompactCourseView.this.day = CompactCourseView.this.day + CompactCourseView.this.track;
							ClassParser.classList.get(cid).startTime[CompactCourseView.this.day] = startTime;
							ClassParser.classList.get(cid).endTime[CompactCourseView.this.day] = endTime;
							//System.out.println(CompactCourseView.this.day + 3);
							//System.out.println(fromTrackPane.getChildren().toString());
							//System.out.println(toTrackPane.getChildren().toString());
						}

		    			//Pane currentTrack = (Pane)ToolBarView.tracks.getChildren().get(day + track + 3);
			    		//System.out.println(currentTrack.getChildren().toString());
		    		}
					CompactCourseView.this.setLayoutX(3);

					if (CompactCourseView.this.isLocked) {
						ClassParser.classList.get(cid).startTime[CompactCourseView.this.day] = startTime;
						ClassParser.classList.get(cid).endTime[CompactCourseView.this.day] = endTime;
						for (CompactCourseView cv: sameCourses) {
							Pane fromTrackPaneCV = (Pane)ToolBarView.tracks.getChildren().get(cv.day + 3);
			    			Pane toTrackPaneCV = (Pane)ToolBarView.tracks.getChildren().get(cv.day + CompactCourseView.this.track + 3);

			    			fromTrackPaneCV.getChildren().remove(cv);
							toTrackPaneCV.getChildren().add(cv);

							ClassParser.classList.get(cv.cid).startTime[cv.day] = 0;
							ClassParser.classList.get(cv.cid).endTime[cv.day] = 0;
							cv.day = cv.day + CompactCourseView.this.track;
							ClassParser.classList.get(cv.cid).startTime[cv.day] = cv.startTime;
							ClassParser.classList.get(cv.cid).endTime[cv.day] = cv.endTime;
							cv.setLayoutX(3);
						}
					}

					//System.out.println(ToolBarView.conflict.professorCheck(ClassParser.classList.get(cid).getInstructor()));
					//ToolBarView.conflict.timeCheck(cid);
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

				event.consume();
			}

		});

		calculateDisplayTime();

		double heightOfCell = (WeeklyScheduleCourseTracks.height)/(WeeklyScheduleView.endHour - WeeklyScheduleView.startHour);
		double pixelMinutes = (heightOfCell / 60);
		double heightOfClass = pixelMinutes * (endTime - startTime);

		this.setPrefWidth(65);
		this.setPrefHeight(Math.round(heightOfClass));

		BuildCompactCourseView();
	}

	private void BuildCompactCourseView() {
		HBox information = new HBox();
		information.setAlignment(Pos.CENTER);
		courseLabel = new Label(this.course);
		courseLabel.getStyleClass().add("CompactCourseText");
		numberLabel = new Label(this.number);
		numberLabel.getStyleClass().add("CompactCourseText");
		Label dividerLabel = new Label(this.divider);
		dividerLabel.getStyleClass().add("CompactCourseText");
		sectionLabel = new Label(Short.toString(this.section));
		sectionLabel.getStyleClass().add("CompactCourseText");
		lockLabel = new Label(" L ");
		lockLabel.getStyleClass().add("CompactCourseText");
		information.getChildren().addAll(courseLabel, numberLabel, dividerLabel, sectionLabel, lockLabel);

		String tooltipText = "Name: " + ClassParser.classList.get(this.getCid()).getTitle() + "\n" +
							 "Prof: " + ClassParser.classList.get(this.getCid()).getInstructor() + "\n" +
							 "Room: " + ClassParser.classList.get(this.getCid()).getRoom() + "\n" +
							 "Seat: S:" + ClassParser.classList.get(this.getCid()).getSoft() + " H:" + ClassParser.classList.get(this.getCid()).getHard() + "\n" +
							 "Cred: " + ClassParser.classList.get(this.getCid()).getCredit();
		courseLabel.setTooltip(new Tooltip(tooltipText));
		numberLabel.setTooltip(new Tooltip(tooltipText));
		dividerLabel.setTooltip(new Tooltip(tooltipText));
		sectionLabel.setTooltip(new Tooltip(tooltipText));

		HBox startTimeInformation = new HBox();
		Label startLabel = new Label("S: ");
		startLabel.getStyleClass().add("CompactCourseText");
		startTimeLabel = new Label(this.start);
		startTimeLabel.getStyleClass().add("CompactCourseText");
		startTimeInformation.getChildren().addAll(startLabel, startTimeLabel);

		HBox endTimeInformation = new HBox();
		Label endLabel = new Label("E: ");
		endLabel.getStyleClass().add("CompactCourseText");
		endTimeLabel = new Label(this.end);
		endTimeLabel.getStyleClass().add("CompactCourseText");
		endTimeInformation.getChildren().addAll(endLabel, endTimeLabel);

		this.getChildren().addAll(information, startTimeInformation, endTimeInformation);
	}

	public String getCid() {
		return cid;
	}

	public void calculateDisplayTime() {
		int    startHour = (int)Math.floor(startTime/60);
		String startAMPM = "";
		if (startHour > 12) {
			startHour = startHour - 12;

			startAMPM = "PM";
		}
		else if (startHour == 12) {
			startAMPM = "PM";
		}
		else {
			startAMPM = "AM";
		}
		int    tempMinute = (int)Math.floor(startTime%60);
		String startMinute = "";
		if (tempMinute < 10) {
			startMinute = "0" + Integer.toString(tempMinute);
		}
		else {
			startMinute = Integer.toString(tempMinute);
		}

		int    endHour = (int)Math.floor(endTime/60);
		String endAMPM = "";
		if (endHour > 12) {
			endHour = endHour - 12;
			endAMPM = "PM";
		}
		else if (endHour == 12) {
			endAMPM = "PM";
		}
		else {
			endAMPM = "AM";
		}
		tempMinute = (int)Math.floor(endTime%60);
		String endMinute = "";
		if (tempMinute < 10) {
			endMinute = "0" + Integer.toString(tempMinute);
		}
		else {
			endMinute = Integer.toString(tempMinute);
		}

		this.start = Integer.toString(startHour) + ":" + startMinute + startAMPM;
		this.end = Integer.toString(endHour) + ":" + endMinute + endAMPM;

		this.startTimeLabel.setText(this.start);
		this.endTimeLabel.setText(this.end);
	}

	@Override
	public String toString() {
		return this.cid;
	}

	public void showPopupDialog(double x, double y) {
		// Popup dialog when right-clicking on a classview
    	Stage popupStage = new Stage(StageStyle.UNDECORATED);
    	BorderPane popupRoot = new BorderPane();
	    Scene popupScene = new Scene(popupRoot,100,125);
	    popupScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	    popupStage.setScene(popupScene);
	    popupStage.setTitle("Popup Dialog");
	    popupStage.initModality(Modality.WINDOW_MODAL);
	    popupStage.initOwner(Main.mainStage);

	    popupStage.setX(x - 50);
	    popupStage.setY(y - 62);

	    /*popupStage.setOnCloseRequest((new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				popupStage.close();
				event.consume();
			}

	    }));*/

	    VBox  mainBox      = new VBox();
	    HBox  listRow      = new HBox();
	    HBox  editRow      = new HBox();
	    HBox  deleteRow    = new HBox();
	    HBox  lockRow      = new HBox();
	    HBox  undoRow 	   = new HBox();
	    HBox  closeRow      = new HBox();
	    Label listButton   = new Label("Class List");
	    Label editButton   = new Label("Edit");
	    Label deleteButton = new Label("Delete");
	    Label lockButton   = new Label("Lock/Unlock");
	    Label undoButton   = new Label("Undo");
	    Label closeButton   = new Label("Close");

	    listRow.getChildren().add(listButton);
	    listRow.alignmentProperty().set(Pos.CENTER);
	    editRow.getChildren().add(editButton);
	    editRow.alignmentProperty().set(Pos.CENTER);
	    deleteRow.getChildren().add(deleteButton);
	    deleteRow.alignmentProperty().set(Pos.CENTER);
	    lockRow.getChildren().add(lockButton);
	    lockRow.alignmentProperty().set(Pos.CENTER);
	    undoRow.getChildren().add(undoButton);
	    undoRow.alignmentProperty().set(Pos.CENTER);
	    closeRow.getChildren().add(closeButton);
	    closeRow.alignmentProperty().set(Pos.CENTER);

	    listRow.getStyleClass().add("ScheduleCell");
	    editRow.getStyleClass().add("ScheduleCell");
	    deleteRow.getStyleClass().add("ScheduleCell");
	    lockRow.getStyleClass().add("ScheduleCell");
	    undoRow.getStyleClass().add("ScheduleCell");
	    closeRow.getStyleClass().add("ScheduleCell");

	    listRow.setOnMouseEntered(new EventHandler<MouseEvent> () {
			@Override
			public void handle(MouseEvent event) {
				listRow.getStyleClass().remove("PopupButton");
				listRow.getStyleClass().add("PopupButtonHighlight");
				event.consume();
			}
	    });
	    listRow.setOnMouseExited(new EventHandler<MouseEvent> () {
			@Override
			public void handle(MouseEvent event) {
				listRow.getStyleClass().remove("PopupButtonHighlight");
				listRow.getStyleClass().add("PopupButton");
				event.consume();
			}
	    });
	    listRow.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Stage classListStage = new Stage();
			    BorderPane classListRoot = new BorderPane();
			    Scene classListScene = new Scene(classListRoot,200,400);
			    classListStage.setScene(classListScene);
			    classListStage.setTitle("Class List");
			    classListStage.initModality(Modality.WINDOW_MODAL);
			    classListStage.initOwner(Main.mainStage);

			    classListStage.setOnCloseRequest((new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent event) {
						classListStage.close();
						event.consume();
					}

			    }));

			    ListView<String> classList = new ListView<String>();

			    String classListStringList = "";
			    if (ToolBarView.filter != null) {
			    	int timeSlot = (CompactCourseView.this.getStartTime()) - (CompactCourseView.this.getStartTime() % 60);
			    	HashSet<String> classListHashList = ToolBarView.filter.daySearch(CompactCourseView.this.getDay(), timeSlot);

			    	for (String cid : classListHashList) {
			    		classListStringList += cid + ";";
			    	}
			    }

			    ObservableList<String> items = FXCollections.observableArrayList(classListStringList.split(";"));
			    classList.setItems(items);

			    classList.setOnMouseClicked(new EventHandler<MouseEvent> () {
					@Override
					public void handle(MouseEvent event) {
						String classId = classList.getSelectionModel().getSelectedItem();
						classId = classId.replaceAll("\\.", "");

						for (int i = 0; i < 6; i++) {
					    	Pane tempPane = (Pane)ToolBarView.tracks.getChildren().get(i+1 + 2);
					    	for (int n = 0; n < tempPane.getChildren().size(); n++) {
					    		CompactCourseView tempCourse = (CompactCourseView)tempPane.getChildren().get(n);

					    		if (tempCourse.getCid().equals(classId)) {
					    			tempPane.getChildren().remove(tempCourse);
					    			tempPane.getChildren().add(tempCourse);
					    		}
					    	}
					    }

						event.consume();
					}
			    });

			    classList.setStyle("-fx-font-family: 'monospace';");

			    classListRoot.setCenter(classList);

			    classListStage.show();

			    classPopupVisible = false;
				popupStage.close();
				event.consume();
			}
	    });

	    editRow.setOnMouseEntered(new EventHandler<MouseEvent> () {
			@Override
			public void handle(MouseEvent event) {
				editRow.getStyleClass().remove("PopupButton");
				editRow.getStyleClass().add("PopupButtonHighlight");
				event.consume();
			}
	    });
	    editRow.setOnMouseExited(new EventHandler<MouseEvent> () {
			@Override
			public void handle(MouseEvent event) {
				editRow.getStyleClass().remove("PopupButtonHighlight");
				editRow.getStyleClass().add("PopupButton");
				event.consume();
			}
	    });
	    editRow.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				new CreateEditCourseDialog(CompactCourseView.this);
				classPopupVisible = false;
				popupStage.close();
				event.consume();
			}
	    });

	    deleteRow.setOnMouseEntered(new EventHandler<MouseEvent> () {
			@Override
			public void handle(MouseEvent event) {
				deleteRow.getStyleClass().remove("PopupButton");
				deleteRow.getStyleClass().add("PopupButtonHighlight");
				event.consume();
			}
	    });
	    deleteRow.setOnMouseExited(new EventHandler<MouseEvent> () {
			@Override
			public void handle(MouseEvent event) {
				deleteRow.getStyleClass().remove("PopupButtonHighlight");
				deleteRow.getStyleClass().add("PopupButton");
				event.consume();
			}
	    });
	    deleteRow.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (CompactCourseView.this.isLocked) {
					Pane parent = (Pane) CompactCourseView.this.getParent();
					parent.getChildren().remove(CompactCourseView.this);
					for (CompactCourseView cv: sameCourses) {
						cv.sameCourses.remove(CompactCourseView.this);
					}

					for (CompactCourseView cv: sameCourses) {
						parent = (Pane) cv.getParent();
						parent.getChildren().remove(cv);

						for (CompactCourseView cv2: cv.sameCourses) {
							cv2.sameCourses.remove(cv);
						}
					}

					ClassParser.classList.get(CompactCourseView.this.getCid()).removeClass();

					classPopupVisible = false;
					popupStage.close();
				}
				else {
					Pane parent = (Pane) CompactCourseView.this.getParent();
					parent.getChildren().remove(CompactCourseView.this);
					for (CompactCourseView cv: sameCourses) {
						cv.sameCourses.remove(CompactCourseView.this);
					}
					classPopupVisible = false;
					popupStage.close();
				}
				event.consume();
			}
	    });

	    lockRow.setOnMouseEntered(new EventHandler<MouseEvent> () {
			@Override
			public void handle(MouseEvent event) {
				lockRow.getStyleClass().remove("PopupButton");
				lockRow.getStyleClass().add("PopupButtonHighlight");
				event.consume();
			}
	    });
	    lockRow.setOnMouseExited(new EventHandler<MouseEvent> () {
			@Override
			public void handle(MouseEvent event) {
				lockRow.getStyleClass().remove("PopupButtonHighlight");
				lockRow.getStyleClass().add("PopupButton");
				event.consume();
			}
	    });
	    lockRow.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				isLocked = !isLocked;
				System.out.println(isLocked);

				if (isLocked) {
					lockLabel.setText(" L ");
				}
				else {
					lockLabel.setText(" U ");
				}

				for (CompactCourseView cv: sameCourses) {
		    		cv.isLocked = isLocked;
		    		if (cv.isLocked) {
						cv.lockLabel.setText(" L ");
					}
					else {
						cv.lockLabel.setText(" U ");
					}
				}

				classPopupVisible = false;
				popupStage.close();
				event.consume();
			}
	    });

	    undoRow.setOnMouseEntered(new EventHandler<MouseEvent> () {
			@Override
			public void handle(MouseEvent event) {
				undoRow.getStyleClass().remove("PopupButton");
				undoRow.getStyleClass().add("PopupButtonHighlight");
				event.consume();
			}
	    });
	    undoRow.setOnMouseExited(new EventHandler<MouseEvent> () {
			@Override
			public void handle(MouseEvent event) {
				undoRow.getStyleClass().remove("PopupButtonHighlight");
				undoRow.getStyleClass().add("PopupButton");
				event.consume();
			}
	    });
	    undoRow.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ClassParser.classList.get(CompactCourseView.this.getCid()).restorePrevTime();

				/*double heightOfCell = (WeeklyScheduleCourseTracks.height) / (WeeklyScheduleView.endHour - WeeklyScheduleView.startHour);
	    		double pixelMinutes = (heightOfCell / 60);

				// TODO: Make this for-loop position the classes instead of relying on Main.toolBarView.PopulateTracks();
				for (int i = 0; i < 6; i++) {
			    	Pane tempPane = (Pane)ToolBarView.tracks.getChildren().get(i+1 + 2);
			    	for (int n = 0; n < tempPane.getChildren().size(); n++) {
			    		CompactCourseView tempCourse = (CompactCourseView)tempPane.getChildren().get(n);
			    	}
			    }*/

				Main.toolBarView.PopulateTracks();

				for (int i = 0; i < 6; i++) {
			    	Pane tempPane = (Pane)ToolBarView.tracks.getChildren().get(i+1 + 2);
			    	for (int n = 0; n < tempPane.getChildren().size(); n++) {
			    		CompactCourseView tempCourse = (CompactCourseView)tempPane.getChildren().get(n);
			    		tempPane.getChildren().remove(tempCourse);
			    		tempPane.getChildren().add(tempCourse);
			    	}
			    }

			    classPopupVisible = false;
				popupStage.close();
				event.consume();
			}
	    });

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
				classPopupVisible = false;
				popupStage.close();
				event.consume();
			}
	    });

	    mainBox.getChildren().add(listRow);
	    mainBox.getChildren().add(editRow);
	    mainBox.getChildren().add(deleteRow);
	    mainBox.getChildren().add(lockRow);
	    mainBox.getChildren().add(undoRow);
	    mainBox.getChildren().add(closeRow);

	    HBox.setHgrow(listButton, Priority.ALWAYS);
	    HBox.setHgrow(editButton, Priority.ALWAYS);
	    HBox.setHgrow(deleteButton, Priority.ALWAYS);
	    HBox.setHgrow(lockButton, Priority.ALWAYS);
	    HBox.setHgrow(undoButton, Priority.ALWAYS);
	    HBox.setHgrow(closeButton, Priority.ALWAYS);

	    VBox.setVgrow(listRow, Priority.ALWAYS);
	    VBox.setVgrow(editRow, Priority.ALWAYS);
	    VBox.setVgrow(deleteRow, Priority.ALWAYS);
	    VBox.setVgrow(lockRow, Priority.ALWAYS);
	    VBox.setVgrow(undoRow, Priority.ALWAYS);
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

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public int getTrack() {
		return track;
	}

	public void setTrack(int track) {
		this.track = track;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public short getSection() {
		return section;
	}

	public void setSection(short section) {
		this.section = section;
	}
}
