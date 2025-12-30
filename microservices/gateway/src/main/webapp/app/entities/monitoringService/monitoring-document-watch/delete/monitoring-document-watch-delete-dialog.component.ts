import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMonitoringDocumentWatch } from '../monitoring-document-watch.model';
import { MonitoringDocumentWatchService } from '../service/monitoring-document-watch.service';

@Component({
  templateUrl: './monitoring-document-watch-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MonitoringDocumentWatchDeleteDialogComponent {
  monitoringDocumentWatch?: IMonitoringDocumentWatch;

  protected monitoringDocumentWatchService = inject(MonitoringDocumentWatchService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.monitoringDocumentWatchService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
