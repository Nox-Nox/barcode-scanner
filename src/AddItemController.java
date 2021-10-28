package com.example.barcode;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddItemController implements Initializable {

  String barcode;
  String productName;
  int quantity;
  float price;
  Stage stage;
  @FXML private TextField barcodeid;
  @FXML private TextField productid;
  @FXML private TextField quantityid;
  @FXML private TextField priceid;
  @FXML private AnchorPane sceneAddItem;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Platform.runLater(() -> barcodeid.requestFocus());
  }

  public void submit() throws IOException {

    barcode = barcodeid.getText();
    productName = productid.getText();
    quantity = Integer.parseInt(quantityid.getText());
    price = Float.parseFloat(priceid.getText());

    Product p = new Product(barcode, productName, quantity, price);

    File myObj = new File("C:\\Users\\Nox\\IdeaProjects\\barcode\\lista.txt");
    BufferedWriter w = new BufferedWriter(new FileWriter(myObj, true));

    w.write((p + "\n"));

    w.flush();
    w.close();

    barcodeid.clear();
    productid.clear();
    quantityid.clear();
    priceid.clear();

    Platform.runLater(() -> barcodeid.requestFocus());
  }

  public void closeAddItem() {
    stage = (Stage) sceneAddItem.getScene().getWindow();
    stage.close();
  }
}
