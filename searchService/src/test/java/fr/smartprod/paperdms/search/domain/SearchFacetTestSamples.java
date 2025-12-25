package fr.smartprod.paperdms.search.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SearchFacetTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SearchFacet getSearchFacetSample1() {
        return new SearchFacet().id(1L).searchQueryId(1L).facetName("facetName1");
    }

    public static SearchFacet getSearchFacetSample2() {
        return new SearchFacet().id(2L).searchQueryId(2L).facetName("facetName2");
    }

    public static SearchFacet getSearchFacetRandomSampleGenerator() {
        return new SearchFacet()
            .id(longCount.incrementAndGet())
            .searchQueryId(longCount.incrementAndGet())
            .facetName(UUID.randomUUID().toString());
    }
}
