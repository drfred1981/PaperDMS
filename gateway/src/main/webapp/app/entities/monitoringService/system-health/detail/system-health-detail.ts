import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { ISystemHealth } from '../system-health.model';

@Component({
  selector: 'jhi-system-health-detail',
  templateUrl: './system-health-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe],
})
export class SystemHealthDetail {
  systemHealth = input<ISystemHealth | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
