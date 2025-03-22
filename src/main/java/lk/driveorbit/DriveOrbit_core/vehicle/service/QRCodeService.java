package lk.driveorbit.DriveOrbit_core.vehicle.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Service
public class QRCodeService {
    private static final Logger logger = LoggerFactory.getLogger(QRCodeService.class);
    
    @Value("${qrcode.upload.dir:upload/qrcodes}")
    private String uploadDir;
    
    @Value("${app.baseUrl:http://localhost:8080}")
    private String baseUrl;

    public String generateQRCodeImage(String text, int width, int height, String fileName) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            
            // Create directory if it doesn't exist
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName + ".png";
            Path filePath = Paths.get(uploadDir, uniqueFileName);
            
            // Write QR code to file
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", filePath);
            
            // Return URL to access QR code
            return baseUrl + "/qrcodes/" + uniqueFileName;
            
        } catch (WriterException | IOException e) {
            logger.error("Error generating QR code: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }
    
    public String generateQRCodeBase64(String text, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            
            // Convert to image
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            
            // Convert to Base64
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            byte[] imageData = baos.toByteArray();
            
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageData);
            
        } catch (WriterException | IOException e) {
            logger.error("Error generating QR code base64: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }
}
