import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IMetaMetaTagCategory } from '../meta-meta-tag-category.model';

@Component({
  selector: 'jhi-meta-meta-tag-category-detail',
  templateUrl: './meta-meta-tag-category-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class MetaMetaTagCategoryDetailComponent {
  metaMetaTagCategory = input<IMetaMetaTagCategory | null>(null);

  previousState(): void {
    window.history.back();
  }
}
