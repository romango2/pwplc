package com.cfm.pwplc.controllers;

import com.cfm.pwplc.service.PowerPointEditService;
import com.cfm.pwplc.service.SongNamesListCreator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller {
    private final SongNamesListCreator listCreatorService = new SongNamesListCreator();

    PowerPointEditService editService = new PowerPointEditService();

    @FXML
    private Button createListButton;

    @FXML
    private TextField praiseOne;
    @FXML
    private TextField praiseTwo;
    @FXML
    private TextField praiseThree;

    @FXML
    private TextField worshipOne;
    @FXML
    private TextField worshipTwo;

    @FXML
    private TextField praiseFour;
    @FXML
    private TextField praiseFive;

    @FXML
    private TextField worshipAltarCall;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void createList(ActionEvent actionEvent) throws IOException {

//        createListButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent e) {
        if ((praiseOne.getText() != null && praiseOne.getText().matches("^[1-9]?[0-9]{1}$|^100$")) &&
                (praiseTwo.getText() != null && praiseTwo.getText().matches("^[1-9]?[0-9]{1}$|^100$")) &&
                (praiseThree.getText() != null && praiseThree.getText().matches("^[1-9]?[0-9]{1}$|^100$")) &&
                (worshipOne.getText() != null && worshipOne.getText().matches("^[1-9]?[0-9]{1}$|^100$")) &&
                (worshipTwo.getText() != null && worshipTwo.getText().matches("^[1-9]?[0-9]{1}$|^100$")) &&
                (praiseFour.getText() != null && praiseFour.getText().matches("^[1-9]?[0-9]{1}$|^100$")) &&
                (praiseFive.getText() != null && praiseFive.getText().matches("^[1-9]?[0-9]{1}$|^100$"))
//                        &&  (worshipAltarCall.getText() != null && worshipAltarCall.getText().matches("^[1-9]?[0-9]{1}$|^100$"))
        ) {

            //ADD CODE TO ADD THE ITEM HERE!
            List<String> songsList = listCreatorService.getSongNamesFromFile(createSongsNumbersList());
//
            try {
                editService.createPowerPointFile(songsList);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.set
            alert.setTitle("Invalid data");
            alert.setContentText("Please fill in valid song numbers");

            alert.showAndWait();
        }
//            }
//        });

    }

    private List<String> createSongsNumbersList() {
        List<String> songsNumbersList = new ArrayList<>();

        songsNumbersList.add(praiseOne.getText());
        songsNumbersList.add(praiseTwo.getText());
        songsNumbersList.add(praiseThree.getText());
        songsNumbersList.add(worshipOne.getText());
        songsNumbersList.add(worshipTwo.getText());
        songsNumbersList.add(praiseFour.getText());
        songsNumbersList.add(praiseFive.getText());
        if ((worshipAltarCall.getText() != null && worshipAltarCall.getText().matches("^[1-9]?[0-9]{1}$|^100$"))) {
            songsNumbersList.add(worshipAltarCall.getText());
        }

        return songsNumbersList;


    }


}
