import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IMetaMetaTagCategory } from 'app/entities/documentService/meta-meta-tag-category/meta-meta-tag-category.model';
import { MetaMetaTagCategoryService } from 'app/entities/documentService/meta-meta-tag-category/service/meta-meta-tag-category.service';
import { MetaTagService } from '../service/meta-tag.service';
import { IMetaTag } from '../meta-tag.model';
import { MetaTagFormService } from './meta-tag-form.service';

import { MetaTagUpdateComponent } from './meta-tag-update.component';

describe('MetaTag Management Update Component', () => {
  let comp: MetaTagUpdateComponent;
  let fixture: ComponentFixture<MetaTagUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let metaTagFormService: MetaTagFormService;
  let metaTagService: MetaTagService;
  let metaMetaTagCategoryService: MetaMetaTagCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MetaTagUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MetaTagUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MetaTagUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    metaTagFormService = TestBed.inject(MetaTagFormService);
    metaTagService = TestBed.inject(MetaTagService);
    metaMetaTagCategoryService = TestBed.inject(MetaMetaTagCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call MetaMetaTagCategory query and add missing value', () => {
      const metaTag: IMetaTag = { id: 29128 };
      const metaMetaTagCategory: IMetaMetaTagCategory = { id: 7139 };
      metaTag.metaMetaTagCategory = metaMetaTagCategory;

      const metaMetaTagCategoryCollection: IMetaMetaTagCategory[] = [{ id: 7139 }];
      jest.spyOn(metaMetaTagCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: metaMetaTagCategoryCollection })));
      const additionalMetaMetaTagCategories = [metaMetaTagCategory];
      const expectedCollection: IMetaMetaTagCategory[] = [...additionalMetaMetaTagCategories, ...metaMetaTagCategoryCollection];
      jest.spyOn(metaMetaTagCategoryService, 'addMetaMetaTagCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ metaTag });
      comp.ngOnInit();

      expect(metaMetaTagCategoryService.query).toHaveBeenCalled();
      expect(metaMetaTagCategoryService.addMetaMetaTagCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        metaMetaTagCategoryCollection,
        ...additionalMetaMetaTagCategories.map(expect.objectContaining),
      );
      expect(comp.metaMetaTagCategoriesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const metaTag: IMetaTag = { id: 29128 };
      const metaMetaTagCategory: IMetaMetaTagCategory = { id: 7139 };
      metaTag.metaMetaTagCategory = metaMetaTagCategory;

      activatedRoute.data = of({ metaTag });
      comp.ngOnInit();

      expect(comp.metaMetaTagCategoriesSharedCollection).toContainEqual(metaMetaTagCategory);
      expect(comp.metaTag).toEqual(metaTag);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMetaTag>>();
      const metaTag = { id: 11753 };
      jest.spyOn(metaTagFormService, 'getMetaTag').mockReturnValue(metaTag);
      jest.spyOn(metaTagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ metaTag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: metaTag }));
      saveSubject.complete();

      // THEN
      expect(metaTagFormService.getMetaTag).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(metaTagService.update).toHaveBeenCalledWith(expect.objectContaining(metaTag));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMetaTag>>();
      const metaTag = { id: 11753 };
      jest.spyOn(metaTagFormService, 'getMetaTag').mockReturnValue({ id: null });
      jest.spyOn(metaTagService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ metaTag: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: metaTag }));
      saveSubject.complete();

      // THEN
      expect(metaTagFormService.getMetaTag).toHaveBeenCalled();
      expect(metaTagService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMetaTag>>();
      const metaTag = { id: 11753 };
      jest.spyOn(metaTagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ metaTag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(metaTagService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMetaMetaTagCategory', () => {
      it('should forward to metaMetaTagCategoryService', () => {
        const entity = { id: 7139 };
        const entity2 = { id: 29219 };
        jest.spyOn(metaMetaTagCategoryService, 'compareMetaMetaTagCategory');
        comp.compareMetaMetaTagCategory(entity, entity2);
        expect(metaMetaTagCategoryService.compareMetaMetaTagCategory).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
