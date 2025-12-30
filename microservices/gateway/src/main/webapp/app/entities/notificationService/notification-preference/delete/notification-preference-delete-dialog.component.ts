import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { INotificationPreference } from '../notification-preference.model';
import { NotificationPreferenceService } from '../service/notification-preference.service';

@Component({
  templateUrl: './notification-preference-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class NotificationPreferenceDeleteDialogComponent {
  notificationPreference?: INotificationPreference;

  protected notificationPreferenceService = inject(NotificationPreferenceService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.notificationPreferenceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
