import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { WebhookLogService } from '../service/webhook-log.service';
import { IWebhookLog } from '../webhook-log.model';

@Component({
  templateUrl: './webhook-log-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class WebhookLogDeleteDialog {
  webhookLog?: IWebhookLog;

  protected webhookLogService = inject(WebhookLogService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.webhookLogService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
