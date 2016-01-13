import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class Main extends JFrame implements MouseMotionListener, MouseListener {
	private static final long serialVersionUID = 1L;

    Weekends     statusWeekends;

	JScrollPane  scheduleScrollPane = new JScrollPane();
	
	MenuBar      menuBar            = new MenuBar();
	ScheduleView scheduleView       = new ScheduleView(8, 15);
	ToolBar      toolBar            = new ToolBar(this);

    int mouseClickX   = -1;
    int mouseClickY   = -1;
    int tlSelectionX  = -1;
    int tlSelectionY  = -1;
    int brSelectionX  = -1;
    int brSelectionY  = -1;

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
		this.setTitle("Class Scheduler");
		this.setLayout(new BorderLayout());

        statusWeekends = Weekends.EXCLUDE;

        scheduleView = new ScheduleView(statusWeekends.GetValue(), 15);
		
		this.setBounds(0, 0, 1280, 720);
		this.setResizable(true);
		
		scheduleScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scheduleScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scheduleScrollPane.getViewport().add(scheduleView);

        scheduleView.addMouseListener(this);
        scheduleView.addMouseMotionListener(this);
		
		this.add(menuBar, BorderLayout.NORTH);
		this.add(scheduleScrollPane, BorderLayout.CENTER);
		this.add(toolBar, BorderLayout.EAST);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setVisible(true);
	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Main program = new Main();
	}

    public void RebuildSchedule() {
        this.remove(scheduleScrollPane);
        scheduleScrollPane = new JScrollPane();
        scheduleView = new ScheduleView(statusWeekends.GetValue(), 15);

        scheduleScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scheduleScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scheduleScrollPane.getViewport().add(scheduleView);

        scheduleView.addMouseListener(this);
        scheduleView.addMouseMotionListener(this);

        this.add(scheduleScrollPane, BorderLayout.CENTER);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int mouseX      = e.getX();
        int mouseY      = e.getY();

        this.mouseClickX = mouseX;
        this.mouseClickY = mouseY;

        JLabel tempCell = scheduleView.scheduleGrid[1][1];

        int cellWidth  = tempCell.getWidth();
        int cellHeight = tempCell.getHeight();

        this.tlSelectionX  = (int)Math.floor(mouseClickX/cellWidth) * cellWidth;
        this.tlSelectionY  = (int)Math.floor(mouseClickY/cellHeight) * cellHeight;
        this.brSelectionX  = (int)tlSelectionX + cellWidth;
        this.brSelectionY  = (int)tlSelectionY + cellHeight;

        for (int x = 1; x < statusWeekends.GetValue(); x++) {
            for (int y = 1; y < 15; y++) {
                JLabel currCell = scheduleView.scheduleGrid[x][y];
                if ((mouseX > currCell.getX() && mouseX < currCell.getX() + cellWidth) &&
                    (mouseY > currCell.getY() && mouseY < currCell.getY() + cellHeight)) {
                    currCell.setOpaque(true);
                }
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int mouseX      = e.getX();
        int mouseY      = e.getY();

        JLabel tempCell = scheduleView.scheduleGrid[1][1];

        int cellWidth  = tempCell.getWidth();
        int cellHeight = tempCell.getHeight();

        int cellHalfWidth  = cellWidth / 2;
        int cellHalfHeight = cellHeight / 2;

        if (mouseX >= mouseClickX && mouseY >= mouseClickY) {
            this.tlSelectionX  = (int)Math.floor(mouseClickX/cellWidth) * cellWidth;
            this.tlSelectionY  = (int)Math.floor(mouseClickY/cellHeight) * cellHeight;
            this.brSelectionX  = ((int)Math.floor(mouseX/cellWidth) * cellWidth) + cellWidth;
            this.brSelectionY  = ((int)Math.floor(mouseY/cellHeight) * cellHeight) + cellHeight;
        }
        else if(mouseX >= mouseClickX && mouseY < mouseClickY) {
            this.tlSelectionX  = (int)Math.floor(mouseClickX/cellWidth) * cellWidth;
            this.brSelectionY  = (int)Math.floor(mouseClickY/cellHeight) * cellHeight;
            this.brSelectionX  = ((int)Math.floor(mouseX/cellWidth) * cellWidth) + cellWidth;
            this.tlSelectionY  = ((int)Math.floor(mouseY/cellHeight) * cellHeight);
        }
        else if (mouseX < mouseClickX && mouseY >= mouseClickY) {
            this.brSelectionX  = (int)Math.floor(mouseClickX/cellWidth) * cellWidth;
            this.tlSelectionY  = (int)Math.floor(mouseClickY/cellHeight) * cellHeight;
            this.tlSelectionX  = ((int)Math.floor(mouseX/cellWidth) * cellWidth);
            this.brSelectionY  = ((int)Math.floor(mouseY/cellHeight) * cellHeight) + cellHeight;
        }
        else if (mouseX < mouseClickX && mouseY < mouseClickY) {
            this.brSelectionX  = (int)Math.floor(mouseClickX/cellWidth) * cellWidth;
            this.brSelectionY  = (int)Math.floor(mouseClickY/cellHeight) * cellHeight;
            this.tlSelectionX  = ((int)Math.floor(mouseX/cellWidth) * cellWidth);
            this.tlSelectionY  = ((int)Math.floor(mouseY/cellHeight) * cellHeight);
        }

        for (int x = 1; x < statusWeekends.GetValue(); x++) {
            for (int y = 1; y < 15; y++) {
                JLabel currCell = scheduleView.scheduleGrid[x][y];
                currCell.setOpaque(false);
                currCell.setText("");
                int midPointX = currCell.getX() + cellHalfWidth;
                int midPointY = currCell.getY() + cellHalfHeight;
                if (mouseX >= mouseClickX && mouseY >= mouseClickY) {
                    if ((midPointX > this.tlSelectionX && midPointX <= this.brSelectionX) &&
                        (midPointY > this.tlSelectionY && midPointY <= this.brSelectionY)) {
                        currCell.setOpaque(true);
                        currCell.setText(" ");
                    }
                }
                else if (mouseX >= mouseClickX && mouseY < mouseClickY) {
                    if ((midPointX > this.tlSelectionX && midPointX <= this.brSelectionX) &&
                        (midPointY > this.tlSelectionY && midPointY <= this.brSelectionY + cellHeight)) {
                        currCell.setOpaque(true);
                        currCell.setText(" ");
                    }
                }
                else if (mouseX < mouseClickX && mouseY >= mouseClickY) {
                    if ((midPointX > this.tlSelectionX && midPointX <= this.brSelectionX + cellWidth) &&
                        (midPointY > this.tlSelectionY && midPointY <= this.brSelectionY)) {
                        currCell.setOpaque(true);
                        currCell.setText(" ");
                    }
                }
                else if (mouseX < mouseClickX && mouseY < mouseClickY) {
                    if ((midPointX > this.tlSelectionX && midPointX <= this.brSelectionX + cellWidth) &&
                        (midPointY > this.tlSelectionY && midPointY <= this.brSelectionY + cellHeight)) {
                        currCell.setOpaque(true);
                        currCell.setText(" ");
                    }
                }
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int mouseX      = e.getX();
        int mouseY      = e.getY();

        JLabel tempCell = scheduleView.scheduleGrid[1][1];

        int cellWidth  = tempCell.getWidth();
        int cellHeight = tempCell.getHeight();

        for (int x = 1; x < statusWeekends.GetValue(); x++) {
            for (int y = 1; y < 15; y++) {
                JLabel currCell = scheduleView.scheduleGrid[x][y];
                currCell.setOpaque(false);
                currCell.setText("");
            }
        }

        this.mouseClickX  = -1;
        this.mouseClickY  = -1;
        this.tlSelectionX = -1;
        this.tlSelectionY = -1;
        this.brSelectionX = -1;
        this.brSelectionY = -1;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
