package co.edu.uniquindio.unieventos.controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Base64;

@Service
public class QRCodeService {

    private static final String QR_CODE_IMAGE_PATH = "./qrcodes/";

    // Método para generar un código QR y guardarlo en un archivo
    public void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    // Método para generar un código QR y devolverlo como un array de bytes (por ejemplo, para incrustar en un correo electrónico)
    public byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    // Método para generar un código QR en Base64 (útil para incrustar en HTML)
    public String getQRCodeImageAsBase64(String text, int width, int height) throws WriterException, IOException {
        byte[] qrImage = getQRCodeImage(text, width, height);
        return Base64.getEncoder().encodeToString(qrImage);
    }
}
