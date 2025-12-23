import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { IContract } from '../contract.model';

@Component({
  selector: 'jhi-contract-detail',
  templateUrl: './contract-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ContractDetail {
  contract = input<IContract | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
