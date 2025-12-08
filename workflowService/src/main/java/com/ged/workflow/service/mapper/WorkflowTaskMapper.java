package com.ged.workflow.service.mapper;

import com.ged.workflow.domain.WorkflowInstance;
import com.ged.workflow.domain.WorkflowStep;
import com.ged.workflow.domain.WorkflowTask;
import com.ged.workflow.service.dto.WorkflowInstanceDTO;
import com.ged.workflow.service.dto.WorkflowStepDTO;
import com.ged.workflow.service.dto.WorkflowTaskDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkflowTask} and its DTO {@link WorkflowTaskDTO}.
 */
@Mapper(componentModel = "spring")
public interface WorkflowTaskMapper extends EntityMapper<WorkflowTaskDTO, WorkflowTask> {
    @Mapping(target = "instance", source = "instance", qualifiedByName = "workflowInstanceId")
    @Mapping(target = "step", source = "step", qualifiedByName = "workflowStepId")
    WorkflowTaskDTO toDto(WorkflowTask s);

    @Named("workflowInstanceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkflowInstanceDTO toDtoWorkflowInstanceId(WorkflowInstance workflowInstance);

    @Named("workflowStepId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkflowStepDTO toDtoWorkflowStepId(WorkflowStep workflowStep);
}
