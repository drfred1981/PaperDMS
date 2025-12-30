import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IReportingScheduledReport } from '../reporting-scheduled-report.model';
import { ReportingScheduledReportService } from '../service/reporting-scheduled-report.service';

@Component({
  templateUrl: './reporting-scheduled-report-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ReportingScheduledReportDeleteDialogComponent {
  reportingScheduledReport?: IReportingScheduledReport;

  protected reportingScheduledReportService = inject(ReportingScheduledReportService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.reportingScheduledReportService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
