/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se2203b.assignments.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se2203b.assignments.service.UserAccountService;

/**
 * FXML Controller class
 *
 * @author Abdelkader
 */
@Component
public class AboutController {

    @FXML
    Button okBtn;

    private final UserAccountService userAccountService ;

    @Autowired
    public AboutController(UserAccountService service) {
        this.userAccountService = service;
    }

    public void exit() {
        Stage stage = (Stage) okBtn.getScene().getWindow();
        stage.close();
    }

}
