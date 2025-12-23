import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { SystemMetricService } from '../service/system-metric.service';
import { ISystemMetric } from '../system-metric.model';

@Component({
  templateUrl: './system-metric-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class SystemMetricDeleteDialog {
  systemMetric?: ISystemMetric;

  protected systemMetricService = inject(SystemMetricService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.systemMetricService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
