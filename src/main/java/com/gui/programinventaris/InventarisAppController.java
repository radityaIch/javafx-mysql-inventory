/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.gui.programinventaris;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Ichinomiya Mori
 */
public class InventarisAppController {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    public void gotoManajemen() throws IOException{
        
        App.setRoot("InventarisManajemen");
    }
    
    @FXML
    public void gotoPeminjaman() throws IOException{
        App.setRoot("InventarisPeminjaman");
    }
    
}
