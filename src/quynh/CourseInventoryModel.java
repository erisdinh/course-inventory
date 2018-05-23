package quynh;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;

public class CourseInventoryModel {

    private ArrayList<Course> courses;
    private Course selectedCourse = null;
    private String newCourseId = null;
    private int CourseIndex;
    private ArrayList<String> categories = new ArrayList<>();

    public CourseInventoryModel() {
        courses = new ArrayList<>();
    }

    public ArrayList<Course> getCourses() {
        return this.courses;
    }

    public int getCourseCount() {
        return courses.size();
    }

    public Course getSelectedCourse() {
        return selectedCourse;
    }

    public int getSelectedCourseIndex() {
        return this.courses.indexOf(selectedCourse);
    }

    public void setSelectedCourse(Course course) {
        this.selectedCourse = course;
    }

    public String getNewCourseId() {
        return newCourseId;
    }

    public void setNewCourseId(String newCourseId) {
        this.newCourseId = newCourseId;
    }

    public int getCourseIndex(String id) {
        return CourseIndex = this.courses.indexOf(getCourseById(id));
    }

    public void setCourseIndex(int newCourseIndex) {
        this.CourseIndex = newCourseIndex;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    // parse DAT file
    public void readCourseFile(File file) {

        // validate param
        if (file == null) {
            return;
        }

        ArrayList<String> lines = new ArrayList<>();
        String line;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            // copy all lines form the file to the memory
            // if no more line -> return null -> check the null
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            // process every line
            for (int i = 0; i < lines.size(); i++) {
                line = lines.get(i);

                // split line
                String[] tokens = line.split(";");

                //assign tokens to Course object
                if (tokens.length == 4) {
                    // trim then set token values to Course
                    String id = tokens[0].trim();
                    String title = tokens[1].trim();
                    int credit = Integer.valueOf(tokens[2].trim());
                    String cat = tokens[3].trim();

                    Course c = new Course(id, title, credit, cat);

                    // put course to ArrayList
                    courses.add(c);
                }
            }
        } catch (IOException e) {
            System.out.println("(ERROR) Failed to read file");
        }
    }

    // save DAT file
    public void saveCourseFile(File file) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferWriter = new BufferedWriter(fileWriter);

            for (int i = 0; i < courses.size(); i++) {
                Course course = courses.get(i);
                bufferWriter.write(course.getId() + " ; " + course.getTitle()
                        + " ; " + course.getCredit() + " ; "
                        + course.getCategory());
                bufferWriter.newLine();
            }

            // close resources
            bufferWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Course> findCoursesById(String id) {
        ArrayList<Course> courses = new ArrayList<>();

        // validation
        if (id == null || id.isEmpty()) {
            return courses; // return empty array

            // make all character are uppercase
        } else {
            String keyword = id.toUpperCase();
            for (int i = 0; i < this.courses.size(); i++) {
                Course course = this.courses.get(i);

                // if there is the course matches (contains the keyword string)
                // add the course to the array
                if (course.getId().contains(keyword)) {
                    courses.add(course);
                }
            }

        }
        return courses;
    }

    public ArrayList<Course> findCourseByTitle(String title) {
        ArrayList<Course> coursesByTitle = new ArrayList<>();

        // validation
        if (title == null || title.isEmpty()) {
            return coursesByTitle;
        } else {
            for (int i = 0; i < courses.size(); i++) {
                Course course = this.courses.get(i);

                // change both title in course and title string into lowercase
                // if there is the course matches (contains the title string)
                // add the course to the array
                if (course.getTitle().toLowerCase().contains(title.toLowerCase())) {
                    coursesByTitle.add(course);
                }
            }
        }
        return coursesByTitle;
    }

    public void removeCourse(Course c) {
        courses.remove(c);
    }

    public void updateCourse(String id, String title, int credit, String cat) {
        Course course = getCourseById(id);
        if (course != null) {
            course.set(id, title, credit, cat);
        }
    }

    public Course getCourseById(String id) {
        for (int i = 0; i < courses.size(); ++i) {
            if (id.equals(courses.get(i).getId())) {
                return courses.get(i);
            }
        }
        return null; // not found 
    }

    public boolean validateId(String id) {

        // regexp: 4 alphabets followed by 5 numeric digits     
        final String PATTERN = "^[A-Z]{4}[0-9]{5}$";
        if (id != null && id.matches(PATTERN)) {
            return true;
        } else {
            return false;
        }
    }

    public void addCourse(String id, String title, int credit, String cat) {
        Course course = new Course(id, title, credit, cat);

        courses.add(course);
        setNewCourseId(id);

        // sort the course list
        Collections.sort(courses, new Comparator<Course>() {
            @Override
            public int compare(Course course1, Course course2) {
                String courseId1 = course1.getId().toUpperCase();
                String courseId2 = course2.getId().toUpperCase();
                return courseId1.compareTo(courseId2);
            }
        });
    }

    // populate the category in comboBox
    public void populateCategories() {
        HashMap<String, Integer> list = new HashMap<>();
        int key = 0;
        for (int i = 0; i < courses.size(); i++) {
            String category = courses.get(i).getCategory();
            if (!list.containsKey(category)) {
                list.put(category, key);
                key++;
            } else {
            }
        }
        for (String category : list.keySet()) {
            categories.add(category);
        }
    }

    // find the courses by category
    public ArrayList<Course> findCourseByCategory(String category) {
        ArrayList<Course> coursesByCategory = new ArrayList<>();

        // return all courses
        if (category.equals("All Categories")) {
            return this.courses;
        } else {
            
            // get the courses lsit based on category
            for (int i = 0; i < courses.size(); i++) {
                Course course = courses.get(i);

                String courseCategory = course.getCategory();

                if (category.equals(courseCategory)) {
                    coursesByCategory.add(course);
                }
            }
            return coursesByCategory;
        }
    }
}
