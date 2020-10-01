package sample.model;

import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DataSource {

    public static final String DB_NAME = "task.db";
    public static final String CONNECTION_STRING ="jdbc:sqlite:" + Path.of(DataSource.DB_NAME).toAbsolutePath();

    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_TASK_ID = "_id";
    public static final String COLUMN_TASK_DESCRIPTION = "description";
    public static final String COLUMN_TASK_ISDONE = "is_done";
    public static final String COLUMN_TASK_ENDDATE = "end_date";
    public static final String COLUMN_TASK_TASKNAME = "name";
    public static final int INDEX_TASK_ID = 1;
    public static final int INDEX_TASK_DESCRIPTION = 2;
    public static final int INDEX_TASK_ISDONE = 3;
    public static final int INDEX_TASK_ENDDATE = 4;
    public static final int INDEX_TASK_TASKNAME = 5;


    public static final String CREATE_TABLE_TASKS = "CREATE TABLE IF NOT EXISTS " + TABLE_TASKS + " ( " + COLUMN_TASK_ID + " INTEGER PRIMARY KEY, " + COLUMN_TASK_DESCRIPTION + " TEXT, " + COLUMN_TASK_ISDONE + " TEXT, " + COLUMN_TASK_ENDDATE + " TEXT, " + COLUMN_TASK_TASKNAME + " TEXT)";
    //INSERT INTO tasks(description, is_done, end_date) VALUES ('get milk', 'true', '1999-02-11')
    public static final String INSERT_TASK_STRING = "INSERT INTO " +TABLE_TASKS+ " ( " +COLUMN_TASK_DESCRIPTION + ',' +COLUMN_TASK_ISDONE + ',' + COLUMN_TASK_ENDDATE + ',' + COLUMN_TASK_TASKNAME + " ) VALUES ( ? ,?, ?, ?)";
    //DELETE FROM tasks WHERE _id = ?
    public static final String DELETE_TASK_STRING = "DELETE FROM " +TABLE_TASKS+ " WHERE " + COLUMN_TASK_ID + " = ";
    //SELECT * FROM tasks ORDER BY _id
    public static final String QUERY_TASKS_STRING = "SELECT * FROM " +TABLE_TASKS+ " ORDER BY " + COLUMN_TASK_ID;


    private Connection connection;
    private PreparedStatement insertTaskStatement;
    private static final DataSource instance = new DataSource();
    private DataSource()
    {

    }

    public static DataSource getInstance() {
        return instance;
    }

    public boolean open()
    {
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);
            Statement statement = connection.createStatement();
            statement.execute(CREATE_TABLE_TASKS);
            statement.close();
            insertTaskStatement = connection.prepareStatement(INSERT_TASK_STRING);
            return true;
        }catch (SQLException e)
        {
            System.out.println("Error opening DB " + e.getMessage() );
            return false;
        }
    }
    public boolean close()
    {
        try {
            if (connection != null)
            {
                connection.close();
            }
            if (insertTaskStatement != null)
            {
                insertTaskStatement.close();
            }
            return true;
        }catch (SQLException e)
        {
            System.out.println("Error closing DB " + e.getMessage() );
            return false;
        }
    }

    public void addTask(String description, String name, String date )
    {
        try {
            insertTaskStatement.setString(1, description);
            insertTaskStatement.setString(2,"false");
            insertTaskStatement.setString(3,date);
            insertTaskStatement.setString(4,name);
            insertTaskStatement.execute();
        }catch (SQLException e)
        {
            System.out.println("Error inserting to DB " + e.getMessage() );
        }
    }

    public void deleteTask(Task task)
    {
        try (Statement statement = connection.createStatement()){
            statement.execute(DELETE_TASK_STRING + task.getId());
        }
        catch (SQLException e)
        {
            System.out.println("Error deleting from DB " + e.getMessage() );
        }
    }

    public List<Task> ListAllTasks()
    {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(QUERY_TASKS_STRING))
        {
            List<Task> tasks = new ArrayList<>();
            while (resultSet.next())
            {
                Task t = new Task();
                //Get task id
                t.setId(resultSet.getInt(INDEX_TASK_ID));
                //Get task description title
                t.setTaskDescription(resultSet.getString(INDEX_TASK_DESCRIPTION));
                //Get if task has been done
                if (resultSet.getString(INDEX_TASK_ISDONE).equals("true"))
                {
                    t.setDone(true);
                }
                else
                {
                    t.setDone(false);
                }
                //Get task name
                t.setTaskName(resultSet.getString(INDEX_TASK_TASKNAME));
                //Get task end date
                String date = resultSet.getString(INDEX_TASK_ENDDATE);
                if (!date.equals("none")) {
                    LocalDate dateTime = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    t.setEndDate(dateTime);
                }
                else{
                    t.setEndDate(LocalDate.MAX);
                }

                tasks.add(t);
            }
            return tasks;
        }catch (SQLException e)
        {
            System.out.println("Error querying DB " + e.getMessage() );
            return null;
        }
    }
}
