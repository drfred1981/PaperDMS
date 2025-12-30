import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IReportingSystemMetric } from '../reporting-system-metric.model';
import { ReportingSystemMetricService } from '../service/reporting-system-metric.service';

@Component({
  templateUrl: './reporting-system-metric-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ReportingSystemMetricDeleteDialogComponent {
  reportingSystemMetric?: IReportingSystemMetric;

  protected reportingSystemMetricService = inject(ReportingSystemMetricService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.reportingSystemMetricService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
