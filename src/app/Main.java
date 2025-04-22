package app;

import java.util.ArrayList;

import controller.auth.AuthController;
import model.*;
import model.entities.Auth;
import model.entities.AuthModel;
import model.entities.Course;
import view.auth.AuthView;


public class Main {

	public static void main(String[] args) {
		
		// Login
		javax.swing.SwingUtilities.invokeLater(() -> {
            AuthView view = new AuthView();
            AuthModel model = new AuthModel();
            new AuthController(view, model);
            view.setVisible(true);
        });
		
		
		// Debug
//		ArrayList<CourseModel> courses = new CourseDA().getCourseList();
//		
//		System.out.println(courses);

	}

}
