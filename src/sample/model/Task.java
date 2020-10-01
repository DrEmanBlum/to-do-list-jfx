package sample.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class Task {

    private SimpleIntegerProperty id;
    private SimpleStringProperty taskDescription;
    private SimpleStringProperty taskName;
    private SimpleBooleanProperty isDone;

    private LocalDate endDate;

    public Task() {
        this.id = new SimpleIntegerProperty();
        this.taskDescription = new SimpleStringProperty();
        this.taskName = new SimpleStringProperty();
        this.isDone = new SimpleBooleanProperty();
    }

    public Task(String taskDescription, String taskName, LocalDate endDate) {
        this.taskDescription = new SimpleStringProperty();
        this.taskDescription.set(taskDescription);
        this.taskName = new SimpleStringProperty();
        this.taskName.set(taskName);
        this.isDone = new SimpleBooleanProperty();
        this.isDone.set(false);
        this.endDate = endDate;
    }

    public String getTaskName() {
        return taskName.get();
    }

    public void setTaskName(String taskName) {
        this.taskName.set(taskName);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getTaskDescription() {
        return taskDescription.get();
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription.set(taskDescription);
    }

    public boolean isDone() {
        return isDone.get();
    }

    public void setDone(boolean done) {
        isDone.set(done);
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


}
