package fr.smartprod.paperdms.business.service.mapper;

import static fr.smartprod.paperdms.business.domain.InvoiceLineAsserts.*;
import static fr.smartprod.paperdms.business.domain.InvoiceLineTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvoiceLineMapperTest {

    private InvoiceLineMapper invoiceLineMapper;

    @BeforeEach
    void setUp() {
        invoiceLineMapper = new InvoiceLineMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInvoiceLineSample1();
        var actual = invoiceLineMapper.toEntity(invoiceLineMapper.toDto(expected));
        assertInvoiceLineAllPropertiesEquals(expected, actual);
    }
}
