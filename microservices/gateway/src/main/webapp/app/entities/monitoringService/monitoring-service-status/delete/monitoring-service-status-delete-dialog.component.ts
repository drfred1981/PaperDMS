import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMonitoringServiceStatus } from '../monitoring-service-status.model';
import { MonitoringServiceStatusService } from '../service/monitoring-service-status.service';

@Component({
  templateUrl: './monitoring-service-status-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MonitoringServiceStatusDeleteDialogComponent {
  monitoringServiceStatus?: IMonitoringServiceStatus;

  protected monitoringServiceStatusService = inject(MonitoringServiceStatusService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.monitoringServiceStatusService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
