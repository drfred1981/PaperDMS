import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IReportingDashboardWidget } from '../reporting-dashboard-widget.model';
import { ReportingDashboardWidgetService } from '../service/reporting-dashboard-widget.service';

@Component({
  templateUrl: './reporting-dashboard-widget-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ReportingDashboardWidgetDeleteDialogComponent {
  reportingDashboardWidget?: IReportingDashboardWidget;

  protected reportingDashboardWidgetService = inject(ReportingDashboardWidgetService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.reportingDashboardWidgetService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
