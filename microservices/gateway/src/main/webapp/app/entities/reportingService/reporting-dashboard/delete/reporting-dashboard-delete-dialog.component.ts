import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IReportingDashboard } from '../reporting-dashboard.model';
import { ReportingDashboardService } from '../service/reporting-dashboard.service';

@Component({
  templateUrl: './reporting-dashboard-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ReportingDashboardDeleteDialogComponent {
  reportingDashboard?: IReportingDashboard;

  protected reportingDashboardService = inject(ReportingDashboardService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.reportingDashboardService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
