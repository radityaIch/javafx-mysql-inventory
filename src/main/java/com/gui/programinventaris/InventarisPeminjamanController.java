/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.gui.programinventaris;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;


//Import SQL

/**
 * FXML Controller class
 *
 * @author Ichinomiya Mori
 */
public class InventarisPeminjamanController implements Initializable {
    private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private final String DB_URL = "jdbc:mysql://localhost/jinventaris";
    private final String USER = "root";
    private final String PASS = "root1234";

    // Menyiapkan objek yang diperlukan untuk mengelola database
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;
    
    @FXML
    private TextField namaPeminjam;
    @FXML
    private TextField no_telp;
    @FXML
    private ComboBox<String> listBarang;
    @FXML
    private TextField jumlahBarang;
    @FXML
    private TableView<PeminjamanModel> tvPinjam;
    @FXML
    private TableColumn<PeminjamanModel, Integer> tcIdPeminjaman;
    @FXML
    private TableColumn<PeminjamanModel, String> tcPeminjam;
    @FXML
    private TableColumn<PeminjamanModel, String> tcNoTelp;
    @FXML
    private TableColumn<PeminjamanModel, String> tcBarang;
    @FXML
    private TableColumn<PeminjamanModel, Integer> tcJumlah;
    @FXML
    private Button btnPinjam;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnKembali;
    @FXML
    private Button clearSelectButton;
    @FXML
    private Label errorLabel;
    
    
    private int id_barang, id_peminjaman;
    private String query;
    private ObservableList<String> observableList = FXCollections.observableArrayList();
    private TableView.TableViewSelectionModel selectionModel;
    private ObservableList<PeminjamanModel> selectedItems;
    
    public InventarisPeminjamanController(){
        try{
            Class.forName(JDBC_DRIVER);
            // buat koneksi ke database
            this.conn = DriverManager.getConnection(DB_URL, USER, PASS);
            // buat objek statement
            this.stmt = this.conn.createStatement();
        }catch(ClassNotFoundException | SQLException e){
            e.getMessage();
        }
    }
    
    /**
     * Initializes the controller class.
     * @throws java.io.IOException
     */
    
    @FXML
    public void gotoMenu() throws IOException{
        App.setRoot("InventarisApp");
    }
//    Proses Pinjam Barang
    @FXML
    public void pinjam() throws SQLException{
//        Create Id barang
        String namaBarang = listBarang.getValue();
        int separator = namaBarang.indexOf("-");
        this.id_barang = Integer.parseInt(namaBarang.substring(0,separator));
        this.query = "SELECT * FROM tb_barang WHERE id_barang = "+this.id_barang+"";
        this.rs = this.stmt.executeQuery(this.query);
        this.rs.next();
        if(rs.getInt("jumlah") < Integer.parseInt(jumlahBarang.getText())){
            System.out.println("Stok barang tersisa : "+rs.getInt("jumlah"));
            return;
        }
        
        this.query = "INSERT INTO tb_peminjaman VALUES(NULL, '"+namaPeminjam.getText()+"', '"+no_telp.getText()+"', "+this.id_barang+", '"+jumlahBarang.getText()+"')";
        this.stmt.execute(this.query);
        System.out.println("Query Success : Data Inserted");
        sync();
    }
//    Proses Kembali Barang
    @FXML
    public void kembali() throws SQLException{
        System.out.println(this.id_peminjaman);
        this.query = "DELETE FROM tb_peminjaman WHERE id_peminjaman = '"+this.id_peminjaman+"'";
        this.stmt.execute(this.query);
        System.out.println("Query Success : Data Deleted");
        sync();
    }
//    Proses Update
    @FXML
    public void update() throws SQLException{
        this.query = "SELECT * FROM tb_barang WHERE id_barang = "+this.id_barang+"";
        this.rs = this.stmt.executeQuery(this.query);
        this.rs.next();
        if(rs.getInt("jumlah") < Integer.parseInt(jumlahBarang.getText())){
            System.out.println("Stok barang tersisa : "+rs.getInt("jumlah"));
            clearSelection();
            return;
        }
        this.query = "UPDATE tb_peminjaman SET nama = '"+namaPeminjam.getText()+"'"
                + ", no_telp = '"+no_telp.getText()+"', jumlah = '"+jumlahBarang.getText()+""
                + "' WHERE id_peminjaman = '"+this.id_peminjaman+"'";
        this.stmt.execute(this.query);
        System.out.println("Query Success : Data Updated");
        sync();
    }
//    Sinkronisasi data
    @FXML
    public void sync(){
        Platform.runLater(()->{
            tvPinjam.getItems().clear();
            listBarang.getItems().clear();
            clearSelection();
            try{
                this.rs = this.stmt.executeQuery("SELECT id_peminjaman, tb_peminjaman.nama AS nama_peminjam, "
                    + "no_telp, tb_peminjaman.id_barang, tb_barang.nama AS nama_barang, "
                    + "tb_peminjaman.jumlah FROM tb_barang INNER JOIN tb_peminjaman ON "
                    + "tb_barang.id_barang = tb_peminjaman.id_barang");
                while(rs.next()){
                    tvPinjam.getItems().add(new PeminjamanModel(rs.getInt("id_peminjaman"), rs.getString("nama_peminjam"), rs.getString("no_telp"), rs.getInt("id_barang"), rs.getString("nama_barang"), rs.getInt("jumlah")));
                }
                
                this.rs = this.stmt.executeQuery("SELECT * FROM tb_barang");
                while(this.rs.next()){
                    observableList.add(rs.getInt("id_barang") + "-" + rs.getString("nama") +"(Stok : "+rs.getInt("jumlah")+")");
                }
            listBarang.setItems(observableList);
            }catch(SQLException e){
                System.err.println(e.getSQLState());
            }
            
            
        });
    }
//    Hapus Pilihan
    @FXML
    public void clearSelection(){
        System.out.println("Clear Selection");
        this.id_barang = 0;
        this.id_peminjaman = 0;
        namaPeminjam.setText("");
        no_telp.setText("");
        jumlahBarang.setText("");
        btnPinjam.setDisable(false);
        btnUpdate.setDisable(true);
        btnKembali.setDisable(true);
        listBarang.setDisable(false);
        clearSelectButton.setDisable(true);
        selectionModel.clearSelection();
    }
//    Inisialisasi Komponen
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        Mencegah inputan string
        jumlahBarang.textProperty().addListener((ov, oldValue, newValue) -> {
            jumlahBarang.setText(newValue.replaceAll("[^0-9$]", ""));
        });
//        Setting kolom Peminjaman
        tcIdPeminjaman.setCellValueFactory((p) -> {
            ObservableValue<Integer> obsInt = new SimpleIntegerProperty(p.getValue().getIdPeminjaman()).asObject();
            return obsInt;
        });

