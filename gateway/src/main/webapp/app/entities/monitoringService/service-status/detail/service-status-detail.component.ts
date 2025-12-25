import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IServiceStatus } from '../service-status.model';

@Component({
  selector: 'jhi-service-status-detail',
  templateUrl: './service-status-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class ServiceStatusDetailComponent {
  serviceStatus = input<IServiceStatus | null>(null);

  previousState(): void {
    window.history.back();
  }
}
