package br.com.wishlist.adapters.controllers;

import br.com.wishlist.entities.ProductEntity;
import br.com.wishlist.entities.WishlistEntity;
import br.com.wishlist.usecases.WishlistUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/wishlists")
@RequiredArgsConstructor
public class WishlistController {
    private final WishlistUseCase wishlistUseCase;


    @Operation(
            summary = "Get a product in customer wishlist",
            description = "Get a product in customer created wishlist",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "The product was retrieved successfully in the wishlist",
                    content = {

                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductEntity.class))

                    })
                    ,
                    @ApiResponse(
                            responseCode = "404",
                            description = "The customer wishlist or product was not found",
                            content = {
                                    @Content(
                                            mediaType = "application/json")
                            })
            }
    )
    @GetMapping("/customers/{customerId}/products/{productId}")
    public ProductEntity getProductInCustomerWishlist(@PathVariable(value = "customerId") String customerId,
                                                      @PathVariable(value = "productId") String productId) {
        return wishlistUseCase.findProductInCustomerWishlist(customerId, productId);
    }

    @Operation(
            summary = "Get all products in customer wishlist",
            description = "Get all products in customer created wishlist",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "The products were retrieved successfully in the wishlist",
                    content = {

                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ProductEntity.class)))
                    })
                    ,
                    @ApiResponse(
                            responseCode = "404",
                            description = "The customer wishlist was not found",
                            content = {
                                    @Content(
                                            mediaType = "application/json")
                            })
            }
    )
    @GetMapping("/customers/{customerId}/products")
    public List<ProductEntity> getCustomerWishlistProducts(@PathVariable(value = "customerId") String customerId) {
        return wishlistUseCase.getWishlistProducts(customerId);
    }

    @Operation(
            summary = "Remove a product in customer wishlist",
            description = "Remove a product in customer created wishlist",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "The product was removed successfully in the wishlist",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = WishlistEntity.class))
                    })
                    ,
                    @ApiResponse(
                            responseCode = "404",
                            description = "The customer wishlist or product were not found",
                            content = {
                                    @Content(
                                            mediaType = "application/json")
                            })
            }
    )

    @DeleteMapping("/customers/{customerId}/products/{productId}")
    public WishlistEntity removeWishlistProduct(@PathVariable(value = "customerId") String customerId,
                                                @PathVariable(value = "productId") String productId) {
        return wishlistUseCase.removeProduct(customerId, productId);
    }

    @Operation(
            summary = "Add product in customer wishlist",
            description = "Add a product in customer created wishlist",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "The product was inserted successfully in the wishlist",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = WishlistEntity.class))
                    })
                    ,
                    @ApiResponse(
                            responseCode = "404",
                            description = "The customer wishlist was not found",
                            content = {
                                    @Content(
                                            mediaType = "application/json")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "The customer wishlist already has the desired product",
                            content = {
                                    @Content(
                                            mediaType = "application/json")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "The customer wishlist is full",
                            content = {
                                    @Content(
                                            mediaType = "application/json")
                            }
                    )
            }
    )
    @PostMapping("/customers/{customerId}/products")
    public WishlistEntity addProductInCustomerWishlist(@PathVariable(value = "customerId") String customerId,
                                                       @RequestBody ProductEntity productEntity) {
        return wishlistUseCase.addProduct(customerId, productEntity.getId());
    }
}
