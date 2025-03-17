package lk.driveorbit.DriveOrbit_core.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lk.driveorbit.DriveOrbit_core.entity.Vehicle;
import lk.driveorbit.DriveOrbit_core.model.VehicleMessage;
import lk.driveorbit.DriveOrbit_core.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

//    public Vehicle registerVehicle(Vehicle vehicle) throws WriterException, IOException {
//        // Generate QR code
//        String qrCodeText = "VehicleID:" + vehicle.getRegistrationNumber(); // Customize this text
//        byte[] qrCode = generateQRCodeImage(qrCodeText, 200, 200);
//
//        // Set QR code to the vehicle entity
//        vehicle.setQrCode(qrCode);
//
//        // Save the vehicle to the database
//        return vehicleRepository.save(vehicle);
//    }
//
//    private byte[] generateQRCodeImage(String text, int width, int height) throws WriterException, IOException {
//        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
//
//        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
//        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
//        return pngOutputStream.toByteArray();
//    }

//    public Vehicle saveVehicle(Vehicle vehicle) {
//        Vehicle savedVehicle = vehicleRepository.save(vehicle);
//        // Broadcast to all subscribers
//        VehicleMessage message = new VehicleMessage("UPDATE", savedVehicle);
//        messagingTemplate.convertAndSend("/topic/vehicles", message);
//        return savedVehicle;
//    }

    public Vehicle saveVehicle(Vehicle vehicle) throws WriterException, IOException {
        // Generate QR code
        String qrCodeText = "VehicleID:" + vehicle.getRegistrationNumber(); // Customize this text
        byte[] qrCode = generateQRCodeImage(qrCodeText, 200, 200);

        // Set QR code to the vehicle entity
        vehicle.setQrCode(qrCode);

        // Save the vehicle to the database
        return vehicleRepository.save(vehicle);
    }

    private byte[] generateQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    public Optional<Vehicle> getVehicleById(Integer id) {
        return vehicleRepository.findById(id);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }
    
    public void deleteVehicle(Integer id) {
        vehicleRepository.deleteById(id);
        // Broadcast delete notification
        VehicleMessage message = new VehicleMessage("DELETE", new Vehicle());
        message.getVehicle().setVehicleId(id);
        messagingTemplate.convertAndSend("/topic/vehicles", message);
    }

    public Vehicle updateVehicleStatus(Integer vehicleId, String status) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vehicleId);
        if (vehicleOpt.isPresent()) {
            Vehicle vehicle = vehicleOpt.get();
            vehicle.setVehicleStatus(status);
            Vehicle updatedVehicle = vehicleRepository.save(vehicle);
            // Broadcast status update to all subscribers
            VehicleMessage message = new VehicleMessage("STATUS_UPDATE", updatedVehicle);
            messagingTemplate.convertAndSend("/topic/vehicles", message);
            return updatedVehicle;
        } else {
            throw new RuntimeException("Vehicle not found with id: " + vehicleId);
        }
    }
}