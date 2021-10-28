package com.example.barcode;

public class tableProducts {

  String barcode;
  String productName;
  Integer quantity;
  Integer stockQuantity;
  Float price;

  public tableProducts(
      String barcode, String productName, Float price, Integer quantity, Integer stockQuantity) {

    this.barcode = barcode;
    this.productName = productName;
    this.quantity = quantity;
    this.stockQuantity = stockQuantity;
    this.price = price;
  }

  public String getBarcode() {
    return barcode;
  }

  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public Integer getStockQuantity() {
    return stockQuantity;
  }

  public void setStockQuantity(Integer stockQuantity) {
    this.stockQuantity = stockQuantity;
  }

  public Float getPrice() {
    return price;
  }

  public void setPrice(Float price) {
    this.price = price;
  }
  @Override
  public String toString() {
    return "{" + barcode + "=" + productName + " " + price + " " + quantity + " "+stockQuantity+"}";
  }
}
