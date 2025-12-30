package fr.smartprod.paperdms.monitoring.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MonitoringDocumentWatchTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MonitoringDocumentWatch getMonitoringDocumentWatchSample1() {
        return new MonitoringDocumentWatch().id(1L).documentSha256("documentSha2561").userId("userId1");
    }

    public static MonitoringDocumentWatch getMonitoringDocumentWatchSample2() {
        return new MonitoringDocumentWatch().id(2L).documentSha256("documentSha2562").userId("userId2");
    }

    public static MonitoringDocumentWatch getMonitoringDocumentWatchRandomSampleGenerator() {
        return new MonitoringDocumentWatch()
            .id(longCount.incrementAndGet())
            .documentSha256(UUID.randomUUID().toString())
            .userId(UUID.randomUUID().toString());
    }
}
