package br.com.wishlist.usecases.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Maximum number of products already inserted")
public class MaxNumberOfProductsAlreadyInsertedException extends RuntimeException {
}
