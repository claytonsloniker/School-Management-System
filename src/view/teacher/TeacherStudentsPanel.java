package view.teacher;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import model.entities.Course;
import model.entities.Student;
import model.table.StudentTableModel;

public class TeacherStudentsPanel extends JPanel {
    
    private JComboBox<Course> courseSelector;
    private JTable studentTable;
    private StudentTableModel tableModel;
    private JButton sendMessageButton;
    
    private ActionListener searchListener;
    private ActionListener resetListener;
    private TableRowSorter<TableModel> tableSorter;
    
    public TeacherStudentsPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel title
        JLabel titleLabel = new JLabel("Students Enrolled in Course");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        // Course selector panel
        JPanel selectorPanel = createCourseSelectorPanel();
        
        // Create search panel
        JPanel searchPanel = createSearchPanel();
        
        // Title and top controls container
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(selectorPanel, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.SOUTH);
        
        // Initialize the table panel
        buildTablePanel();
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sendMessageButton = new JButton("Send Message to Selected Student");
        
        buttonPanel.add(sendMessageButton);
        
        // Add components to panel
        add(topPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createCourseSelectorPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JLabel courseLabel = new JLabel("Select Course:");
        courseSelector = new JComboBox<>();
        
        panel.add(courseLabel);
        panel.add(courseSelector);
        
        return panel;
    }
    
    /**
     * Build the table panel with the student table
     */
    private void buildTablePanel() {
        // Initialize the table model
        tableModel = new StudentTableModel();
        
        // Create the table with the model
        studentTable = new JTable(tableModel);
        
        // Create and set row sorter
        tableSorter = new TableRowSorter<>(tableModel);
        studentTable.setRowSorter(tableSorter);
        
        // Set selection mode
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Add the table to a scroll pane and add it to this panel
        JScrollPane scrollPane = new JScrollPane(studentTable);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        // Create search components
        JLabel searchLabel = new JLabel("Search:");
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton resetButton = new JButton("Reset");
        
        // Add filter dropdown
        JLabel filterLabel = new JLabel("Filter by:");
        String[] filterOptions = {"Name", "ID", "Email"};
        JComboBox<String> filterComboBox = new JComboBox<>(filterOptions);
        
        // Add components to panel
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(filterLabel);
        searchPanel.add(filterComboBox);
        searchPanel.add(searchButton);
        searchPanel.add(resetButton);
        
        // Add action listeners
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText().trim();
            String filterCriteria = (String) filterComboBox.getSelectedItem();
            triggerSearchEvent(searchText, filterCriteria);
        });
        
        resetButton.addActionListener(e -> {
            searchField.setText("");
            filterComboBox.setSelectedIndex(0);
            notifyResetEvent();
        });
        
        return searchPanel;
    }
    
    // Methods to update course selector
    public void updateCourseSelector(ArrayList<Course> courses) {
        courseSelector.removeAllItems();
        for (Course course : courses) {
            courseSelector.addItem(course);
        }
    }
    
    // Methods to set listeners
    public void setSendMessageButtonListener(ActionListener listener) {
        sendMessageButton.addActionListener(listener);
    }
    
    public void setCourseSelectionListener(ActionListener listener) {
        courseSelector.addActionListener(listener);
    }
    
    // Method to update table data
    public void updateStudentTable(ArrayList<Student> students) {
        tableModel.updateStudentList(students);
    }
    
    // Get selected course and student
    public Course getSelectedCourse() {
        return (Course) courseSelector.getSelectedItem();
    }
    
    public Student getSelectedStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = studentTable.convertRowIndexToModel(selectedRow);
            return tableModel.getStudentAt(modelRow);
        }
        return null;
    }
    
    // Search and filter methods
    public void setSearchListener(ActionListener listener) {
        this.searchListener = listener;
    }

    public void setResetListener(ActionListener listener) {
        this.resetListener = listener;
    }

    private void triggerSearchEvent(String searchText, String filterCriteria) {
        if (searchListener != null) {
            ActionEvent event = new ActionEvent(
                this, 
                ActionEvent.ACTION_PERFORMED,
                searchText + "|" + filterCriteria
            );
            searchListener.actionPerformed(event);
        }
    }

    private void notifyResetEvent() {
        clearFilter();
        
        if (resetListener != null) {
            resetListener.actionPerformed(
                new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "reset")
            );
        }
    }

    public void applyFilter(String searchText, String filterCriteria) {
        // Check if tableSorter is initialized
        if (tableSorter == null) {
            tableSorter = new TableRowSorter<>(tableModel);
            studentTable.setRowSorter(tableSorter);
        }
        
        RowFilter<TableModel, Object> rf = null;
        
        try {
            if (searchText.trim().length() == 0) {
                rf = null; // No filter
            } else if (filterCriteria.equals("Name")) {
                // Special case for name - search both first and last name columns
                ArrayList<RowFilter<Object, Object>> filters = new ArrayList<>();
                filters.add(RowFilter.regexFilter("(?i)" + searchText, 1)); // First name (column 1)
                filters.add(RowFilter.regexFilter("(?i)" + searchText, 2)); // Last name (column 2)
                rf = RowFilter.orFilter(filters);
            } else {
                int columnIndex = getColumnIndexForCriteria(filterCriteria);
                rf = RowFilter.regexFilter("(?i)" + searchText, columnIndex);
            }
        } catch (java.util.regex.PatternSyntaxException e) {
            return; // Bad regex pattern, don't apply filter
        }
        
        tableSorter.setRowFilter(rf);
    }

    private int getColumnIndexForCriteria(String criteria) {
        switch (criteria) {
            case "ID": return 0;
            case "Name": return 1; // This will be handled specially above
            case "Email": return 3;
            default: return 0;
        }
    }
    
    public void clearFilter() {
        if (tableSorter != null) {
            tableSorter.setRowFilter(null);
        }
    }
}