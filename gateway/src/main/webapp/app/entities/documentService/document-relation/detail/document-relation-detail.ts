import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatetimePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { IDocumentRelation } from '../document-relation.model';

@Component({
  selector: 'jhi-document-relation-detail',
  templateUrl: './document-relation-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatetimePipe],
})
export class DocumentRelationDetail {
  documentRelation = input<IDocumentRelation | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
