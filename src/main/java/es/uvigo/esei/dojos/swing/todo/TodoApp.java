package es.uvigo.esei.dojos.swing.todo;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import es.uvigo.esei.dojos.swing.todo.gui.MainWindow;

public class TodoApp {

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		configureLookAndFeel();
		
		MainWindow window = new MainWindow();
		window.setLocationRelativeTo( null );
		window.setVisible(true);
	}

	private static void configureLookAndFeel() {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("GTK+".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break; //preferred!
		        }
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		        }
		    }
		} catch (Exception e) {}
	}
}
