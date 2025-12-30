import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { INotificationWebhookLog } from '../notification-webhook-log.model';
import { NotificationWebhookLogService } from '../service/notification-webhook-log.service';

@Component({
  templateUrl: './notification-webhook-log-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class NotificationWebhookLogDeleteDialogComponent {
  notificationWebhookLog?: INotificationWebhookLog;

  protected notificationWebhookLogService = inject(NotificationWebhookLogService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.notificationWebhookLogService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
