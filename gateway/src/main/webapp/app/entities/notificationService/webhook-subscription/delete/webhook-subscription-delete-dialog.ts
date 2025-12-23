import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { WebhookSubscriptionService } from '../service/webhook-subscription.service';
import { IWebhookSubscription } from '../webhook-subscription.model';

@Component({
  templateUrl: './webhook-subscription-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class WebhookSubscriptionDeleteDialog {
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
