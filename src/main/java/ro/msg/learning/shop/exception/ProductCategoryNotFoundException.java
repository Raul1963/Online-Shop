package ro.msg.learning.shop.exception;

public class ProductCategoryNotFoundException extends RuntimeException {
    public ProductCategoryNotFoundException(String message) {
        super(message);
    }
}
