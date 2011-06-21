package tr.edu.istanbul.pso4tp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Random;
import javax.swing.JFileChooser;
import javax.swing.JFrame;


public class Main {
	
	public static void main(String[] args) {
		UI ui = new UI();
		ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ui.setVisible(true);
	}
}
