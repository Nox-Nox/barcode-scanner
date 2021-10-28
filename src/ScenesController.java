package com.example.barcode;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ScenesController {

  public void addItem() throws IOException {

    Stage stage = new Stage();
    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("scene1.fxml")));
    stage.setTitle("Add item");
    stage.initModality(Modality.WINDOW_MODAL);
    stage.setScene(new Scene(root));
    stage.show();
  }

  public void viewItems() throws IOException {

    Stage stage = new Stage();
    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("scene2.fxml")));
    stage.setTitle("View Items");
    stage.initModality(Modality.WINDOW_MODAL);
    stage.setScene(new Scene(root));
    stage.showAndWait();
  }

  public void scanItem() throws IOException {

    Stage stage = new Stage();
    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("scene3.fxml")));
    stage.setTitle("Scan Items");
    stage.initModality(Modality.WINDOW_MODAL);
    stage.setScene(new Scene(root));
    stage.showAndWait();
  }
}
