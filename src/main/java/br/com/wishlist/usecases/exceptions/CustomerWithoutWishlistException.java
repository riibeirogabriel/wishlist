package br.com.wishlist.usecases.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Customer wishlist not found")
public class CustomerWithoutWishlistException extends RuntimeException{
}
