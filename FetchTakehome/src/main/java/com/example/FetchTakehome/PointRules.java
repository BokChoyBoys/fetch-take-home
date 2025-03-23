package com.example.FetchTakehome;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;

public final class PointRules {
    public static final Function<Receipt, Long> RETAILER_POINTS = PointRules::calculateRetailerPoints;
    public static final Function<Receipt, Long> TOTAL_CENTS_POINTS = PointRules::calculateCentsTotalPoints;
    public static final Function<Receipt, Long> ITEM_PAIRS_POINTS = PointRules::calculateItemPairsPoints;
    public static final Function<Receipt, Long> TRIMMED_DESC_POINTS = PointRules::calculateTrimmedPoints;
    public static final Function<Receipt, Long> ODD_DAYS_POINTS = PointRules::calculateOddPurchaseDatePoints;
    public static final Function<Receipt, Long> TIME_OF_PURCHASE_POINTS = PointRules::calculateTimeOfPurchasePoints;

    private static final String RETAILER_PATTERN = "^[\\w\\s\\-&]+$";
    private static final String TOTAL_PATTERN = "^\\d+\\.\\d{2}$";

    private static final String ITEM_SHORT_DESC_PATTERN = "^[\\w\\s\\-]+$";

    private static final String ITEM_PRICE_PATTERN = "^\\d+\\.\\d{2}$";
    public static boolean checkValidRetailer(String retailer) {
        return retailer != null && retailer.matches(RETAILER_PATTERN);
    }

    public static boolean checkValidTotal(String total) {
        return total != null && total.matches(TOTAL_PATTERN);
    }

    public static boolean checkValidItems(LineItem[] items) {
        if (items == null || items.length == 0) {
            return false;
        }
        for (LineItem item: items) {
            if (item.shortDescription() == null || !item.shortDescription().matches(ITEM_SHORT_DESC_PATTERN)) {
                return false;
            }

            if (item.price() == null || !item.price().matches(ITEM_PRICE_PATTERN)) {
                return false;
            }
        }
        return true;
    }

    private static long calculateRetailerPoints(Receipt receipt) {
        long points = 0L;
        for (int i = 0; i < receipt.retailer().length(); i++) {
            if (Character.isLetterOrDigit(receipt.retailer().charAt(i))) {
                points++;
            }
        }
        return points;
    }
    public static long calculateCentsTotalPoints(Receipt receipt) {
        long points = 0L;
        String[] dollarsAndCents = receipt.total().split("\\.");
        String dollars = dollarsAndCents[0];
        String cents = dollarsAndCents[1];

        if (cents.equals("00")) {
            points += 50;
        }

        if (Integer.parseInt(cents) % 25 == 0) {
            points += 25;
        }
        return points;
    }

    public static long calculateItemPairsPoints(Receipt receipt) {
        return 5L * (receipt.items().length / 2); // todo hard
    }

    public static long calculateTrimmedPoints(Receipt receipt) {
        long points = 0L;
        for (LineItem item: receipt.items()) {
            if (item.shortDescription().trim().length() % 3 == 0) {
                points += (long) Math.ceil(Double.parseDouble(item.price()) * 0.2);
            }
        }
        return points;
    }

    public static long calculateOddPurchaseDatePoints(Receipt receipt) {
        long points = 0L;
        if (receipt.purchaseDate().getDayOfMonth() % 2 == 1) {
            points += 6; // TODO not hardcode
        }
        return points;
    }

    public static long calculateTimeOfPurchasePoints(Receipt receipt) {
        long points = 0L;
        if (receipt.purchaseTime().isAfter(LocalTime.of(14, 0))
                && receipt.purchaseTime().isBefore(LocalTime.of(16, 0))) {
            points += 10;
        }
        return points;
    }

}
