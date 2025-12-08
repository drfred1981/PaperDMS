package com.ged.workflow.service.mapper;

import com.ged.workflow.domain.Workflow;
import com.ged.workflow.domain.WorkflowStep;
import com.ged.workflow.service.dto.WorkflowDTO;
import com.ged.workflow.service.dto.WorkflowStepDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkflowStep} and its DTO {@link WorkflowStepDTO}.
 */
@Mapper(componentModel = "spring")
public interface WorkflowStepMapper extends EntityMapper<WorkflowStepDTO, WorkflowStep> {
    @Mapping(target = "workflow", source = "workflow", qualifiedByName = "workflowId")
    WorkflowStepDTO toDto(WorkflowStep s);

    @Named("workflowId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkflowDTO toDtoWorkflowId(Workflow workflow);
}
