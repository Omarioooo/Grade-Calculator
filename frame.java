import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class frame extends JFrame implements ActionListener {

    protected final JPanel mainPanel, inputPanel, footerPanel, displayPanel, gradePanel;
    private final JButton addButton, removeButton, clearButton, calculateButton;
    protected JTextField subjectField, gradeField, displayTotalField,
                         displayAvgField, displayGradeField;
    protected JLabel totalLabel, avgLabel, gradeLabel;
    protected final JTable infoTable;
    private final DefaultTableModel model;
    protected final JScrollPane scrollPane;

    public frame() {
        // create the dynamic table
        String[] columnsName = {"Subject name", "Grade"};
        model = new DefaultTableModel(columnsName, 0);
        infoTable = new JTable(model);
        scrollPane = new JScrollPane(infoTable);

        // handel cell renderer for table cells
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        cellRenderer.setBackground(new Color(235, 245, 255));
        infoTable.setDefaultRenderer(Object.class, cellRenderer);

        // handel the table header
        JTableHeader header = infoTable.getTableHeader();
        header.setBackground(new Color(0, 120, 215));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));

        // Set row height and font
        infoTable.setRowHeight(30);
        infoTable.setFont(new Font("ink", Font.ITALIC, 14));

        // Set selection background color
        infoTable.setSelectionBackground(new Color(0, 120, 215));
        infoTable.setSelectionForeground(Color.WHITE);

        // Remove grid lines
        infoTable.setShowGrid(false);


        // create the input panel
        inputPanel = new JPanel();

        // create add button
        addButton = new JButton("Add");
        addButton.setFocusable(false);
        addButton.addActionListener(this);
        // create remove button
        removeButton = new JButton("Remove");
        removeButton.setFocusable(false);
        removeButton.addActionListener(this);
        // create clear button
        clearButton = new JButton("Clear");
        clearButton.setFocusable(false);
        clearButton.addActionListener(this);

        // create the input fields
        subjectField = new JTextField(15);
        gradeField = new JTextField(5);

        // add all input component to input Panel
        inputPanel.add(new JLabel("Subject"));
        inputPanel.add(subjectField);
        inputPanel.add(new JLabel("Grade"));
        inputPanel.add(gradeField);
        inputPanel.add(addButton);
        inputPanel.add(removeButton);
        inputPanel.add(clearButton);


        // create footer panel and add to the main panel
        footerPanel = new JPanel();
        footerPanel.add(new JLabel("Grade Calculator.. :)"));


        // create the panel for total ,avgGrades and grades
        gradePanel = new JPanel(new GridLayout(4, 2, 10, 15));
        gradePanel.setBorder(BorderFactory.createEmptyBorder(40, 10, 20, 55));

        // Create labels
        totalLabel = new JLabel("Total:");
        avgLabel = new JLabel("Average:");
        gradeLabel = new JLabel("Grade:");

        // Create text fields
        displayTotalField = new JTextField();
        displayTotalField.setEditable(false);
        displayAvgField = new JTextField();
        displayAvgField.setEditable(false);
        displayGradeField = new JTextField();
        displayGradeField.setEditable(false);

        // Add components to the grade panel
        gradePanel.add(totalLabel);
        gradePanel.add(displayTotalField);
        gradePanel.add(avgLabel);
        gradePanel.add(displayAvgField);
        gradePanel.add(gradeLabel);
        gradePanel.add(displayGradeField);

        // create the calculate button to find the final grade
        calculateButton = new JButton("Calculate grade");
        calculateButton.setFocusable(false);
        calculateButton.addActionListener(this);

        // create the display panel
        displayPanel = new JPanel(new BorderLayout());
        displayPanel.add(gradePanel, BorderLayout.NORTH);
        displayPanel.add(calculateButton, BorderLayout.SOUTH);


        // create the main panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.WEST);  // ---> add the table components to the table panel
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        mainPanel.add(displayPanel, BorderLayout.EAST);
        add(mainPanel);  // ---> add the main panel to the frame

        // Frame setting
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(640, 500);
        setVisible(true);
    }

    @Override
    // Make the buttons do some things
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
              addSubject();
        } else if (e.getSource() == removeButton) {
             removeSubject();
        } else if (e.getSource() == clearButton) {
            clearTable();
        } else if (e.getSource() == calculateButton) {
             calculateResults();
        }
    }

    // A method to check the grade if between ---> 0 & 100
    private boolean isValidGrade(String degreeStr) {
        try {
            int degree = Integer.parseInt(degreeStr);
            return degree >= 0 && degree <= 100;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // A method to add subject to the table
    public void addSubject(){
        String subject = subjectField.getText();
        String degree = gradeField.getText();

        if (!subject.isEmpty() && !degree.isEmpty()) {
            if (isValidGrade(degree)) {
                model.addRow(new Object[]{subject, degree});
                subjectField.setText("");
                gradeField.setText("");
            }else{
                JOptionPane.showMessageDialog(this, "Please enter correct grade", "Input Error", JOptionPane.ERROR_MESSAGE);
                gradeField.setText("");
            }
        }else{
            JOptionPane.showMessageDialog(this, "Please enter both subject and grade", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // A method to remove subject from the table
    private void removeSubject(){
        if (infoTable.getSelectedRow() != -1) {
            model.removeRow(infoTable.getSelectedRow());
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to remove", "Selection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // A method to clear the table
    private void clearTable(){
        model.setRowCount(0);
        displayTotalField.setText("");
        displayGradeField.setText("");
        displayAvgField.setText("");
    }

    // A method to calculate the grades
    private void calculateResults() {
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "The table is empty", "Calculation Error", JOptionPane.ERROR_MESSAGE);
        }
        double total = totalGrades();
        double avg = avgGrades(total, model.getRowCount());
        String grade = grade(avg);

        displayTotalField.setText(String.valueOf(total));
        displayAvgField.setText(String.valueOf(avg));
        displayGradeField.setText(grade);
    }

    // A method to calculate the total grades
    private double totalGrades() {
        double sum = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            sum += Double.parseDouble(model.getValueAt(i, 1).toString());
        }
        return sum;
    }

    // A method to calculate average
    private double avgGrades(double total, int numOfSubjects) {
        return total / numOfSubjects;
    }

    // A method to calculate the Grade
    private String grade(double avg) {
        if (avg >= 95 && avg <= 100) {
            return "A++";
        } else if (avg >= 90 && avg < 95) {
            return "A+";
        } else if (avg >= 85 && avg < 90) {
            return "A";
        } else if (avg >= 80 && avg < 85) {
            return "B+";
        } else if (avg >= 75 && avg < 80) {
            return "B";
        } else if (avg >= 70 && avg < 75) {
            return "C+";
        } else if (avg >= 65 && avg < 70) {
            return "C";
        } else if (avg >= 60 && avg < 65) {
            return "D+";
        } else if (avg >= 55 && avg < 60) {
            return "D";
        } else if (avg >= 50 && avg < 55) {
            return "D-";
        } else {
            return "F";
        }
    }
}