package fr.smartprod.paperdms.workflow.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ApprovalHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ApprovalHistory getApprovalHistorySample1() {
        return new ApprovalHistory()
            .id(1L)
            .documentId(1L)
            .workflowInstanceId(1L)
            .stepNumber(1)
            .actionBy("actionBy1")
            .previousAssignee("previousAssignee1")
            .timeTaken(1L);
    }

    public static ApprovalHistory getApprovalHistorySample2() {
        return new ApprovalHistory()
            .id(2L)
            .documentId(2L)
            .workflowInstanceId(2L)
            .stepNumber(2)
            .actionBy("actionBy2")
            .previousAssignee("previousAssignee2")
            .timeTaken(2L);
    }

    public static ApprovalHistory getApprovalHistoryRandomSampleGenerator() {
        return new ApprovalHistory()
            .id(longCount.incrementAndGet())
            .documentId(longCount.incrementAndGet())
            .workflowInstanceId(longCount.incrementAndGet())
            .stepNumber(intCount.incrementAndGet())
            .actionBy(UUID.randomUUID().toString())
            .previousAssignee(UUID.randomUUID().toString())
            .timeTaken(longCount.incrementAndGet());
    }
}
