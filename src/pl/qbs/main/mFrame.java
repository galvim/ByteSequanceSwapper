package pl.qbs.main;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import pl.qbs.util.FileUtil;
import pl.qbs.util.Util;

public class mFrame extends JPanel implements ActionListener {

	private JButton catalog;
	final JFileChooser fileChooser = new JFileChooser();
	public static JTextArea log;
	private JButton start;
	private File directory;
	private JPanel buttonFrame;
	private JTextField keyToSearch;
	private JTextField keyToSwap;
	private JPanel textKey;
	private byte[] newKey;
	private JTextField extensionText;

	private static final long serialVersionUID = -4724656228210263096L;

	public mFrame() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		log = new JTextArea(5, 20);
		log.setMargin(new Insets(5, 5, 5, 5));
		log.setEditable(false);
		JScrollPane logScrollPane = new JScrollPane(log);

		catalog = new JButton();
		catalog.setSize(50, 20);
		catalog.setText("Wybierz katalog");
		catalog.addActionListener(this);
		start = new JButton();
		start.setSize(50, 20);
		start.setText("Start");
		start.addActionListener(this);
		start.setEnabled(false);
		buttonFrame = new JPanel();
		//buttonFrame.setSize(480, 300);

		buttonFrame.add(catalog);
		buttonFrame.add(start);

		JLabel keySecondLabel = new JLabel();
		keySecondLabel.setText("Wpisz drugi ciąg bajtów");
		keyToSwap = new JTextField();
		//keyToSwap.setText("Ciąg do zamiany");
		keyToSwap.setColumns(10);

		JLabel keyLabel = new JLabel();
		keyLabel.setText("Podaj pierwszy ciąg bajtów : ");
		keyToSearch = new JTextField();
		keyToSearch.setColumns(10);

		JLabel extensionLabel = new JLabel();
		extensionLabel.setText("Podaj rozszerzenie szukanych plików");
		extensionText = new JTextField();
		extensionText.setColumns(10);


		textKey = new JPanel();
		textKey.add(keyLabel);
		textKey.add(keyToSearch);

		JPanel textSecondKey = new JPanel();
		textSecondKey.add(keySecondLabel);
		textSecondKey.add(keyToSwap);

		JPanel extensionPanel = new JPanel();
		extensionPanel.add(extensionLabel);
		extensionPanel.add(extensionText);

		add(buttonFrame);
		add(extensionPanel);
		add(textKey);
		add(textSecondKey);
		add(log);

	}

	static void makeGui() {
		JFrame frame = new JFrame("Swap binary sequance");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//setLayout( new mFrame());
		frame.add(new mFrame());
		frame.setSize(500, 500);
		//frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		// fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (action.getSource() == catalog) {
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				directory = fileChooser.getSelectedFile();
				start.setEnabled(true);
				log.append("Wybrany katalog " + directory + "." + "\n");
			}
		} else if (action.getSource() == start) {
			List<File> filesToProcess = FileUtil.getAllFiles(extensionText.getText(),directory);
				for (int i = 0; i < filesToProcess.size(); i++) {
					try {
						File file = filesToProcess.get(i);
						byte[] fileBuffer = FileUtil.readFile(file);
						byte[] key = Util.stringToHex(keyToSearch.getText());

						try {
							newKey = Util.stringToHex(keyToSwap.getText());
						} catch (IllegalArgumentException e){
							System.out.println("Error: " + e.getMessage());
						}


						log.append("file: " + file.getName() + "\n");
						log.append("before: " + Util.bytesToHex(fileBuffer) + "\n");
						List<Integer> indexes = FileUtil.getIndexesOfOcurrencies(fileBuffer, key);


						log.append("Counter = "+indexes.size()+"\n");
						if (indexes.size() > 0){
							byte[] newFile = FileUtil.replaceBytes(fileBuffer, key, newKey,indexes);
							log.append("after: " + Util.bytesToHex(newFile) + "\n");
							FileUtil.writeFile(newFile,file.getAbsolutePath());
							log.append("Plik zapisany :DDDDD");
						 }
						log.append("\n");
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
		}

	}





}
