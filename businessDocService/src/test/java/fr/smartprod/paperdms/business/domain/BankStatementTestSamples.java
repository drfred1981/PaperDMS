package fr.smartprod.paperdms.business.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BankStatementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BankStatement getBankStatementSample1() {
        return new BankStatement().id(1L).documentId(1L).accountNumber("accountNumber1").bankName("bankName1").currency("currency1");
    }

    public static BankStatement getBankStatementSample2() {
        return new BankStatement().id(2L).documentId(2L).accountNumber("accountNumber2").bankName("bankName2").currency("currency2");
    }

    public static BankStatement getBankStatementRandomSampleGenerator() {
        return new BankStatement()
            .id(longCount.incrementAndGet())
            .documentId(longCount.incrementAndGet())
            .accountNumber(UUID.randomUUID().toString())
            .bankName(UUID.randomUUID().toString())
            .currency(UUID.randomUUID().toString());
    }
}
