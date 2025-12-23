import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { TagCategoryService } from 'app/entities/documentService/tag-category/service/tag-category.service';
import { ITagCategory } from 'app/entities/documentService/tag-category/tag-category.model';
import SharedModule from 'app/shared/shared.module';
import { TagService } from '../service/tag.service';
import { ITag } from '../tag.model';

import { TagFormGroup, TagFormService } from './tag-form.service';

@Component({
  selector: 'jhi-tag-update',
  templateUrl: './tag-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class TagUpdate implements OnInit {
  isSaving = false;
  tag: ITag | null = null;

  tagCategoriesSharedCollection = signal<ITagCategory[]>([]);

  protected tagService = inject(TagService);
  protected tagFormService = inject(TagFormService);
  protected tagCategoryService = inject(TagCategoryService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TagFormGroup = this.tagFormService.createTagFormGroup();

  compareTagCategory = (o1: ITagCategory | null, o2: ITagCategory | null): boolean => this.tagCategoryService.compareTagCategory(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tag }) => {
      this.tag = tag;
      if (tag) {
        this.updateForm(tag);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tag = this.tagFormService.getTag(this.editForm);
    if (tag.id === null) {
      this.subscribeToSaveResponse(this.tagService.create(tag));
    } else {
      this.subscribeToSaveResponse(this.tagService.update(tag));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITag>>): void {
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

  protected updateForm(tag: ITag): void {
    this.tag = tag;
    this.tagFormService.resetForm(this.editForm, tag);

    this.tagCategoriesSharedCollection.set(
      this.tagCategoryService.addTagCategoryToCollectionIfMissing<ITagCategory>(this.tagCategoriesSharedCollection(), tag.tagCategory),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tagCategoryService
      .query()
      .pipe(map((res: HttpResponse<ITagCategory[]>) => res.body ?? []))
      .pipe(
        map((tagCategories: ITagCategory[]) =>
          this.tagCategoryService.addTagCategoryToCollectionIfMissing<ITagCategory>(tagCategories, this.tag?.tagCategory),
        ),
      )
      .subscribe((tagCategories: ITagCategory[]) => this.tagCategoriesSharedCollection.set(tagCategories));
  }
}
