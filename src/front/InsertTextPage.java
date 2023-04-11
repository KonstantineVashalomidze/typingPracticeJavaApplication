package front;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

public class InsertTextPage extends JFrame implements ActionListener {

    public static int countHowManyTextsAreInFile = countLines();

    private JTextArea textInputToAddList = new JTextArea();

    public static final String filename = "src/textlist.txt";
    private JPanel panelForDeleting = new JPanel(new FlowLayout());

    private JPanel panelForInserting = new JPanel(new FlowLayout());
    private String[] arrayOfText = new String[countHowManyTextsAreInFile];

    private JTextArea insertNewText = new JTextArea();

    private JButton startPracticeButton = new JButton("StartPracticing");
    private JButton addText = new JButton("AddText");



    {
        // Read from file
        readFromFile();
    }
    private JComboBox<String> comboBoxOfText = new JComboBox<>(this.arrayOfText);

    private JButton deleteText = new JButton("DeleteText");



    public InsertTextPage(){

        // delete text button size dimensions
        this.deleteText.setPreferredSize(new Dimension(100, 20));
        // set combo box size
        this.comboBoxOfText.setPreferredSize(new Dimension(150, 20));
        // set start practicing button size
        this.startPracticeButton.setPreferredSize(new Dimension(150, 20));
        // add button and combo box to the panel
        this.panelForDeleting.add(this.comboBoxOfText);
        this.panelForDeleting.add(this.deleteText);
        this.panelForDeleting.add(this.startPracticeButton);
        // apply action listener to the button
        this.deleteText.addActionListener(this);
        this.startPracticeButton.addActionListener(this);
        // add this panel to the grid layout top row
        this.add(this.panelForDeleting);



        // define textarea size
        this.insertNewText.setPreferredSize(new Dimension(400, 100));
        // set button size
        this.addText.setPreferredSize(new Dimension(100, 20));
        // add button and insert text to the panel
        this.panelForInserting.add(this.insertNewText);
        this.panelForInserting.add(this.addText);
        // apply action listener to button
        this.addText.addActionListener(this);
        // add this panel now to the grid layout bottom row
        this.add(this.panelForInserting);



        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(2, 1));
        this.setPreferredSize(new Dimension(700, 450));
        this.pack();
        // Center the location of pop up screen
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }


    private void readFromFile(){
        int tempCounter = 0;
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                this.arrayOfText[tempCounter] = data;
                tempCounter++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void deleteLineAtIndex(int index) {
        countHowManyTextsAreInFile--;
        try {
            // Create a temporary file
            File inputFile = new File(filename);
            File tempFile = new File("temp.txt");
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            // Copy all lines from the original file to the temporary file, except for the line at the specified index
            String currentLine;
            int lineNumber = 1;
            while ((currentLine = reader.readLine()) != null) {
                if (lineNumber != index) {
                    writer.write(currentLine);
                    writer.newLine();
                }
                lineNumber++;
            }

            // Close the input and output streams
            reader.close();
            writer.close();

            // Delete the original file and rename the temporary file to the original filename
            if (inputFile.delete()) {
                tempFile.renameTo(inputFile);
            } else {
                throw new IOException("Could not delete file");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendLine(String line) {
        countHowManyTextsAreInFile++;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
            writer.write(line);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int countLines(){
        int tempCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            while (br.readLine() != null) {
                tempCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempCount;
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.deleteText){
            deleteLineAtIndex(this.comboBoxOfText.getSelectedIndex() + 1);
        }

        if(e.getSource() == this.addText){
            appendLine(typingPracticeMainPage.translate(this.insertNewText.getText()));
        }

        if(e.getSource() == this.startPracticeButton){
            this.dispose();
            new typingPracticeMainPage();
        }
    }
}
