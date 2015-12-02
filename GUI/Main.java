import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class Main extends JFrame {
	private static final long serialVersionUID = 1L;
	
    Weekends     includeWeekends;

	JScrollPane  scheduleScrollPane = new JScrollPane();
	
	MenuBar      menuBar            = new MenuBar();
	ScheduleView scheduleView       = new ScheduleView(8, 15);
	StatusBar    statusBar          = new StatusBar();

    static enum Weekends {
        EXCLUDE(6),
        INCLUDE(8);

        private final int value;

        Weekends(int value) {
            this.value = value;
        }

        public int GetValue() {
            return this.value;
        }
    }

	public Main() {
		Initialize();
	}
	
	public void Initialize() {
        this.includeWeekends = Weekends.INCLUDE;
		this.setTitle("Class Scheduler");
		this.setLayout(new BorderLayout());

        scheduleView = new ScheduleView(includeWeekends.GetValue(), 15);
		
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

    public void IncludeWeekends(boolean b) {
        if (b) {
            includeWeekends = Weekends.INCLUDE;
        }
        else {
            includeWeekends = Weekends.EXCLUDE;
        }

        InvalidateSchedule();
    }

    public void InvalidateSchedule() {
        scheduleView = new ScheduleView(includeWeekends.GetValue(), 15);
    }

}
