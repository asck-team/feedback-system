package org.asck.web.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import lombok.AccessLevel;
import lombok.Getter;

@Controller
@Getter(value = AccessLevel.PROTECTED)
public class QrCodeController extends AbstractController {
	
	@Value("${qr.code.host:127.0.0.1}") 
	private String ipAdress;
	@Value("${qr.code.port:8080}")
	private String port;
	
	private static final Logger LOGGER = LogManager.getLogger(NewEventController.class);

	@GetMapping("/qrCode")
	public String generateQrCode(@RequestParam("eventId") Long eventId, Model model) {
		byte[] qrCodeContent = createAsByteArray(createURLForQRCode(eventId));
		String qrCodeString = Base64.getEncoder().encodeToString(qrCodeContent); 
		model.addAttribute("qrCode", qrCodeString);
		return "qrCode";
	}

	protected String createURLForQRCode(Long eventId) {
		LOGGER.info("Host {} and port {} as content for qr code", ipAdress, port);
		return String.format("http://%s:%s/answer?eventId=%s", getIpAdress(), getPort(), eventId);
	}
	
	private byte[] createAsByteArray(String link) {
		try {
			return getQRCodeImage(link, 350, 350);
        } catch (WriterException e) {
            LOGGER.error("Could not generate QR Code, WriterException :: " + e.getMessage());
        } catch (IOException e) {
        	LOGGER.error("Could not generate QR Code, IOException :: " + e.getMessage());
        }
		return new ByteArrayOutputStream().toByteArray();
	}
	
	private byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
	    QRCodeWriter qrCodeWriter = new QRCodeWriter();
	    BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
	    
	    ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
	    MatrixToImageConfig config =  new MatrixToImageConfig(MatrixToImageConfig.BLACK, 0xffffff);
		MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream, config);
	    return pngOutputStream.toByteArray();
	}
}