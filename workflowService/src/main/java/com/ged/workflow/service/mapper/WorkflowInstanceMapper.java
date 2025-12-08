package com.ged.workflow.service.mapper;

import com.ged.workflow.domain.Workflow;
import com.ged.workflow.domain.WorkflowInstance;
import com.ged.workflow.service.dto.WorkflowDTO;
import com.ged.workflow.service.dto.WorkflowInstanceDTO;
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
