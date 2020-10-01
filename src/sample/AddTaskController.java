package sample;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.model.DataSource;


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
        if (details.isEmpty() && shortDescription.isEmpty())
        {
            return;
        }
        String date;
        if (toggleDate.isSelected() || (deadlinePicker.getValue() == null))
        date = "none";
        else
            date = deadlinePicker.getValue().toString();

        DataSource.getInstance().addTask(shortDescription,details,date);
    }

    @FXML
    public void setToggleDate()
    {
        deadlinePicker.setDisable(toggleDate.isSelected());
    }
}
