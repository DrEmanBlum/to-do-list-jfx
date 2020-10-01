package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import sample.model.DataSource;
import sample.model.Task;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

public class Controller {


    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private TableView<Task> taskTable;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private ContextMenu listContextMenu;

    @FXML
    private TextArea itemDetailsTextArea;

    @FXML
    private Label deadlineLabel;

    @FXML
    private HBox deadLineHBox;

    //Similar to Android's onCreate()
    public void initialize()
    {
        //Creates a new ContextMenu (Window when right clicking)
        listContextMenu = new ContextMenu();
        //Adds a new button for the context menu
        MenuItem deleteMenuItem = new MenuItem("Delete");
        MenuItem editMenuItem = new MenuItem("Edit");
        //Creates an Event handler for the delete button
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                deleteTask();
            }
        });

        listContextMenu.getItems().addAll(deleteMenuItem);
        taskTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Task>() {
            @Override
            public void changed(ObservableValue<? extends Task> observableValue, Task task, Task t1) {
                Task item = taskTable.getSelectionModel().getSelectedItem();
                itemDetailsTextArea.setText(item.getTaskDescription());
                LocalDate deadLine = item.getEndDate();
                if (!deadLine.equals(LocalDate.MAX)) {
                    deadLineHBox.setVisible(true);
                    deadlineLabel.setText(item.getEndDate().toString());
                }
                else
                {
                    deadLineHBox.setVisible(false);
                }
            }
        });

        taskTable.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.SECONDARY))
                {
                    listContextMenu.show(taskTable,mouseEvent.getScreenX(),mouseEvent.getScreenY());
                }
            }
        });
        taskTable.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY))
                {
                    listContextMenu.hide();
                }
            }
        });




        taskTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        deadLineHBox.setVisible(false);


    }
    //Button to list all tasks
    @FXML
    public void listTasks()
    {
        javafx.concurrent.Task<ObservableList<Task>> threadTask = new GetAllTasks();
            taskTable.itemsProperty().bind(threadTask.valueProperty());
            progressBar.progressProperty().bind(threadTask.progressProperty());
            progressBar.setVisible(true);
            threadTask.setOnSucceeded((e) -> progressBar.setVisible(false));
            threadTask.setOnFailed((e) -> progressBar.setVisible(false));

            new Thread(threadTask).start();


    }


    //Button to delete task
    //Todo figure out why delete button crashes listTasks method
    @FXML
    public void deleteTask()
    {
        listContextMenu.hide();
        Task item = taskTable.getSelectionModel().getSelectedItem();
        if (!(item == null)) {
            DataSource.getInstance().deleteTask(item);
//            listTasks();
        }
    }

    //Button to add a new task to the sql
    //Function opens new window to add variables
    @FXML
    public void addTask()
    {
        //Create new window for AddTask view
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add new Task");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("window_add_task.fxml"));
        try{
            //JavaFX has a bug where opening a new window on Linux
            //forces it into the size of one pixel.
            if (System.getProperty("os.name").equals("Linux"))
            {
                dialog.setResizable(true);
            }
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e)
        {
            System.out.println("Error opening Window " + e.getMessage());
            return;
        }

        //Adds ok and cancel button to the add task window
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        //Wait for Input
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() ==ButtonType.OK)
        {
            //Run proccessResult() method from AddTaskController class and refresh the list
            AddTaskController controller = fxmlLoader.getController();
            controller.processResults();
            listTasks();
        }

    }
}


class GetAllTasks extends javafx.concurrent.Task<ObservableList<Task>>
{
    @Override
    protected ObservableList<Task> call() throws Exception {
            return FXCollections.observableArrayList(DataSource.getInstance().ListAllTasks());
    }
}