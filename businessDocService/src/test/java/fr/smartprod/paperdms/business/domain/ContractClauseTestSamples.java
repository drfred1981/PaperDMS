package fr.smartprod.paperdms.business.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContractClauseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static ContractClause getContractClauseSample1() {
        return new ContractClause().id(1L).contractId(1L).clauseNumber("clauseNumber1").title("title1");
    }

    public static ContractClause getContractClauseSample2() {
        return new ContractClause().id(2L).contractId(2L).clauseNumber("clauseNumber2").title("title2");
    }

    public static ContractClause getContractClauseRandomSampleGenerator() {
        return new ContractClause()
            .id(longCount.incrementAndGet())
            .contractId(longCount.incrementAndGet())
            .clauseNumber(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString());
    }
}
