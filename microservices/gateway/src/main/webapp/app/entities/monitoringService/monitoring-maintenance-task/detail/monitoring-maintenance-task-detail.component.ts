import { Component, inject, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { DataUtils } from 'app/core/util/data-util.service';
import { IMonitoringMaintenanceTask } from '../monitoring-maintenance-task.model';

@Component({
  selector: 'jhi-monitoring-maintenance-task-detail',
  templateUrl: './monitoring-maintenance-task-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class MonitoringMaintenanceTaskDetailComponent {
  monitoringMaintenanceTask = input<IMonitoringMaintenanceTask | null>(null);

  protected dataUtils = inject(DataUtils);

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
