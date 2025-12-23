package fr.smartprod.paperdms.workflow.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class WorkflowTaskTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static WorkflowTask getWorkflowTaskSample1() {
        return new WorkflowTask().id(1L).assigneeId("assigneeId1").delegatedTo("delegatedTo1");
    }

    public static WorkflowTask getWorkflowTaskSample2() {
        return new WorkflowTask().id(2L).assigneeId("assigneeId2").delegatedTo("delegatedTo2");
    }

    public static WorkflowTask getWorkflowTaskRandomSampleGenerator() {
        return new WorkflowTask()
            .id(longCount.incrementAndGet())
            .assigneeId(UUID.randomUUID().toString())
            .delegatedTo(UUID.randomUUID().toString());
    }
}
