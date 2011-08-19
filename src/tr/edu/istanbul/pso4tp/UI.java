package tr.edu.istanbul.pso4tp;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class UI extends JFrame {
	

	private JTable problem_view;
	private JButton yukle; 
	private JButton olustur; 
	private JButton kaydet; 
	private JButton lingo_kaydet; 
	private JComboBox parcaciksayisi;
	private JComboBox zaman;
	private JComboBox deneme;
	private JComboBox iterasyon;
	private JProgressBar progress;
	private JButton calistir;
	private JCheckBox amount_0_gizle;
	private JCheckBox amountlari_gizle;
	private JCheckBox costlari_gizle;
	private JComboBox iyi_cozumler;
	private JCheckBox vogel_yaklasim;
	private JCheckBox classic_mutation;
	private JCheckBox extreme_classic_mutation;
	private JButton batch;
	private boolean batch_calistir;
	private Object batch_lock = new Object();
	private JLabel sonuc;
	private JFileChooser fc;
	private TPProblem current_problem;
	public static UI instance;
	private DefaultTableModel model;
	private UIProblemOlustur ui_olustur;
	private boolean isRunning;
	private int vogel_sonucu;
	private Suru suru;
	public UI() {
		instance = this;
		Container c = getContentPane();
		c.setLayout(new GridBagLayout());
		JPanel control_panel = new JPanel();
		control_panel.setLayout(new GridBagLayout());
		control_panel.setOpaque(true);
		control_panel.setBackground(Color.WHITE);
		model = new DefaultTableModel();
		problem_view = new JTable(model){
			public boolean isCellEditable(int row, int column){  
			    return false;  
			} 
		};
		problem_view .setOpaque(true);
		problem_view .setBackground(Color.WHITE);
		fc = new JFileChooser();
		fc.setCurrentDirectory(new File("."));
		progress = new JProgressBar(JProgressBar.HORIZONTAL);
		iyi_cozumler = new JComboBox();
		iyi_cozumler.addItem("Çözümler");
		sonuc = new JLabel("sonuçlar: ");
		iyi_cozumler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cozumu_guncelle();
			}
		});
		GridBagConstraints g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 0;
		g.gridheight = 3;
		g.weighty = 1;
		g.fill = GridBagConstraints.BOTH;
		c.add(control_panel, g);
		
		g = new GridBagConstraints();
		g.gridx = 1;
		g.gridy = 0;
		g.weightx = 1;
		g.fill = GridBagConstraints.BOTH;
		c.add(progress, g);
		
		g = new GridBagConstraints();
		g.gridx = 2;
		g.gridy = 0;
		g.fill = GridBagConstraints.BOTH;
		c.add(iyi_cozumler, g);
		
		g = new GridBagConstraints();
		g.gridx = 1;
		g.gridy = 1;
		g.weightx = 1;
		g.gridwidth = 2;
		g.fill = GridBagConstraints.BOTH;
		c.add(sonuc, g);
		
		g = new GridBagConstraints();
		g.gridx = 1;
		g.gridy = 2;
		g.weighty = 1;
		g.weightx = 1;
		g.gridwidth = 2;
		g.fill = GridBagConstraints.BOTH;
		g.weightx = 1;
		c.add(new JScrollPane(problem_view),g);
		problem_view.setTableHeader(null);
		
		problem_view.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		yukle = new JButton("Dosyadan Yükle");
		yukle.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dosyadan_yukle();
			}
		});
		kaydet = new JButton("Dosyaya Kaydet");
		kaydet.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dosyaya_kaydet();
			}
		});
		lingo_kaydet = new JButton("Lingo Formülü Kaydet");
		lingo_kaydet.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				lingo_dosyaya_kaydet();
			}
		});
		calistir = new JButton("Çalıştır");
		calistir.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				calistir();
			}
		});
		olustur = new JButton("Problem Oluştur");
		olustur .addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				olustur();
			}
		});
		parcaciksayisi = new JComboBox( new Object[]{
				new ComboItem(1,"1 Parçacık"),
				new ComboItem(5,"5 Parçacık"),
				new ComboItem(10,"10 Parçacık"),
				new ComboItem(15,"15 Parçacık"),
				new ComboItem(20,"20 Parçacık"),
			}
		);
		parcaciksayisi.setSelectedIndex(2);
		
		deneme = new JComboBox( new Object[]{
				new ComboItem(1,"1 Deneme"),
				new ComboItem(5,"5 Deneme"),
				new ComboItem(10,"10 Deneme"),
			}
		);
		
		iterasyon = new JComboBox( new Object[]{
				new ComboItem(100,"100 İterasyon"),
				new ComboItem(200,"200 İterasyon"),
				new ComboItem(500,"500 İterasyon"),
				new ComboItem(1000,"1000 İterasyon"),
				new ComboItem(5000,"5000 İterasyon"),
				new ComboItem(10000,"10000 İterasyon"),
				new ComboItem(-1,"Sınır Yok"),
			}
		);
		iterasyon.setSelectedIndex(4);
		
		zaman = new JComboBox( new Object[]{
				new ComboItem(30*1000,"0.5 Dakika"),
				new ComboItem(60*1000,"1 Dakika"),
				new ComboItem(5*60*1000,"5 Dakika"),
				new ComboItem(10*60*1000,"10 Dakika"),
				new ComboItem(-1,"Sınır Yok"),
			}
		);
		zaman.setSelectedIndex(zaman.getItemCount()-1);
		
		int row = 0;
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = row++;
		g.gridwidth = 2;
		row++;
		g.fill = GridBagConstraints.BOTH;
		control_panel.add(calistir,g);
		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = row++;
		g.fill = GridBagConstraints.BOTH;
		control_panel.add(yukle,g);
		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = row++;
		g.fill = GridBagConstraints.BOTH;
		control_panel.add(kaydet,g);
		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = row++;
		g.fill = GridBagConstraints.BOTH;
		control_panel.add(lingo_kaydet,g);
		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = row++;
		g.fill = GridBagConstraints.BOTH;
		control_panel.add(olustur,g);
		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = row++;
		g.fill = GridBagConstraints.BOTH;
		control_panel.add(new JLabel("Deneme Sayısı"),g);

		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = row++;
		g.fill = GridBagConstraints.BOTH;
		control_panel.add(deneme,g);
		

		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = row++;
		g.fill = GridBagConstraints.BOTH;
		control_panel.add(new JLabel("İterasyon Kısıtı"),g);

		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = row++;
		g.fill = GridBagConstraints.BOTH;
		control_panel.add(iterasyon,g);

		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = row++;
		g.fill = GridBagConstraints.BOTH;
		control_panel.add(new JLabel("Zaman Kısıtı"),g);

		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = row++;
		g.fill = GridBagConstraints.BOTH;
		control_panel.add(zaman,g);
		
		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = row++;
		g.fill = GridBagConstraints.BOTH;
		control_panel.add(new JLabel("Parçacık Sayısı(PSO)"),g);

		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = row++;
		g.fill = GridBagConstraints.BOTH;
		control_panel.add(parcaciksayisi,g);
		
		amount_0_gizle = new JCheckBox("Miktarı 0 olan hücreleri gizle");
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = row++;
		g.fill = GridBagConstraints.BOTH;
		control_panel.add(amount_0_gizle,g);
		amount_0_gizle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tablo_guncelle();
			}
		});
		
		amountlari_gizle = new JCheckBox("Miktar bilgilerini gizle");
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = row++;
		g.fill = GridBagConstraints.BOTH;
		control_panel.add(amountlari_gizle,g);
		amountlari_gizle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tablo_guncelle();
			}
		});
		
		costlari_gizle = new JCheckBox("Maliyet bilgilerini gizle");
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = row++;
		g.fill = GridBagConstraints.BOTH;
		control_panel.add(costlari_gizle,g);
		costlari_gizle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tablo_guncelle();
			}
		});
		
		vogel_yaklasim = new JCheckBox("Vogel İle Başla");
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = row++;
		g.fill = GridBagConstraints.BOTH;
		control_panel.add(vogel_yaklasim,g);
		
		batch = new JButton("Toplu Halde Çöz");
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = row++;
		g.fill = GridBagConstraints.BOTH;
		control_panel.add(batch,g);
		batch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				batch_coz();
			}
		});
		
		classic_mutation = new JCheckBox("Klasik Mutasyon");
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = row++;
		g.fill = GridBagConstraints.BOTH;
