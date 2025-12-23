import { Component, inject, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { DataUtils } from 'app/core/util/data-util.service';
import SharedModule from 'app/shared/shared.module';
import { ISearchFacet } from '../search-facet.model';

@Component({
  selector: 'jhi-search-facet-detail',
  templateUrl: './search-facet-detail.html',
  imports: [SharedModule, RouterLink],
})
export class SearchFacetDetail {
  searchFacet = input<ISearchFacet | null>(null);

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
