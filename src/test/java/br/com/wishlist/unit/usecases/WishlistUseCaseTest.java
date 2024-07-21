package br.com.wishlist.unit.usecases;

import br.com.wishlist.adapters.repositories.WishlistRepositoryAdapter;
import br.com.wishlist.entities.ProductEntity;
import br.com.wishlist.entities.WishlistEntity;
import br.com.wishlist.usecases.WishlistUseCase;
import br.com.wishlist.usecases.exceptions.CustomerWithoutWishlistException;
import br.com.wishlist.usecases.exceptions.MaxNumberOfProductsAlreadyInsertedException;
import br.com.wishlist.usecases.exceptions.ProductAlreadyInsertedInWishlistException;
import br.com.wishlist.usecases.exceptions.ProductNotFoundInWishlistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WishlistUseCaseTest {

    @InjectMocks
    private WishlistUseCase wishlistUseCase;

    @Mock
    private WishlistRepositoryAdapter wishlistRepositoryAdapter;

    private final String customerId = "customer123";
    private final String productId = "product123";


    @Test
    void testAddProduct_WishlistDoesNotExist() {
        when(wishlistRepositoryAdapter.existsWishlistByCustomerId(customerId)).thenReturn(false);

        WishlistEntity wishlistEntity = WishlistEntity.builder().customerId(customerId).products(
                List.of(ProductEntity.builder().id(productId).build())).build();
        when(wishlistRepositoryAdapter.create(eq(wishlistEntity))).thenReturn(wishlistEntity);

        WishlistEntity result = wishlistUseCase.addProduct(customerId, productId);

        assertEquals(customerId, result.getCustomerId());
        assertEquals(1, result.getProducts().size());
        assertEquals(productId, result.getProducts().getFirst().getId());
        verify(wishlistRepositoryAdapter).create(eq(wishlistEntity));
    }

    @Test
    void testAddProduct_WishlistExists() {
        ReflectionTestUtils.setField(wishlistUseCase, "maxNumberOfProductsInWishlist", 3);

        when(wishlistRepositoryAdapter.existsWishlistByCustomerId(customerId)).thenReturn(true);
        when(wishlistRepositoryAdapter.getAmountOfProducts(customerId)).thenReturn(1);
        when(wishlistRepositoryAdapter.findCustomerProductById(customerId, productId)).thenReturn(null);

        ProductEntity productEntityOne = ProductEntity.builder().id("product456").build();
        ProductEntity productEntityTwo = ProductEntity.builder().id(productId).build();
        WishlistEntity wishlistEntity = WishlistEntity.builder().customerId(customerId)
                .products(List.of(productEntityOne, productEntityTwo)).build();
        when(wishlistRepositoryAdapter.addCustomerProduct(customerId, productEntityTwo)).thenReturn(wishlistEntity);

        WishlistEntity result = wishlistUseCase.addProduct(customerId, productId);

        assertEquals(customerId, result.getCustomerId());
        assertEquals(2, result.getProducts().size());
        verify(wishlistRepositoryAdapter).addCustomerProduct(customerId, productEntityTwo);
    }

    @Test
    void testAddProduct_MaxNumberOfProductsReached() {
        ReflectionTestUtils.setField(wishlistUseCase, "maxNumberOfProductsInWishlist", 3);

        when(wishlistRepositoryAdapter.existsWishlistByCustomerId(customerId)).thenReturn(true);
        when(wishlistRepositoryAdapter.getAmountOfProducts(customerId)).thenReturn(3);

        assertThrows(MaxNumberOfProductsAlreadyInsertedException.class, () -> wishlistUseCase.addProduct(customerId, productId));
    }

    @Test
    void testAddProduct_ProductAlreadyInWishlist() {
        ReflectionTestUtils.setField(wishlistUseCase, "maxNumberOfProductsInWishlist", 3);

        when(wishlistRepositoryAdapter.existsWishlistByCustomerId(customerId)).thenReturn(true);
        when(wishlistRepositoryAdapter.getAmountOfProducts(customerId)).thenReturn(1);
        when(wishlistRepositoryAdapter.findCustomerProductById(customerId, productId)).thenReturn(ProductEntity.builder().id(productId).build());

        assertThrows(ProductAlreadyInsertedInWishlistException.class, () -> wishlistUseCase.addProduct(customerId, productId));
    }

    @Test
    void testGetWishlistProducts_WishlistDoesNotExist() {
        when(wishlistRepositoryAdapter.existsWishlistByCustomerId(customerId)).thenReturn(false);

        assertThrows(CustomerWithoutWishlistException.class, () -> wishlistUseCase.getWishlistProducts(customerId));
    }

    @Test
    void testGetWishlistProducts_Success() {
        when(wishlistRepositoryAdapter.existsWishlistByCustomerId(customerId)).thenReturn(true);
        List<ProductEntity> products = List.of(ProductEntity.builder().id(productId).build());
        when(wishlistRepositoryAdapter.findAllCustomerProducts(customerId)).thenReturn(products);

        List<ProductEntity> result = wishlistUseCase.getWishlistProducts(customerId);

        assertEquals(1, result.size());
        verify(wishlistRepositoryAdapter).findAllCustomerProducts(customerId);
    }

    @Test
    void testFindProductInCustomerWishlist_WishlistDoesNotExist() {
        when(wishlistRepositoryAdapter.existsWishlistByCustomerId(customerId)).thenReturn(false);

        assertThrows(CustomerWithoutWishlistException.class, () -> wishlistUseCase.findProductInCustomerWishlist(customerId, productId));
    }

    @Test
    void testFindProductInCustomerWishlist_ProductNotFound() {
        when(wishlistRepositoryAdapter.existsWishlistByCustomerId(customerId)).thenReturn(true);
        when(wishlistRepositoryAdapter.findCustomerProductById(customerId, productId)).thenReturn(null);

        assertThrows(ProductNotFoundInWishlistException.class, () -> wishlistUseCase.findProductInCustomerWishlist(customerId, productId));
    }

    @Test
    void testFindProductInCustomerWishlist_Success() {
        when(wishlistRepositoryAdapter.existsWishlistByCustomerId(customerId)).thenReturn(true);
        ProductEntity productEntity = ProductEntity.builder().id(productId).build();
        when(wishlistRepositoryAdapter.findCustomerProductById(customerId, productId)).thenReturn(productEntity);

        ProductEntity result = wishlistUseCase.findProductInCustomerWishlist(customerId, productId);

        assertNotNull(result);
        assertEquals(productId, result.getId());
        verify(wishlistRepositoryAdapter, times(1)).findCustomerProductById(customerId, productId);
    }

    @Test
    void testRemoveProduct_WishlistDoesNotExist() {
        when(wishlistRepositoryAdapter.existsWishlistByCustomerId(customerId)).thenReturn(false);

        assertThrows(CustomerWithoutWishlistException.class, () -> wishlistUseCase.removeProduct(customerId, productId));
    }

    @Test
    void testRemoveProduct_ProductNotFoundInWishlist() {
        when(wishlistRepositoryAdapter.existsWishlistByCustomerId(customerId)).thenReturn(true);
        when(wishlistRepositoryAdapter.deleteCustomerProduct(customerId, productId)).thenReturn(null);

        assertThrows(ProductNotFoundInWishlistException.class, () -> wishlistUseCase.removeProduct(customerId, productId));
    }

    @Test
    void testRemoveProduct_Success() {
        when(wishlistRepositoryAdapter.existsWishlistByCustomerId(customerId)).thenReturn(true);
        when(wishlistRepositoryAdapter.deleteCustomerProduct(customerId, productId)).thenReturn(WishlistEntity.builder().build());

        WishlistEntity result = wishlistUseCase.removeProduct(customerId, productId);

        assertNotNull(result);
        verify(wishlistRepositoryAdapter).deleteCustomerProduct(customerId, productId);
    }
}
