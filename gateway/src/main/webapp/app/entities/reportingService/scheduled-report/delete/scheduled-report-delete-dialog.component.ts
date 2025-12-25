import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IScheduledReport } from '../scheduled-report.model';
import { ScheduledReportService } from '../service/scheduled-report.service';

@Component({
  templateUrl: './scheduled-report-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ScheduledReportDeleteDialogComponent {
  scheduledReport?: IScheduledReport;

  protected scheduledReportService = inject(ScheduledReportService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.scheduledReportService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
