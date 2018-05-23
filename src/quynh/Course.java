package quynh;

import java.util.Objects;

public class Course {
    
    private String id;
    private String title;
    private int credit;
    private String category;
    
    public Course() {
    }
    
    public Course(String id, String title, int credit, String category) {
        set(id, title, credit, category);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
    public void set(String id, String title, int credit, String category){
        this.id = id;
        this.title = title;
        this.credit = credit;
        this.category = category;
    }
    
    public String toString() {
        return String.format("Course:\n"
                + "ID: %s\n"
                + "Title: %\n"
                + "Credit: %s\n"
                + "Category: %s\n", id, title, credit, category);
    }
    
    // overide equals method
    public boolean equals(Object o) {
        if(o instanceof Course) {
            
            // downcast
            Course course = (Course) o;
            
            // need more
            return id.equals(course.getId());
        } else {
            return false;
        }
    }
    
    //overide hashCode()
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.id);
        return hash;
    }
}
