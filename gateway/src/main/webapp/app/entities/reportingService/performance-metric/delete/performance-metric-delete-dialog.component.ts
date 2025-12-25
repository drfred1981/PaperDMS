import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPerformanceMetric } from '../performance-metric.model';
import { PerformanceMetricService } from '../service/performance-metric.service';

@Component({
  templateUrl: './performance-metric-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PerformanceMetricDeleteDialogComponent {
  performanceMetric?: IPerformanceMetric;

  protected performanceMetricService = inject(PerformanceMetricService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.performanceMetricService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
