import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { IServiceStatus } from '../service-status.model';

@Component({
  selector: 'jhi-service-status-detail',
  templateUrl: './service-status-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe],
})
export class ServiceStatusDetail {
  serviceStatus = input<IServiceStatus | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
