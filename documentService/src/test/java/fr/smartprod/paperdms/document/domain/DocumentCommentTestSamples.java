package fr.smartprod.paperdms.document.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentCommentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static DocumentComment getDocumentCommentSample1() {
        return new DocumentComment().id(1L).pageNumber(1).authorId("authorId1");
    }

    public static DocumentComment getDocumentCommentSample2() {
        return new DocumentComment().id(2L).pageNumber(2).authorId("authorId2");
    }

    public static DocumentComment getDocumentCommentRandomSampleGenerator() {
        return new DocumentComment()
            .id(longCount.incrementAndGet())
            .pageNumber(intCount.incrementAndGet())
            .authorId(UUID.randomUUID().toString());
    }
}
