import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { INotificationEvent } from '../notification-event.model';
import { NotificationEventService } from '../service/notification-event.service';

@Component({
  templateUrl: './notification-event-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class NotificationEventDeleteDialogComponent {
  notificationEvent?: INotificationEvent;

  protected notificationEventService = inject(NotificationEventService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.notificationEventService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
