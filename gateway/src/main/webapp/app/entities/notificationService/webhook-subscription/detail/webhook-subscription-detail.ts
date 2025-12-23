import { Component, inject, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { DataUtils } from 'app/core/util/data-util.service';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { IWebhookSubscription } from '../webhook-subscription.model';

@Component({
  selector: 'jhi-webhook-subscription-detail',
  templateUrl: './webhook-subscription-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe],
})
export class WebhookSubscriptionDetail {
  webhookSubscription = input<IWebhookSubscription | null>(null);

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
