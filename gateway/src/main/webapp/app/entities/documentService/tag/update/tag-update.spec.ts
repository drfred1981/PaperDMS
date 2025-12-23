import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { TagCategoryService } from 'app/entities/documentService/tag-category/service/tag-category.service';
import { ITagCategory } from 'app/entities/documentService/tag-category/tag-category.model';
import { TagService } from '../service/tag.service';
import { ITag } from '../tag.model';

import { TagFormService } from './tag-form.service';
import { TagUpdate } from './tag-update';

describe('Tag Management Update Component', () => {
  let comp: TagUpdate;
  let fixture: ComponentFixture<TagUpdate>;
  let activatedRoute: ActivatedRoute;
  let tagFormService: TagFormService;
  let tagService: TagService;
  let tagCategoryService: TagCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TranslateModule.forRoot()],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(TagUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tagFormService = TestBed.inject(TagFormService);
    tagService = TestBed.inject(TagService);
    tagCategoryService = TestBed.inject(TagCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call TagCategory query and add missing value', () => {
      const tag: ITag = { id: 16779 };
      const tagCategory: ITagCategory = { id: 10601 };
      tag.tagCategory = tagCategory;

      const tagCategoryCollection: ITagCategory[] = [{ id: 10601 }];
      jest.spyOn(tagCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: tagCategoryCollection })));
      const additionalTagCategories = [tagCategory];
      const expectedCollection: ITagCategory[] = [...additionalTagCategories, ...tagCategoryCollection];
      jest.spyOn(tagCategoryService, 'addTagCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tag });
      comp.ngOnInit();

      expect(tagCategoryService.query).toHaveBeenCalled();
      expect(tagCategoryService.addTagCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        tagCategoryCollection,
        ...additionalTagCategories.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.tagCategoriesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const tag: ITag = { id: 16779 };
      const tagCategory: ITagCategory = { id: 10601 };
      tag.tagCategory = tagCategory;

      activatedRoute.data = of({ tag });
      comp.ngOnInit();

      expect(comp.tagCategoriesSharedCollection()).toContainEqual(tagCategory);
      expect(comp.tag).toEqual(tag);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITag>>();
      const tag = { id: 19931 };
      jest.spyOn(tagFormService, 'getTag').mockReturnValue(tag);
      jest.spyOn(tagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tag }));
      saveSubject.complete();

      // THEN
      expect(tagFormService.getTag).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tagService.update).toHaveBeenCalledWith(expect.objectContaining(tag));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITag>>();
      const tag = { id: 19931 };
      jest.spyOn(tagFormService, 'getTag').mockReturnValue({ id: null });
      jest.spyOn(tagService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tag: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tag }));
      saveSubject.complete();

      // THEN
      expect(tagFormService.getTag).toHaveBeenCalled();
      expect(tagService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITag>>();
      const tag = { id: 19931 };
      jest.spyOn(tagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tagService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTagCategory', () => {
      it('should forward to tagCategoryService', () => {
        const entity = { id: 10601 };
        const entity2 = { id: 13926 };
        jest.spyOn(tagCategoryService, 'compareTagCategory');
        comp.compareTagCategory(entity, entity2);
        expect(tagCategoryService.compareTagCategory).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
