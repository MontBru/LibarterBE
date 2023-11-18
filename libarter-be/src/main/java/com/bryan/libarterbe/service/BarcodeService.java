package com.bryan.libarterbe.service;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class BarcodeService {

    private String filterBase64String(String image)
    {
        image=image.replace("\"","");
        image=image.replace("}","");
        if(image.startsWith("{image:data:image/png;base64,")) {
            image = image.replace("{image:data:image/png;base64,", "");
        }
        return image;
    }
    public BinaryBitmap decodeBase64ToBinaryBitmap(String base64Image) throws IOException, NotFoundException {
        // Decode base64 image to a byte array
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        // Convert byte array to BufferedImage
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));

        // Create LuminanceSource and BinaryBitmap
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        return new BinaryBitmap(new HybridBinarizer(source));
    }
    public String readBarcode(String image) throws NotFoundException, IOException {
        try{
            image = filterBase64String(image);

            BinaryBitmap bitmap = decodeBase64ToBinaryBitmap(image);

            Result result= new MultiFormatReader().decode(bitmap);

            return result.getText();
        }
        catch(Exception e)
        {
            throw e;
        }
    }

}
