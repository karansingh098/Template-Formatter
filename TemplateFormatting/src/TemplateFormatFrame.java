//Author: Karan Singh
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

//The TemplateFormatFrame class takes user input in a GUI text area and prints
//the formatted text input into an output text area.
//User input can be manually entered or can be extracted from a file that the user can browse and select
public class TemplateFormatFrame {
	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TemplateFormatFrame window = new TemplateFormatFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TemplateFormatFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 650);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//Making scroll pane for input text area
		JScrollPane inputScrollPane = new JScrollPane();
		inputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		inputScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		JTextArea inputArea = new JTextArea();
		inputArea.setBounds(10, 26, 330, 578);
		
		inputScrollPane.setBounds(10,26,330,578);
		inputScrollPane.getViewport().setBackground(Color.WHITE);
		inputScrollPane.getViewport().add(inputArea);
		inputScrollPane.setBorder(BorderFactory.createLineBorder(Color.black));
		frame.getContentPane().add(inputScrollPane);
		
		//Making scroll pane for output text area
		JScrollPane outputScrollPane = new JScrollPane();
		outputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		outputScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		JTextArea outputArea = new JTextArea();
		outputArea.setBounds(450, 26, 330, 578);
				
		outputScrollPane.setBounds(450,26,330,578);
		outputScrollPane.getViewport().setBackground(Color.WHITE);
		outputScrollPane.getViewport().add(outputArea);
		outputScrollPane.setBorder(BorderFactory.createLineBorder(Color.black));
		frame.getContentPane().add(outputScrollPane);
		
		//The Process button interprets the user input with ! terminals and @ destinations and formats it
		JButton btnNewButton = new JButton("Process");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputArea.setText("");
				Map<String, String> terminals = new HashMap<>();
				//splits user input line by line using regular expressions
				String [] inputLines = inputArea.getText().split("\\n");

				//finding all terminals(lines with ! assignments) and putting them into a map
				//doing this before formatting so user can put ! terminals in any order
				for (int i = 0; i<inputLines.length;i++) {
					//if a line begins with ! and contains an =, it is a terminal
					//throws an IllegalArgumentException if a line begins with ! but does 
					//not have an assignment
					String trimLine = inputLines[i].trim();
					if(trimLine.length() != 0 && trimLine.charAt(0) == '!') {
						String [] terminal = trimLine.split("=");
						if(terminal.length < 2) {
							throw new IllegalArgumentException
								("Invalid input, ! terminal does not have an assignment");
						}
						terminals.put(terminal[0].substring(1).trim(),terminal[1].trim());
					}
					inputLines[i] = trimLine;
				}
				//once all terminals have been mapped, program begins to format user input
				for(String line : inputLines) {
					//using regular expressions (regex) to split apart delimiters and words in each line
					ArrayList<String> words = new ArrayList<>(Arrays.asList(line.split("\\b")));
					int size = words.size();
					//only need to run through formatting cases if there is an @ symbol
					if(line.contains("@")) {
						for(int i = 0; i<size; i++) {
							String spaceless_word = words.get(i).replaceAll("\\s+","");
							//special case: @{}
							//if there are curly braces, only remove portion of the word within the braces
							if (spaceless_word.equals("@{")) {
								//find index of end curly brace
								int endIndex = words.indexOf("}");
								if(terminals.containsKey(words.get(i+1)) && endIndex > 0) {
									//retain spacing
									String new_word = words.get(i).substring(0,words.get(i).indexOf("@"))
											+ terminals.get(words.get(i+1));
									words.set(i,new_word);
									words.subList(i+1,endIndex+1).clear();
									size -= endIndex-i;
								}
							}
							//special case: multiple consecutive @, ex. @@ or @@@ or @ @ 
							//gets rid of unnecessary @ symbols
							else if (spaceless_word.contains("@@")){
								//retaining spacing
								int index = words.get(i).indexOf("@");
								int lastIndex = words.get(i).lastIndexOf('@');
								words.set(i,words.get(i).substring(0,index) + 
										words.get(i).substring(lastIndex));
							}
							//default case
							else if(spaceless_word.contains("@")){
								if(terminals.containsKey(words.get(i+1))) {
									//retain spacing
									String new_word = words.get(i).substring(0,words.get(i).indexOf("@"))
											+ terminals.get(words.get(i+1));
									words.set(i,new_word);
									words.remove(i+1);
									size--;
								}
							}
						}
					}
					//reconstructs the string from the split list
					String line_mod = "";
					for(String word : words) {
						line_mod += word;
					}
					
					//if it is a terminal line, the assignment of the terminal in the map
					//is updated in case there were substituted @ words
					if(line.length() != 0 && line.charAt(0) == '!') {
						String [] terminal_mod = line_mod.split("=");
						terminals.put(terminal_mod[0].substring(1).trim(),terminal_mod[1].trim());
					}
					//only prints out non terminal lines to the output text area
					else {
						outputArea.append(line_mod + "\n");
					}
				}
			}
		});
		btnNewButton.setBounds(350, 266, 89, 23);
		frame.getContentPane().add(btnNewButton);
		
		//browse button allows user to choose a file and it prints all the text into 
		//the input text area while retaining format
		JButton btnNewButton_1 = new JButton("Browse");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.showOpenDialog(null);
				File f = chooser.getSelectedFile();
				//path for the file
				String filename = f.getAbsolutePath();
				try {
					FileReader reader = new FileReader(filename);
					BufferedReader br = new BufferedReader(reader);
					inputArea.read(br,null);
					br.close();
					inputArea.requestFocus();
				}
				catch(Exception e1){
					JOptionPane.showMessageDialog(null, e1);
				}
				
			}
		});
		
		btnNewButton_1.setBounds(350, 158, 89, 23);
		frame.getContentPane().add(btnNewButton_1);

	}
}
