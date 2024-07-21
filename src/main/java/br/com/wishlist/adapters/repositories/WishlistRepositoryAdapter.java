package br.com.wishlist.adapters.repositories;

import br.com.wishlist.entities.ProductEntity;
import br.com.wishlist.entities.WishlistEntity;

import java.util.List;

public interface WishlistRepositoryAdapter {
    ProductEntity findCustomerProductById(String customerId, String productId);
    List<ProductEntity> findAllCustomerProducts(String customerId);
    WishlistEntity addCustomerProduct(String customerId, ProductEntity productEntity);
    WishlistEntity create(WishlistEntity wishlistEntity);
    WishlistEntity deleteCustomerProduct(String customerId, String productId);
    Boolean existsWishlistByCustomerId(String customerId);
    Integer getAmountOfProducts(String customerId);
}
