package com.ged.workflow.service.mapper;

import com.ged.workflow.domain.ApprovalHistory;
import com.ged.workflow.service.dto.ApprovalHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ApprovalHistory} and its DTO {@link ApprovalHistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ApprovalHistoryMapper extends EntityMapper<ApprovalHistoryDTO, ApprovalHistory> {}
