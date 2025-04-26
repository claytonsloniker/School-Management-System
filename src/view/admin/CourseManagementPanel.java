package view.admin;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import model.entities.CourseWithTeacher;
import model.table.CourseTableModel;

public class CourseManagementPanel extends JPanel {
    
    private JTable courseTable;
    private CourseTableModel tableModel;
    private JButton addButton, editButton, deleteButton;
    
    private ActionListener searchListener;
    private ActionListener resetListener;
    private TableRowSorter<TableModel> tableSorter;
    
    public CourseManagementPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel title
        JLabel titleLabel = new JLabel("Course Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        // Create search panel
        JPanel searchPanel = createSearchPanel();
        
        // Title and search panel container
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        
        // Initialize the table panel
        buildTablePanel();
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addButton = new JButton("Add Course");
        editButton = new JButton("Edit Course");
        deleteButton = new JButton("Delete Course");
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        // Add components to panel
        add(topPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Build the table panel with the course table
     */
    private void buildTablePanel() {
        // Initialize the table model
        tableModel = new CourseTableModel();
        
        // Create the table with the model
        courseTable = new JTable(tableModel);
        
        // Create and set row sorter
        tableSorter = new TableRowSorter<>(tableModel);
        courseTable.setRowSorter(tableSorter);
        
        // Set selection mode
        courseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Add the table to a scroll pane and add it to this panel
        JScrollPane scrollPane = new JScrollPane(courseTable);
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
        String[] filterOptions = {"Name","Code","Max Capacity", "Teacher"};
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
    
    // Methods to set listeners
    public void setAddButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }
    
    public void setEditButtonListener(ActionListener listener) {
        editButton.addActionListener(listener);
    }
    
    public void setDeleteButtonListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }
    
    // Method to update table data
    public void updateCourseTable(ArrayList<CourseWithTeacher> courses) {
        tableModel.updateCourseList(courses);
    }
    
    // Get selected course
    public CourseWithTeacher getSelectedCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = courseTable.convertRowIndexToModel(selectedRow);
            return tableModel.getCourseAt(modelRow);
        }
        return null;
    }
    
    // Get selected row index
    public int getSelectedRowIndex() {
        return courseTable.getSelectedRow();
    }
    
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
        
        // Notify controller
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
            courseTable.setRowSorter(tableSorter);
        }
        
        RowFilter<TableModel, Object> rf = null;
        
        try {
            if (searchText.trim().length() == 0) {
                rf = null; // No filter
            } else {
                int columnIndex = getColumnIndexForCriteria(filterCriteria);
                rf = RowFilter.regexFilter("(?i)" + searchText, columnIndex);
            }
        } catch (java.util.regex.PatternSyntaxException e) {
            return; // Bad regex pattern, don't apply filter
        }
        
        tableSorter.setRowFilter(rf);
    }
    
    public void clearFilter() {
        if (tableSorter != null) {
            tableSorter.setRowFilter(null);
        }
    }

    private int getColumnIndexForCriteria(String criteria) {
        switch (criteria) {
            case "Code": return 0;
            case "Name": return 1;
            case "Max Capacity": return 2;
            case "Teacher": return 3;
            default: return 0;
        }
    }
}