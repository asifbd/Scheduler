
package scheduler.components;


public enum TimeSlots {
    HOUR_9_11, 
    HOUR_11_13, 
    HOUR_13_15, 
    HOUR_15_17, 
    HOUR_17_19, 
    HOUR_19_21, 
    NULL_HOUR ;
    
    /** 
    * @return the option represented
    */
    public String option() {
          return this.name().toLowerCase();
    }
}