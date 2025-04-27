package view;

import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;

public interface BaseView {
    
    /**
     * Show a specific panel in the content area
     * @param panelName Name of the panel to show
     */
    void showPanel(String panelName);
    
    /**
     * Update the profile picture display
     * @param profilePicturePath Path to the profile picture
     */
    void updateProfilePicture(String profilePicturePath);
    
    /**
     * Add a property change listener
     * @param listener The property change listener
     */
    void addPropertyChangeListener(PropertyChangeListener listener);
    
    /**
     * Remove a property change listener
     * @param listener The property change listener
     */
    void removePropertyChangeListener(PropertyChangeListener listener);
    
    /**
     * Set the logout listener
     * @param listener The action listener
     */
    void setLogoutListener(ActionListener listener);
    
    /**
     * Dispose of the view
     */
    void dispose();
    
    /**
     * Set the view to be visible or not
     * @param visible Whether the view should be visible
     */
    void setVisible(boolean visible);
    
    JFrame getFrame();
}