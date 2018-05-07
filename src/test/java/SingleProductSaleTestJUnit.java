
import org.assertj.core.util.Lists;
import service.Lcd;
import service.Printer;
import domain.Product;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import repository.ProductRepository;
import service.SingleProductSale;
import service.SingleProductSaleImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class SingleProductSaleTestJUnit {


    private static final Logger logger = LogManager.getLogger(SingleProductSaleTestJUnit.class);


    private SingleProductSale singleProductSale;


    @Mock
    private ProductRepository productRepository;

    @Mock
    private Lcd lcd;

    @Mock
    private Printer printer;

    @Before
    public void setUp() {

        singleProductSale = new SingleProductSaleImpl(productRepository, lcd, printer);

    }


    @Test
    public void shouldNameAndPricePrintOnLcd() {

        //given
        Product product = givenProduct("4582664XX", "orange", new BigDecimal(10));
        Mockito.when(productRepository.getByBarcode("4582664XX")).thenReturn(product);
        Mockito.when(lcd.show(product)).thenReturn("Product name " + product.getName() + "." + "Product price " + product.getPrice());
        //when
        String message = singleProductSale.showInformationAboutProduct("4582664XX");
        //then
        assertThat(message).isEqualTo("Product name orange.Product price 10");
    }

    @Test
    public void shouldErrorNotFoundPrintOnLcd() {

        //given
        Product product = null;
        Mockito.when(productRepository.getByBarcode("8875115YT")).thenReturn(product);
        Mockito.when(lcd.show(product)).thenReturn("Product not found");
        //when
        String message = singleProductSale.showInformationAboutProduct("8875115YT");
        //then
        assertThat(message).isEqualTo("Product not found");

    }


    @Test
    public void shouldErrorInvalidBarcodePrintOnLcd() {

        //given
        Product product = new Product();
        Mockito.when(productRepository.getByBarcode("")).thenReturn(product);
        Mockito.when(lcd.show(product)).thenReturn("Invalid bar-code");
        //when
        String message = singleProductSale.showInformationAboutProduct("");
        //then
        assertThat(message).isEqualTo("Invalid bar-code");
    }

    @Test
    public void shouldAddProductToReceipt() {
        //given
        Product product = givenProduct("4582664XX", "orange", new BigDecimal(10));
        Mockito.when(productRepository.getByBarcode("4582664XX")).thenReturn(product);
        //when
        singleProductSale.showInformationAboutProduct("4582664XX");
        //then
        assertThat(singleProductSale.getScannedProducts()).containsOnly(product);
    }


    @Test
    public void shouldReceiptWithNameAndPricePreviouslyProductPrintOnPrinter() {
        //given
        Product product = givenProduct("4582664XX", "orange", new BigDecimal(10));
        Mockito.when(productRepository.getByBarcode("4582664XX")).thenReturn(product);

        Product productA = givenProduct("784332XX", "apple", new BigDecimal(12));
        Mockito.when(productRepository.getByBarcode("784332XX")).thenReturn(productA);

        Product productB = givenProduct("987655XX", "banana", new BigDecimal(23));
        Mockito.when(productRepository.getByBarcode("987655XX")).thenReturn(productB);
        //when
        singleProductSale.showInformationAboutProduct("4582664XX");
        singleProductSale.showInformationAboutProduct("784332XX");
        singleProductSale.showInformationAboutProduct("987655XX");
        singleProductSale.showInformationAboutProduct("exit");
        singleProductSale.showInformationAboutProduct("784332XX");
        singleProductSale.showInformationAboutProduct("987655XX");
        //then
        assertThat(singleProductSale.getScannedProducts().size()).isEqualTo(3);
    }

    @Test
    public void shouldTotalSumPrintdOnLcd() {

    }


    private Product givenProduct(String barcode, String name, BigDecimal price) {
        Product product = new Product();
        product.setBarcode(barcode);
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
