package br.com.wishlist.usecases;

import br.com.wishlist.adapters.repositories.WishlistRepositoryAdapter;
import br.com.wishlist.entities.ProductEntity;
import br.com.wishlist.entities.WishlistEntity;
import br.com.wishlist.usecases.exceptions.CustomerWithoutWishlistException;
import br.com.wishlist.usecases.exceptions.MaxNumberOfProductsAlreadyInsertedException;
import br.com.wishlist.usecases.exceptions.ProductAlreadyInsertedInWishlistException;
import br.com.wishlist.usecases.exceptions.ProductNotFoundInWishlistException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishlistUseCase {
    @Value("${wishlist.customer.products.limit}")
    private Integer maxNumberOfProductsInWishlist;

    private final WishlistRepositoryAdapter wishlistRepositoryAdapter;

    public WishlistEntity addProduct(String customerId, String productId) {
        if (!wishlistRepositoryAdapter.existsWishlistByCustomerId(customerId)) {
            return wishlistRepositoryAdapter.create(
                    WishlistEntity.builder()
                            .customerId(customerId)
                            .products(List.of(ProductEntity.builder().id(productId).build()))
                            .build()
            );
        }

        if (wishlistRepositoryAdapter.getAmountOfProducts(customerId) >= maxNumberOfProductsInWishlist) {
            throw new MaxNumberOfProductsAlreadyInsertedException();
        }

        if (Objects.nonNull(wishlistRepositoryAdapter.findCustomerProductById(customerId, productId))) {
            throw new ProductAlreadyInsertedInWishlistException();
        }

        return wishlistRepositoryAdapter.addCustomerProduct(
                customerId,
                ProductEntity.builder().id(productId).build()
        );
    }

    public WishlistEntity removeProduct(String customerId, String productId) {
        if (wishlistRepositoryAdapter.existsWishlistByCustomerId(customerId)) {
            return Optional.ofNullable(wishlistRepositoryAdapter.deleteCustomerProduct(
                    customerId,
                    productId)).orElseThrow(ProductNotFoundInWishlistException::new);

        } else
            throw new CustomerWithoutWishlistException();

    }

    public List<ProductEntity> getWishlistProducts(String customerId) {
        if (wishlistRepositoryAdapter.existsWishlistByCustomerId(customerId))
            return wishlistRepositoryAdapter.findAllCustomerProducts(customerId);
        else
            throw new CustomerWithoutWishlistException();
    }

    public ProductEntity findProductInCustomerWishlist(String customerId, String productId) {
        if (wishlistRepositoryAdapter.existsWishlistByCustomerId(customerId)) {
            return Optional.ofNullable(
                            wishlistRepositoryAdapter.findCustomerProductById(customerId, productId))
                    .orElseThrow(ProductNotFoundInWishlistException::new);
        } else
            throw new CustomerWithoutWishlistException();

    }

}
