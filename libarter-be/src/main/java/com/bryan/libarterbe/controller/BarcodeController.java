package com.bryan.libarterbe.controller;

import com.bryan.libarterbe.service.BarcodeService;
import com.google.zxing.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/user/barcode")
@CrossOrigin("*")
public class BarcodeController {
    @Autowired
    BarcodeService barcodeService;

    @PostMapping("/readBarcode")
    public ResponseEntity<String> readBarcode(@RequestBody String image)
    {
        image=image.replace("\"","");
        if(image.startsWith("data:image/png;base64,"))
        {
            image = image.replace("data:image/png;base64,","");
        }
        try {
            return ResponseEntity.ok(barcodeService.readBarcode(image));
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
