package fr.smartprod.paperdms.workflow.service.mapper;

import fr.smartprod.paperdms.workflow.domain.Workflow;
import fr.smartprod.paperdms.workflow.domain.WorkflowInstance;
import fr.smartprod.paperdms.workflow.service.dto.WorkflowDTO;
import fr.smartprod.paperdms.workflow.service.dto.WorkflowInstanceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkflowInstance} and its DTO {@link WorkflowInstanceDTO}.
 */
@Mapper(componentModel = "spring")
public interface WorkflowInstanceMapper extends EntityMapper<WorkflowInstanceDTO, WorkflowInstance> {
    @Mapping(target = "workflow", source = "workflow", qualifiedByName = "workflowId")
    WorkflowInstanceDTO toDto(WorkflowInstance s);

    @Named("workflowId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkflowDTO toDtoWorkflowId(Workflow workflow);
}
