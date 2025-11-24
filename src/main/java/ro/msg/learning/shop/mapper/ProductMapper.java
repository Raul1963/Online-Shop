package ro.msg.learning.shop.mapper;

import jdk.jfr.Category;
import ro.msg.learning.shop.dto.ProductCreateDto;
import ro.msg.learning.shop.dto.ProductDto;
import ro.msg.learning.shop.model.Product;
import ro.msg.learning.shop.model.ProductCategory;

public class ProductMapper {

    public static ProductDto toDto(Product product) {
        if(product == null) return null;

        return ProductDto.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .weight(product.getWeight())
                .imageUrl(product.getImageUrl())
//                .stockLocations(product.getStocks().stream().map(StockMapper::toDto).toList())
                .categoryName(product.getCategory().getName())
                .categoryDescription(product.getCategory().getDescription())
                .build();
    }

    public static Product toProduct(ProductDto productDto) {
        if(productDto == null) return null;

        Product product = Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .weight(productDto.getWeight())
//                .category((new ProductCategory(productDto.getCategoryName(), productDto.getCategoryDescription())))
                .imageUrl(productDto.getImageUrl())
                .build();
        product.setId(productDto.getId());
        return product;

    }

    public static Product toCreateProduct(ProductCreateDto productCreateDto) {
        if(productCreateDto == null) return null;
        return Product.builder()
                .name(productCreateDto.getName())
                .description(productCreateDto.getDescription())
                .price(productCreateDto.getPrice())
                .weight(productCreateDto.getWeight())
                .imageUrl(productCreateDto.getImageUrl())
                .build();
    }
}
