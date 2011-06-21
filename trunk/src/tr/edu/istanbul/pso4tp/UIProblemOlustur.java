package tr.edu.istanbul.pso4tp;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class UIProblemOlustur extends JFrame {
	
	private JTextField kaynak;
	private JTextField hedef;
	private JTextField min_miktar;
	private JTextField max_miktar;
	private JTextField max_maliyet;
	private JTextField min_maliyet;
	private JTable table;
	private DefaultTableModel model;
	private TPProblem current_problem;
	public UIProblemOlustur() {
		Container con = getContentPane();
		con.setLayout(new GridBagLayout());
		
		GridBagConstraints g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 0;
		g.fill = GridBagConstraints.BOTH;
		con.add(new JLabel("Kaynak Sayısı"),g);
		
		g = new GridBagConstraints();
		g.gridx = 1;
		g.gridy = 0;
		g.fill = GridBagConstraints.BOTH;
		con.add(new JLabel("Hedef Sayısı"),g);
		
		g = new GridBagConstraints();
		g.gridx = 2;
		g.gridy = 0;
		g.fill = GridBagConstraints.BOTH;
		con.add(new JLabel("Min. Miktar"),g);
		
		g = new GridBagConstraints();
		g.gridx = 3;
		g.gridy = 0;
		g.fill = GridBagConstraints.BOTH;
		con.add(new JLabel("Max. Miktar"),g);
		
		g = new GridBagConstraints();
		g.gridx = 4;
		g.gridy = 0;
		g.fill = GridBagConstraints.BOTH;
		con.add(new JLabel("Min. Maliyet"),g);
		
		g = new GridBagConstraints();
		g.gridx = 5;
		g.gridy = 0;
		g.fill = GridBagConstraints.BOTH;
		con.add(new JLabel("Max. Maliyet"),g);
		
		g = new GridBagConstraints();
		g.gridx = 6;
		g.gridy = 0;
		g.fill = GridBagConstraints.BOTH;
		g.gridheight = 2;
		JButton btn = new JButton("Oluştur");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				olustur();
			}
		});
		con.add(btn,g);
		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 1;
		g.fill = GridBagConstraints.BOTH;
		kaynak = new JTextField(){
			protected javax.swing.text.Document createDefaultModel() {
				return new IntTextDocument();
			};
		};
		con.add(kaynak,g);
		
		g = new GridBagConstraints();
		g.gridx = 1;
		g.gridy = 1;
		g.fill = GridBagConstraints.BOTH;
		hedef = new JTextField(){
			protected javax.swing.text.Document createDefaultModel() {
				return new IntTextDocument();
			};
		};
		con.add(hedef,g);
		
		g = new GridBagConstraints();
		g.gridx = 2;
		g.gridy = 1;
		g.fill = GridBagConstraints.BOTH;
		min_miktar = new JTextField(){
			protected javax.swing.text.Document createDefaultModel() {
				return new IntTextDocument();
			};
		};
		con.add(min_miktar,g);
		
		g = new GridBagConstraints();
		g.gridx = 3;
		g.gridy = 1;
		g.fill = GridBagConstraints.BOTH;
		max_miktar = new JTextField(){
			protected javax.swing.text.Document createDefaultModel() {
				return new IntTextDocument();
			};
		};
		con.add(max_miktar,g);
		
		g = new GridBagConstraints();
		g.gridx = 4;
		g.gridy = 1;
		g.fill = GridBagConstraints.BOTH;
		min_maliyet = new JTextField(){
			protected javax.swing.text.Document createDefaultModel() {
				return new IntTextDocument();
			};
		};
		con.add(min_maliyet,g);
		
		g = new GridBagConstraints();
		g.gridx = 5;
		g.gridy = 1;
		g.fill = GridBagConstraints.BOTH;
		max_maliyet= new JTextField(){
			protected javax.swing.text.Document createDefaultModel() {
				return new IntTextDocument();
			};
		};
		con.add(max_maliyet,g);
		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 2;
		g.weightx = 1;
		g.weighty = 1;
		g.fill = GridBagConstraints.BOTH;
		g.gridwidth = 7;
		model = new DefaultTableModel();
		table = new JTable(model);
		con.add(new JScrollPane(table),g);
		table.setOpaque(true);
		table.setBackground(Color.WHITE);
		table.setTableHeader(null);
		
		
		setSize(600,300);
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				UI.instance.setCurrentProblem(current_problem);
				UI.instance.free_ui_olustur();
				super.windowClosed(e);
			}
		});
	}
	private void olustur(){
		try {
			int num_of_kaynak = Integer.parseInt(kaynak.getText());
			int num_of_hedef = Integer.parseInt(hedef.getText());
			int min_maliyet = Integer.parseInt(this.min_maliyet.getText());
			int max_maliyet = Integer.parseInt(this.max_maliyet.getText());
			int max_miktar = Integer.parseInt(this.max_miktar.getText());
			int min_miktar = Integer.parseInt(this.min_miktar.getText());
			TPProblem p = new TPProblem(num_of_kaynak,num_of_hedef);
			p.generate(min_miktar, max_miktar, min_maliyet, max_maliyet);
			current_problem = p;
			tablo_guncelle(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 class IntTextDocument extends PlainDocument {
		    public void insertString(int offs, String str, AttributeSet a)
		        throws BadLocationException {
		      if (str == null)
		        return;
		      String oldString = getText(0, getLength());
		      String newString = oldString.substring(0, offs) + str
		          + oldString.substring(offs);
		      try {
		        Integer.parseInt(newString + "0");
		        super.insertString(offs, str, a);
		      } catch (NumberFormatException e) {
		      }
		    }
		  }
	 
	 private void tablo_guncelle(TPProblem current_problem ){
			if (current_problem != null){
				
				model.setColumnCount(current_problem.getNumOfDestinations()+1);
				model.setRowCount(current_problem.getNumOfSources()+1);
				
				for (int i = 0; i < current_problem.getNumOfSources(); i++) {
					model.setValueAt(""+current_problem.getAmountOfSource(i), i+1, 0);
					for (int j = 0; j < current_problem.getNumOfDestinations(); j++) {
						if (i == 0){
							model.setValueAt(""+current_problem.getAmountOfDestination(j), 0, j+1);
						}
						
						StringBuffer buf = new StringBuffer();
						buf.append(current_problem.getCellOfCost(i, j));
						model.setValueAt(buf, i+1, j+1);
					}
				}
			}
		}

}
