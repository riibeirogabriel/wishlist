package br.com.wishlist.drivers.repositories.mongodb;

import br.com.wishlist.adapters.repositories.WishlistRepositoryAdapter;
import br.com.wishlist.drivers.repositories.mongodb.entities.WishlistMongoDbEntity;
import br.com.wishlist.drivers.repositories.mongodb.entities.mappers.WishlistMapper;
import br.com.wishlist.entities.ProductEntity;
import br.com.wishlist.entities.WishlistEntity;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;


@Component
@RequiredArgsConstructor
@Repository
public class WishlistMongoDbRepository implements WishlistRepositoryAdapter {

    private final WishlistMongoDbJpaRepository wishlistMongoDbJpaRepository;
    private final MongoTemplate mongoTemplate;

    private final WishlistMapper wishlistMapper = WishlistMapper.INSTANCE;

    @Override
    public ProductEntity findCustomerProductById(String customerId, String productId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("customerId").is(customerId));
        query.fields().include("id").elemMatch("products", Criteria.where("_id").is(productId));
        WishlistMongoDbEntity wishlistEntity = mongoTemplate.findOne(query, WishlistMongoDbEntity.class);
        return Optional.ofNullable(wishlistEntity)
                .map(WishlistMongoDbEntity::getProducts)
                .map(List::getFirst)
                .orElse(null);
    }

    @Override
    public List<ProductEntity> findAllCustomerProducts(String customerId) {
        return wishlistMapper.wishlistMongoDbEntityToWishlistEntity(
                        wishlistMongoDbJpaRepository.findByCustomerId(customerId))
                .getProducts();
    }

    @Override
    public WishlistEntity addCustomerProduct(String customerId, ProductEntity productEntity) {
        Update update = new Update();
        update.push("products", productEntity);

        Criteria criteria = Criteria.where("customerId").is(customerId);
        UpdateResult updateResult = mongoTemplate.updateFirst(Query.query(criteria), update, "wishlist");

        if (updateResult.getModifiedCount() > 0)
            return wishlistMapper.wishlistMongoDbEntityToWishlistEntity(
                    wishlistMongoDbJpaRepository.findByCustomerId(customerId));
        else
            return null;
    }

    @Override
    public WishlistEntity create(WishlistEntity wishlistEntity) {
        WishlistMongoDbEntity wishlistMongoDbEntity = wishlistMapper.wishlistEntityToWishlistMongoDbEntity(wishlistEntity);
        WishlistMongoDbEntity result = wishlistMongoDbJpaRepository.save(wishlistMongoDbEntity);
        return wishlistMapper.wishlistMongoDbEntityToWishlistEntity(result);
    }

    @Override
    public WishlistEntity deleteCustomerProduct(String customerId, String productId) {
        Update update = new Update();
        update.pull("products", ProductEntity.builder().id(productId).build());

        Criteria criteria = Criteria.where("customerId").is(customerId);
        UpdateResult updateResult = mongoTemplate.updateFirst(Query.query(criteria), update, "wishlist");

        if (updateResult.getModifiedCount() > 0)
            return wishlistMapper.wishlistMongoDbEntityToWishlistEntity(
                    wishlistMongoDbJpaRepository.findByCustomerId(customerId));
        else
            return null;

    }

    @Override
    public Boolean existsWishlistByCustomerId(String customerId) {
        return wishlistMongoDbJpaRepository.existsByCustomerId(customerId);
    }

    @Override
    public Integer getAmountOfProducts(String customerId) {

        Aggregation aggregation = newAggregation(
                match(Criteria.where("customerId").is(customerId)),
                project().and("products").size().as("count"));

        AggregationResults<Document> results = mongoTemplate.aggregate(
                aggregation, "wishlist", Document.class
        );
        return Optional.ofNullable(results.getUniqueMappedResult()).map(result -> result.getInteger("count")).orElse(null);
    }
}
