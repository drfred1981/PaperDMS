import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IWebhookLog } from '../webhook-log.model';
import { WebhookLogService } from '../service/webhook-log.service';

@Component({
  templateUrl: './webhook-log-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class WebhookLogDeleteDialogComponent {
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
