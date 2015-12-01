import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class MenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	
	JMenu     menuFile       = new JMenu("File");
	JMenuItem itemFileNew    = new JMenuItem("New");
	JMenuItem itemFileOpen   = new JMenuItem("Open...");
	JMenuItem itemFileSave   = new JMenuItem("Save");
	JMenuItem itemFileSaveAs = new JMenuItem("Save As...");
	JMenuItem itemFileExit   = new JMenuItem("Exit");
	
	public MenuBar() {
		Initialize();
	}
	
	public void Initialize() {
		// File Menu Construction
		menuFile.add(itemFileNew);
		menuFile.addSeparator();
		menuFile.add(itemFileOpen);
		menuFile.addSeparator();
		menuFile.add(itemFileSave);
		menuFile.add(itemFileSaveAs);
		menuFile.addSeparator();
		menuFile.add(itemFileExit);
		this.add(menuFile);
	}
}
