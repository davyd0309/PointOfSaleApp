package service;

import domain.Product;

public interface Lcd {

     default String show(Product product) {
        return "";
    }

}
