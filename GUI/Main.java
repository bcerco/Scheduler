import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class Main extends JFrame {
	private static final long serialVersionUID = 1L;
	
	JScrollPane  scheduleScrollPane = new JScrollPane();
	
	MenuBar      menuBar            = new MenuBar();
	ScheduleView scheduleView       = new ScheduleView(7, 15);
	StatusBar    statusBar          = new StatusBar();

	public Main() {
		Initialize();
	}
	
	public void Initialize() {
		this.setTitle("Class Scheduler");
		this.setLayout(new BorderLayout());
		
		this.setBounds(0, 0, 1280, 720);
		this.setResizable(true);
		
		scheduleScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scheduleScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scheduleScrollPane.getViewport().add(scheduleView);
		
		this.add(menuBar, BorderLayout.NORTH);
		this.add(scheduleScrollPane, BorderLayout.CENTER);
		this.add(statusBar, BorderLayout.SOUTH);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setVisible(true);
	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Main program = new Main();
	}

}
