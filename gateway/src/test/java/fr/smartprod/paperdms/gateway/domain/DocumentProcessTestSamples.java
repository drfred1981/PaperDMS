package fr.smartprod.paperdms.gateway.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentProcessTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static DocumentProcess getDocumentProcessSample1() {
        return new DocumentProcess().id(1L).documentId(1L).documentSha256("documentSha2561");
    }

    public static DocumentProcess getDocumentProcessSample2() {
        return new DocumentProcess().id(2L).documentId(2L).documentSha256("documentSha2562");
    }

    public static DocumentProcess getDocumentProcessRandomSampleGenerator() {
        return new DocumentProcess()
            .id(longCount.incrementAndGet())
            .documentId(longCount.incrementAndGet())
            .documentSha256(UUID.randomUUID().toString());
    }
}
