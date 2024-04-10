package com.oop.ticketmasterswiftiebackend.external.service;

import org.springframework.core.io.ByteArrayResource;

public interface QRCodeGeneratorService {

    ByteArrayResource generateQRCodeImage(String text, int width, int height);

}
