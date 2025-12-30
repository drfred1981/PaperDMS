import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { MetaBookmarkType } from 'app/entities/enumerations/meta-bookmark-type.model';
import { IMetaBookmark } from '../meta-bookmark.model';
import { MetaBookmarkService } from '../service/meta-bookmark.service';
import { MetaBookmarkFormGroup, MetaBookmarkFormService } from './meta-bookmark-form.service';

@Component({
  selector: 'jhi-meta-bookmark-update',
  templateUrl: './meta-bookmark-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MetaBookmarkUpdateComponent implements OnInit {
  isSaving = false;
  metaBookmark: IMetaBookmark | null = null;
  metaBookmarkTypeValues = Object.keys(MetaBookmarkType);

  protected metaBookmarkService = inject(MetaBookmarkService);
  protected metaBookmarkFormService = inject(MetaBookmarkFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MetaBookmarkFormGroup = this.metaBookmarkFormService.createMetaBookmarkFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ metaBookmark }) => {
      this.metaBookmark = metaBookmark;
      if (metaBookmark) {
        this.updateForm(metaBookmark);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const metaBookmark = this.metaBookmarkFormService.getMetaBookmark(this.editForm);
    if (metaBookmark.id !== null) {
      this.subscribeToSaveResponse(this.metaBookmarkService.update(metaBookmark));
    } else {
      this.subscribeToSaveResponse(this.metaBookmarkService.create(metaBookmark));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMetaBookmark>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(metaBookmark: IMetaBookmark): void {
    this.metaBookmark = metaBookmark;
    this.metaBookmarkFormService.resetForm(this.editForm, metaBookmark);
  }
}
