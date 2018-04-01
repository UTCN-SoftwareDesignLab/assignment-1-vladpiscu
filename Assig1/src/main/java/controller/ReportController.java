package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import model.Activity;
import service.ReportWriter;
import service.activity.ActivityService;

import java.sql.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReportController implements Observer {
    @FXML
    private Button backButton;
    @FXML
    private Button generateReportButton;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;


    private ActivityService activityService;
    private Activity activity;
    private FXMLLoader adminLoader;
    private Long userId;
    private ReportWriter reportWriter;

    public ReportController(ActivityService activityService, Activity activity, FXMLLoader adminLoader, ReportWriter reportWriter) {
        this.activityService = activityService;
        this.activity = activity;
        this.adminLoader = adminLoader;
        this.reportWriter = reportWriter;
        activity.addObserver(this);
    }

    public void setUserId(Long userId){
        this.userId = userId;
    }

    @Override
    public void update(Observable o, Object arg) {
        activityService.save(activity);
    }

    @FXML
    private void generateReportHandler(ActionEvent e){
        if(startDate.getValue() != null && endDate.getValue() != null){
            if(startDate.getValue().compareTo(endDate.getValue()) < 0){
                List<Activity> activities = activityService.getActivityforPeriod(userId,
                        Date.valueOf(startDate.getValue()), Date.valueOf(endDate.getValue()));
                reportWriter.write(activities.stream().map(a -> a.getOperation() + " at time " + a.getTimeStamp().toString()).collect(Collectors.toList()));
            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Operation unavailable");
                alert.setContentText("Please select the start date and end date. The start date should be before the end date.");
                alert.showAndWait();
            }
        }

    }

    @FXML
    private void backHandler(ActionEvent e){
        Scene scene = backButton.getScene();
        scene.setRoot(adminLoader.getRoot());
    }
}
