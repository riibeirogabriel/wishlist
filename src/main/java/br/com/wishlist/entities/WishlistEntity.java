package br.com.wishlist.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistEntity {
    String id;
    String customerId;
    List<ProductEntity> products;
}
