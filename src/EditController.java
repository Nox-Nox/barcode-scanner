package com.example.barcode;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EditController implements Initializable {

  Stage stage;
  String c;

  ObservableList<Product> list = FXCollections.observableArrayList();
  FilteredList<Product> filteredList = new FilteredList<>(list, b -> true);
  SortedList<Product> sortedProductList = new SortedList<>(filteredList);

  @FXML private TableView<Product> tableView;
  @FXML private TableColumn<Product, String> barcodeID;
  @FXML private TableColumn<Product, String> productID;
  @FXML private TableColumn<Product, Float> priceID;
  @FXML private TableColumn<Product, Integer> quantityID;
  @FXML private AnchorPane sceneViewItems;
  @FXML private TextField searchField;

  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {

    barcodeID.setCellValueFactory(new PropertyValueFactory<Product, String>("Barcode"));
    productID.setCellValueFactory((new PropertyValueFactory<Product, String>("ProductName")));
    quantityID.setCellValueFactory(new PropertyValueFactory<Product, Integer>("Quantity"));
    priceID.setCellValueFactory(new PropertyValueFactory<Product, Float>("Price"));

    File myObj = new File("C:\\Users\\Nox\\IdeaProjects\\barcode\\lista.txt");
    BufferedReader r;
    try {
      r = new BufferedReader(new FileReader(myObj));

      while ((c = r.readLine()) != null) {

        String[] row = c.split("[={}]");
        if (row.length == 3) {
          String barcode = row[1].trim();
          String value = row[2].trim();

          String[] row1 = value.split(" ");

          if (row1.length > 3) {

            StringBuilder productName1 = new StringBuilder();

            for (int i = 0; i < row1.length - 2; i++) {

              String s = row1[i].concat(" ");
              productName1.append(s);
            }

            int quantity = Integer.parseInt(row1[row1.length - 2]);
            float price = Float.parseFloat(row1[row1.length - 1]);

            list.add(new Product(barcode, productName1.toString(), quantity, price));

          } else {

            String productName = row1[0].trim();
            int quantity = Integer.parseInt(row1[1].trim());
            float price = Float.parseFloat(row1[2].trim());

            list.add(new Product(barcode, productName, quantity, price));
          }

          tableView.setItems(list);
        }
      }

      searchField
          .textProperty()
          .addListener(
              (observable, oldValue, newValue) ->
                  filteredList.setPredicate(
                      Product -> {
                        if (newValue.isBlank() || newValue.isEmpty()) {
                          return true;
                        }
                        String searchWord = newValue.toLowerCase();

                        if (Product.getBarcode().toLowerCase().contains(searchWord)) {
                          return true;
                        } else if (Product.getProductName().toLowerCase().contains(searchWord)) {
                          return true;
                        } else if (String.valueOf(Product.getQuantity()).contains(searchWord)) {
                          return true;
                        } else return String.valueOf(Product.getPrice()).contains(searchWord);
                      }));

      sortedProductList.comparatorProperty().bind(tableView.comparatorProperty());
      tableView.setItems(sortedProductList);

    } catch (IOException e) {
      e.printStackTrace();
    }

    tableView.setEditable(true);
    barcodeID.setCellFactory(TextFieldTableCell.forTableColumn());
    productID.setCellFactory(TextFieldTableCell.forTableColumn());
    quantityID.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    priceID.setCellFactory((TextFieldTableCell.forTableColumn(new FloatStringConverter())));
  }

  public void closViewItems() {
    stage = (Stage) sceneViewItems.getScene().getWindow();
    stage.close();
  }

  public void DeleteItem() throws IOException {

    ObservableList<Product> selectedProduct;
    selectedProduct = tableView.getSelectionModel().getSelectedItems();

    String barcode = selectedProduct.get(0).getBarcode();
    String product = selectedProduct.get(0).getProductName().trim();
    int quantity = selectedProduct.get(0).getQuantity();
    float price = selectedProduct.get(0).getPrice();
    String record =
        "{" + barcode.concat("=" + product).concat(" " + quantity).concat(" " + price + "}");

    List<String> lines = new ArrayList<>();

    String line;

    File myObj = new File("C:\\Users\\Nox\\IdeaProjects\\barcode\\lista.txt");
    BufferedReader r = new BufferedReader(new FileReader(myObj));
    while ((line = r.readLine()) != null) {

      if ((line.equals(record))) {

        line = line.replace(record, " ");
      }
      lines.add(line);

      BufferedWriter w = new BufferedWriter(new FileWriter(myObj));
      for (String s : lines) w.write(s + "\n");

      w.flush();
      w.close();
    }

    int v = tableView.getSelectionModel().getSelectedIndex();
    int s = sortedProductList.getSourceIndexFor(list, v);
    list.remove(s);
  }

  public void editBarcode(TableColumn.CellEditEvent e) throws IOException {

    String oldBarcode = tableView.getSelectionModel().getSelectedItem().getBarcode();
    String newBarcode = e.getNewValue().toString();

    List<String> lines = new ArrayList<>();
    String line;

    File myObj = new File("C:\\Users\\Nox\\IdeaProjects\\barcode\\lista.txt");
    BufferedReader r = new BufferedReader(new FileReader(myObj));
    while ((line = r.readLine()) != null) {
      if ((line.contains(oldBarcode))) line = line.replace(oldBarcode, newBarcode);
      lines.add(line);
    }
    r.close();

    BufferedWriter w = new BufferedWriter(new FileWriter(myObj));
    for (String s : lines) w.write(s + "\n");
    w.flush();
    w.close();
  }

  public void editProduct(TableColumn.CellEditEvent e) throws IOException {

    String oldProduct = tableView.getSelectionModel().getSelectedItem().getProductName();
    String newProduct = e.getNewValue().toString();
    String currBarcode = tableView.getSelectionModel().getSelectedItem().getBarcode();

    List<String> lines = new ArrayList<>();
    String line;

    File myObj = new File("C:\\Users\\Nox\\IdeaProjects\\barcode\\lista.txt");

    BufferedReader r = new BufferedReader(new FileReader(myObj));
    while ((line = r.readLine()) != null) {
      if ((line.contains(oldProduct) && (line.contains(currBarcode))))
        line = line.replace(oldProduct, newProduct);
      lines.add(line);
    }
    r.close();

    BufferedWriter w = new BufferedWriter(new FileWriter(myObj));
    for (String s : lines) w.write(s + "\n");
    w.flush();
    w.close();
  }

  public void editQuantity(TableColumn.CellEditEvent e) throws IOException {

    String oldQuantity = tableView.getSelectionModel().getSelectedItem().getQuantity().toString();
    String newQuantity = e.getNewValue().toString();
    String currBarcode = tableView.getSelectionModel().getSelectedItem().getBarcode();

    List<String> lines = new ArrayList<>();
    String line;

    File myObj = new File("C:\\Users\\Nox\\IdeaProjects\\barcode\\lista.txt");
    BufferedReader r = new BufferedReader(new FileReader(myObj));
    while ((line = r.readLine()) != null) {
      if ((line.contains(oldQuantity)) && (line.contains(currBarcode)))
        line = line.replace(oldQuantity, newQuantity);
      lines.add(line);
    }
    r.close();

    BufferedWriter w = new BufferedWriter(new FileWriter(myObj));
    for (String s : lines) w.write(s + "\n");
    w.flush();
    w.close();
  }

  public void editPrice(TableColumn.CellEditEvent e) throws IOException {

    String oldPrice = tableView.getSelectionModel().getSelectedItem().getPrice().toString();
    String newPrice = e.getNewValue().toString();
    String currBarcode = tableView.getSelectionModel().getSelectedItem().getBarcode();

    List<String> lines = new ArrayList<>();
    String line;

    File myObj = new File("C:\\Users\\Nox\\IdeaProjects\\barcode\\lista.txt");
    BufferedReader r = new BufferedReader(new FileReader(myObj));
    while ((line = r.readLine()) != null) {
      if ((line.contains(oldPrice)) && (line.contains(currBarcode)))
        line = line.replace(oldPrice, newPrice);
      lines.add(line);
    }
    r.close();

    BufferedWriter w = new BufferedWriter(new FileWriter(myObj));
    for (String s : lines) w.write(s + "\n");
    w.flush();
    w.close();
  }
}
