package fr.smartprod.paperdms.business.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContractTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Contract getContractSample1() {
        return new Contract()
            .id(1L)
            .documentId(1L)
            .contractNumber("contractNumber1")
            .title("title1")
            .partyA("partyA1")
            .partyB("partyB1")
            .currency("currency1");
    }

    public static Contract getContractSample2() {
        return new Contract()
            .id(2L)
            .documentId(2L)
            .contractNumber("contractNumber2")
            .title("title2")
            .partyA("partyA2")
            .partyB("partyB2")
            .currency("currency2");
    }

    public static Contract getContractRandomSampleGenerator() {
        return new Contract()
            .id(longCount.incrementAndGet())
            .documentId(longCount.incrementAndGet())
            .contractNumber(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString())
            .partyA(UUID.randomUUID().toString())
            .partyB(UUID.randomUUID().toString())
            .currency(UUID.randomUUID().toString());
    }
}
