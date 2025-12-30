package fr.smartprod.paperdms.pdftoimage.service.mapper;

import fr.smartprod.paperdms.pdftoimage.domain.ImageConversionBatch;
import fr.smartprod.paperdms.pdftoimage.domain.ImagePdfConversionRequest;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImageConversionBatchDTO;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImagePdfConversionRequestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ImagePdfConversionRequest} and its DTO {@link ImagePdfConversionRequestDTO}.
 */
@Mapper(componentModel = "spring")
public interface ImagePdfConversionRequestMapper extends EntityMapper<ImagePdfConversionRequestDTO, ImagePdfConversionRequest> {
    @Mapping(target = "batch", source = "batch", qualifiedByName = "imageConversionBatchId")
    ImagePdfConversionRequestDTO toDto(ImagePdfConversionRequest s);

    @Named("imageConversionBatchId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ImageConversionBatchDTO toDtoImageConversionBatchId(ImageConversionBatch imageConversionBatch);
}
