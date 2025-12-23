package fr.smartprod.paperdms.business.service.mapper;

import fr.smartprod.paperdms.business.domain.Invoice;
import fr.smartprod.paperdms.business.service.dto.InvoiceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Invoice} and its DTO {@link InvoiceDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvoiceMapper extends EntityMapper<InvoiceDTO, Invoice> {}
