package com.example.barcode;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class ScanController implements Initializable {

  ObservableList<tableProducts> list = FXCollections.observableArrayList();
  ObservableList<tableProducts> scannedItems = FXCollections.observableArrayList();
  ObservableList<Product> currentList = FXCollections.observableArrayList();

  Stage stage;

  @FXML private TextField scanID;
  @FXML private TableView<tableProducts> tableView;
  @FXML private TableColumn<tableProducts, String> barcodeID;
  @FXML private TableColumn<tableProducts, String> productID;
  @FXML private TableColumn<tableProducts, Float> priceID;
  @FXML private TableColumn<tableProducts, Integer> quantityID;
  @FXML private TableColumn<tableProducts, Integer> stockID;
  @FXML private Label totalID;
  @FXML private AnchorPane sceneScan;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    Platform.runLater(() -> scanID.requestFocus());

    barcodeID.setCellValueFactory(new PropertyValueFactory<tableProducts, String>("Barcode"));
    productID.setCellValueFactory((new PropertyValueFactory<tableProducts, String>("ProductName")));
    priceID.setCellValueFactory(new PropertyValueFactory<tableProducts, Float>("Price"));
    quantityID.setCellValueFactory(new PropertyValueFactory<tableProducts, Integer>("Quantity"));
    stockID.setCellValueFactory(new PropertyValueFactory<tableProducts, Integer>("StockQuantity"));

    File myObj = new File("C:\\Users\\Nox\\IdeaProjects\\barcode\\lista.txt");
    String c;

    try {
      BufferedReader r = new BufferedReader(new FileReader(myObj));
      while ((c = r.readLine()) != null) {

        String[] row = c.split("[={}]");

        if (row.length == 3) {
          String barcode = row[1].trim();
          String value = row[2].trim();

          String[] row1 = value.split(" ");

          if (row1.length > 3) {

            StringBuilder productName = new StringBuilder();

            for (int i = 0; i < row1.length - 2; i++) {

              String s = row1[i].concat(" ");
              productName.append(s);
            }

            Integer quantity1 = 1;
            Integer stockQuantity = Integer.parseInt(row1[row1.length - 2]);
            Float price = Float.parseFloat(row1[row1.length - 1]);

            list.add(
                new tableProducts(
                    barcode, productName.toString(), price, quantity1, stockQuantity));
            currentList.add(new Product(barcode, productName.toString(), stockQuantity, price));

          } else {

            String productName = row1[0].trim();
            Integer quantity1 = 1;
            Integer stockQuantity = Integer.parseInt(row1[1].trim());
            Float price = Float.parseFloat(row1[2].trim());

            list.add(new tableProducts(barcode, productName, price, quantity1, stockQuantity));
            currentList.add(new Product(barcode, productName, stockQuantity, price));
          }
        }
      }
      r.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
    tableView.setEditable(true);
    quantityID.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
  }

  public void getIT() {

    if (!scanID.getText().isEmpty() || !scanID.getText().isBlank()) {

      String code = scanID.getText();

      for (tableProducts p : list) {
        if (code.equals(p.getBarcode())) {

          scannedItems.add(p);
          tableView.setItems(scannedItems);
        }
      }
    }
    scanID.clear();
    Platform.runLater(() -> scanID.requestFocus());

    float tot = 0;

    for (tableProducts k : scannedItems) {

      float tempP = k.getPrice();
      int tempQ = k.getQuantity();

      if (tempQ > 1) {

        tot += tempP * tempQ;

      } else {
        tot += tempP;
      }
    }
    System.out.println("tot without newQuantity " + tot);
    DecimalFormat f = new DecimalFormat("#.##");
    String totS = f.format(tot);
    totalID.setText("€" + totS);
  }

  public void editQuantity(TableColumn.CellEditEvent e) {

    ObservableList<tableProducts> tempList = FXCollections.observableArrayList();
    Integer newQuantity = (Integer) e.getNewValue();
    String barCode = tableView.getSelectionModel().getSelectedItem().getBarcode();
    System.out.println(barCode + " new quantity: " + newQuantity);

    for (tableProducts x : scannedItems) {

      if (x.getBarcode().equals(barCode)) {

        System.out.println(
            "selected row's barcode " + barCode + " and x's barcode: " + x.getBarcode());
        String barcode = x.getBarcode();
        String product = x.getProductName();
        Integer stockQuantity = x.getStockQuantity();
        Float price = x.getPrice();

        tempList.add(new tableProducts(barcode, product, price, newQuantity, stockQuantity));

      } else {

        String barcode = x.getBarcode();
        String product = x.getProductName();
        Integer quantity = x.getQuantity();
        Integer stockQuantity = x.getStockQuantity();
        Float price = x.getPrice();

        tempList.add(new tableProducts(barcode, product, price, quantity, stockQuantity));
      }
    }
    scannedItems.clear();
    scannedItems.addAll(tempList);

    getIT();
  }

  public void closeScan() {

    stage = (Stage) sceneScan.getScene().getWindow();
    stage.close();
  }

  public void submitScan() throws IOException {

    File myObj = new File("C:\\Users\\Nox\\IdeaProjects\\barcode\\lista.txt");

    for (Product a : currentList) {
      for (tableProducts s : scannedItems) {
        if (a.getBarcode().equals(s.getBarcode())) {
          int f = a.getQuantity() - s.getQuantity();
          System.out.println(f);
          if (f < 0) {
            a.setQuantity(0);
            break;
          } else {
            int updatedQuantity = a.getQuantity() - s.getQuantity();
            a.setQuantity(updatedQuantity);
          }
        }
      }
    }
    System.out.println(currentList);
    BufferedWriter w = new BufferedWriter(new FileWriter(myObj));
    for (Product g : currentList) {
      w.write(g.toString() + "\n");
    }
    w.close();
    stage = (Stage) sceneScan.getScene().getWindow();
    stage.close();
  }
}
