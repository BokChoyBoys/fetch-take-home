package com.example.FetchTakehome;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static com.example.FetchTakehome.PointRules.*;

@Service
public class ReceiptService {
    private final Map<String, Long> receiptPoints = new ConcurrentHashMap<>();

    private final List<Function<Receipt, Long>> defaultRules = List.of(RETAILER_POINTS, TOTAL_CENTS_POINTS, ITEM_PAIRS_POINTS, TRIMMED_DESC_POINTS, ODD_DAYS_POINTS, TIME_OF_PURCHASE_POINTS);
    public void processReceipt(Receipt receipt) {
        long points = calculatePoints(receipt);
        receiptPoints.put(receipt.id().toString(), points);
    }

    public long getPoints(String id) {
        return receiptPoints.getOrDefault(id, 0L);
    }

    public boolean containsReceipt(String id) {
        return receiptPoints.containsKey(id);
    }
    public long calculatePoints(Receipt receipt) {
        long points = 0L;

        for (Function<Receipt, Long> rule: defaultRules) {
            points += rule.apply(receipt);
        }

        return points;
    }
}
