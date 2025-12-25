package fr.smartprod.paperdms.workflow.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class WorkflowStepTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static WorkflowStep getWorkflowStepSample1() {
        return new WorkflowStep().id(1L).stepNumber(1).name("name1").assigneeId("assigneeId1").assigneeGroup("assigneeGroup1").dueInDays(1);
    }

    public static WorkflowStep getWorkflowStepSample2() {
        return new WorkflowStep().id(2L).stepNumber(2).name("name2").assigneeId("assigneeId2").assigneeGroup("assigneeGroup2").dueInDays(2);
    }

    public static WorkflowStep getWorkflowStepRandomSampleGenerator() {
        return new WorkflowStep()
            .id(longCount.incrementAndGet())
            .stepNumber(intCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .assigneeId(UUID.randomUUID().toString())
            .assigneeGroup(UUID.randomUUID().toString())
            .dueInDays(intCount.incrementAndGet());
    }
}
