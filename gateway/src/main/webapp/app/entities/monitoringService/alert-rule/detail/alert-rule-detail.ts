import { Component, inject, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { DataUtils } from 'app/core/util/data-util.service';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { IAlertRule } from '../alert-rule.model';

@Component({
  selector: 'jhi-alert-rule-detail',
  templateUrl: './alert-rule-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe],
})
export class AlertRuleDetail {
  alertRule = input<IAlertRule | null>(null);

  protected dataUtils = inject(DataUtils);

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    globalThis.history.back();
  }
}
