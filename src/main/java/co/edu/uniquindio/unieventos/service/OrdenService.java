package co.edu.uniquindio.unieventos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdenService {

    @Autowired
    private QRCodeService qrCodeService;

    public void generarOrdenCompra(String codigoOrden) throws Exception {
        // Lógica para crear la orden...

        // Generar el código QR
        String qrCodeFilePath = "./qrcodes/" + codigoOrden + ".png";
        qrCodeService.generateQRCodeImage(codigoOrden, 250, 250, qrCodeFilePath);

        // Si prefieres obtener el QR en Base64 para incluirlo en un correo
        String qrCodeBase64 = qrCodeService.getQRCodeImageAsBase64(codigoOrden, 250, 250);

        // Lógica para enviar el correo electrónico con el QR adjunto o incrustado...

        String htmlBody = "<h1>Detalles de la Orden</h1>"
                + "<p>Gracias por tu compra. Aquí está tu código QR:</p>"
                + "<img src='data:image/png;base64," + qrCodeBase64 + "' />";

    }
}

