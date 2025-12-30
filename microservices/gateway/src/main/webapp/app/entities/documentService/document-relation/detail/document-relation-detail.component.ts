import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IDocumentRelation } from '../document-relation.model';

@Component({
  selector: 'jhi-document-relation-detail',
  templateUrl: './document-relation-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class DocumentRelationDetailComponent {
  documentRelation = input<IDocumentRelation | null>(null);

  previousState(): void {
    window.history.back();
  }
}
