package model;


import java.sql.Date;
import java.util.Objects;
import java.util.Observable;

public class Activity extends Observable{
    private Long id;
    private Long userId;
    private String operation;
    private Date timeStamp;

    public Activity(){

    }

    public Activity(String operation, Date timeStamp) {
        this.operation = operation;
        this.timeStamp = timeStamp;
    }

    public String getOperation() {
        return operation;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public Long getUserId() {

        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOperationAndTimeStamp(String operation, Date timeStamp){
        this.operation = operation;
        this.timeStamp = timeStamp;
        setChanged();
        notifyObservers();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return Objects.equals(id, activity.id) &&
                Objects.equals(operation, activity.operation) &&
                Objects.equals(timeStamp, activity.timeStamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, operation, timeStamp);
    }
}
