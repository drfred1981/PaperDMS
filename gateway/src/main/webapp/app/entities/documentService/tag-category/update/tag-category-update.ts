import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { TagCategoryService } from '../service/tag-category.service';
import { ITagCategory } from '../tag-category.model';

import { TagCategoryFormGroup, TagCategoryFormService } from './tag-category-form.service';

@Component({
  selector: 'jhi-tag-category-update',
  templateUrl: './tag-category-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class TagCategoryUpdate implements OnInit {
  isSaving = false;
  tagCategory: ITagCategory | null = null;

  tagCategoriesSharedCollection = signal<ITagCategory[]>([]);

  protected tagCategoryService = inject(TagCategoryService);
  protected tagCategoryFormService = inject(TagCategoryFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TagCategoryFormGroup = this.tagCategoryFormService.createTagCategoryFormGroup();

  compareTagCategory = (o1: ITagCategory | null, o2: ITagCategory | null): boolean => this.tagCategoryService.compareTagCategory(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tagCategory }) => {
      this.tagCategory = tagCategory;
      if (tagCategory) {
        this.updateForm(tagCategory);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tagCategory = this.tagCategoryFormService.getTagCategory(this.editForm);
    if (tagCategory.id === null) {
      this.subscribeToSaveResponse(this.tagCategoryService.create(tagCategory));
    } else {
      this.subscribeToSaveResponse(this.tagCategoryService.update(tagCategory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITagCategory>>): void {
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

  protected updateForm(tagCategory: ITagCategory): void {
    this.tagCategory = tagCategory;
    this.tagCategoryFormService.resetForm(this.editForm, tagCategory);

    this.tagCategoriesSharedCollection.set(
      this.tagCategoryService.addTagCategoryToCollectionIfMissing<ITagCategory>(this.tagCategoriesSharedCollection(), tagCategory.parent),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tagCategoryService
      .query()
      .pipe(map((res: HttpResponse<ITagCategory[]>) => res.body ?? []))
      .pipe(
        map((tagCategories: ITagCategory[]) =>
          this.tagCategoryService.addTagCategoryToCollectionIfMissing<ITagCategory>(tagCategories, this.tagCategory?.parent),
        ),
      )
      .subscribe((tagCategories: ITagCategory[]) => this.tagCategoriesSharedCollection.set(tagCategories));
  }
}
