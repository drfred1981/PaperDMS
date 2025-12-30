package fr.smartprod.paperdms.emailimportdocument.web.rest;

import static fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportMappingAsserts.*;
import static fr.smartprod.paperdms.emailimportdocument.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.emailimportdocument.IntegrationTest;
import fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportMapping;
import fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportRule;
import fr.smartprod.paperdms.emailimportdocument.domain.enumeration.EmailField;
import fr.smartprod.paperdms.emailimportdocument.domain.enumeration.MappingTransformation;
import fr.smartprod.paperdms.emailimportdocument.repository.EmailImportImportMappingRepository;
import fr.smartprod.paperdms.emailimportdocument.service.dto.EmailImportImportMappingDTO;
import fr.smartprod.paperdms.emailimportdocument.service.mapper.EmailImportImportMappingMapper;
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
 * Integration tests for the {@link EmailImportImportMappingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmailImportImportMappingResourceIT {

    private static final EmailField DEFAULT_EMAIL_FIELD = EmailField.FROM;
    private static final EmailField UPDATED_EMAIL_FIELD = EmailField.TO;

    private static final String DEFAULT_DOCUMENT_FIELD = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_FIELD = "BBBBBBBBBB";

    private static final MappingTransformation DEFAULT_TRANSFORMATION = MappingTransformation.NONE;
    private static final MappingTransformation UPDATED_TRANSFORMATION = MappingTransformation.UPPERCASE;

    private static final String DEFAULT_TRANSFORMATION_CONFIG = "AAAAAAAAAA";
    private static final String UPDATED_TRANSFORMATION_CONFIG = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_REQUIRED = false;
    private static final Boolean UPDATED_IS_REQUIRED = true;

    private static final String DEFAULT_DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_DEFAULT_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_VALIDATION_REGEX = "AAAAAAAAAA";
    private static final String UPDATED_VALIDATION_REGEX = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/email-import-import-mappings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmailImportImportMappingRepository emailImportImportMappingRepository;

    @Autowired
    private EmailImportImportMappingMapper emailImportImportMappingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmailImportImportMappingMockMvc;

    private EmailImportImportMapping emailImportImportMapping;

    private EmailImportImportMapping insertedEmailImportImportMapping;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmailImportImportMapping createEntity() {
        return new EmailImportImportMapping()
            .emailField(DEFAULT_EMAIL_FIELD)
            .documentField(DEFAULT_DOCUMENT_FIELD)
            .transformation(DEFAULT_TRANSFORMATION)
            .transformationConfig(DEFAULT_TRANSFORMATION_CONFIG)
            .isRequired(DEFAULT_IS_REQUIRED)
            .defaultValue(DEFAULT_DEFAULT_VALUE)
            .validationRegex(DEFAULT_VALIDATION_REGEX);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmailImportImportMapping createUpdatedEntity() {
        return new EmailImportImportMapping()
            .emailField(UPDATED_EMAIL_FIELD)
            .documentField(UPDATED_DOCUMENT_FIELD)
            .transformation(UPDATED_TRANSFORMATION)
            .transformationConfig(UPDATED_TRANSFORMATION_CONFIG)
            .isRequired(UPDATED_IS_REQUIRED)
            .defaultValue(UPDATED_DEFAULT_VALUE)
            .validationRegex(UPDATED_VALIDATION_REGEX);
    }

    @BeforeEach
    void initTest() {
        emailImportImportMapping = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEmailImportImportMapping != null) {
            emailImportImportMappingRepository.delete(insertedEmailImportImportMapping);
            insertedEmailImportImportMapping = null;
        }
    }

    @Test
    @Transactional
    void createEmailImportImportMapping() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EmailImportImportMapping
        EmailImportImportMappingDTO emailImportImportMappingDTO = emailImportImportMappingMapper.toDto(emailImportImportMapping);
        var returnedEmailImportImportMappingDTO = om.readValue(
            restEmailImportImportMappingMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportImportMappingDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmailImportImportMappingDTO.class
        );

        // Validate the EmailImportImportMapping in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEmailImportImportMapping = emailImportImportMappingMapper.toEntity(returnedEmailImportImportMappingDTO);
        assertEmailImportImportMappingUpdatableFieldsEquals(
            returnedEmailImportImportMapping,
            getPersistedEmailImportImportMapping(returnedEmailImportImportMapping)
        );

        insertedEmailImportImportMapping = returnedEmailImportImportMapping;
    }

    @Test
    @Transactional
    void createEmailImportImportMappingWithExistingId() throws Exception {
        // Create the EmailImportImportMapping with an existing ID
        emailImportImportMapping.setId(1L);
        EmailImportImportMappingDTO emailImportImportMappingDTO = emailImportImportMappingMapper.toDto(emailImportImportMapping);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmailImportImportMappingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportImportMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailImportImportMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmailFieldIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emailImportImportMapping.setEmailField(null);

        // Create the EmailImportImportMapping, which fails.
        EmailImportImportMappingDTO emailImportImportMappingDTO = emailImportImportMappingMapper.toDto(emailImportImportMapping);

        restEmailImportImportMappingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportImportMappingDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentFieldIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emailImportImportMapping.setDocumentField(null);

        // Create the EmailImportImportMapping, which fails.
        EmailImportImportMappingDTO emailImportImportMappingDTO = emailImportImportMappingMapper.toDto(emailImportImportMapping);

        restEmailImportImportMappingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportImportMappingDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsRequiredIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emailImportImportMapping.setIsRequired(null);

        // Create the EmailImportImportMapping, which fails.
        EmailImportImportMappingDTO emailImportImportMappingDTO = emailImportImportMappingMapper.toDto(emailImportImportMapping);

        restEmailImportImportMappingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportImportMappingDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmailImportImportMappings() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        // Get all the emailImportImportMappingList
        restEmailImportImportMappingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailImportImportMapping.getId().intValue())))
            .andExpect(jsonPath("$.[*].emailField").value(hasItem(DEFAULT_EMAIL_FIELD.toString())))
            .andExpect(jsonPath("$.[*].documentField").value(hasItem(DEFAULT_DOCUMENT_FIELD)))
            .andExpect(jsonPath("$.[*].transformation").value(hasItem(DEFAULT_TRANSFORMATION.toString())))
            .andExpect(jsonPath("$.[*].transformationConfig").value(hasItem(DEFAULT_TRANSFORMATION_CONFIG)))
            .andExpect(jsonPath("$.[*].isRequired").value(hasItem(DEFAULT_IS_REQUIRED)))
            .andExpect(jsonPath("$.[*].defaultValue").value(hasItem(DEFAULT_DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].validationRegex").value(hasItem(DEFAULT_VALIDATION_REGEX)));
    }

    @Test
    @Transactional
    void getEmailImportImportMapping() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        // Get the emailImportImportMapping
        restEmailImportImportMappingMockMvc
            .perform(get(ENTITY_API_URL_ID, emailImportImportMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(emailImportImportMapping.getId().intValue()))
            .andExpect(jsonPath("$.emailField").value(DEFAULT_EMAIL_FIELD.toString()))
            .andExpect(jsonPath("$.documentField").value(DEFAULT_DOCUMENT_FIELD))
            .andExpect(jsonPath("$.transformation").value(DEFAULT_TRANSFORMATION.toString()))
            .andExpect(jsonPath("$.transformationConfig").value(DEFAULT_TRANSFORMATION_CONFIG))
            .andExpect(jsonPath("$.isRequired").value(DEFAULT_IS_REQUIRED))
            .andExpect(jsonPath("$.defaultValue").value(DEFAULT_DEFAULT_VALUE))
            .andExpect(jsonPath("$.validationRegex").value(DEFAULT_VALIDATION_REGEX));
    }

    @Test
    @Transactional
    void getEmailImportImportMappingsByIdFiltering() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        Long id = emailImportImportMapping.getId();

        defaultEmailImportImportMappingFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEmailImportImportMappingFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEmailImportImportMappingFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmailImportImportMappingsByEmailFieldIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        // Get all the emailImportImportMappingList where emailField equals to
        defaultEmailImportImportMappingFiltering("emailField.equals=" + DEFAULT_EMAIL_FIELD, "emailField.equals=" + UPDATED_EMAIL_FIELD);
    }

    @Test
    @Transactional
    void getAllEmailImportImportMappingsByEmailFieldIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        // Get all the emailImportImportMappingList where emailField in
        defaultEmailImportImportMappingFiltering(
            "emailField.in=" + DEFAULT_EMAIL_FIELD + "," + UPDATED_EMAIL_FIELD,
            "emailField.in=" + UPDATED_EMAIL_FIELD
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportMappingsByEmailFieldIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        // Get all the emailImportImportMappingList where emailField is not null
        defaultEmailImportImportMappingFiltering("emailField.specified=true", "emailField.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportImportMappingsByDocumentFieldIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        // Get all the emailImportImportMappingList where documentField equals to
        defaultEmailImportImportMappingFiltering(
            "documentField.equals=" + DEFAULT_DOCUMENT_FIELD,
            "documentField.equals=" + UPDATED_DOCUMENT_FIELD
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportMappingsByDocumentFieldIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        // Get all the emailImportImportMappingList where documentField in
        defaultEmailImportImportMappingFiltering(
            "documentField.in=" + DEFAULT_DOCUMENT_FIELD + "," + UPDATED_DOCUMENT_FIELD,
            "documentField.in=" + UPDATED_DOCUMENT_FIELD
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportMappingsByDocumentFieldIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        // Get all the emailImportImportMappingList where documentField is not null
        defaultEmailImportImportMappingFiltering("documentField.specified=true", "documentField.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportImportMappingsByDocumentFieldContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        // Get all the emailImportImportMappingList where documentField contains
        defaultEmailImportImportMappingFiltering(
            "documentField.contains=" + DEFAULT_DOCUMENT_FIELD,
            "documentField.contains=" + UPDATED_DOCUMENT_FIELD
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportMappingsByDocumentFieldNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        // Get all the emailImportImportMappingList where documentField does not contain
        defaultEmailImportImportMappingFiltering(
            "documentField.doesNotContain=" + UPDATED_DOCUMENT_FIELD,
            "documentField.doesNotContain=" + DEFAULT_DOCUMENT_FIELD
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportMappingsByTransformationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        // Get all the emailImportImportMappingList where transformation equals to
        defaultEmailImportImportMappingFiltering(
            "transformation.equals=" + DEFAULT_TRANSFORMATION,
            "transformation.equals=" + UPDATED_TRANSFORMATION
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportMappingsByTransformationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        // Get all the emailImportImportMappingList where transformation in
        defaultEmailImportImportMappingFiltering(
            "transformation.in=" + DEFAULT_TRANSFORMATION + "," + UPDATED_TRANSFORMATION,
            "transformation.in=" + UPDATED_TRANSFORMATION
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportMappingsByTransformationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        // Get all the emailImportImportMappingList where transformation is not null
        defaultEmailImportImportMappingFiltering("transformation.specified=true", "transformation.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportImportMappingsByIsRequiredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        // Get all the emailImportImportMappingList where isRequired equals to
        defaultEmailImportImportMappingFiltering("isRequired.equals=" + DEFAULT_IS_REQUIRED, "isRequired.equals=" + UPDATED_IS_REQUIRED);
    }

    @Test
    @Transactional
    void getAllEmailImportImportMappingsByIsRequiredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        // Get all the emailImportImportMappingList where isRequired in
        defaultEmailImportImportMappingFiltering(
            "isRequired.in=" + DEFAULT_IS_REQUIRED + "," + UPDATED_IS_REQUIRED,
            "isRequired.in=" + UPDATED_IS_REQUIRED
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportMappingsByIsRequiredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        // Get all the emailImportImportMappingList where isRequired is not null
        defaultEmailImportImportMappingFiltering("isRequired.specified=true", "isRequired.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportImportMappingsByDefaultValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        // Get all the emailImportImportMappingList where defaultValue equals to
        defaultEmailImportImportMappingFiltering(
            "defaultValue.equals=" + DEFAULT_DEFAULT_VALUE,
            "defaultValue.equals=" + UPDATED_DEFAULT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportMappingsByDefaultValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        // Get all the emailImportImportMappingList where defaultValue in
        defaultEmailImportImportMappingFiltering(
            "defaultValue.in=" + DEFAULT_DEFAULT_VALUE + "," + UPDATED_DEFAULT_VALUE,
            "defaultValue.in=" + UPDATED_DEFAULT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportMappingsByDefaultValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        // Get all the emailImportImportMappingList where defaultValue is not null
        defaultEmailImportImportMappingFiltering("defaultValue.specified=true", "defaultValue.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportImportMappingsByDefaultValueContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        // Get all the emailImportImportMappingList where defaultValue contains
        defaultEmailImportImportMappingFiltering(
            "defaultValue.contains=" + DEFAULT_DEFAULT_VALUE,
            "defaultValue.contains=" + UPDATED_DEFAULT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportMappingsByDefaultValueNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        // Get all the emailImportImportMappingList where defaultValue does not contain
        defaultEmailImportImportMappingFiltering(
            "defaultValue.doesNotContain=" + UPDATED_DEFAULT_VALUE,
            "defaultValue.doesNotContain=" + DEFAULT_DEFAULT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportMappingsByValidationRegexIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        // Get all the emailImportImportMappingList where validationRegex equals to
        defaultEmailImportImportMappingFiltering(
            "validationRegex.equals=" + DEFAULT_VALIDATION_REGEX,
            "validationRegex.equals=" + UPDATED_VALIDATION_REGEX
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportMappingsByValidationRegexIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        // Get all the emailImportImportMappingList where validationRegex in
        defaultEmailImportImportMappingFiltering(
            "validationRegex.in=" + DEFAULT_VALIDATION_REGEX + "," + UPDATED_VALIDATION_REGEX,
            "validationRegex.in=" + UPDATED_VALIDATION_REGEX
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportMappingsByValidationRegexIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        // Get all the emailImportImportMappingList where validationRegex is not null
        defaultEmailImportImportMappingFiltering("validationRegex.specified=true", "validationRegex.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportImportMappingsByValidationRegexContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        // Get all the emailImportImportMappingList where validationRegex contains
        defaultEmailImportImportMappingFiltering(
            "validationRegex.contains=" + DEFAULT_VALIDATION_REGEX,
            "validationRegex.contains=" + UPDATED_VALIDATION_REGEX
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportMappingsByValidationRegexNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        // Get all the emailImportImportMappingList where validationRegex does not contain
        defaultEmailImportImportMappingFiltering(
            "validationRegex.doesNotContain=" + UPDATED_VALIDATION_REGEX,
            "validationRegex.doesNotContain=" + DEFAULT_VALIDATION_REGEX
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportMappingsByRuleIsEqualToSomething() throws Exception {
        EmailImportImportRule rule;
        if (TestUtil.findAll(em, EmailImportImportRule.class).isEmpty()) {
            emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);
            rule = EmailImportImportRuleResourceIT.createEntity();
        } else {
            rule = TestUtil.findAll(em, EmailImportImportRule.class).get(0);
        }
        em.persist(rule);
        em.flush();
        emailImportImportMapping.setRule(rule);
        emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);
        Long ruleId = rule.getId();
        // Get all the emailImportImportMappingList where rule equals to ruleId
        defaultEmailImportImportMappingShouldBeFound("ruleId.equals=" + ruleId);

        // Get all the emailImportImportMappingList where rule equals to (ruleId + 1)
        defaultEmailImportImportMappingShouldNotBeFound("ruleId.equals=" + (ruleId + 1));
    }

    private void defaultEmailImportImportMappingFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEmailImportImportMappingShouldBeFound(shouldBeFound);
        defaultEmailImportImportMappingShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmailImportImportMappingShouldBeFound(String filter) throws Exception {
        restEmailImportImportMappingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailImportImportMapping.getId().intValue())))
            .andExpect(jsonPath("$.[*].emailField").value(hasItem(DEFAULT_EMAIL_FIELD.toString())))
            .andExpect(jsonPath("$.[*].documentField").value(hasItem(DEFAULT_DOCUMENT_FIELD)))
            .andExpect(jsonPath("$.[*].transformation").value(hasItem(DEFAULT_TRANSFORMATION.toString())))
            .andExpect(jsonPath("$.[*].transformationConfig").value(hasItem(DEFAULT_TRANSFORMATION_CONFIG)))
            .andExpect(jsonPath("$.[*].isRequired").value(hasItem(DEFAULT_IS_REQUIRED)))
            .andExpect(jsonPath("$.[*].defaultValue").value(hasItem(DEFAULT_DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].validationRegex").value(hasItem(DEFAULT_VALIDATION_REGEX)));

        // Check, that the count call also returns 1
        restEmailImportImportMappingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmailImportImportMappingShouldNotBeFound(String filter) throws Exception {
        restEmailImportImportMappingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmailImportImportMappingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmailImportImportMapping() throws Exception {
        // Get the emailImportImportMapping
        restEmailImportImportMappingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmailImportImportMapping() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the emailImportImportMapping
        EmailImportImportMapping updatedEmailImportImportMapping = emailImportImportMappingRepository
            .findById(emailImportImportMapping.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedEmailImportImportMapping are not directly saved in db
        em.detach(updatedEmailImportImportMapping);
        updatedEmailImportImportMapping
            .emailField(UPDATED_EMAIL_FIELD)
            .documentField(UPDATED_DOCUMENT_FIELD)
            .transformation(UPDATED_TRANSFORMATION)
            .transformationConfig(UPDATED_TRANSFORMATION_CONFIG)
            .isRequired(UPDATED_IS_REQUIRED)
            .defaultValue(UPDATED_DEFAULT_VALUE)
            .validationRegex(UPDATED_VALIDATION_REGEX);
        EmailImportImportMappingDTO emailImportImportMappingDTO = emailImportImportMappingMapper.toDto(updatedEmailImportImportMapping);

        restEmailImportImportMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emailImportImportMappingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(emailImportImportMappingDTO))
            )
            .andExpect(status().isOk());

        // Validate the EmailImportImportMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmailImportImportMappingToMatchAllProperties(updatedEmailImportImportMapping);
    }

    @Test
    @Transactional
    void putNonExistingEmailImportImportMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImportImportMapping.setId(longCount.incrementAndGet());

        // Create the EmailImportImportMapping
        EmailImportImportMappingDTO emailImportImportMappingDTO = emailImportImportMappingMapper.toDto(emailImportImportMapping);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailImportImportMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emailImportImportMappingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(emailImportImportMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailImportImportMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmailImportImportMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImportImportMapping.setId(longCount.incrementAndGet());

        // Create the EmailImportImportMapping
        EmailImportImportMappingDTO emailImportImportMappingDTO = emailImportImportMappingMapper.toDto(emailImportImportMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailImportImportMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(emailImportImportMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailImportImportMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmailImportImportMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImportImportMapping.setId(longCount.incrementAndGet());

        // Create the EmailImportImportMapping
        EmailImportImportMappingDTO emailImportImportMappingDTO = emailImportImportMappingMapper.toDto(emailImportImportMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailImportImportMappingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportImportMappingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmailImportImportMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmailImportImportMappingWithPatch() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the emailImportImportMapping using partial update
        EmailImportImportMapping partialUpdatedEmailImportImportMapping = new EmailImportImportMapping();
        partialUpdatedEmailImportImportMapping.setId(emailImportImportMapping.getId());

        partialUpdatedEmailImportImportMapping
            .emailField(UPDATED_EMAIL_FIELD)
            .transformation(UPDATED_TRANSFORMATION)
            .validationRegex(UPDATED_VALIDATION_REGEX);

        restEmailImportImportMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmailImportImportMapping.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmailImportImportMapping))
            )
            .andExpect(status().isOk());

        // Validate the EmailImportImportMapping in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmailImportImportMappingUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEmailImportImportMapping, emailImportImportMapping),
            getPersistedEmailImportImportMapping(emailImportImportMapping)
        );
    }

    @Test
    @Transactional
    void fullUpdateEmailImportImportMappingWithPatch() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the emailImportImportMapping using partial update
        EmailImportImportMapping partialUpdatedEmailImportImportMapping = new EmailImportImportMapping();
        partialUpdatedEmailImportImportMapping.setId(emailImportImportMapping.getId());

        partialUpdatedEmailImportImportMapping
            .emailField(UPDATED_EMAIL_FIELD)
            .documentField(UPDATED_DOCUMENT_FIELD)
            .transformation(UPDATED_TRANSFORMATION)
            .transformationConfig(UPDATED_TRANSFORMATION_CONFIG)
            .isRequired(UPDATED_IS_REQUIRED)
            .defaultValue(UPDATED_DEFAULT_VALUE)
            .validationRegex(UPDATED_VALIDATION_REGEX);

        restEmailImportImportMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmailImportImportMapping.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmailImportImportMapping))
            )
            .andExpect(status().isOk());

        // Validate the EmailImportImportMapping in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmailImportImportMappingUpdatableFieldsEquals(
            partialUpdatedEmailImportImportMapping,
            getPersistedEmailImportImportMapping(partialUpdatedEmailImportImportMapping)
        );
    }

    @Test
    @Transactional
    void patchNonExistingEmailImportImportMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImportImportMapping.setId(longCount.incrementAndGet());

        // Create the EmailImportImportMapping
        EmailImportImportMappingDTO emailImportImportMappingDTO = emailImportImportMappingMapper.toDto(emailImportImportMapping);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailImportImportMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, emailImportImportMappingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(emailImportImportMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailImportImportMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmailImportImportMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImportImportMapping.setId(longCount.incrementAndGet());

        // Create the EmailImportImportMapping
        EmailImportImportMappingDTO emailImportImportMappingDTO = emailImportImportMappingMapper.toDto(emailImportImportMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailImportImportMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(emailImportImportMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailImportImportMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmailImportImportMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImportImportMapping.setId(longCount.incrementAndGet());

        // Create the EmailImportImportMapping
        EmailImportImportMappingDTO emailImportImportMappingDTO = emailImportImportMappingMapper.toDto(emailImportImportMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailImportImportMappingMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(emailImportImportMappingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmailImportImportMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmailImportImportMapping() throws Exception {
        // Initialize the database
        insertedEmailImportImportMapping = emailImportImportMappingRepository.saveAndFlush(emailImportImportMapping);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the emailImportImportMapping
        restEmailImportImportMappingMockMvc
            .perform(delete(ENTITY_API_URL_ID, emailImportImportMapping.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return emailImportImportMappingRepository.count();
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

    protected EmailImportImportMapping getPersistedEmailImportImportMapping(EmailImportImportMapping emailImportImportMapping) {
        return emailImportImportMappingRepository.findById(emailImportImportMapping.getId()).orElseThrow();
    }

    protected void assertPersistedEmailImportImportMappingToMatchAllProperties(EmailImportImportMapping expectedEmailImportImportMapping) {
        assertEmailImportImportMappingAllPropertiesEquals(
            expectedEmailImportImportMapping,
            getPersistedEmailImportImportMapping(expectedEmailImportImportMapping)
        );
    }

    protected void assertPersistedEmailImportImportMappingToMatchUpdatableProperties(
        EmailImportImportMapping expectedEmailImportImportMapping
    ) {
        assertEmailImportImportMappingAllUpdatablePropertiesEquals(
            expectedEmailImportImportMapping,
            getPersistedEmailImportImportMapping(expectedEmailImportImportMapping)
        );
    }
}
