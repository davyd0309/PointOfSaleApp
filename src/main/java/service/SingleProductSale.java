package service;

import domain.Product;

import java.util.List;

public interface SingleProductSale {


    String showInformationAboutProduct(String barcode);

    List<Product> getScannedProducts();


}
