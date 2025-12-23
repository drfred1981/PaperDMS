import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IOcrComparison } from '../ocr-comparison.model';
import { OcrComparisonService } from '../service/ocr-comparison.service';

import { OcrComparisonFormService } from './ocr-comparison-form.service';
import { OcrComparisonUpdate } from './ocr-comparison-update';

describe('OcrComparison Management Update Component', () => {
  let comp: OcrComparisonUpdate;
  let fixture: ComponentFixture<OcrComparisonUpdate>;
  let activatedRoute: ActivatedRoute;
  let ocrComparisonFormService: OcrComparisonFormService;
  let ocrComparisonService: OcrComparisonService;

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

    fixture = TestBed.createComponent(OcrComparisonUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ocrComparisonFormService = TestBed.inject(OcrComparisonFormService);
    ocrComparisonService = TestBed.inject(OcrComparisonService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const ocrComparison: IOcrComparison = { id: 21003 };

      activatedRoute.data = of({ ocrComparison });
      comp.ngOnInit();

      expect(comp.ocrComparison).toEqual(ocrComparison);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOcrComparison>>();
      const ocrComparison = { id: 31164 };
      jest.spyOn(ocrComparisonFormService, 'getOcrComparison').mockReturnValue(ocrComparison);
      jest.spyOn(ocrComparisonService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ocrComparison });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ocrComparison }));
      saveSubject.complete();

      // THEN
      expect(ocrComparisonFormService.getOcrComparison).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ocrComparisonService.update).toHaveBeenCalledWith(expect.objectContaining(ocrComparison));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOcrComparison>>();
      const ocrComparison = { id: 31164 };
      jest.spyOn(ocrComparisonFormService, 'getOcrComparison').mockReturnValue({ id: null });
      jest.spyOn(ocrComparisonService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ocrComparison: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ocrComparison }));
      saveSubject.complete();

      // THEN
      expect(ocrComparisonFormService.getOcrComparison).toHaveBeenCalled();
      expect(ocrComparisonService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOcrComparison>>();
      const ocrComparison = { id: 31164 };
      jest.spyOn(ocrComparisonService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ocrComparison });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ocrComparisonService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
