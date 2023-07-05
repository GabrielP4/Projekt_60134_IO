package com.example.projekt_60134_io;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    @FXML
    private Button btn_logout;
    @FXML
    public TableView<taskData> tv_task_list;
    @FXML
    public TableColumn<taskData,Integer> tc_task_nr;
    @FXML
    public TableColumn<taskData,String> tc_task_des;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    public ObservableList<taskData> taskListData(){
        ObservableList<taskData> listData = FXCollections.observableArrayList();
        String selectData = "SELECT * FROM tasks";

        connection = DBUtils.connect();

        try{
            preparedStatement = connection.prepareStatement(selectData);
            resultSet = preparedStatement.executeQuery();

            taskData tData;
            while (resultSet.next()){
                tData = new taskData(
                        resultSet.getInt("task_number"),
                        resultSet.getString("task_des")
                );


                listData.add(tData);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listData;
    }
    private ObservableList<taskData> taskData;

    public void taskShowData(){
        taskData = taskListData();
        tc_task_nr.setCellValueFactory(new PropertyValueFactory<>("taskNumber"));
        tc_task_des.setCellValueFactory(new PropertyValueFactory<>("taskDes"));

        tv_task_list.setItems(taskData);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        taskShowData();
        btn_logout.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "first-page.fxml", "Zaloguj się");
            }
        });
    }
}
