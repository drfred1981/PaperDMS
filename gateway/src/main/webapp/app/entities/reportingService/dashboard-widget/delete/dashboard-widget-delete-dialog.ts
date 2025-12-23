import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { IDashboardWidget } from '../dashboard-widget.model';
import { DashboardWidgetService } from '../service/dashboard-widget.service';

@Component({
  templateUrl: './dashboard-widget-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class DashboardWidgetDeleteDialog {
  dashboardWidget?: IDashboardWidget;

  protected dashboardWidgetService = inject(DashboardWidgetService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dashboardWidgetService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
