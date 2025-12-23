package fr.smartprod.paperdms.document.service.impl;

import fr.smartprod.paperdms.document.domain.TagCategory;
import fr.smartprod.paperdms.document.repository.TagCategoryRepository;
import fr.smartprod.paperdms.document.repository.search.TagCategorySearchRepository;
import fr.smartprod.paperdms.document.service.TagCategoryService;
import fr.smartprod.paperdms.document.service.dto.TagCategoryDTO;
import fr.smartprod.paperdms.document.service.mapper.TagCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.TagCategory}.
 */
@Service
@Transactional
public class TagCategoryServiceImpl implements TagCategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(TagCategoryServiceImpl.class);

    private final TagCategoryRepository tagCategoryRepository;

    private final TagCategoryMapper tagCategoryMapper;

    private final TagCategorySearchRepository tagCategorySearchRepository;

    public TagCategoryServiceImpl(
        TagCategoryRepository tagCategoryRepository,
        TagCategoryMapper tagCategoryMapper,
        TagCategorySearchRepository tagCategorySearchRepository
    ) {
        this.tagCategoryRepository = tagCategoryRepository;
        this.tagCategoryMapper = tagCategoryMapper;
        this.tagCategorySearchRepository = tagCategorySearchRepository;
    }

    @Override
    public TagCategoryDTO save(TagCategoryDTO tagCategoryDTO) {
        LOG.debug("Request to save TagCategory : {}", tagCategoryDTO);
        TagCategory tagCategory = tagCategoryMapper.toEntity(tagCategoryDTO);
        tagCategory = tagCategoryRepository.save(tagCategory);
        tagCategorySearchRepository.index(tagCategory);
        return tagCategoryMapper.toDto(tagCategory);
    }

    @Override
    public TagCategoryDTO update(TagCategoryDTO tagCategoryDTO) {
        LOG.debug("Request to update TagCategory : {}", tagCategoryDTO);
        TagCategory tagCategory = tagCategoryMapper.toEntity(tagCategoryDTO);
        tagCategory = tagCategoryRepository.save(tagCategory);
        tagCategorySearchRepository.index(tagCategory);
        return tagCategoryMapper.toDto(tagCategory);
    }

    @Override
    public Optional<TagCategoryDTO> partialUpdate(TagCategoryDTO tagCategoryDTO) {
        LOG.debug("Request to partially update TagCategory : {}", tagCategoryDTO);

        return tagCategoryRepository
            .findById(tagCategoryDTO.getId())
            .map(existingTagCategory -> {
                tagCategoryMapper.partialUpdate(existingTagCategory, tagCategoryDTO);

                return existingTagCategory;
            })
            .map(tagCategoryRepository::save)
            .map(savedTagCategory -> {
                tagCategorySearchRepository.index(savedTagCategory);
                return savedTagCategory;
            })
            .map(tagCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TagCategoryDTO> findOne(Long id) {
        LOG.debug("Request to get TagCategory : {}", id);
        return tagCategoryRepository.findById(id).map(tagCategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete TagCategory : {}", id);
        tagCategoryRepository.deleteById(id);
        tagCategorySearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TagCategoryDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of TagCategories for query {}", query);
        return tagCategorySearchRepository.search(query, pageable).map(tagCategoryMapper::toDto);
    }
}
