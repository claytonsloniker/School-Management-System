package view.teacher;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import model.entities.Course;
import model.entities.Message;
import model.table.MessageTableModel;

public class TeacherMessagesPanel extends JPanel {
    
    private JComboBox<Course> courseSelector;
    private JTable messageTable;
    private MessageTableModel tableModel;
    private JTextArea messageViewArea;
    private JTextArea replyArea;
    private JButton replyButton;
    private JButton markAsReadButton;
    
    private TableRowSorter<TableModel> tableSorter;
    
    public TeacherMessagesPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel title
        JLabel titleLabel = new JLabel("Student Messages");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        // Course selector panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(createCourseSelectorPanel(), BorderLayout.CENTER);
        
        // Initialize the table model and setup the message table
        buildTablePanel();
        
        // Create the main content panel (split between message list and message view)
        JSplitPane splitPane = createMainContentPanel();
        
        // Add components to panel
        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel createCourseSelectorPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JLabel courseLabel = new JLabel("Select Course:");
        courseSelector = new JComboBox<>();
        
        panel.add(courseLabel);
        panel.add(courseSelector);
        
        return panel;
    }
    
    private JSplitPane createMainContentPanel() {
        // Left panel - Message list
        JPanel leftPanel = new JPanel(new BorderLayout());
        
        // Message table
//        messageTable = new JTable();
//        messageTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(messageTable);
        
        // Add table to left panel
        leftPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Button panel for left side
        JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        markAsReadButton = new JButton("Mark as Read");
        leftButtonPanel.add(markAsReadButton);
        leftPanel.add(leftButtonPanel, BorderLayout.SOUTH);
        
        // Right panel - Message view and reply
        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        
        // Message view area
        JLabel messageViewLabel = new JLabel("Message Content:");
        messageViewArea = new JTextArea();
        messageViewArea.setEditable(false);
        messageViewArea.setLineWrap(true);
        messageViewArea.setWrapStyleWord(true);
        JScrollPane messageViewScroll = new JScrollPane(messageViewArea);
        messageViewScroll.setPreferredSize(new Dimension(400, 150));
        
        // Reply area
        JLabel replyLabel = new JLabel("Reply:");
        replyArea = new JTextArea();
        replyArea.setLineWrap(true);
        replyArea.setWrapStyleWord(true);
        JScrollPane replyScroll = new JScrollPane(replyArea);
        replyScroll.setPreferredSize(new Dimension(400, 100));
        
        // Reply button
        replyButton = new JButton("Send Reply");
        JPanel replyButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        replyButtonPanel.add(replyButton);
        
        // Add components to right panel
        JPanel messageViewPanel = new JPanel(new BorderLayout());
        messageViewPanel.add(messageViewLabel, BorderLayout.NORTH);
        messageViewPanel.add(messageViewScroll, BorderLayout.CENTER);
        
        JPanel replyPanel = new JPanel(new BorderLayout());
        replyPanel.add(replyLabel, BorderLayout.NORTH);
        replyPanel.add(replyScroll, BorderLayout.CENTER);
        replyPanel.add(replyButtonPanel, BorderLayout.SOUTH);
        
        rightPanel.add(messageViewPanel, BorderLayout.NORTH);
        rightPanel.add(replyPanel, BorderLayout.CENTER);
        
        // Create and configure split pane
        JSplitPane splitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            leftPanel,
            rightPanel
        );
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.5);
        
        return splitPane;
    }
    
    private void buildTablePanel() {
        // Initialize the table model
        tableModel = new MessageTableModel();
        
        // Create the table with the model
        messageTable = new JTable(tableModel);
        
        // Set selection mode
        messageTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Add row selection listener
        messageTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = messageTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Message selectedMessage = tableModel.getMessageAt(selectedRow);
                    displayMessage(selectedMessage);
                }
            }
        });
    }
    
    // Methods to update course selector
    public void updateCourseSelector(ArrayList<Course> courses) {
        courseSelector.removeAllItems();
        for (Course course : courses) {
            courseSelector.addItem(course);
        }
    }
    
    // Methods to set listeners
    public void setReplyButtonListener(ActionListener listener) {
        replyButton.addActionListener(listener);
    }
    
    public void setMarkAsReadButtonListener(ActionListener listener) {
        markAsReadButton.addActionListener(listener);
    }
    
    public void setCourseSelectionListener(ActionListener listener) {
        courseSelector.addActionListener(listener);
    }
    
    public void setMessageTableSelectionListener(ListSelectionListener listener) {
        messageTable.getSelectionModel().addListSelectionListener(listener);
    }
    
    public void updateMessageTable(ArrayList<Message> messages) {
        if (tableModel != null) {
            tableModel.updateMessageList(messages);
        }
    }
    
    // Method to display a selected message
    public void displayMessage(Message message) {
        if (message != null) {
            messageViewArea.setText("From: " + message.getSenderName() + "\n" +
                                  "Subject: " + message.getSubject() + "\n" +
                                  "Date: " + message.getTimestamp() + "\n\n" +
                                  message.getMessage());
            messageViewArea.setCaretPosition(0); // Scroll to top
        } else {
            messageViewArea.setText("");
        }
    }
    
    // Getters for controller use
    public Course getSelectedCourse() {
        return (Course) courseSelector.getSelectedItem();
    }
    
    public Message getSelectedMessage() {
        int selectedRow = messageTable.getSelectedRow();
        if (selectedRow >= 0) {
            return tableModel.getMessageAt(selectedRow);
        }
        return null;
    }
    
    public String getReplyText() {
        return replyArea.getText();
    }
    
    // Clear the reply area after sending
    public void clearReplyArea() {
        replyArea.setText("");
    }
}