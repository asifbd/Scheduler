
package scheduler;

import scheduler.components.Component;
import scheduler.components.Course;
import java.util.*;

public class ScheduleCourses extends ArrayList<Course> implements Comparable {

    /** The heuristic score*/
    private int heuristicScore;

    /**@return the heuristicScore */
    public int getHeuristicScore() { return heuristicScore; }

    /** @param heuristicScore the heuristicScore to set */
    public void setHeuristicScore(int heuristicScore) { this.heuristicScore = heuristicScore; }

    /** Copy constructor based on a father schedule.
     * @param parent The schedule from which this instance will copy all it's courses.
     */
    public ScheduleCourses(ScheduleCourses parent) {
        super();
        this.setHeuristicScore(parent.getHeuristicScore());
        for (int i = 0; i < parent.size(); i++) {
            this.add(parent.get(i));
        }
    }

    /** Default constructor based on specific courses. The heuristic score is set to -1234.
     * @param courses The courses that will be added to the schedule. */
    public ScheduleCourses( ArrayList<Course> courses) {
        super();
        this.heuristicScore = Component.NULL_ID; 
        for (int i = 0; i < courses.size(); i++) { 
            this.add(courses.get(i));
            this.add(courses.get(i));
        }
    }    

    /** Checks if the schedule is terminal.
     * @return True if the schedule is terminal (heuristicScore = 0) or false if it isn't. */
    public boolean isTerminal() {
        if (this.getHeuristicScore() == 0) {
            return true;
        }
        return false;
    }

    /**Generates the hashcode of each instance.*/
    @Override public int hashCode() {
        return 47 + 8 * heuristicScore;
    }

    /** Compares the schedule's id with o's and returns true if they are equal
     * @param s 
     */
    @Override public boolean equals(Object s) {
        if (s == null)  // Null references are not equal to this instance.
            return false;
        if (!s.getClass ().equals (ScheduleCourses.class)) //Neither are they instances of different classes
            return false;
        return (((ScheduleCourses) s).getHeuristicScore() == this.getHeuristicScore());
    }


    /** Returns the schedule's courses as a string. */
    @Override public String toString(){
        String string= "";
        if(!this.isEmpty()){
            Course course;
            for(int i=0; i<this.size() && (course = this.get(i))!=null; i++){
                string += course.toString() + "\n";
            }
        }
        return string;
    }

    /**Compares the two schedules based on their heuristic scores. */
    public int compareTo(Object o) {
        ScheduleCourses schedule = (ScheduleCourses) o;
        if (o == null || schedule.getHeuristicScore()== Component.NULL_ID)  // Null references are not equal to this instance.
            return 0;
        if( schedule.getHeuristicScore() == this.heuristicScore   )
                return 0;
        else if( schedule.getHeuristicScore() < this.heuristicScore   )
                return -1;
        return 1;
    }
}
