import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMonitoringSystemHealth } from '../monitoring-system-health.model';
import { MonitoringSystemHealthService } from '../service/monitoring-system-health.service';

@Component({
  templateUrl: './monitoring-system-health-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MonitoringSystemHealthDeleteDialogComponent {
  monitoringSystemHealth?: IMonitoringSystemHealth;

  protected monitoringSystemHealthService = inject(MonitoringSystemHealthService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.monitoringSystemHealthService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
