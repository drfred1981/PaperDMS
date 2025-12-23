package fr.smartprod.paperdms.workflow.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class WorkflowTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Workflow getWorkflowSample1() {
        return new Workflow().id(1L).name("name1").version(1).triggerEvent("triggerEvent1").createdBy("createdBy1");
    }

    public static Workflow getWorkflowSample2() {
        return new Workflow().id(2L).name("name2").version(2).triggerEvent("triggerEvent2").createdBy("createdBy2");
    }

    public static Workflow getWorkflowRandomSampleGenerator() {
        return new Workflow()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .version(intCount.incrementAndGet())
            .triggerEvent(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString());
    }
}
