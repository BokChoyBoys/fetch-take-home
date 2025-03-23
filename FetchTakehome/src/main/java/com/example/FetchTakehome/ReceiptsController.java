package com.example.FetchTakehome;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

@RestController
@RequestMapping("/receipts")
public class ReceiptsController {
    private final ReceiptService receiptService = new ReceiptService();

    @PostMapping(value= "/process",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> processReceipt(@RequestBody Receipt receipt) {
        if (receipt == null || !PointRules.checkValidRetailer(receipt.retailer()) || !PointRules.checkValidTotal(receipt.total()) || !PointRules.checkValidItems(receipt.items())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("id", "The receipt is invalid."));
        }

        receiptService.processReceipt(receipt);
        return ResponseEntity.ok(Collections.singletonMap("id", receipt.id().toString()));
    }

    @GetMapping(value="/{id}/points",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getPoints(@PathVariable("id") String id) {
        if (!receiptService.containsReceipt(id) || !id.matches("^\\S+$")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("points", "No receipt found for that ID."));
        }
        return ResponseEntity.ok(Collections.singletonMap("points", receiptService.getPoints(id)));
    }
}