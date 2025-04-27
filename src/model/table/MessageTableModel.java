package model.table;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import model.entities.Message;

public class MessageTableModel extends AbstractTableModel {
    private String[] columnNames = {"From", "Subject", "Date", "Status"};
    private ArrayList<Message> messages;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");

    public MessageTableModel(ArrayList<Message> messages) {
        this.messages = messages;
    }
    
    public MessageTableModel() {
        this.messages = new ArrayList<Message>();
    }

    @Override
    public int getRowCount() {
        return messages.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        Message message = messages.get(row);
        switch (col) {
            case 0:
                return message.getSenderName();
            case 1:
                return message.getSubject();
            case 2:
                return dateFormat.format(message.getTimestamp());
            case 3:
                return message.getStatus();
            default:
                return null;
        }
    }

    public void updateMessageList(ArrayList<Message> messages) {
        this.messages = messages;
        fireTableDataChanged();
    }
    
    public Message getMessageAt(int row) {
        return messages.get(row);
    }
}