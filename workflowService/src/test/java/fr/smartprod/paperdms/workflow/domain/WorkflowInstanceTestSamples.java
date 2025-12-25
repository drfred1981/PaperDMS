package fr.smartprod.paperdms.workflow.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class WorkflowInstanceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static WorkflowInstance getWorkflowInstanceSample1() {
        return new WorkflowInstance().id(1L).documentId(1L).currentStepNumber(1).createdBy("createdBy1");
    }

    public static WorkflowInstance getWorkflowInstanceSample2() {
        return new WorkflowInstance().id(2L).documentId(2L).currentStepNumber(2).createdBy("createdBy2");
    }

    public static WorkflowInstance getWorkflowInstanceRandomSampleGenerator() {
        return new WorkflowInstance()
            .id(longCount.incrementAndGet())
            .documentId(longCount.incrementAndGet())
            .currentStepNumber(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString());
    }
}
