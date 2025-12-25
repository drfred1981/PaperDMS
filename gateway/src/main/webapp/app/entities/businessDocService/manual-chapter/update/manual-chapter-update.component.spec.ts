import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IManual } from 'app/entities/businessDocService/manual/manual.model';
import { ManualService } from 'app/entities/businessDocService/manual/service/manual.service';
import { ManualChapterService } from '../service/manual-chapter.service';
import { IManualChapter } from '../manual-chapter.model';
import { ManualChapterFormService } from './manual-chapter-form.service';

import { ManualChapterUpdateComponent } from './manual-chapter-update.component';

describe('ManualChapter Management Update Component', () => {
  let comp: ManualChapterUpdateComponent;
  let fixture: ComponentFixture<ManualChapterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let manualChapterFormService: ManualChapterFormService;
  let manualChapterService: ManualChapterService;
  let manualService: ManualService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ManualChapterUpdateComponent],
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
      .overrideTemplate(ManualChapterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ManualChapterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    manualChapterFormService = TestBed.inject(ManualChapterFormService);
    manualChapterService = TestBed.inject(ManualChapterService);
    manualService = TestBed.inject(ManualService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ManualChapter query and add missing value', () => {
      const manualChapter: IManualChapter = { id: 10019 };
      const parentChapter: IManualChapter = { id: 31776 };
      manualChapter.parentChapter = parentChapter;

      const manualChapterCollection: IManualChapter[] = [{ id: 31776 }];
      jest.spyOn(manualChapterService, 'query').mockReturnValue(of(new HttpResponse({ body: manualChapterCollection })));
      const additionalManualChapters = [parentChapter];
      const expectedCollection: IManualChapter[] = [...additionalManualChapters, ...manualChapterCollection];
      jest.spyOn(manualChapterService, 'addManualChapterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ manualChapter });
      comp.ngOnInit();

      expect(manualChapterService.query).toHaveBeenCalled();
      expect(manualChapterService.addManualChapterToCollectionIfMissing).toHaveBeenCalledWith(
        manualChapterCollection,
        ...additionalManualChapters.map(expect.objectContaining),
      );
      expect(comp.manualChaptersSharedCollection).toEqual(expectedCollection);
    });

    it('should call Manual query and add missing value', () => {
      const manualChapter: IManualChapter = { id: 10019 };
      const manual: IManual = { id: 16259 };
      manualChapter.manual = manual;

      const manualCollection: IManual[] = [{ id: 16259 }];
      jest.spyOn(manualService, 'query').mockReturnValue(of(new HttpResponse({ body: manualCollection })));
      const additionalManuals = [manual];
      const expectedCollection: IManual[] = [...additionalManuals, ...manualCollection];
      jest.spyOn(manualService, 'addManualToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ manualChapter });
      comp.ngOnInit();

      expect(manualService.query).toHaveBeenCalled();
      expect(manualService.addManualToCollectionIfMissing).toHaveBeenCalledWith(
        manualCollection,
        ...additionalManuals.map(expect.objectContaining),
      );
      expect(comp.manualsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const manualChapter: IManualChapter = { id: 10019 };
      const parentChapter: IManualChapter = { id: 31776 };
      manualChapter.parentChapter = parentChapter;
      const manual: IManual = { id: 16259 };
      manualChapter.manual = manual;

      activatedRoute.data = of({ manualChapter });
      comp.ngOnInit();

      expect(comp.manualChaptersSharedCollection).toContainEqual(parentChapter);
      expect(comp.manualsSharedCollection).toContainEqual(manual);
      expect(comp.manualChapter).toEqual(manualChapter);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IManualChapter>>();
      const manualChapter = { id: 31776 };
      jest.spyOn(manualChapterFormService, 'getManualChapter').mockReturnValue(manualChapter);
      jest.spyOn(manualChapterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ manualChapter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: manualChapter }));
      saveSubject.complete();

      // THEN
      expect(manualChapterFormService.getManualChapter).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(manualChapterService.update).toHaveBeenCalledWith(expect.objectContaining(manualChapter));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IManualChapter>>();
      const manualChapter = { id: 31776 };
      jest.spyOn(manualChapterFormService, 'getManualChapter').mockReturnValue({ id: null });
      jest.spyOn(manualChapterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ manualChapter: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: manualChapter }));
      saveSubject.complete();

      // THEN
      expect(manualChapterFormService.getManualChapter).toHaveBeenCalled();
      expect(manualChapterService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IManualChapter>>();
      const manualChapter = { id: 31776 };
      jest.spyOn(manualChapterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ manualChapter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(manualChapterService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareManualChapter', () => {
      it('should forward to manualChapterService', () => {
        const entity = { id: 31776 };
        const entity2 = { id: 10019 };
        jest.spyOn(manualChapterService, 'compareManualChapter');
        comp.compareManualChapter(entity, entity2);
        expect(manualChapterService.compareManualChapter).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareManual', () => {
      it('should forward to manualService', () => {
        const entity = { id: 16259 };
        const entity2 = { id: 17096 };
        jest.spyOn(manualService, 'compareManual');
        comp.compareManual(entity, entity2);
        expect(manualService.compareManual).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
