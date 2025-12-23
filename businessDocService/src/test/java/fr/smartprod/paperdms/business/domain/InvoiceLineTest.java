package fr.smartprod.paperdms.business.domain;

import static fr.smartprod.paperdms.business.domain.InvoiceLineTestSamples.*;
import static fr.smartprod.paperdms.business.domain.InvoiceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.business.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoiceLineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceLine.class);
        InvoiceLine invoiceLine1 = getInvoiceLineSample1();
        InvoiceLine invoiceLine2 = new InvoiceLine();
        assertThat(invoiceLine1).isNotEqualTo(invoiceLine2);

        invoiceLine2.setId(invoiceLine1.getId());
        assertThat(invoiceLine1).isEqualTo(invoiceLine2);

        invoiceLine2 = getInvoiceLineSample2();
        assertThat(invoiceLine1).isNotEqualTo(invoiceLine2);
    }

    @Test
    void invoiceTest() {
        InvoiceLine invoiceLine = getInvoiceLineRandomSampleGenerator();
        Invoice invoiceBack = getInvoiceRandomSampleGenerator();

        invoiceLine.setInvoice(invoiceBack);
        assertThat(invoiceLine.getInvoice()).isEqualTo(invoiceBack);

        invoiceLine.invoice(null);
        assertThat(invoiceLine.getInvoice()).isNull();
    }
}
