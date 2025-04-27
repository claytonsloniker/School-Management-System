package view.student;

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
import model.entities.Teacher;
import model.table.MessageTableModel;

public class StudentMessagesPanel extends JPanel {
    
    private JComboBox<Course> courseSelector;
    private JComboBox<Teacher> teacherSelector;
    private JTable messageTable;
    private MessageTableModel tableModel;
    private JTextArea messageViewArea;
    private JTextArea newMessageArea;
    private JTextField subjectField;
    private JButton sendButton;
    private JButton replyButton;
    
    private TableRowSorter<TableModel> tableSorter;
    
    public StudentMessagesPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel title
        JLabel titleLabel = new JLabel("Teacher Messages");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        // Course and teacher selector panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(createSelectorPanel(), BorderLayout.CENTER);
        
        // Initialize the table model and setup the message table
        buildTablePanel();
        
        // Create the main content panel (split between message list and message view)
        JSplitPane splitPane = createMainContentPanel();
        
        // Add components to panel
        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel createSelectorPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JLabel courseLabel = new JLabel("Select Course:");
        courseSelector = new JComboBox<>();
        
        JLabel teacherLabel = new JLabel("Select Teacher:");
        teacherSelector = new JComboBox<>();
        
        panel.add(courseLabel);
        panel.add(courseSelector);
        panel.add(teacherLabel);
        panel.add(teacherSelector);
        
        return panel;
    }
    
    private JSplitPane createMainContentPanel() {
        // Left panel - Message list
        JPanel leftPanel = new JPanel(new BorderLayout());
        
        // Message table
        JScrollPane tableScrollPane = new JScrollPane(messageTable);
        
        // Add table to left panel
        leftPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Right panel - Message view and new message
        JPanel rightPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        
        // Message view area
        JLabel messageViewLabel = new JLabel("Selected Message:");
        messageViewArea = new JTextArea();
        messageViewArea.setEditable(false);
        messageViewArea.setLineWrap(true);
        messageViewArea.setWrapStyleWord(true);
        JScrollPane messageViewScroll = new JScrollPane(messageViewArea);
        messageViewScroll.setPreferredSize(new Dimension(400, 150));
        
        // Reply button
        replyButton = new JButton("Reply to Selected");
        
        // New message section
        JLabel newMessageLabel = new JLabel("New Message:");
        JLabel subjectLabel = new JLabel("Subject:");
        subjectField = new JTextField(30);
        
        newMessageArea = new JTextArea();
        newMessageArea.setLineWrap(true);
        newMessageArea.setWrapStyleWord(true);
        JScrollPane newMessageScroll = new JScrollPane(newMessageArea);
        newMessageScroll.setPreferredSize(new Dimension(400, 150));
        
        // Send button
        sendButton = new JButton("Send Message");
        
        // Add components to right panel using GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        rightPanel.add(messageViewLabel, gbc);
        
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.4;
        rightPanel.add(messageViewScroll, gbc);
        
        gbc.gridy = 2;
        gbc.weighty = 0.0;
        rightPanel.add(replyButton, gbc);
        
        gbc.gridy = 3;
        rightPanel.add(newMessageLabel, gbc);
        
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        rightPanel.add(subjectLabel, gbc);
        
        gbc.gridx = 1;
        rightPanel.add(subjectField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weighty = 0.4;
        rightPanel.add(newMessageScroll, gbc);
        
        gbc.gridy = 6;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.EAST;
        rightPanel.add(sendButton, gbc);
        
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
    
    // Methods to update course and teacher selectors
    public void updateCourseSelector(ArrayList<Course> courses) {
        courseSelector.removeAllItems();
        for (Course course : courses) {
            courseSelector.addItem(course);
        }
    }
    
    public void updateTeacherSelector(ArrayList<Teacher> teachers) {
        teacherSelector.removeAllItems();
        for (Teacher teacher : teachers) {
            teacherSelector.addItem(teacher);
        }
    }
    
    // Methods to set listeners
    public void setSendButtonListener(ActionListener listener) {
        sendButton.addActionListener(listener);
    }
    
    public void setReplyButtonListener(ActionListener listener) {
        replyButton.addActionListener(listener);
    }
    
    public void setCourseSelectionListener(ActionListener listener) {
        courseSelector.addActionListener(listener);
    }
    
    public void setTeacherSelectionListener(ActionListener listener) {
        teacherSelector.addActionListener(listener);
    }
    
    public void setMessageTableSelectionListener(ListSelectionListener listener) {
        messageTable.getSelectionModel().addListSelectionListener(listener);
    }
    
    public void updateMessageTable(ArrayList<Message> messages) {
        tableModel.updateMessageList(messages);
    }
    
    // Method to display a selected message
    public void displayMessage(Message message) {
        if (message != null) {
            messageViewArea.setText("From: " + message.getSenderName() + "\n" +
                                  "Subject: " + message.getSubject() + "\n" +
                                  "Date: " + message.getTimestamp() + "\n\n" +
                                  message.getMessage());
            messageViewArea.setCaretPosition(0); // Scroll to top
            
            // When displaying a message, also set the subject field for reply
            if (message.getSubject().startsWith("RE: ")) {
                subjectField.setText(message.getSubject());
            } else {
                subjectField.setText("RE: " + message.getSubject());
            }
        } else {
            messageViewArea.setText("");
        }
    }
    
    // Getters for controller use
    public Course getSelectedCourse() {
        return (Course) courseSelector.getSelectedItem();
    }
    
    public Teacher getSelectedTeacher() {
        return (Teacher) teacherSelector.getSelectedItem();
    }
    
    public Message getSelectedMessage() {
        int selectedRow = messageTable.getSelectedRow();
        if (selectedRow >= 0) {
            return tableModel.getMessageAt(selectedRow);
        }
        return null;
    }
    
    public String getMessageSubject() {
        return subjectField.getText();
    }
    
    public String getMessageText() {
        return newMessageArea.getText();
    }
    
    // Clear the message fields after sending
    public void clearMessageFields() {
        subjectField.setText("");
        newMessageArea.setText("");
    }
}