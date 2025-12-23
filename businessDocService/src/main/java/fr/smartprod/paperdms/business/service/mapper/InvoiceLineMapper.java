package fr.smartprod.paperdms.business.service.mapper;

import fr.smartprod.paperdms.business.domain.Invoice;
import fr.smartprod.paperdms.business.domain.InvoiceLine;
import fr.smartprod.paperdms.business.service.dto.InvoiceDTO;
import fr.smartprod.paperdms.business.service.dto.InvoiceLineDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InvoiceLine} and its DTO {@link InvoiceLineDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvoiceLineMapper extends EntityMapper<InvoiceLineDTO, InvoiceLine> {
    @Mapping(target = "invoice", source = "invoice", qualifiedByName = "invoiceId")
    InvoiceLineDTO toDto(InvoiceLine s);

    @Named("invoiceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InvoiceDTO toDtoInvoiceId(Invoice invoice);
}
