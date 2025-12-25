package fr.smartprod.paperdms.business.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class InvoiceLineTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static InvoiceLine getInvoiceLineSample1() {
        return new InvoiceLine().id(1L).invoiceId(1L).lineNumber(1).description("description1");
    }

    public static InvoiceLine getInvoiceLineSample2() {
        return new InvoiceLine().id(2L).invoiceId(2L).lineNumber(2).description("description2");
    }

    public static InvoiceLine getInvoiceLineRandomSampleGenerator() {
        return new InvoiceLine()
            .id(longCount.incrementAndGet())
            .invoiceId(longCount.incrementAndGet())
            .lineNumber(intCount.incrementAndGet())
            .description(UUID.randomUUID().toString());
    }
}
