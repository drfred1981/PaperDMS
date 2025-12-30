package fr.smartprod.paperdms.workflow.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class WorkflowApprovalHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static WorkflowApprovalHistory getWorkflowApprovalHistorySample1() {
        return new WorkflowApprovalHistory()
            .id(1L)
            .documentSha256("documentSha2561")
            .stepNumber(1)
            .actionBy("actionBy1")
            .previousAssignee("previousAssignee1")
            .timeTaken(1L);
    }

    public static WorkflowApprovalHistory getWorkflowApprovalHistorySample2() {
        return new WorkflowApprovalHistory()
            .id(2L)
            .documentSha256("documentSha2562")
            .stepNumber(2)
            .actionBy("actionBy2")
            .previousAssignee("previousAssignee2")
            .timeTaken(2L);
    }

    public static WorkflowApprovalHistory getWorkflowApprovalHistoryRandomSampleGenerator() {
        return new WorkflowApprovalHistory()
            .id(longCount.incrementAndGet())
            .documentSha256(UUID.randomUUID().toString())
            .stepNumber(intCount.incrementAndGet())
            .actionBy(UUID.randomUUID().toString())
            .previousAssignee(UUID.randomUUID().toString())
            .timeTaken(longCount.incrementAndGet());
    }
}
