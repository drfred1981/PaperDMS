import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IWebhookSubscription } from '../webhook-subscription.model';
import { WebhookSubscriptionService } from '../service/webhook-subscription.service';

@Component({
  templateUrl: './webhook-subscription-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class WebhookSubscriptionDeleteDialogComponent {
  webhookSubscription?: IWebhookSubscription;

  protected webhookSubscriptionService = inject(WebhookSubscriptionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.webhookSubscriptionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
