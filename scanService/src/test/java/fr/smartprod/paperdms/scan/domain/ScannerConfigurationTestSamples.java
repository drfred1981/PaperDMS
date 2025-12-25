package fr.smartprod.paperdms.scan.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ScannerConfigurationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ScannerConfiguration getScannerConfigurationSample1() {
        return new ScannerConfiguration()
            .id(1L)
            .name("name1")
            .ipAddress("ipAddress1")
            .port(1)
            .protocol("protocol1")
            .manufacturer("manufacturer1")
            .model("model1")
            .defaultResolution(1);
    }

    public static ScannerConfiguration getScannerConfigurationSample2() {
        return new ScannerConfiguration()
            .id(2L)
            .name("name2")
            .ipAddress("ipAddress2")
            .port(2)
            .protocol("protocol2")
            .manufacturer("manufacturer2")
            .model("model2")
            .defaultResolution(2);
    }

    public static ScannerConfiguration getScannerConfigurationRandomSampleGenerator() {
        return new ScannerConfiguration()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .ipAddress(UUID.randomUUID().toString())
            .port(intCount.incrementAndGet())
            .protocol(UUID.randomUUID().toString())
            .manufacturer(UUID.randomUUID().toString())
            .model(UUID.randomUUID().toString())
            .defaultResolution(intCount.incrementAndGet());
    }
}
