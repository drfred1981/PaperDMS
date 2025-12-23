import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { BookmarkType } from 'app/entities/enumerations/bookmark-type.model';
import SharedModule from 'app/shared/shared.module';
import { IBookmark } from '../bookmark.model';
import { BookmarkService } from '../service/bookmark.service';

import { BookmarkFormGroup, BookmarkFormService } from './bookmark-form.service';

@Component({
  selector: 'jhi-bookmark-update',
  templateUrl: './bookmark-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class BookmarkUpdate implements OnInit {
  isSaving = false;
  bookmark: IBookmark | null = null;
  bookmarkTypeValues = Object.keys(BookmarkType);

  protected bookmarkService = inject(BookmarkService);
  protected bookmarkFormService = inject(BookmarkFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BookmarkFormGroup = this.bookmarkFormService.createBookmarkFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bookmark }) => {
      this.bookmark = bookmark;
      if (bookmark) {
        this.updateForm(bookmark);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bookmark = this.bookmarkFormService.getBookmark(this.editForm);
    if (bookmark.id === null) {
      this.subscribeToSaveResponse(this.bookmarkService.create(bookmark));
    } else {
      this.subscribeToSaveResponse(this.bookmarkService.update(bookmark));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBookmark>>): void {
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

  protected updateForm(bookmark: IBookmark): void {
    this.bookmark = bookmark;
    this.bookmarkFormService.resetForm(this.editForm, bookmark);
  }
}
