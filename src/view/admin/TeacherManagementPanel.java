package view.admin;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import model.entities.Teacher;
import model.table.TeacherTableModel;

public class TeacherManagementPanel extends JPanel {
    
    private JTable teacherTable;
    private TeacherTableModel tableModel;
    private JButton addButton, editButton, deleteButton;
    
    private ActionListener searchListener;
    private ActionListener resetListener;
    private TableRowSorter<TableModel> tableSorter;
    
    public TeacherManagementPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel title
        JLabel titleLabel = new JLabel("Teacher Management");
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
        addButton = new JButton("Add Teacher");
        editButton = new JButton("Edit Teacher");
        deleteButton = new JButton("Delete Teacher");
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        // Add components to panel
        add(topPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Build the table panel with the teacher table
     */
    private void buildTablePanel() {
        // Initialize the table model
        tableModel = new TeacherTableModel();
        
        // Create the table with the model
        teacherTable = new JTable(tableModel);
        
        //create and set row sorter
        tableSorter = new TableRowSorter<>(tableModel);
        teacherTable.setRowSorter(tableSorter);
        
        // Set selection mode
        teacherTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Add the table to a scroll pane and add it to this panel
        JScrollPane scrollPane = new JScrollPane(teacherTable);
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
        String[] filterOptions = {"Name", "ID", "Email", "Status"};
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
    public void updateTeacherTable(ArrayList<Teacher> teachers) {
        tableModel.updateTeacherList(teachers);
    }
    
    // Get selected teacher
    public Teacher getSelectedTeacher() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow >= 0) {
            return tableModel.getTeacherAt(selectedRow);
        }
        return null;
    }
    
    // Get selected row index
    public int getSelectedRowIndex() {
        return teacherTable.getSelectedRow();
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
        
        //notify controller
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
            teacherTable.setRowSorter(tableSorter);
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
    
    public void clearFilter() {
        if (tableSorter != null) {
            tableSorter.setRowFilter(null);
        }
    }

    private int getColumnIndexForCriteria(String criteria) {
        switch (criteria) {
            case "ID": return 0;
            case "Name": return 1; 
            case "Email": return 3;
            case "Status": return 4;
            default: return 0; 
        }
    }
}