import { Component, inject, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { DataUtils } from 'app/core/util/data-util.service';
import SharedModule from 'app/shared/shared.module';
import { IContractClause } from '../contract-clause.model';

@Component({
  selector: 'jhi-contract-clause-detail',
  templateUrl: './contract-clause-detail.html',
  imports: [SharedModule, RouterLink],
})
export class ContractClauseDetail {
  contractClause = input<IContractClause | null>(null);

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
