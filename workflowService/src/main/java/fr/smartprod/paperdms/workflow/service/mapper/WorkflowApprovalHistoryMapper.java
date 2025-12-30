package fr.smartprod.paperdms.workflow.service.mapper;

import fr.smartprod.paperdms.workflow.domain.WorkflowApprovalHistory;
import fr.smartprod.paperdms.workflow.domain.WorkflowInstance;
import fr.smartprod.paperdms.workflow.service.dto.WorkflowApprovalHistoryDTO;
import fr.smartprod.paperdms.workflow.service.dto.WorkflowInstanceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkflowApprovalHistory} and its DTO {@link WorkflowApprovalHistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface WorkflowApprovalHistoryMapper extends EntityMapper<WorkflowApprovalHistoryDTO, WorkflowApprovalHistory> {
    @Mapping(target = "workflowInstance", source = "workflowInstance", qualifiedByName = "workflowInstanceId")
    WorkflowApprovalHistoryDTO toDto(WorkflowApprovalHistory s);

    @Named("workflowInstanceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkflowInstanceDTO toDtoWorkflowInstanceId(WorkflowInstance workflowInstance);
}
