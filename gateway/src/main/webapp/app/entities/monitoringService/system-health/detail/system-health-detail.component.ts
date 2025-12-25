import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { ISystemHealth } from '../system-health.model';

@Component({
  selector: 'jhi-system-health-detail',
  templateUrl: './system-health-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class SystemHealthDetailComponent {
  systemHealth = input<ISystemHealth | null>(null);

  previousState(): void {
    window.history.back();
  }
}
