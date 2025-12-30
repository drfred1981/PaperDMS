import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { INotificationWebhookSubscription } from '../notification-webhook-subscription.model';
import { NotificationWebhookSubscriptionService } from '../service/notification-webhook-subscription.service';

@Component({
  templateUrl: './notification-webhook-subscription-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class NotificationWebhookSubscriptionDeleteDialogComponent {
  notificationWebhookSubscription?: INotificationWebhookSubscription;

  protected notificationWebhookSubscriptionService = inject(NotificationWebhookSubscriptionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.notificationWebhookSubscriptionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
