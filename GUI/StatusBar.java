import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class StatusBar extends JPanel {
	private static final long serialVersionUID = 1L;
	
	JPanel     filterPanel = new JPanel();
	JLabel     filterLabel = new JLabel("Filter");
	JTextField filterText  = new JTextField();
	
	public StatusBar() {
		Initialize();
	}
	
	public void Initialize() {
		this.setLayout(new BorderLayout());
		
		filterText.setColumns(16);
		
		filterPanel.add(filterLabel);
		filterPanel.add(filterText);
		this.add(filterPanel, BorderLayout.WEST);
	}
}
