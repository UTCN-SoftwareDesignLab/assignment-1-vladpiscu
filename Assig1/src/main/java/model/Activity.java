package model;

import org.joda.time.DateTime;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Objects;

public class Activity {
    private Long id;
    private String operation;
    private Date timeStamp;

    public Activity(String operation, Date timeStamp) {
        this.id = id;
        this.operation = operation;
        this.timeStamp = timeStamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
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
