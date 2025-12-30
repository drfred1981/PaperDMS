package fr.smartprod.paperdms.pdftoimage.service.mapper;

import fr.smartprod.paperdms.pdftoimage.domain.ImageGeneratedImage;
import fr.smartprod.paperdms.pdftoimage.domain.ImagePdfConversionRequest;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImageGeneratedImageDTO;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImagePdfConversionRequestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ImageGeneratedImage} and its DTO {@link ImageGeneratedImageDTO}.
 */
@Mapper(componentModel = "spring")
public interface ImageGeneratedImageMapper extends EntityMapper<ImageGeneratedImageDTO, ImageGeneratedImage> {
    @Mapping(target = "conversionRequest", source = "conversionRequest", qualifiedByName = "imagePdfConversionRequestId")
    ImageGeneratedImageDTO toDto(ImageGeneratedImage s);

    @Named("imagePdfConversionRequestId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ImagePdfConversionRequestDTO toDtoImagePdfConversionRequestId(ImagePdfConversionRequest imagePdfConversionRequest);
}
