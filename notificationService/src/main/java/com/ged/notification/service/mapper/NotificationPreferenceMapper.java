package com.ged.notification.service.mapper;

import com.ged.notification.domain.NotificationPreference;
import com.ged.notification.service.dto.NotificationPreferenceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NotificationPreference} and its DTO {@link NotificationPreferenceDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationPreferenceMapper extends EntityMapper<NotificationPreferenceDTO, NotificationPreference> {}
