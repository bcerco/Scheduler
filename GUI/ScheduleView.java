import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.SoftBevelBorder;


public class ScheduleView extends JPanel {
	private static final long serialVersionUID = 1L;
	
	int gridWidth, gridHeight;
	
	JLabel[][] scheduleGrid;

	public ScheduleView(int gw, int gh) {
		gridWidth  = gw;
		gridHeight = gh;
		
		scheduleGrid = new JLabel[gridWidth][gridHeight];
		Initialize();
	}
	
	public void Initialize() {
		this.setLayout(new GridLayout(gridHeight, gridWidth));
		
		for (int y = 0; y < gridHeight; y++) {
			for (int x = 0; x < gridWidth; x++) {
				if (x == 0 && y == 0) {
					scheduleGrid[x][y] = new JLabel();
					
					scheduleGrid[x][y].setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.RAISED));
				}
				else if (x == 0) {
					if (((y-1)+8) <= 12) {
						if (((y-1)+8) != 12) {
							scheduleGrid[x][y] = new JLabel("" + ((y-1)+8) + ":00a");
						}
						else {
							scheduleGrid[x][y] = new JLabel("" + ((y-1)+8) + ":00p");
						}
					}
					else {
						scheduleGrid[x][y] = new JLabel("" + (((y-1)+8)-12) + ":00p");
					}
					
					scheduleGrid[x][y].setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.RAISED));
				}
				else if (y == 0) {
					switch (x) {
					case 1:
						scheduleGrid[x][y] = new JLabel("Monday");
						break;
					case 2:
						scheduleGrid[x][y] = new JLabel("Tuesday");
						break;
					case 3:
						scheduleGrid[x][y] = new JLabel("Wednesday");
						break;
					case 4:
						scheduleGrid[x][y] = new JLabel("Thursday");
						break;
					case 5:
						scheduleGrid[x][y] = new JLabel("Friday");
						break;
					case 6:
						scheduleGrid[x][y] = new JLabel("Saturday");
						break;
                    case 7:
                        scheduleGrid[x][y] = new JLabel("Sunday");
                        break;
					}
					
					scheduleGrid[x][y].setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.RAISED));
				}
				else {
					scheduleGrid[x][y] = new JLabel();
					
					scheduleGrid[x][y].setBorder(BorderFactory.createLineBorder(Color.WHITE));
				}
				
				scheduleGrid[x][y].setHorizontalAlignment(JLabel.CENTER);
				this.add(scheduleGrid[x][y]);
			}
		}
	}
}
