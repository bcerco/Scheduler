import java.awt.BorderLayout;

import java.awt.event.*;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;


public class StatusBar extends JPanel {
	private static final long serialVersionUID = 1L;

    Main mainFrame;

	JPanel     filterPanel          = new JPanel();
	JLabel     filterLabel          = new JLabel("Filter");
	JTextField filterText           = new JTextField();
    JCheckBox  filterByCharCheckbox = new JCheckBox("Apply Real Time");
    JCheckBox  includeWeekends      = new JCheckBox("Include Weekends");
	
	public StatusBar(Main m) {
        mainFrame = m;
		Initialize();
	}
	
	public void Initialize() {
		this.setLayout(new BorderLayout());
		
        includeWeekends.addActionListener(new WeekendsListener());

		filterText.setColumns(16);
		
		filterPanel.add(filterLabel);
		filterPanel.add(filterText);
        filterPanel.add(filterByCharCheckbox);
		this.add(filterPanel, BorderLayout.WEST);
        this.add(includeWeekends, BorderLayout.EAST);
	}

    public class WeekendsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AbstractButton checkBox = (AbstractButton) e.getSource();
            if (checkBox.getModel().isSelected()) {
                mainFrame.statusWeekends = Main.Weekends.INCLUDE;
                mainFrame.RebuildSchedule();
                mainFrame.revalidate();
                mainFrame.repaint();
            }
            else {
                mainFrame.statusWeekends = Main.Weekends.EXCLUDE;
                mainFrame.RebuildSchedule();
                mainFrame.revalidate();
                mainFrame.repaint();
            }
        }
    }
}
