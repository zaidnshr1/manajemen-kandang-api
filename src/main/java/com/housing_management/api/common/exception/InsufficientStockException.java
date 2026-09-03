package com.housing_management.api.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String namaPakan, Object stokTersedia, Object jumlahDiminta) {
      super(String.format("Stok pakan '%s' tidak mencukupi. Tersedia %s, Diminta: %s",
              namaPakan, stokTersedia, jumlahDiminta));
    }

    public InsufficientStockException(String message) {super(message);}
}
