package com.example.barcode;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainApp extends Application {

  public static void main(String[] args) {

    File myObj = new File("lista.txt");
    String line;
    List<String> lines = new ArrayList<>();
    try {
      BufferedReader r = new BufferedReader(new FileReader(myObj));

      while ((line = r.readLine()) != null) {
        if (!line.isBlank()) {
          lines.add(line);
        }
      }
      r.close();

      BufferedWriter w = new BufferedWriter(new FileWriter(myObj));
      for (String c : lines) w.write(c + "\n");
      w.flush();
      w.close();

    } catch (IOException e) {
      e.printStackTrace();
    }

    launch();
  }

  @Override
  public void start(Stage stage) throws IOException {

    Parent root = FXMLLoader.load(getClass().getResource("scene.fxml"));

    Scene scene = new Scene(root);
    stage.setTitle("Hello!");
    stage.setScene(scene);
    stage.show();
  }
}
