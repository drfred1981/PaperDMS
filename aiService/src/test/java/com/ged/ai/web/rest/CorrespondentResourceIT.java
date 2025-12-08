package com.ged.ai.web.rest;

import static com.ged.ai.domain.CorrespondentAsserts.*;
import static com.ged.ai.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ged.ai.IntegrationTest;
import com.ged.ai.domain.Correspondent;
import com.ged.ai.domain.CorrespondentExtraction;
import com.ged.ai.domain.enumeration.CorrespondentRole;
import com.ged.ai.domain.enumeration.CorrespondentType;
import com.ged.ai.repository.CorrespondentRepository;
import com.ged.ai.repository.search.CorrespondentSearchRepository;
import com.ged.ai.service.dto.CorrespondentDTO;
import com.ged.ai.service.mapper.CorrespondentMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CorrespondentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CorrespondentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY = "BBBBBBBBBB";

    private static final CorrespondentType DEFAULT_TYPE = CorrespondentType.PERSON;
    private static final CorrespondentType UPDATED_TYPE = CorrespondentType.ORGANIZATION;

    private static final CorrespondentRole DEFAULT_ROLE = CorrespondentRole.SENDER;
    private static final CorrespondentRole UPDATED_ROLE = CorrespondentRole.RECIPIENT;

    private static final Double DEFAULT_CONFIDENCE = 0D;
    private static final Double UPDATED_CONFIDENCE = 1D;
    private static final Double SMALLER_CONFIDENCE = 0D - 1D;

    private static final Boolean DEFAULT_IS_VERIFIED = false;
    private static final Boolean UPDATED_IS_VERIFIED = true;

    private static final String DEFAULT_VERIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_VERIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_VERIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VERIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_METADATA = "AAAAAAAAAA";
    private static final String UPDATED_METADATA = "BBBBBBBBBB";

    private static final Instant DEFAULT_EXTRACTED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXTRACTED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/correspondents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/correspondents/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CorrespondentRepository correspondentRepository;

    @Autowired
    private CorrespondentMapper correspondentMapper;

    @Autowired
    private CorrespondentSearchRepository correspondentSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCorrespondentMockMvc;

    private Correspondent correspondent;

    private Correspondent insertedCorrespondent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Correspondent createEntity(EntityManager em) {
        Correspondent correspondent = new Correspondent()
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .address(DEFAULT_ADDRESS)
            .company(DEFAULT_COMPANY)
            .type(DEFAULT_TYPE)
            .role(DEFAULT_ROLE)
            .confidence(DEFAULT_CONFIDENCE)
            .isVerified(DEFAULT_IS_VERIFIED)
            .verifiedBy(DEFAULT_VERIFIED_BY)
            .verifiedDate(DEFAULT_VERIFIED_DATE)
            .metadata(DEFAULT_METADATA)
            .extractedDate(DEFAULT_EXTRACTED_DATE);
        // Add required entity
        CorrespondentExtraction correspondentExtraction;
        if (TestUtil.findAll(em, CorrespondentExtraction.class).isEmpty()) {
            correspondentExtraction = CorrespondentExtractionResourceIT.createEntity();
            em.persist(correspondentExtraction);
            em.flush();
        } else {
            correspondentExtraction = TestUtil.findAll(em, CorrespondentExtraction.class).get(0);
        }
        correspondent.setExtraction(correspondentExtraction);
        return correspondent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Correspondent createUpdatedEntity(EntityManager em) {
        Correspondent updatedCorrespondent = new Correspondent()
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .company(UPDATED_COMPANY)
            .type(UPDATED_TYPE)
            .role(UPDATED_ROLE)
            .confidence(UPDATED_CONFIDENCE)
            .isVerified(UPDATED_IS_VERIFIED)
            .verifiedBy(UPDATED_VERIFIED_BY)
            .verifiedDate(UPDATED_VERIFIED_DATE)
            .metadata(UPDATED_METADATA)
            .extractedDate(UPDATED_EXTRACTED_DATE);
        // Add required entity
        CorrespondentExtraction correspondentExtraction;
        if (TestUtil.findAll(em, CorrespondentExtraction.class).isEmpty()) {
            correspondentExtraction = CorrespondentExtractionResourceIT.createUpdatedEntity();
            em.persist(correspondentExtraction);
            em.flush();
        } else {
            correspondentExtraction = TestUtil.findAll(em, CorrespondentExtraction.class).get(0);
        }
        updatedCorrespondent.setExtraction(correspondentExtraction);
        return updatedCorrespondent;
    }

    @BeforeEach
    void initTest() {
        correspondent = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCorrespondent != null) {
            correspondentRepository.delete(insertedCorrespondent);
            correspondentSearchRepository.delete(insertedCorrespondent);
            insertedCorrespondent = null;
        }
    }

    @Test
    @Transactional
    void createCorrespondent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(correspondentSearchRepository.findAll());
        // Create the Correspondent
        CorrespondentDTO correspondentDTO = correspondentMapper.toDto(correspondent);
        var returnedCorrespondentDTO = om.readValue(
            restCorrespondentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(correspondentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CorrespondentDTO.class
        );

        // Validate the Correspondent in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCorrespondent = correspondentMapper.toEntity(returnedCorrespondentDTO);
        assertCorrespondentUpdatableFieldsEquals(returnedCorrespondent, getPersistedCorrespondent(returnedCorrespondent));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(correspondentSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedCorrespondent = returnedCorrespondent;
    }

    @Test
    @Transactional
    void createCorrespondentWithExistingId() throws Exception {
        // Create the Correspondent with an existing ID
        correspondent.setId(1L);
        CorrespondentDTO correspondentDTO = correspondentMapper.toDto(correspondent);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(correspondentSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restCorrespondentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(correspondentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Correspondent in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(correspondentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(correspondentSearchRepository.findAll());
        // set the field null
        correspondent.setName(null);

        // Create the Correspondent, which fails.
        CorrespondentDTO correspondentDTO = correspondentMapper.toDto(correspondent);

        restCorrespondentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(correspondentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(correspondentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(correspondentSearchRepository.findAll());
        // set the field null
        correspondent.setType(null);

        // Create the Correspondent, which fails.
        CorrespondentDTO correspondentDTO = correspondentMapper.toDto(correspondent);

        restCorrespondentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(correspondentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(correspondentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkRoleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(correspondentSearchRepository.findAll());
        // set the field null
        correspondent.setRole(null);

        // Create the Correspondent, which fails.
        CorrespondentDTO correspondentDTO = correspondentMapper.toDto(correspondent);

        restCorrespondentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(correspondentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(correspondentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsVerifiedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(correspondentSearchRepository.findAll());
        // set the field null
        correspondent.setIsVerified(null);

        // Create the Correspondent, which fails.
        CorrespondentDTO correspondentDTO = correspondentMapper.toDto(correspondent);

        restCorrespondentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(correspondentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(correspondentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkExtractedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(correspondentSearchRepository.findAll());
        // set the field null
        correspondent.setExtractedDate(null);

        // Create the Correspondent, which fails.
        CorrespondentDTO correspondentDTO = correspondentMapper.toDto(correspondent);

        restCorrespondentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(correspondentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(correspondentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllCorrespondents() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList
        restCorrespondentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(correspondent.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].company").value(hasItem(DEFAULT_COMPANY)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].isVerified").value(hasItem(DEFAULT_IS_VERIFIED)))
            .andExpect(jsonPath("$.[*].verifiedBy").value(hasItem(DEFAULT_VERIFIED_BY)))
            .andExpect(jsonPath("$.[*].verifiedDate").value(hasItem(DEFAULT_VERIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)))
            .andExpect(jsonPath("$.[*].extractedDate").value(hasItem(DEFAULT_EXTRACTED_DATE.toString())));
    }

    @Test
    @Transactional
    void getCorrespondent() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get the correspondent
        restCorrespondentMockMvc
            .perform(get(ENTITY_API_URL_ID, correspondent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(correspondent.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.company").value(DEFAULT_COMPANY))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
            .andExpect(jsonPath("$.confidence").value(DEFAULT_CONFIDENCE))
            .andExpect(jsonPath("$.isVerified").value(DEFAULT_IS_VERIFIED))
            .andExpect(jsonPath("$.verifiedBy").value(DEFAULT_VERIFIED_BY))
            .andExpect(jsonPath("$.verifiedDate").value(DEFAULT_VERIFIED_DATE.toString()))
            .andExpect(jsonPath("$.metadata").value(DEFAULT_METADATA))
            .andExpect(jsonPath("$.extractedDate").value(DEFAULT_EXTRACTED_DATE.toString()));
    }

    @Test
    @Transactional
    void getCorrespondentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        Long id = correspondent.getId();

        defaultCorrespondentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCorrespondentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCorrespondentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCorrespondentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where name equals to
        defaultCorrespondentFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCorrespondentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where name in
        defaultCorrespondentFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCorrespondentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where name is not null
        defaultCorrespondentFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCorrespondentsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where name contains
        defaultCorrespondentFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCorrespondentsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where name does not contain
        defaultCorrespondentFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCorrespondentsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where email equals to
        defaultCorrespondentFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCorrespondentsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where email in
        defaultCorrespondentFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCorrespondentsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where email is not null
        defaultCorrespondentFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllCorrespondentsByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where email contains
        defaultCorrespondentFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCorrespondentsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where email does not contain
        defaultCorrespondentFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllCorrespondentsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where phone equals to
        defaultCorrespondentFiltering("phone.equals=" + DEFAULT_PHONE, "phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllCorrespondentsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where phone in
        defaultCorrespondentFiltering("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE, "phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllCorrespondentsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where phone is not null
        defaultCorrespondentFiltering("phone.specified=true", "phone.specified=false");
    }

    @Test
    @Transactional
    void getAllCorrespondentsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where phone contains
        defaultCorrespondentFiltering("phone.contains=" + DEFAULT_PHONE, "phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllCorrespondentsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where phone does not contain
        defaultCorrespondentFiltering("phone.doesNotContain=" + UPDATED_PHONE, "phone.doesNotContain=" + DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void getAllCorrespondentsByCompanyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where company equals to
        defaultCorrespondentFiltering("company.equals=" + DEFAULT_COMPANY, "company.equals=" + UPDATED_COMPANY);
    }

    @Test
    @Transactional
    void getAllCorrespondentsByCompanyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where company in
        defaultCorrespondentFiltering("company.in=" + DEFAULT_COMPANY + "," + UPDATED_COMPANY, "company.in=" + UPDATED_COMPANY);
    }

    @Test
    @Transactional
    void getAllCorrespondentsByCompanyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where company is not null
        defaultCorrespondentFiltering("company.specified=true", "company.specified=false");
    }

    @Test
    @Transactional
    void getAllCorrespondentsByCompanyContainsSomething() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where company contains
        defaultCorrespondentFiltering("company.contains=" + DEFAULT_COMPANY, "company.contains=" + UPDATED_COMPANY);
    }

    @Test
    @Transactional
    void getAllCorrespondentsByCompanyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where company does not contain
        defaultCorrespondentFiltering("company.doesNotContain=" + UPDATED_COMPANY, "company.doesNotContain=" + DEFAULT_COMPANY);
    }

    @Test
    @Transactional
    void getAllCorrespondentsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where type equals to
        defaultCorrespondentFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllCorrespondentsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where type in
        defaultCorrespondentFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllCorrespondentsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where type is not null
        defaultCorrespondentFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllCorrespondentsByRoleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where role equals to
        defaultCorrespondentFiltering("role.equals=" + DEFAULT_ROLE, "role.equals=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    void getAllCorrespondentsByRoleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where role in
        defaultCorrespondentFiltering("role.in=" + DEFAULT_ROLE + "," + UPDATED_ROLE, "role.in=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    void getAllCorrespondentsByRoleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where role is not null
        defaultCorrespondentFiltering("role.specified=true", "role.specified=false");
    }

    @Test
    @Transactional
    void getAllCorrespondentsByConfidenceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where confidence equals to
        defaultCorrespondentFiltering("confidence.equals=" + DEFAULT_CONFIDENCE, "confidence.equals=" + UPDATED_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllCorrespondentsByConfidenceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where confidence in
        defaultCorrespondentFiltering(
            "confidence.in=" + DEFAULT_CONFIDENCE + "," + UPDATED_CONFIDENCE,
            "confidence.in=" + UPDATED_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllCorrespondentsByConfidenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where confidence is not null
        defaultCorrespondentFiltering("confidence.specified=true", "confidence.specified=false");
    }

    @Test
    @Transactional
    void getAllCorrespondentsByConfidenceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where confidence is greater than or equal to
        defaultCorrespondentFiltering(
            "confidence.greaterThanOrEqual=" + DEFAULT_CONFIDENCE,
            "confidence.greaterThanOrEqual=" + (DEFAULT_CONFIDENCE + 1)
        );
    }

    @Test
    @Transactional
    void getAllCorrespondentsByConfidenceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where confidence is less than or equal to
        defaultCorrespondentFiltering(
            "confidence.lessThanOrEqual=" + DEFAULT_CONFIDENCE,
            "confidence.lessThanOrEqual=" + SMALLER_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllCorrespondentsByConfidenceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where confidence is less than
        defaultCorrespondentFiltering("confidence.lessThan=" + (DEFAULT_CONFIDENCE + 1), "confidence.lessThan=" + DEFAULT_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllCorrespondentsByConfidenceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where confidence is greater than
        defaultCorrespondentFiltering("confidence.greaterThan=" + SMALLER_CONFIDENCE, "confidence.greaterThan=" + DEFAULT_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllCorrespondentsByIsVerifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where isVerified equals to
        defaultCorrespondentFiltering("isVerified.equals=" + DEFAULT_IS_VERIFIED, "isVerified.equals=" + UPDATED_IS_VERIFIED);
    }

    @Test
    @Transactional
    void getAllCorrespondentsByIsVerifiedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where isVerified in
        defaultCorrespondentFiltering(
            "isVerified.in=" + DEFAULT_IS_VERIFIED + "," + UPDATED_IS_VERIFIED,
            "isVerified.in=" + UPDATED_IS_VERIFIED
        );
    }

    @Test
    @Transactional
    void getAllCorrespondentsByIsVerifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where isVerified is not null
        defaultCorrespondentFiltering("isVerified.specified=true", "isVerified.specified=false");
    }

    @Test
    @Transactional
    void getAllCorrespondentsByVerifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where verifiedBy equals to
        defaultCorrespondentFiltering("verifiedBy.equals=" + DEFAULT_VERIFIED_BY, "verifiedBy.equals=" + UPDATED_VERIFIED_BY);
    }

    @Test
    @Transactional
    void getAllCorrespondentsByVerifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where verifiedBy in
        defaultCorrespondentFiltering(
            "verifiedBy.in=" + DEFAULT_VERIFIED_BY + "," + UPDATED_VERIFIED_BY,
            "verifiedBy.in=" + UPDATED_VERIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllCorrespondentsByVerifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where verifiedBy is not null
        defaultCorrespondentFiltering("verifiedBy.specified=true", "verifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCorrespondentsByVerifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where verifiedBy contains
        defaultCorrespondentFiltering("verifiedBy.contains=" + DEFAULT_VERIFIED_BY, "verifiedBy.contains=" + UPDATED_VERIFIED_BY);
    }

    @Test
    @Transactional
    void getAllCorrespondentsByVerifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where verifiedBy does not contain
        defaultCorrespondentFiltering(
            "verifiedBy.doesNotContain=" + UPDATED_VERIFIED_BY,
            "verifiedBy.doesNotContain=" + DEFAULT_VERIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllCorrespondentsByVerifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where verifiedDate equals to
        defaultCorrespondentFiltering("verifiedDate.equals=" + DEFAULT_VERIFIED_DATE, "verifiedDate.equals=" + UPDATED_VERIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllCorrespondentsByVerifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where verifiedDate in
        defaultCorrespondentFiltering(
            "verifiedDate.in=" + DEFAULT_VERIFIED_DATE + "," + UPDATED_VERIFIED_DATE,
            "verifiedDate.in=" + UPDATED_VERIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllCorrespondentsByVerifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where verifiedDate is not null
        defaultCorrespondentFiltering("verifiedDate.specified=true", "verifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCorrespondentsByExtractedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where extractedDate equals to
        defaultCorrespondentFiltering("extractedDate.equals=" + DEFAULT_EXTRACTED_DATE, "extractedDate.equals=" + UPDATED_EXTRACTED_DATE);
    }

    @Test
    @Transactional
    void getAllCorrespondentsByExtractedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where extractedDate in
        defaultCorrespondentFiltering(
            "extractedDate.in=" + DEFAULT_EXTRACTED_DATE + "," + UPDATED_EXTRACTED_DATE,
            "extractedDate.in=" + UPDATED_EXTRACTED_DATE
        );
    }

    @Test
    @Transactional
    void getAllCorrespondentsByExtractedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        // Get all the correspondentList where extractedDate is not null
        defaultCorrespondentFiltering("extractedDate.specified=true", "extractedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCorrespondentsByExtractionIsEqualToSomething() throws Exception {
        CorrespondentExtraction extraction;
        if (TestUtil.findAll(em, CorrespondentExtraction.class).isEmpty()) {
            correspondentRepository.saveAndFlush(correspondent);
            extraction = CorrespondentExtractionResourceIT.createEntity();
        } else {
            extraction = TestUtil.findAll(em, CorrespondentExtraction.class).get(0);
        }
        em.persist(extraction);
        em.flush();
        correspondent.setExtraction(extraction);
        correspondentRepository.saveAndFlush(correspondent);
        Long extractionId = extraction.getId();
        // Get all the correspondentList where extraction equals to extractionId
        defaultCorrespondentShouldBeFound("extractionId.equals=" + extractionId);

        // Get all the correspondentList where extraction equals to (extractionId + 1)
        defaultCorrespondentShouldNotBeFound("extractionId.equals=" + (extractionId + 1));
    }

    private void defaultCorrespondentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCorrespondentShouldBeFound(shouldBeFound);
        defaultCorrespondentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCorrespondentShouldBeFound(String filter) throws Exception {
        restCorrespondentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(correspondent.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].company").value(hasItem(DEFAULT_COMPANY)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].isVerified").value(hasItem(DEFAULT_IS_VERIFIED)))
            .andExpect(jsonPath("$.[*].verifiedBy").value(hasItem(DEFAULT_VERIFIED_BY)))
            .andExpect(jsonPath("$.[*].verifiedDate").value(hasItem(DEFAULT_VERIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)))
            .andExpect(jsonPath("$.[*].extractedDate").value(hasItem(DEFAULT_EXTRACTED_DATE.toString())));

        // Check, that the count call also returns 1
        restCorrespondentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCorrespondentShouldNotBeFound(String filter) throws Exception {
        restCorrespondentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCorrespondentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCorrespondent() throws Exception {
        // Get the correspondent
        restCorrespondentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCorrespondent() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        correspondentSearchRepository.save(correspondent);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(correspondentSearchRepository.findAll());

        // Update the correspondent
        Correspondent updatedCorrespondent = correspondentRepository.findById(correspondent.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCorrespondent are not directly saved in db
        em.detach(updatedCorrespondent);
        updatedCorrespondent
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .company(UPDATED_COMPANY)
            .type(UPDATED_TYPE)
            .role(UPDATED_ROLE)
            .confidence(UPDATED_CONFIDENCE)
            .isVerified(UPDATED_IS_VERIFIED)
            .verifiedBy(UPDATED_VERIFIED_BY)
            .verifiedDate(UPDATED_VERIFIED_DATE)
            .metadata(UPDATED_METADATA)
            .extractedDate(UPDATED_EXTRACTED_DATE);
        CorrespondentDTO correspondentDTO = correspondentMapper.toDto(updatedCorrespondent);

        restCorrespondentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, correspondentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(correspondentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Correspondent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCorrespondentToMatchAllProperties(updatedCorrespondent);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(correspondentSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Correspondent> correspondentSearchList = Streamable.of(correspondentSearchRepository.findAll()).toList();
                Correspondent testCorrespondentSearch = correspondentSearchList.get(searchDatabaseSizeAfter - 1);

                assertCorrespondentAllPropertiesEquals(testCorrespondentSearch, updatedCorrespondent);
            });
    }

    @Test
    @Transactional
    void putNonExistingCorrespondent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(correspondentSearchRepository.findAll());
        correspondent.setId(longCount.incrementAndGet());

        // Create the Correspondent
        CorrespondentDTO correspondentDTO = correspondentMapper.toDto(correspondent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCorrespondentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, correspondentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(correspondentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Correspondent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(correspondentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchCorrespondent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(correspondentSearchRepository.findAll());
        correspondent.setId(longCount.incrementAndGet());

        // Create the Correspondent
        CorrespondentDTO correspondentDTO = correspondentMapper.toDto(correspondent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorrespondentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(correspondentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Correspondent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(correspondentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCorrespondent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(correspondentSearchRepository.findAll());
        correspondent.setId(longCount.incrementAndGet());

        // Create the Correspondent
        CorrespondentDTO correspondentDTO = correspondentMapper.toDto(correspondent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorrespondentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(correspondentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Correspondent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(correspondentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateCorrespondentWithPatch() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the correspondent using partial update
        Correspondent partialUpdatedCorrespondent = new Correspondent();
        partialUpdatedCorrespondent.setId(correspondent.getId());

        partialUpdatedCorrespondent
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .company(UPDATED_COMPANY)
            .type(UPDATED_TYPE)
            .role(UPDATED_ROLE)
            .verifiedBy(UPDATED_VERIFIED_BY)
            .verifiedDate(UPDATED_VERIFIED_DATE)
            .metadata(UPDATED_METADATA)
            .extractedDate(UPDATED_EXTRACTED_DATE);

        restCorrespondentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCorrespondent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCorrespondent))
            )
            .andExpect(status().isOk());

        // Validate the Correspondent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCorrespondentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCorrespondent, correspondent),
            getPersistedCorrespondent(correspondent)
        );
    }

    @Test
    @Transactional
    void fullUpdateCorrespondentWithPatch() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the correspondent using partial update
        Correspondent partialUpdatedCorrespondent = new Correspondent();
        partialUpdatedCorrespondent.setId(correspondent.getId());

        partialUpdatedCorrespondent
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .company(UPDATED_COMPANY)
            .type(UPDATED_TYPE)
            .role(UPDATED_ROLE)
            .confidence(UPDATED_CONFIDENCE)
            .isVerified(UPDATED_IS_VERIFIED)
            .verifiedBy(UPDATED_VERIFIED_BY)
            .verifiedDate(UPDATED_VERIFIED_DATE)
            .metadata(UPDATED_METADATA)
            .extractedDate(UPDATED_EXTRACTED_DATE);

        restCorrespondentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCorrespondent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCorrespondent))
            )
            .andExpect(status().isOk());

        // Validate the Correspondent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCorrespondentUpdatableFieldsEquals(partialUpdatedCorrespondent, getPersistedCorrespondent(partialUpdatedCorrespondent));
    }

    @Test
    @Transactional
    void patchNonExistingCorrespondent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(correspondentSearchRepository.findAll());
        correspondent.setId(longCount.incrementAndGet());

        // Create the Correspondent
        CorrespondentDTO correspondentDTO = correspondentMapper.toDto(correspondent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCorrespondentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, correspondentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(correspondentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Correspondent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(correspondentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCorrespondent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(correspondentSearchRepository.findAll());
        correspondent.setId(longCount.incrementAndGet());

        // Create the Correspondent
        CorrespondentDTO correspondentDTO = correspondentMapper.toDto(correspondent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorrespondentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(correspondentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Correspondent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(correspondentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCorrespondent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(correspondentSearchRepository.findAll());
        correspondent.setId(longCount.incrementAndGet());

        // Create the Correspondent
        CorrespondentDTO correspondentDTO = correspondentMapper.toDto(correspondent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorrespondentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(correspondentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Correspondent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(correspondentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteCorrespondent() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);
        correspondentRepository.save(correspondent);
        correspondentSearchRepository.save(correspondent);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(correspondentSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the correspondent
        restCorrespondentMockMvc
            .perform(delete(ENTITY_API_URL_ID, correspondent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(correspondentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchCorrespondent() throws Exception {
        // Initialize the database
        insertedCorrespondent = correspondentRepository.saveAndFlush(correspondent);
        correspondentSearchRepository.save(correspondent);

        // Search the correspondent
        restCorrespondentMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + correspondent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(correspondent.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].company").value(hasItem(DEFAULT_COMPANY)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].isVerified").value(hasItem(DEFAULT_IS_VERIFIED)))
            .andExpect(jsonPath("$.[*].verifiedBy").value(hasItem(DEFAULT_VERIFIED_BY)))
            .andExpect(jsonPath("$.[*].verifiedDate").value(hasItem(DEFAULT_VERIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA.toString())))
            .andExpect(jsonPath("$.[*].extractedDate").value(hasItem(DEFAULT_EXTRACTED_DATE.toString())));
    }

    protected long getRepositoryCount() {
        return correspondentRepository.count();
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

    protected Correspondent getPersistedCorrespondent(Correspondent correspondent) {
        return correspondentRepository.findById(correspondent.getId()).orElseThrow();
    }

    protected void assertPersistedCorrespondentToMatchAllProperties(Correspondent expectedCorrespondent) {
        assertCorrespondentAllPropertiesEquals(expectedCorrespondent, getPersistedCorrespondent(expectedCorrespondent));
    }

    protected void assertPersistedCorrespondentToMatchUpdatableProperties(Correspondent expectedCorrespondent) {
        assertCorrespondentAllUpdatablePropertiesEquals(expectedCorrespondent, getPersistedCorrespondent(expectedCorrespondent));
    }
}
