package fr.smartprod.paperdms.business.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BankTransactionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static BankTransaction getBankTransactionSample1() {
        return new BankTransaction().id(1L).statementId(1L).description("description1");
    }

    public static BankTransaction getBankTransactionSample2() {
        return new BankTransaction().id(2L).statementId(2L).description("description2");
    }

    public static BankTransaction getBankTransactionRandomSampleGenerator() {
        return new BankTransaction()
            .id(longCount.incrementAndGet())
            .statementId(longCount.incrementAndGet())
            .description(UUID.randomUUID().toString());
    }
}
