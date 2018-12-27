package org.asck.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Controller
public class QrCodeController extends AbstractController {
	
	private static final Logger LOGGER = LogManager.getLogger(NewEventController.class);

	@GetMapping("/qrCode")
	public String generateQrCode(@RequestParam("eventId") Long eventId, Model model) {
		byte[] qrCore = createAsByteArray("http://192.168.0.88:8081/answer?eventId=" + eventId);
		String qrCodeString = Base64.getEncoder().encodeToString(qrCore); 
		model.addAttribute("qrCode", qrCodeString);
		return "qrCode";
	}
	
	private byte[] createAsByteArray(String link) {
		try {
			return getQRCodeImage(link, 350, 350);
        } catch (WriterException e) {
            LOGGER.error("Could not generate QR Code, WriterException :: " + e.getMessage());
        } catch (IOException e) {
        	LOGGER.error("Could not generate QR Code, IOException :: " + e.getMessage());
        }
		return null;
	}
	
	private byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
	    QRCodeWriter qrCodeWriter = new QRCodeWriter();
	    BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
	    
	    ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
	    MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
	    byte[] pngData = pngOutputStream.toByteArray(); 
	    return pngData;
	}
}