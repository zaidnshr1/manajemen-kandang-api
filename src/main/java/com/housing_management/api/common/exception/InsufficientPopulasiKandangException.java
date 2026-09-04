package com.housing_management.api.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientPopulasiKandangException extends RuntimeException {

  public InsufficientPopulasiKandangException(String namaKandang, Integer kapasitasKandang, Integer populasiKandangSaatIni, Integer requsetPopulasi) {
    super(String.format("Kandang %s penuh! Kapasitas: %d, Terisi: %d, Mencoba menambah: %d",
            namaKandang, kapasitasKandang, populasiKandangSaatIni, requsetPopulasi));
  }
    public InsufficientPopulasiKandangException(String message) {
        super(message);
    }
}
