package com.ged.workflow.service.mapper;

import com.ged.workflow.domain.Workflow;
import com.ged.workflow.service.dto.WorkflowDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Workflow} and its DTO {@link WorkflowDTO}.
 */
@Mapper(componentModel = "spring")
public interface WorkflowMapper extends EntityMapper<WorkflowDTO, Workflow> {}
