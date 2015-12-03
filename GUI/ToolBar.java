import java.awt.BorderLayout;

import java.awt.event.*;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JCheckBox;


public class ToolBar extends JPanel {
	private static final long serialVersionUID = 1L;

    Main mainFrame;

	JPanel            filterPanel          = new JPanel();
	JLabel            filterLabel          = new JLabel("Filter");
	JTextField        filterText           = new JTextField();
    JComboBox<String> filterSaved          = new JComboBox<String>();
    JCheckBox         filterRealtime       = new JCheckBox("Apply Real Time");
    JCheckBox         includeWeekends      = new JCheckBox("Include Weekends");
	
	public ToolBar(Main m) {
        mainFrame = m;
		Initialize();
	}
	
	public void Initialize() {
		this.setLayout(new BorderLayout());
	
        includeWeekends.addActionListener(new WeekendsListener());

		filterText.setColumns(16);

        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
		
		filterPanel.add(filterLabel);
		filterPanel.add(filterText);
        filterPanel.add(filterSaved);
        filterPanel.add(filterRealtime);
		this.add(filterPanel, BorderLayout.NORTH);
        this.add(includeWeekends, BorderLayout.SOUTH);
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
