package br.com.wishlist.drivers.repositories.mongodb.entities;


import br.com.wishlist.entities.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
@Document("wishlist")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WishlistMongoDbEntity {
    @Id
    String id;
    @Indexed(unique = true)
    String customerId;
    List<ProductEntity> products;
}
