/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.gui.programinventaris;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Ichinomiya Mori
 */
public class InventarisManajemenController implements Initializable {
//    Initialize Database
    private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private final String DB_URL = "jdbc:mysql://localhost/jinventaris";
    private final String USER = "root";
    private final String PASS = "root1234";

    // Creating an Object for MySQL Database
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;
    
    @FXML
    private TextField namaBarang; 
    @FXML
    private TextField jumlahBarang;
    @FXML 
    private TableView<BarangModel> tbview;
    @FXML
    private TableColumn<BarangModel, Integer> tcIdBarang;
    @FXML
    private TableColumn<BarangModel,String> tcNama;
    @FXML
    private TableColumn<BarangModel,Integer> tcJumlah;
    @FXML
    private Button btnTambah;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnHapus;
    @FXML
    private Button clearSelectButton;
    
//    Reactive Model
    private TableViewSelectionModel selectionModel;
    private ObservableList<BarangModel> selectedItems;
    private int id_barang;
    private String query;
    
    
    public InventarisManajemenController(){
        try{
            // register driver yang akan dipakai
            Class.forName(JDBC_DRIVER);
            // buat koneksi ke database
            this.conn = DriverManager.getConnection(DB_URL, USER, PASS);
            // buat objek statement
            this.stmt = this.conn.createStatement();
            
        }catch(ClassNotFoundException | SQLException e){
            e.getMessage();
        }
    }
//    go to menu awal
    @FXML
    public void gotoMenu() throws IOException{
        App.setRoot("InventarisApp");
    }
//    nambah data
    @FXML
    public void nampilin() throws IOException{
        System.out.println(namaBarang.getText());
        System.out.println(jumlahBarang.getText());
        this.query = "INSERT INTO tb_barang VALUES(NULL,'"+namaBarang.getText()+"','"+jumlahBarang.getText()+"')";

        try{
            this.stmt.execute(this.query);
            sync();
            System.out.println("Query Success : Data Inserted"); 
        }catch(SQLException e){
            System.err.println(e);
        }
    }
//    update data
    @FXML
    public void update(){
        System.out.println(this.id_barang);
        System.out.println(namaBarang.getText());
        System.out.println(jumlahBarang.getText());
        this.query = "UPDATE tb_barang SET nama = '"+namaBarang.getText()+"', jumlah = '"+jumlahBarang.getText()+"' WHERE id_barang = '"+this.id_barang+"'";
        try{
            this.stmt.execute(this.query);
            sync();
            System.out.println("Query Success : Data Updated"); 
        }catch(SQLException e){
            System.err.println(e);
        }
    }
//    hapus data
    @FXML
    public void delete(){
        System.out.println(this.id_barang);
        System.out.println(namaBarang.getText());
        System.out.println(jumlahBarang.getText());
        this.query = "DELETE FROM tb_barang WHERE id_barang = '"+this.id_barang+"'";
        try{
            this.stmt.execute(this.query);
            sync();
            System.out.println("Query Success : Data Updated"); 
        }catch(SQLException e){
            System.err.println(e);
        }
    }
//    sinkronisasi data
    @FXML
    public void sync(){
        Platform.runLater(()->{
            tbview.getItems().clear();
            clearSelection();
            try{
                this.rs = this.stmt.executeQuery("SELECT * FROM tb_barang");
                while(rs.next()){
                    tbview.getItems().add(new BarangModel(rs.getInt("id_barang"), rs.getString("nama"), rs.getInt("jumlah")));
                }
            }catch(SQLException e){
                System.err.println(e.getSQLState());
            }
            
        });
    }
//    hapus semua pilihan
    @FXML
    public void clearSelection(){
        System.out.println("Clear Selection");
        this.id_barang = 0;
        namaBarang.setText("");
        jumlahBarang.setText("");
        btnTambah.setDisable(false);
        btnEdit.setDisable(true);
        btnHapus.setDisable(true);
        clearSelectButton.setDisable(true);
        selectedItems.clear();
        selectionModel.clearSelection();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        jumlahBarang.textProperty().addListener((ov, oldValue, newValue) -> {
//            All values except  number will be replaced
            jumlahBarang.setText(newValue.replaceAll("[^0-9$]", ""));
        });
        try{
//            Showing table
            this.rs = this.stmt.executeQuery("SELECT * FROM tb_barang");
//            Set Collumn 
            tcIdBarang.setCellValueFactory((p) -> {
                ObservableValue<Integer> obsInt = new SimpleIntegerProperty(p.getValue().getIdBarang()).asObject();
                return obsInt; 
            });
            tcNama.setCellValueFactory((p) -> {
                ObservableValue<String> obsStr = new SimpleStringProperty(p.getValue().getNama());
                return obsStr; 
            });
            tcJumlah.setCellValueFactory((p) -> {
                ObservableValue<Integer> obsInt = new SimpleIntegerProperty(p.getValue().getJumlah()).asObject();
                return obsInt;
            });
            while(rs.next()){
                tbview.getItems().add(new BarangModel(rs.getInt("id_barang"), rs.getString("nama"), rs.getInt("jumlah")));
            }
            
//            Inisialisasi single item table
            selectionModel = tbview.getSelectionModel();
            selectionModel.setSelectionMode(SelectionMode.SINGLE);
            
//            Get selected item
            selectedItems = selectionModel.getSelectedItems();
            
//            Add listener => get item 
            selectedItems.addListener((Change<? extends BarangModel> change) -> {
                namaBarang.setText(change.getList().get(0).getNama());
                jumlahBarang.setText(""+change.getList().get(0).getJumlah());
                this.id_barang = change.getList().get(0).getIdBarang();
                if(btnEdit.isDisabled() == true && btnHapus.isDisabled() == true && btnTambah.isDisabled() == false){
                    btnTambah.setDisable(true);
                    btnEdit.setDisable(false);
                    btnHapus.setDisable(false);
                    clearSelectButton.setDisable(false);
                }
            });
        }catch(SQLException e){
            e.getMessage();
        }
    }
    
}