        tcPeminjam.setCellValueFactory((p) -> {
            ObservableValue<String> obsStr = new SimpleStringProperty(p.getValue().getNama());
            return obsStr;
        });

        tcBarang.setCellValueFactory((p) -> {
            ObservableValue<String> obsStr = new SimpleStringProperty(p.getValue().getNamaBarang());
            return obsStr;
        });

        tcNoTelp.setCellValueFactory((p) -> {
            ObservableValue<String> obsStr = new SimpleStringProperty(p.getValue().getNoTelp());
            return obsStr;
        });

        tcJumlah.setCellValueFactory((p) -> {
            ObservableValue<Integer> obsInt = new SimpleIntegerProperty(p.getValue().getJumlah()).asObject();
            return obsInt;
        });
        
        try{
            //Load data pemijaman
            this.rs = this.stmt.executeQuery("SELECT id_peminjaman, tb_peminjaman.nama AS nama_peminjam, "
                    + "no_telp, tb_peminjaman.id_barang, tb_barang.nama AS nama_barang, "
                    + "tb_peminjaman.jumlah FROM tb_barang INNER JOIN tb_peminjaman ON "
                    + "tb_barang.id_barang = tb_peminjaman.id_barang");
            while(this.rs.next()){
                tvPinjam.getItems().add(new PeminjamanModel(rs.getInt("id_peminjaman"), rs.getString("nama_peminjam"), rs.getString("no_telp"), rs.getInt("id_barang"), rs.getString("nama_barang"), rs.getInt("jumlah")));
            }
            
            //SET LISTENER
            //Inisialisasi Listener
            selectionModel = tvPinjam.getSelectionModel();
            selectionModel.setSelectionMode(SelectionMode.SINGLE);
            
            //Get selected item
            selectedItems = selectionModel.getSelectedItems();
            
            selectedItems.addListener((ListChangeListener.Change<? extends PeminjamanModel> change) -> {
                namaPeminjam.setText(change.getList().get(0).getNama());
                no_telp.setText(change.getList().get(0).getNoTelp());
                jumlahBarang.setText(""+change.getList().get(0).getJumlah());
                this.id_barang = change.getList().get(0).getIdBarang();
                this.id_peminjaman = change.getList().get(0).getIdPeminjaman();
                listBarang.getSelectionModel().select(id_barang+"-"+change.getList().get(0).getNamaBarang()+"(Stok : "+change.getList().get(0).getJumlah()+"");
                if(btnUpdate.isDisabled() == true && btnKembali.isDisabled() == true && btnPinjam.isDisabled() == false){
                    listBarang.setDisable(true);
                    btnPinjam.setDisable(true);
                    btnUpdate.setDisable(false);
                    btnKembali.setDisable(false);
                    clearSelectButton.setDisable(false);
                }
            });
//            Load semua item dari database
            this.rs = this.stmt.executeQuery("SELECT * FROM tb_barang");
            while(this.rs.next()){
                observableList.add(rs.getInt("id_barang") + "-" + rs.getString("nama") +"(Stok : "+rs.getInt("jumlah")+")");
            }
            listBarang.setItems(observableList);
        }catch(SQLException e){
            errorLabel.setText(e.getMessage());
        }
    }    
    
}
