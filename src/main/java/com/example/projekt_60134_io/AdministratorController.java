package com.example.projekt_60134_io;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdministratorController implements Initializable {
    @FXML
    private Button btn_sign_up;
    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_userfirstname;
    @FXML
    private TextField tf_userlastname;
    @FXML
    private TextField tf_password;
    @FXML
    private TextField tf_email;
    @FXML
    private CheckBox cb_administrator;
    @FXML
    private Button btn_logout;
    @FXML
    public TextField tf_des;
    @FXML
    public TextField tf_nr;
    @FXML
    public TableView<taskData> tv_task_list;
    @FXML
    public TableColumn<taskData,Integer> tc_task_nr;
    @FXML
    public TableColumn<taskData,String> tc_task_des;

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private Alert alert;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        taskShowData();
        btn_sign_up.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (!tf_username.getText().trim().isEmpty() &&
                        !tf_userfirstname.getText().trim().isEmpty() &&
                        !tf_userlastname.getText().trim().isEmpty() &&
                        !tf_password.getText().trim().isEmpty() &&
                        !tf_email.getText().trim().isEmpty()) {
                    DBUtils.signUpUser(actionEvent,
                            tf_username.getText(),
                            tf_userfirstname.getText(),
                            tf_userlastname.getText(),
                            tf_password.getText(),
                            tf_email.getText(),
                            cb_administrator.isSelected()
                    );
                } else {
                    System.out.println("Uzupełnij wszystkie dane");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Błąd");
                    alert.setContentText("Uzupełnij wszystkie dane");
                    alert.show();
                }
            }
        });
        btn_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "first-page.fxml", "Zaloguj się");
            }
        });
    }
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
    public void taskSelectData(MouseEvent mouseEvent) {
        taskData sData = tv_task_list.getSelectionModel().getSelectedItem();
        int num = tv_task_list.getSelectionModel().getSelectedIndex();

        if((num-1)<-1)return;

        tf_nr.setText(String.valueOf(sData.getTaskNumber()));
        tf_des.setText(sData.getTaskDes());
    }

    public void btn_OA_Add(ActionEvent actionEvent) {
        connection = DBUtils.connect();

        try{
            if (tf_des.getText().isEmpty()||tf_nr.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Podaj dane");
                alert.showAndWait();
            }else {
                String chceckData = "SELECT task_number FROM tasks WHERE task_number = "+tf_nr.getText();
                preparedStatement = connection.prepareStatement(chceckData);
                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Zadanie z numerem: "+tf_nr.getText()+" już istnieje");
                    alert.showAndWait();
                }else {
                    String insertData = "INSERT INTO tasks (task_number, task_des) VALUES(?,?)";
                    preparedStatement = connection.prepareStatement(insertData);
                    preparedStatement.setString(1,tf_nr.getText());
                    preparedStatement.setString(2,tf_des.getText());


                    preparedStatement.executeUpdate();


                    taskShowData();
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Dodano");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void btn_OA_edit(ActionEvent actionEvent) {
        connection = DBUtils.connect();
        try {
            if (tf_des.getText().isEmpty()||tf_nr.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Podaj dane");
                alert.showAndWait();
            }else {
                String updateData = "UPDATE tasks SET "
                        + "task_des = '" + tf_des.getText()
                        + "' WHERE task_number = " + tf_nr.getText();
                preparedStatement = connection.prepareStatement(updateData);


                preparedStatement.executeUpdate();


                taskShowData();
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Edytowano");
                alert.showAndWait();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void btn_OA_del(ActionEvent actionEvent) {
        connection = DBUtils.connect();
        int num = tv_task_list.getSelectionModel().getSelectedIndex()+1;
        try {
            if (tf_nr.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Podaj dane");
                alert.showAndWait();
            }else {
                String dleteData = "DELETE FROM tasks WHERE task_number = " + tf_nr.getText();
                preparedStatement = connection.prepareStatement(dleteData);


                preparedStatement.executeUpdate();


                taskShowData();
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Usuniéto");
                alert.showAndWait();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