//		control_panel.add(classic_mutation,g);
		
		extreme_classic_mutation = new JCheckBox("Geliştirilmiş Mutasyon");
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = row++;
		g.fill = GridBagConstraints.BOTH;
//		control_panel.add(extreme_classic_mutation,g);
		extreme_classic_mutation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (extreme_classic_mutation.isSelected()){
					classic_mutation.setSelected(false);
				}
			}
		});
		
		
		classic_mutation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (classic_mutation.isSelected()){
					extreme_classic_mutation.setSelected(false);
				}
			}
		});
		
		
		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = row++;
		g.weighty = 1;
		control_panel.add(new JLabel(),g);
		
		
		fc.setFileFilter(new FileFilter(){
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().endsWith(".txt");
			}
			public String getDescription() {
				return "TP Problemi";
			}
		});
		setSize(700, 600);
		sonuc.setOpaque(true);
		sonuc.setBackground(Color.WHITE);
		sonuc_guncelle();
	}
	public void free_ui_olustur(){
		ui_olustur = null;
	}
	private void olustur(){
		if (ui_olustur == null){
			ui_olustur = new UIProblemOlustur();
		}
	}
	private void cozumu_guncelle(){
		System.out.println("cozumu güncellen");
		Object item = iyi_cozumler.getSelectedItem();
		if (current_problem != null && item instanceof ComboItem){
			ComboItem c = (ComboItem)item;
			current_problem.orderWith(c.solution);
			sonuc_guncelle();
			tablo_guncelle();
		}
	}
	private void batch_coz(){
		if (batch_calistir){
			batch_calistir = false;
			calistir();
			return;
		}
		batch_calistir = true;
		batch.setText("Durdur");
		FileFilter f = fc.getFileFilter();
		fc.setFileFilter(new FileFilter() {
			public String getDescription() {
				return "";
			}
			public boolean accept(File f) {
				return f.isDirectory();
			}
		});
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.showOpenDialog(this);
		final File selected = fc.getSelectedFile();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setFileFilter(f);
		if (selected != null){
			final File files[] = selected.listFiles();
			Thread t = new Thread(){
				public void run() {
					
					File out = new File(selected.getAbsolutePath()+File.separator+"out");
					delete(out);
					out.mkdir();
					File message_file = new File(out+File.separator+"total.txt");
					
					FileOutputStream writer = null;
					try {
						message_file.createNewFile();
						writer = new FileOutputStream(message_file);
					} catch (Exception e) {
					}
					
					write(writer, "# parçacık sayısı = "+((ComboItem)parcaciksayisi.getSelectedItem()).i+"\n");
					write(writer, "# iterasyon = "+((ComboItem)iterasyon.getSelectedItem()).i+"\n");
					write(writer, "# deneme = "+((ComboItem)deneme.getSelectedItem()).i+"\n");
					write(writer, "# vogel = "+vogel_yaklasim.isSelected()+"\n");
					
					System.out.println("fff22--:"+files.length);
					for (int i = 0; i < files.length && batch_calistir ; i++) {
						if (!files[i].getName().endsWith(".txt")){
							continue;
						}
						System.out.println("fff--:"+files[i]);
						
						current_problem = new TPProblem();
						FileInputStream fi = null;
						System.out.println("çözülüen("+(i+1)+"/"+(files.length)+") > "+files[i]);
						batch.setText("Durdur ("+(i+1)+"/"+(files.length)+")");
						try {
							fi = new FileInputStream(files[i]);
							
							current_problem .load(fi);
							long ti = System.currentTimeMillis();
							calistir();
							synchronized (batch_lock) {
								batch_lock.wait();
							}
							ti = System.currentTimeMillis() - ti;
							write(writer, files[i].getName());
							write(writer, "\t"+ti);
							write(writer, "\t");
							
							File f_lingo = new File(out+File.separator+"c_"+files[i].getName().substring(0,files[i].getName().indexOf('.'))+"_lingo.LNG");
							FileOutputStream fo_l = null;
							try {
								f_lingo.delete();
								f_lingo.createNewFile();
								fo_l = new FileOutputStream(f_lingo);
								fo_l.write(current_problem.getLingoFormat().getBytes());
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								fo_l.close();
							}
							
							for (int j = 0; j < iyi_cozumler.getItemCount(); j++){
								if (!(iyi_cozumler.getItemAt(j) instanceof ComboItem)){
									continue;
								}
								System.out.println("iyi_cozumler:"+j);
								ComboItem c = (ComboItem)iyi_cozumler.getItemAt(j);
								FileOutputStream fo = null;
								try {
									File f = new File(out+File.separator+"c_"+files[i].getName().substring(0,files[i].getName().indexOf('.'))+"_"+j+".txt");
									
									System.out.println("f:"+f);
									
									f.createNewFile();
									fo = new FileOutputStream(f);
									for (int k = 0; k < c.solution.length; k++) {
										write(fo, c.solution[k]+" ");
									}
								} catch (Exception e) {
									e.printStackTrace();
								} finally {
									try {
										fo.close();
									} catch (Exception e) {
									}
								}
								write(writer, c.i+"\t");
							}
							iyi_cozumler.removeAll();
							write(writer, "\n");
						} catch (Exception e) {
							System.out.println("ex:"+files[i]);
							e.printStackTrace();
						} finally {
							try {
								fi.close();
							} catch (Exception e) {
							}
						}
						System.out.println("bitti");
					}
					try {
						writer.close();
					} catch (Exception e) {
					}
					batch.setText("Toplu Halde Çöz");
					System.out.println("tamamen bitti");
				}
			};
			t.start();
		}
	}
	private void delete(File f){
		if (f.isDirectory()){
			File fi[] = f.listFiles();
			for (int i = 0; i < fi.length; i++) {
				delete(fi[i]);
			}
		}
		f.delete();
	}
	private static void write(OutputStream output,String txt){
		if (output != null){
			try {
				output.write(txt.getBytes());
			} catch (Exception e) {
			}
		}
		
	}
	
	private void calistir(){
		if (suru == null){
			if (current_problem == null){
				return;
			}
			calistir.setText("Durdur");
			kaydet.setEnabled(false);
			yukle.setEnabled(false);
			olustur.setEnabled(false);
			deneme.setEnabled(false);
			iterasyon.setEnabled(false);
			zaman.setEnabled(false);
			parcaciksayisi.setEnabled(false);
			isRunning = true;
			
			if (vogel_yaklasim.isSelected()){
				int vogel[] = current_problem.generateVogelsSolution();
				current_problem.orderWith(vogel);
				vogel_sonucu = current_problem.getTotalCost(); 
			} else {
				vogel_sonucu = 0;
			}
			final boolean mutation = classic_mutation.isSelected();
			final boolean ex_mutation = extreme_classic_mutation.isSelected();
			
			final int deneme_sayisi = ((ComboItem)this.deneme.getSelectedItem()).i;
			final int iterasyon_sayisi = ((ComboItem)this.iterasyon.getSelectedItem()).i;
			final int zaman_kisiti = ((ComboItem)this.zaman.getSelectedItem()).i;
			final int parcacik_sayisi = ((ComboItem)this.parcaciksayisi.getSelectedItem()).i;
			Thread t = new Thread(){
				public void run() {
					int progress_amount = 100/deneme_sayisi;
					int progress_current = 0;
					iyi_cozumler.removeAllItems();
					iyi_cozumler.addItem("Çözümler");
					for (int i = 0; i < deneme_sayisi && isRunning ; i++) {
						if (current_problem != null){
							suru = new Suru();
							suru.setMutasyon(mutation, ex_mutation);
							suru.init(current_problem, parcacik_sayisi, Constants.XMIN, Constants.XMAX, Constants.VMIN, Constants.VMAX, Constants.MIN_INTERTIA, Constants.INTERTIA_FACTOR, Constants.START_INTERTIA);
							
							suru.setProgressBounds(progress_current, progress_amount);
							progress_current += progress_amount;
							suru.solve(iterasyon_sayisi, zaman_kisiti);
							if (i == 0){
								iyi_cozumler.removeAllItems();
							}
							current_problem.orderWith(suru.getBest());
							iyi_cozumler.addItem(new ComboItem(current_problem.getSolution(),current_problem.getTotalCost(), (i+1)+". Çözüm ("+current_problem.getTotalCost()+")"));
						}
					}
					if (vogel_yaklasim.isSelected()){
						current_problem.orderWith(current_problem.generateVogelsSolution());
						iyi_cozumler.addItem(new ComboItem(current_problem.getSolution(),current_problem.getTotalCost(), "Vogel Çözümü ("+current_problem.getTotalCost()+")"));
					}
					int min_cost = ((ComboItem)iyi_cozumler.getItemAt(0)).i;
					for (int j = 1; j < iyi_cozumler.getItemCount(); j++) {
						ComboItem c = (ComboItem)iyi_cozumler.getItemAt(j);
						if (min_cost > c.i){
							min_cost = c.i;
							iyi_cozumler.setSelectedIndex(j);
						}
					}
					
					
					progress.setValue(100);
					calistir.setText("Çalıştır");
					kaydet.setEnabled(true);
					yukle.setEnabled(true);
					olustur.setEnabled(true);
					deneme.setEnabled(true);
					iterasyon.setEnabled(true);
					zaman.setEnabled(true);
					parcaciksayisi.setEnabled(true);
					
					suru = null;
					synchronized (batch_lock) {
						batch_lock.notifyAll();
					}
				}
			};
			t.start();
		} else {
			isRunning = false;
			if (suru != null){
				suru.stop();
			}
			calistir.setText("Çalıştır");
			kaydet.setEnabled(true);
			yukle.setEnabled(true);
			olustur.setEnabled(true);
			deneme.setEnabled(true);
			iterasyon.setEnabled(true);
			zaman.setEnabled(true);
			parcaciksayisi.setEnabled(true);
		}
	}
	public void setProgress(int p){
		progress.setValue(p);
	}
	public void update(int iterasyon,long zaman,int best){
		sonuc_guncelle();
		tablo_guncelle();
	}
	private void dosyadan_yukle(){
		fc.showOpenDialog(this);
		File f = fc.getSelectedFile();
		if (f != null){
			TPProblem p = new TPProblem();
			FileInputStream fi = null;
			try {
				fi = new FileInputStream(f);
				p.load(fi);
				current_problem = p;
				sonuc_guncelle();
				tablo_guncelle();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Dosya geçerli değil","Hata",JOptionPane.ERROR_MESSAGE);
			} finally {
				try {
					fi.close();
				} catch (Exception e) {
				} 
			}
		}
	}
	public void setCurrentProblem(TPProblem p){
		current_problem = p;
		sonuc_guncelle();
		tablo_guncelle();
	}
	private void lingo_dosyaya_kaydet(){
		if (current_problem == null){
			return;
		}
		fc.showSaveDialog(this);
		File f = fc.getSelectedFile();
		if (f != null){
			FileOutputStream fo = null;
			try {
				if (!f.getName().endsWith(".lng")){
					f = new File(f.getAbsoluteFile()+".lng");
				}
				if (!f.exists()){
					f.createNewFile();
				}
				fo = new FileOutputStream(f);
				String str = current_problem.getLingoFormat();
				fo.write(str.getBytes());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					fo.close();
				} catch (Exception e) {
				}
			}
		}
		
	}
	private void dosyaya_kaydet(){
		if (current_problem == null){
			return;
		}
		fc.showSaveDialog(this);
		File f = fc.getSelectedFile();
		if (f != null){
			FileOutputStream fo = null;
			try {
				if (!f.getName().endsWith(".txt")){
					f = new File(f.getAbsoluteFile()+".txt");
				}
				if (!f.exists()){
					f.createNewFile();
				}
				fo = new FileOutputStream(f);
				current_problem.save(fo);
			} catch (Exception e) {
			} finally {
				try {
					fo.close();
				} catch (Exception e) {
				}
			}
		}
	}
	
	private void vogel_uygula(){
		if (current_problem != null){
			if (vogel_yaklasim.isSelected()){
				int solution[] = current_problem.generateVogelsSolution();
				current_problem.orderWith(solution);
				sonuc_guncelle();
				tablo_guncelle();
			}
		}
	}
	public boolean isStartWithVogel(){
		return vogel_yaklasim.isSelected();
	}
	private void tablo_guncelle(){
		if (current_problem != null){
			
			boolean amount_0_gizle = this.amount_0_gizle.isSelected();
			boolean amountlari_gizle = !this.amountlari_gizle.isSelected();
			boolean costlari_gizle = !this.costlari_gizle.isSelected();
			
			boolean dikey;
//			if (current_problem.getNumOfDestinations() > current_problem.getNumOfSources()){
//			} else {
//				dikey = true;
//			}
			dikey = true;
			
			if (dikey){
				model.setColumnCount(current_problem.getNumOfDestinations()+1);
				model.setRowCount(current_problem.getNumOfSources()+1);
			} else {
				model.setRowCount(current_problem.getNumOfDestinations()+1);
				model.setColumnCount(current_problem.getNumOfSources()+1);
			}
			
			for (int i = 0; i < current_problem.getNumOfSources(); i++) {
				if (dikey){
					model.setValueAt(""+current_problem.getAmountOfSource(i), i+1, 0);
				} else {
					model.setValueAt(""+current_problem.getAmountOfSource(i),  0,i+1);
				}
				for (int j = 0; j < current_problem.getNumOfDestinations(); j++) {
					if (i == 0){
						if (dikey){
							model.setValueAt(""+current_problem.getAmountOfDestination(j), 0, j+1);
						} else {
							model.setValueAt(""+current_problem.getAmountOfDestination(j),  j+1,0);
						}
					}
					if (amount_0_gizle && current_problem.getCellOfAmount(i, j) == 0){
						if (dikey){
							model.setValueAt("", i+1, j+1);
						} else {
							model.setValueAt("",  j+1,i+1);
						}
						continue;
					}
					StringBuffer buf = new StringBuffer();
					if (amountlari_gizle){
						buf.append(current_problem.getCellOfAmount(i, j));
					}
					if (amountlari_gizle && costlari_gizle){
						buf.append('(');
					}
					if (costlari_gizle){
						buf.append(current_problem.getCellOfCost(i, j));
					}
					if (amountlari_gizle && costlari_gizle){
						buf.append(')');
					}
					if (dikey){
						model.setValueAt(buf, i+1, j+1);
					} else {
						model.setValueAt(buf,  j+1,i+1);
					}
				}
			}
		}
	}
	private void sonuc_guncelle(){
		String str = "<html><body>Sonuç:<br>";
		if (current_problem != null){
			str += "Kaynak Sayısı: "+current_problem.getNumOfSources()+"<br>";
			str += "Hedef Sayısı: "+current_problem.getNumOfDestinations()+"<br>";
			if (suru == null){
				str += "Toplam Maliyet: 0<br>";
			} else {
				str += "Toplam Maliyet: "+suru.getBestCost()+"<br>";
			}
			if (vogel_sonucu != 0){
				str += "Vogel Yaklaştırma Maliyeti: "+vogel_sonucu+"<br>";
			}
		} else {
			str += "Kaynak Sayısı:<br>";
			str += "Hedef Sayısı:<br>";
			str += "Toplam Maliyet:<br>";
		}
		str+= "</body></html>";
		sonuc.setText(str);
	}
}


