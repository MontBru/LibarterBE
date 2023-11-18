package com.bryan.libarterbe.controller.User;

import com.bryan.libarterbe.service.BarcodeService;
import com.google.zxing.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/user/barcode")
public class BarcodeController {
    @Autowired
    BarcodeService barcodeService;

    @PostMapping("/readBarcode")
    public ResponseEntity<String> readBarcode(@RequestBody String image)
    {
        try {
            return ResponseEntity.ok(barcodeService.readBarcode(image));
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
