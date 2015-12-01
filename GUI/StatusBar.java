import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;


public class StatusBar extends JPanel {
	private static final long serialVersionUID = 1L;
	
	JPanel     filterPanel          = new JPanel();
	JLabel     filterLabel          = new JLabel("Filter");
	JTextField filterText           = new JTextField();
    JCheckBox  filterByCharCheckbox = new JCheckBox("Apply Real Time");
	
	public StatusBar() {
		Initialize();
	}
	
	public void Initialize() {
		this.setLayout(new BorderLayout());
		
		filterText.setColumns(16);
		
		filterPanel.add(filterLabel);
		filterPanel.add(filterText);
        filterPanel.add(filterByCharCheckbox);
		this.add(filterPanel, BorderLayout.WEST);
	}
}
