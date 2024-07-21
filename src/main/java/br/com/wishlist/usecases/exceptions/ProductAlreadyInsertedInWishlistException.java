package br.com.wishlist.usecases.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Products already inserted in wishlist")
public class ProductAlreadyInsertedInWishlistException extends RuntimeException {
}
