package fr.smartprod.paperdms.business.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InvoiceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Invoice getInvoiceSample1() {
        return new Invoice()
            .id(1L)
            .documentId(1L)
            .invoiceNumber("invoiceNumber1")
            .supplierName("supplierName1")
            .customerName("customerName1")
            .currency("currency1");
    }

    public static Invoice getInvoiceSample2() {
        return new Invoice()
            .id(2L)
            .documentId(2L)
            .invoiceNumber("invoiceNumber2")
            .supplierName("supplierName2")
            .customerName("customerName2")
            .currency("currency2");
    }

    public static Invoice getInvoiceRandomSampleGenerator() {
        return new Invoice()
            .id(longCount.incrementAndGet())
            .documentId(longCount.incrementAndGet())
            .invoiceNumber(UUID.randomUUID().toString())
            .supplierName(UUID.randomUUID().toString())
            .customerName(UUID.randomUUID().toString())
            .currency(UUID.randomUUID().toString());
    }
}
