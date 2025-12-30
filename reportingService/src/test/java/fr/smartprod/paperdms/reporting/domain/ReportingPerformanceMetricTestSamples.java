package fr.smartprod.paperdms.reporting.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReportingPerformanceMetricTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ReportingPerformanceMetric getReportingPerformanceMetricSample1() {
        return new ReportingPerformanceMetric().id(1L).metricName("metricName1").unit("unit1").serviceName("serviceName1");
    }

    public static ReportingPerformanceMetric getReportingPerformanceMetricSample2() {
        return new ReportingPerformanceMetric().id(2L).metricName("metricName2").unit("unit2").serviceName("serviceName2");
    }

    public static ReportingPerformanceMetric getReportingPerformanceMetricRandomSampleGenerator() {
        return new ReportingPerformanceMetric()
            .id(longCount.incrementAndGet())
            .metricName(UUID.randomUUID().toString())
            .unit(UUID.randomUUID().toString())
            .serviceName(UUID.randomUUID().toString());
    }
}
