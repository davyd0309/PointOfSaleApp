package service;

import domain.Product;
import repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;


public class SingleProductSaleImpl implements SingleProductSale {


    private ProductRepository productRepository;
    private Lcd lcd;
    private Printer printed;
    private List<Product> scannedProducts = new ArrayList<>();

    public SingleProductSaleImpl(ProductRepository productRepository, Lcd lcd, Printer printed) {
        this.productRepository = productRepository;
        this.lcd = lcd;
        this.printed = printed;
    }

    public String showInformationAboutProduct(String barcode) {
        Product product = productRepository.getByBarcode(barcode);
        if(barcode.equalsIgnoreCase("exit")){
            return showOnPrinter(scannedProducts);
        }
        scannedProducts.add(product);
        return showOnLcd(product);
    }


    private String showOnLcd(Product product){
        return lcd.show(product);
    }

    private String showOnPrinter(List<Product> scannedProducts ){
        return printed.show();
    }

    public List<Product> getScannedProducts() {
        return scannedProducts;
    }
}
