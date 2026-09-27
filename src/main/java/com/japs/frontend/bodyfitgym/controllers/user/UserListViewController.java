/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.japs.frontend.bodyfitgym.controllers.user;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.japs.frontend.bodyfitgym.controllers.main.MainViewController;
import com.japs.frontend.bodyfitgym.models.User;
import com.japs.frontend.bodyfitgym.response.PageResponse;
import com.japs.frontend.bodyfitgym.response.ServiceResponse;
import com.japs.frontend.bodyfitgym.services.UserService;
import com.japs.frontend.bodyfitgym.utils.AlertUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * FXML Controller class
 *
 * @author carde
 */
public class UserListViewController implements Initializable {
    @FXML
    private TableColumn<User, String> userColumn;
    @FXML
    private TableColumn<User, String> documentColumn;
    @FXML
    private TableColumn<User, String> lastNameColumn;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, String> statusColumn;
    @FXML
    private TableView<User> userTable;
    private final ObservableList<User> userList = FXCollections.observableArrayList();
    private final UserService userService = new UserService();

    private String currentName = null;
    private String currentUserName = null;
    private String currentDocument = null;
    @FXML
    private FontIcon iconSearch;
    @FXML
    private TextField fieldSearch;
    @FXML
    private Pagination pagination;
    @FXML
    private TableColumn<?, ?> actionsColumn;
    @FXML
    private ComboBox<String> filterComboBox;

    /**
     * Initializes the controller class.
     */

    private void configureContextMenuStyle() {
        ContextMenu contextMenu = userTable.getContextMenu();

        if (contextMenu != null) {
            contextMenu.setOnShowing(event -> {
                String css = getClass().getResource("/css/user/TableUser.css").toExternalForm();
                if (!contextMenu.getScene().getStylesheets().contains(css)) {
                    contextMenu.getScene().getStylesheets().add(css);
                }
            });
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pagination.currentPageIndexProperty().addListener((observable)->{
            listUsers(currentName,currentUserName,currentDocument,pagination.getCurrentPageIndex());
        });
        configureColumns();
        configureContextMenuStyle();
        listUsers(null,null,null,0);
        configureRowFactory();
    }
    private void configureColumns(){
        userColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        documentColumn.setCellValueFactory(new PropertyValueFactory<>("document"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setVisible(false);
    }
    private void listUsers(String name,String userName,String document,int page){
        userList.clear();

        ServiceResponse<PageResponse<User>> serviceResponse = userService.searchUser(name,userName,document,page);
        System.out.println(serviceResponse);
        if(serviceResponse.getCode() == 200){
            PageResponse pageResponse = serviceResponse.getData();
            userList.addAll(pageResponse.getContent());
            userTable.setItems(userList);
            pagination.setPageCount(pageResponse.getTotalPages());
        }else{
            userTable.setItems(userList);
            pagination.setPageCount(1);
        }

    }

    @FXML
    private void searchUser(ActionEvent event) {
        String searchCriteria = filterComboBox.getSelectionModel().getSelectedItem();

        switch (searchCriteria){
            case null -> {
                showMessage("Seleccione un parametro de busqueda","Error",Alert.AlertType.ERROR);
            }
            case "Nombre" ->{
                currentName = fieldSearch.getText();
                currentUserName = null;
                currentDocument = null;
                listUsers(currentName,currentUserName,currentDocument,0);
            }
            case "Usuario" ->{
                currentName = null;
                currentUserName =  fieldSearch.getText();
                currentDocument = null;
                listUsers(currentName,currentUserName,currentDocument,0);
            }
            case "Documento" ->{
                currentName = null;
                currentUserName = null;
                currentDocument =  fieldSearch.getText();
                listUsers(currentName,currentUserName,currentDocument,0);
            }
            default -> {}
        }
    }
    private void showMessage(String contentText,String tittle,Alert.AlertType tipo){
        Alert alert = new Alert (tipo) ;
        alert.setHeaderText(null) ;
        alert.setTitle(tittle);
        alert.setContentText(contentText);
        alert.showAndWait ();
    }


    @FXML
    private void showUserCreateView(ActionEvent event) {
        MainViewController.getInstance().cargarVista("/templates/user/UserCreateView.fxml");
    }

    @FXML
    private void mirarId(ActionEvent event) {
        User seleccionado = userTable.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            System.out.println(seleccionado.getId());
        } else {
            AlertUtils.showConfirm("No se pudo eliminar el usuario");
        }
    }

    private void configureRowFactory() {
        userTable.setRowFactory(tv -> {
            TableRow<User> fila = new TableRow<>();

            fila.setOnContextMenuRequested(event -> {
                if (!fila.isEmpty()) {
                    userTable.getSelectionModel().select(fila.getIndex());
                }
            });

            return fila;
        });
    }
}
