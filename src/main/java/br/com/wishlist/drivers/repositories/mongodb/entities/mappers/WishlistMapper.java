package br.com.wishlist.drivers.repositories.mongodb.entities.mappers;

import br.com.wishlist.drivers.repositories.mongodb.entities.WishlistMongoDbEntity;
import br.com.wishlist.entities.WishlistEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WishlistMapper {
    WishlistMapper INSTANCE = Mappers.getMapper(WishlistMapper.class);

    WishlistMongoDbEntity wishlistEntityToWishlistMongoDbEntity(WishlistEntity wishlistEntity);

    WishlistEntity wishlistMongoDbEntityToWishlistEntity(WishlistMongoDbEntity wishlistMongoDbEntity);

}