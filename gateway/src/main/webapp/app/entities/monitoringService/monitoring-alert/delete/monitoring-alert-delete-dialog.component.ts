import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMonitoringAlert } from '../monitoring-alert.model';
import { MonitoringAlertService } from '../service/monitoring-alert.service';

@Component({
  templateUrl: './monitoring-alert-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MonitoringAlertDeleteDialogComponent {
  monitoringAlert?: IMonitoringAlert;

  protected monitoringAlertService = inject(MonitoringAlertService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.monitoringAlertService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
