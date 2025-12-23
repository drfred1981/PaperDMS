package fr.smartprod.paperdms.workflow.service.mapper;

import fr.smartprod.paperdms.workflow.domain.ApprovalHistory;
import fr.smartprod.paperdms.workflow.service.dto.ApprovalHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ApprovalHistory} and its DTO {@link ApprovalHistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ApprovalHistoryMapper extends EntityMapper<ApprovalHistoryDTO, ApprovalHistory> {}
