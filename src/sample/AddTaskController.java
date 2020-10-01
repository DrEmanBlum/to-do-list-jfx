package sample;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.model.DataSource;
import sample.model.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AddTaskController {


    @FXML
    private TextField shortDescriptionField;

    @FXML
    private TextArea detailsArea;

    @FXML
    private DatePicker deadlinePicker;

    @FXML
    private CheckBox toggleDate;

    public void processResults() {
        String shortDescription = shortDescriptionField.getText().trim();
        String details = detailsArea.getText().trim();
        String date;
        if (toggleDate.isSelected())
        date = "none";
        else
            date = deadlinePicker.getValue().toString();

        DataSource.getInstance().addTask(shortDescription,details,date);
    }

    @FXML
    public void setToggleDate()
    {
        if (toggleDate.isSelected())
        {
            deadlinePicker.setDisable(true);
        }
        else
        {
            deadlinePicker.setDisable(false);
        }
    }
}
