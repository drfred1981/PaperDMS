import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISystemMetric } from '../system-metric.model';
import { SystemMetricService } from '../service/system-metric.service';

@Component({
  templateUrl: './system-metric-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SystemMetricDeleteDialogComponent {
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
