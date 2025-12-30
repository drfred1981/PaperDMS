import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMonitoringMaintenanceTask } from '../monitoring-maintenance-task.model';
import { MonitoringMaintenanceTaskService } from '../service/monitoring-maintenance-task.service';

@Component({
  templateUrl: './monitoring-maintenance-task-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MonitoringMaintenanceTaskDeleteDialogComponent {
  monitoringMaintenanceTask?: IMonitoringMaintenanceTask;

  protected monitoringMaintenanceTaskService = inject(MonitoringMaintenanceTaskService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.monitoringMaintenanceTaskService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
