package fr.smartprod.paperdms.notification.service.mapper;

import fr.smartprod.paperdms.notification.domain.NotificationPreference;
import fr.smartprod.paperdms.notification.service.dto.NotificationPreferenceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NotificationPreference} and its DTO {@link NotificationPreferenceDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationPreferenceMapper extends EntityMapper<NotificationPreferenceDTO, NotificationPreference> {}
