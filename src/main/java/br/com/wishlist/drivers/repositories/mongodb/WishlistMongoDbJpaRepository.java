package br.com.wishlist.drivers.repositories.mongodb;

import br.com.wishlist.drivers.repositories.mongodb.entities.WishlistMongoDbEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistMongoDbJpaRepository extends MongoRepository<WishlistMongoDbEntity, String> {
    WishlistMongoDbEntity findByCustomerId(String customerId);
    Boolean existsByCustomerId(String customerId);
}
