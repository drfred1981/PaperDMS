package fr.smartprod.paperdms.business.web.rest;

import static fr.smartprod.paperdms.business.domain.InvoiceLineAsserts.*;
import static fr.smartprod.paperdms.business.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.business.IntegrationTest;
import fr.smartprod.paperdms.business.domain.Invoice;
import fr.smartprod.paperdms.business.domain.InvoiceLine;
import fr.smartprod.paperdms.business.repository.InvoiceLineRepository;
import fr.smartprod.paperdms.business.service.dto.InvoiceLineDTO;
import fr.smartprod.paperdms.business.service.mapper.InvoiceLineMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link InvoiceLineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvoiceLineResourceIT {

    private static final Long DEFAULT_INVOICE_ID = 1L;
    private static final Long UPDATED_INVOICE_ID = 2L;

    private static final Integer DEFAULT_LINE_NUMBER = 1;
    private static final Integer UPDATED_LINE_NUMBER = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_QUANTITY = 1D;
    private static final Double UPDATED_QUANTITY = 2D;

    private static final Double DEFAULT_UNIT_PRICE = 1D;
    private static final Double UPDATED_UNIT_PRICE = 2D;

    private static final Double DEFAULT_TAX_RATE = 1D;
    private static final Double UPDATED_TAX_RATE = 2D;

    private static final Double DEFAULT_TOTAL_AMOUNT_EXCL_TAX = 1D;
    private static final Double UPDATED_TOTAL_AMOUNT_EXCL_TAX = 2D;

    private static final String ENTITY_API_URL = "/api/invoice-lines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InvoiceLineRepository invoiceLineRepository;

    @Autowired
    private InvoiceLineMapper invoiceLineMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceLineMockMvc;

    private InvoiceLine invoiceLine;

    private InvoiceLine insertedInvoiceLine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceLine createEntity(EntityManager em) {
        InvoiceLine invoiceLine = new InvoiceLine()
            .invoiceId(DEFAULT_INVOICE_ID)
            .lineNumber(DEFAULT_LINE_NUMBER)
            .description(DEFAULT_DESCRIPTION)
            .quantity(DEFAULT_QUANTITY)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .taxRate(DEFAULT_TAX_RATE)
            .totalAmountExclTax(DEFAULT_TOTAL_AMOUNT_EXCL_TAX);
        // Add required entity
        Invoice invoice;
        if (TestUtil.findAll(em, Invoice.class).isEmpty()) {
            invoice = InvoiceResourceIT.createEntity();
            em.persist(invoice);
            em.flush();
        } else {
            invoice = TestUtil.findAll(em, Invoice.class).get(0);
        }
        invoiceLine.setInvoice(invoice);
        return invoiceLine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceLine createUpdatedEntity(EntityManager em) {
        InvoiceLine updatedInvoiceLine = new InvoiceLine()
            .invoiceId(UPDATED_INVOICE_ID)
            .lineNumber(UPDATED_LINE_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .taxRate(UPDATED_TAX_RATE)
            .totalAmountExclTax(UPDATED_TOTAL_AMOUNT_EXCL_TAX);
        // Add required entity
        Invoice invoice;
        if (TestUtil.findAll(em, Invoice.class).isEmpty()) {
            invoice = InvoiceResourceIT.createUpdatedEntity();
            em.persist(invoice);
            em.flush();
        } else {
            invoice = TestUtil.findAll(em, Invoice.class).get(0);
        }
        updatedInvoiceLine.setInvoice(invoice);
        return updatedInvoiceLine;
    }

    @BeforeEach
    void initTest() {
        invoiceLine = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedInvoiceLine != null) {
            invoiceLineRepository.delete(insertedInvoiceLine);
            insertedInvoiceLine = null;
        }
    }

    @Test
    @Transactional
    void createInvoiceLine() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InvoiceLine
        InvoiceLineDTO invoiceLineDTO = invoiceLineMapper.toDto(invoiceLine);
        var returnedInvoiceLineDTO = om.readValue(
            restInvoiceLineMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceLineDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InvoiceLineDTO.class
        );

        // Validate the InvoiceLine in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInvoiceLine = invoiceLineMapper.toEntity(returnedInvoiceLineDTO);
        assertInvoiceLineUpdatableFieldsEquals(returnedInvoiceLine, getPersistedInvoiceLine(returnedInvoiceLine));

        insertedInvoiceLine = returnedInvoiceLine;
    }

    @Test
    @Transactional
    void createInvoiceLineWithExistingId() throws Exception {
        // Create the InvoiceLine with an existing ID
        invoiceLine.setId(1L);
        InvoiceLineDTO invoiceLineDTO = invoiceLineMapper.toDto(invoiceLine);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceLineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceLine in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInvoiceIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceLine.setInvoiceId(null);

        // Create the InvoiceLine, which fails.
        InvoiceLineDTO invoiceLineDTO = invoiceLineMapper.toDto(invoiceLine);

        restInvoiceLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceLineDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLineNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceLine.setLineNumber(null);

        // Create the InvoiceLine, which fails.
        InvoiceLineDTO invoiceLineDTO = invoiceLineMapper.toDto(invoiceLine);

        restInvoiceLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceLineDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceLine.setDescription(null);

        // Create the InvoiceLine, which fails.
        InvoiceLineDTO invoiceLineDTO = invoiceLineMapper.toDto(invoiceLine);

        restInvoiceLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceLineDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceLine.setQuantity(null);

        // Create the InvoiceLine, which fails.
        InvoiceLineDTO invoiceLineDTO = invoiceLineMapper.toDto(invoiceLine);

        restInvoiceLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceLineDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUnitPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceLine.setUnitPrice(null);

        // Create the InvoiceLine, which fails.
        InvoiceLineDTO invoiceLineDTO = invoiceLineMapper.toDto(invoiceLine);

        restInvoiceLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceLineDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTaxRateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceLine.setTaxRate(null);

        // Create the InvoiceLine, which fails.
        InvoiceLineDTO invoiceLineDTO = invoiceLineMapper.toDto(invoiceLine);

        restInvoiceLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceLineDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalAmountExclTaxIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceLine.setTotalAmountExclTax(null);

        // Create the InvoiceLine, which fails.
        InvoiceLineDTO invoiceLineDTO = invoiceLineMapper.toDto(invoiceLine);

        restInvoiceLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceLineDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInvoiceLines() throws Exception {
        // Initialize the database
        insertedInvoiceLine = invoiceLineRepository.saveAndFlush(invoiceLine);

        // Get all the invoiceLineList
        restInvoiceLineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceId").value(hasItem(DEFAULT_INVOICE_ID.intValue())))
            .andExpect(jsonPath("$.[*].lineNumber").value(hasItem(DEFAULT_LINE_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE)))
            .andExpect(jsonPath("$.[*].taxRate").value(hasItem(DEFAULT_TAX_RATE)))
            .andExpect(jsonPath("$.[*].totalAmountExclTax").value(hasItem(DEFAULT_TOTAL_AMOUNT_EXCL_TAX)));
    }

    @Test
    @Transactional
    void getInvoiceLine() throws Exception {
        // Initialize the database
        insertedInvoiceLine = invoiceLineRepository.saveAndFlush(invoiceLine);

        // Get the invoiceLine
        restInvoiceLineMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceLine.getId().intValue()))
            .andExpect(jsonPath("$.invoiceId").value(DEFAULT_INVOICE_ID.intValue()))
            .andExpect(jsonPath("$.lineNumber").value(DEFAULT_LINE_NUMBER))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE))
            .andExpect(jsonPath("$.taxRate").value(DEFAULT_TAX_RATE))
            .andExpect(jsonPath("$.totalAmountExclTax").value(DEFAULT_TOTAL_AMOUNT_EXCL_TAX));
    }

    @Test
    @Transactional
    void getNonExistingInvoiceLine() throws Exception {
        // Get the invoiceLine
        restInvoiceLineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInvoiceLine() throws Exception {
        // Initialize the database
        insertedInvoiceLine = invoiceLineRepository.saveAndFlush(invoiceLine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceLine
        InvoiceLine updatedInvoiceLine = invoiceLineRepository.findById(invoiceLine.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInvoiceLine are not directly saved in db
        em.detach(updatedInvoiceLine);
        updatedInvoiceLine
            .invoiceId(UPDATED_INVOICE_ID)
            .lineNumber(UPDATED_LINE_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .taxRate(UPDATED_TAX_RATE)
            .totalAmountExclTax(UPDATED_TOTAL_AMOUNT_EXCL_TAX);
        InvoiceLineDTO invoiceLineDTO = invoiceLineMapper.toDto(updatedInvoiceLine);

        restInvoiceLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceLineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceLineDTO))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInvoiceLineToMatchAllProperties(updatedInvoiceLine);
    }

    @Test
    @Transactional
    void putNonExistingInvoiceLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceLine.setId(longCount.incrementAndGet());

        // Create the InvoiceLine
        InvoiceLineDTO invoiceLineDTO = invoiceLineMapper.toDto(invoiceLine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceLineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceLineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoiceLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceLine.setId(longCount.incrementAndGet());

        // Create the InvoiceLine
        InvoiceLineDTO invoiceLineDTO = invoiceLineMapper.toDto(invoiceLine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceLineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoiceLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceLine.setId(longCount.incrementAndGet());

        // Create the InvoiceLine
        InvoiceLineDTO invoiceLineDTO = invoiceLineMapper.toDto(invoiceLine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceLineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceLineDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceLineWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceLine = invoiceLineRepository.saveAndFlush(invoiceLine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceLine using partial update
        InvoiceLine partialUpdatedInvoiceLine = new InvoiceLine();
        partialUpdatedInvoiceLine.setId(invoiceLine.getId());

        partialUpdatedInvoiceLine.quantity(UPDATED_QUANTITY).unitPrice(UPDATED_UNIT_PRICE).taxRate(UPDATED_TAX_RATE);

        restInvoiceLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceLine))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceLine in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceLineUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInvoiceLine, invoiceLine),
            getPersistedInvoiceLine(invoiceLine)
        );
    }

    @Test
    @Transactional
    void fullUpdateInvoiceLineWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceLine = invoiceLineRepository.saveAndFlush(invoiceLine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceLine using partial update
        InvoiceLine partialUpdatedInvoiceLine = new InvoiceLine();
        partialUpdatedInvoiceLine.setId(invoiceLine.getId());

        partialUpdatedInvoiceLine
            .invoiceId(UPDATED_INVOICE_ID)
            .lineNumber(UPDATED_LINE_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .taxRate(UPDATED_TAX_RATE)
            .totalAmountExclTax(UPDATED_TOTAL_AMOUNT_EXCL_TAX);

        restInvoiceLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceLine))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceLine in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceLineUpdatableFieldsEquals(partialUpdatedInvoiceLine, getPersistedInvoiceLine(partialUpdatedInvoiceLine));
    }

    @Test
    @Transactional
    void patchNonExistingInvoiceLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceLine.setId(longCount.incrementAndGet());

        // Create the InvoiceLine
        InvoiceLineDTO invoiceLineDTO = invoiceLineMapper.toDto(invoiceLine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceLineDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceLineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoiceLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceLine.setId(longCount.incrementAndGet());

        // Create the InvoiceLine
        InvoiceLineDTO invoiceLineDTO = invoiceLineMapper.toDto(invoiceLine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceLineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoiceLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceLine.setId(longCount.incrementAndGet());

        // Create the InvoiceLine
        InvoiceLineDTO invoiceLineDTO = invoiceLineMapper.toDto(invoiceLine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceLineMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(invoiceLineDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoiceLine() throws Exception {
        // Initialize the database
        insertedInvoiceLine = invoiceLineRepository.saveAndFlush(invoiceLine);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the invoiceLine
        restInvoiceLineMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceLine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return invoiceLineRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected InvoiceLine getPersistedInvoiceLine(InvoiceLine invoiceLine) {
        return invoiceLineRepository.findById(invoiceLine.getId()).orElseThrow();
    }

    protected void assertPersistedInvoiceLineToMatchAllProperties(InvoiceLine expectedInvoiceLine) {
        assertInvoiceLineAllPropertiesEquals(expectedInvoiceLine, getPersistedInvoiceLine(expectedInvoiceLine));
    }

    protected void assertPersistedInvoiceLineToMatchUpdatableProperties(InvoiceLine expectedInvoiceLine) {
        assertInvoiceLineAllUpdatablePropertiesEquals(expectedInvoiceLine, getPersistedInvoiceLine(expectedInvoiceLine));
    }
}
