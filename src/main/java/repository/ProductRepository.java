package repository;

import domain.Product;

public interface ProductRepository {


    Product getByBarcode(String barcode);


}
