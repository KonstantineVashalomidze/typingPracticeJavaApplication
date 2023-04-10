package front;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Caret;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class typingPracticeMainPage extends JFrame implements ActionListener {
    private static final Map<Character, String> map = Map.of('\t', "\\t", '\b', "\\b", '\n',
            "\\n", '\f', "\\f", '\\', "\\\'", '"', "\\\"", '\r', "\\r");

    private JPanel userInputField = new JPanel();
    private JPanel screenField = new JPanel();
    private JTextPane screen = new JTextPane();
    {
        // This block runs with constructor
        this.screen.setText(insertNewlines(Objects.requireNonNull(selectRandomLine())));
    }
    private JTextArea userInput = new JTextArea();

    private JLayeredPane layeredPane = new JLayeredPane();
    private Caret caretOfScreen = new DefaultCaret();
    private int caretPosition = 0;

    private StyledDocument doc = this.screen.getStyledDocument();

    private JButton optionsButton = new JButton("Options");

    private int wpm = 0;

    SimpleAttributeSet attrs = new SimpleAttributeSet();

    private JPanel panelForBottomButtons = new JPanel(new GridLayout(0, 1));

    private JPanel panelOfScreens = new JPanel(new GridLayout(2, 1));

    private JButton restartButton = new JButton("Restart");

    private long startTime;


    public typingPracticeMainPage(){

        // Screen setup


        // set the width and height of the window
        this.screen.setPreferredSize(new Dimension(500, 350));
        // set background color
        this.screen.setBackground(Color.LIGHT_GRAY);
        // set font of the text and size
        this.screen.setFont(new Font("Tahoma", Font.PLAIN, 20));
        // set brder and border colorfor text
        this.screen.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        // set custom made caret here
        this.screen.setCaret(this.caretOfScreen);
        // set caret visible
        this.screen.getCaret().setVisible(true);
        // set caret position
        this.screen.setCaretPosition(this.caretPosition);
        // make the screen not focusable
        this.screen.setFocusable(false);
        // screen shouldn't be editable
        this.screen.setEditable(false);


        // add object to the screen panel
        this.screenField.add(this.screen);



        // User input setup


        // set the width and height of the window
        this.userInput.setPreferredSize(new Dimension(500, 350));
        // set background color
        this.userInput.setBackground(Color.LIGHT_GRAY);
        // set font of the text and size
        this.userInput.setFont(new Font("Tahoma", Font.PLAIN, 20));
        // set border and border color for text
        this.userInput.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        // focus on this user input immediately after the frame is loaded
        this.userInput.requestFocus();
        // add userinput to userinputfield
        this.userInputField.add(this.userInput);





        // Add action performed listener to the user input
        this.userInput.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                if (userInput.getText().length() == 1){
                    // Record start time
                     startTime = System.currentTimeMillis();
                }
                if (userInput.getText().length() <= screen.getText().length()){
                    // check if the end of the typing has came
                    if (checkIfGivenStringUserTypedCorrectly() && userInput.getText().length() == screen.getText().length()){
                        // Record end time
                        long endTime = System.currentTimeMillis();

                        // assume characterCount and elapsedTimeInMs are already initialized
                        int wpm = (int) ((((double) screen.getText().length()) / 5) / (((double) endTime - startTime) / 60000));

                        // Display an alert dialog with a message
                        JOptionPane.showMessageDialog(null, wpm + "WPM");
                        dispose();
                        new typingPracticeMainPage();
                    }
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (userInput.getText().length() <= screen.getText().length()) {
                    int lengthOfUserInput = userInput.getText().length();
                    int lengthOfTheScreen = screen.getText().length();
                    checkIfGivenStringUserTypedCorrectly();
                    // in case of deleting always make deleted chars black
                    StyleConstants.setForeground(attrs, Color.BLACK);
                    doc.setCharacterAttributes(userInput.getText().length(), lengthOfTheScreen - lengthOfUserInput, attrs, false);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // text attributes changed (e.g. font, color)
                System.out.println("Text attributes changed");
            }
        });


        // add size for opotions button
        this.optionsButton.setSize(new Dimension(100, 20));
        // add action listener to options button
        this.optionsButton.addActionListener(this);
        // add button on bottom panel
        this.panelForBottomButtons.add(this.optionsButton);

        // add size for restart button
        this.restartButton.setPreferredSize(new Dimension(100, 20));
        // add action listener to restart button
        this.restartButton.addActionListener(this);
        // add button on bottom panel
        this.panelForBottomButtons.add(this.restartButton);

        // add screens to screen panel
        this.panelOfScreens.add(this.screenField);
        this.panelOfScreens.add(this.userInputField);

        // add panels to main panel
        this.add(this.panelOfScreens);
        this.add(this.panelForBottomButtons);



        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new FlowLayout());
        this.pack();
        // Center the location of pop up screen
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.optionsButton){
            this.dispose();
            new InsertTextPage();
        }
        if (e.getSource() == this.restartButton){
            this.dispose();
            new typingPracticeMainPage();
        }

    }

    private boolean checkIfGivenStringUserTypedCorrectly(){
        int userInputLength = this.userInput.getText().length() - 1;

        this.screen.setCaretPosition(this.userInput.getText().length());

        // Checks if user input is positive or zero
        if (userInputLength < 0){
            return true;
        }
        // Checks if user typed correctly
        // Body sets the blue color in that case to the fond beginning from the 0 and length of the user input
        if (this.userInput.getText().equals(this.screen.getText().substring(0, userInputLength + 1))){
            StyleConstants.setForeground(this.attrs, Color.BLUE);
            this.doc.setCharacterAttributes(0, userInputLength + 1, this.attrs, false);
            return true;
        } else {
            // In case of error set the color to red
            StyleConstants.setForeground(this.attrs, Color.RED);
            this.doc.setCharacterAttributes(userInputLength, 1, this.attrs, false);
            return false;
        }

    }

    // This function modifies the string
    private String insertNewlines(String input) {
        int lineLength = 50;
        String[] words = input.split(" ");
        StringBuilder result = new StringBuilder();

        int currentLineLength = 0;
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            int wordLength = word.length();

            if (currentLineLength + wordLength > lineLength) {
                result.append("\n");
                currentLineLength = 0;
            }

            // Check if the next word will fit on the current line
            if (currentLineLength + wordLength + 1 <= lineLength || i == 0) {
                if (i > 0) {
                    result.append(" ");
                    currentLineLength++;
                }
                result.append(word);
                currentLineLength += wordLength;
            } else {
                result.append("\n").append(word);
                currentLineLength = wordLength;
            }
        }

        return result.toString();
    }

    public static String selectRandomLine() {
        String filename = InsertTextPage.filename;
        try {
            // Count the number of lines in the file
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            int numLines = InsertTextPage.countHowManyTextsAreInFile;

            reader.close();

            // Generate a random line number
            Random rand = new Random();
            int randomLineNum = rand.nextInt(numLines) + 1;

            // Select the random line from the file
            reader = new BufferedReader(new FileReader(filename));
            String randomLine = "";
            for (int i = 0; i < randomLineNum; i++) {
                randomLine = reader.readLine();
            }
            reader.close();

            return randomLine;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Translates the string such that the string formatters like \n will be ignored
    public static String translate(String text) {
        return text.chars()
                .mapToObj(ch -> map.getOrDefault((char)ch, Character.toString(ch)))
                .collect(Collectors.joining());
    }

}
