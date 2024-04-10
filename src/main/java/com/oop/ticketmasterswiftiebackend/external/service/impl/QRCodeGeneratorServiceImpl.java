package com.oop.ticketmasterswiftiebackend.external.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.oop.ticketmasterswiftiebackend.common.CommonUtils;
import com.oop.ticketmasterswiftiebackend.common.models.BaseException;
import com.oop.ticketmasterswiftiebackend.external.constants.QRCodeGeneratorError;
import com.oop.ticketmasterswiftiebackend.external.service.QRCodeGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@RequiredArgsConstructor
@Service
@Slf4j
public class QRCodeGeneratorServiceImpl implements QRCodeGeneratorService {
    private final QRCodeWriter qrCodeWriter = new QRCodeWriter();

    @Override
    public ByteArrayResource generateQRCodeImage(String text, int width, int height) {
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            return new ByteArrayResource(pngOutputStream.toByteArray());
        } catch (WriterException writerException) {
            log.error("Error occurred when trying to generate QR Code: {}", writerException.getMessage());
            throw new BaseException(QRCodeGeneratorError.ERROR_GENERATING_QRCODE.getCode(), QRCodeGeneratorError.ERROR_GENERATING_QRCODE.getBusinessCode(), QRCodeGeneratorError.ERROR_GENERATING_QRCODE.getDescription());
        } catch (Exception error) {
            log.error("Error occurred when trying to generate QR Code: {}", error.getMessage());
            throw CommonUtils.commonExceptionHandler(error);
        }


    }
}
