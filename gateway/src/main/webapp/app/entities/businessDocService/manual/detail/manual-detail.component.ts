import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IManual } from '../manual.model';

@Component({
  selector: 'jhi-manual-detail',
  templateUrl: './manual-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ManualDetailComponent {
  manual = input<IManual | null>(null);

  previousState(): void {
    window.history.back();
  }
}
