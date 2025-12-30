import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IOcrJob } from 'app/entities/ocrService/ocr-job/ocr-job.model';
import { OcrJobService } from 'app/entities/ocrService/ocr-job/service/ocr-job.service';
import { OcrResultService } from '../service/ocr-result.service';
import { IOcrResult } from '../ocr-result.model';
import { OcrResultFormService } from './ocr-result-form.service';

import { OcrResultUpdateComponent } from './ocr-result-update.component';

describe('OcrResult Management Update Component', () => {
  let comp: OcrResultUpdateComponent;
  let fixture: ComponentFixture<OcrResultUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ocrResultFormService: OcrResultFormService;
  let ocrResultService: OcrResultService;
  let ocrJobService: OcrJobService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [OcrResultUpdateComponent],
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
      .overrideTemplate(OcrResultUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OcrResultUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ocrResultFormService = TestBed.inject(OcrResultFormService);
    ocrResultService = TestBed.inject(OcrResultService);
    ocrJobService = TestBed.inject(OcrJobService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call OcrJob query and add missing value', () => {
      const ocrResult: IOcrResult = { id: 13914 };
      const job: IOcrJob = { id: 3289 };
      ocrResult.job = job;

      const ocrJobCollection: IOcrJob[] = [{ id: 3289 }];
      jest.spyOn(ocrJobService, 'query').mockReturnValue(of(new HttpResponse({ body: ocrJobCollection })));
      const additionalOcrJobs = [job];
      const expectedCollection: IOcrJob[] = [...additionalOcrJobs, ...ocrJobCollection];
      jest.spyOn(ocrJobService, 'addOcrJobToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ocrResult });
      comp.ngOnInit();

      expect(ocrJobService.query).toHaveBeenCalled();
      expect(ocrJobService.addOcrJobToCollectionIfMissing).toHaveBeenCalledWith(
        ocrJobCollection,
        ...additionalOcrJobs.map(expect.objectContaining),
      );
      expect(comp.ocrJobsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const ocrResult: IOcrResult = { id: 13914 };
      const job: IOcrJob = { id: 3289 };
      ocrResult.job = job;

      activatedRoute.data = of({ ocrResult });
      comp.ngOnInit();

      expect(comp.ocrJobsSharedCollection).toContainEqual(job);
      expect(comp.ocrResult).toEqual(ocrResult);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOcrResult>>();
      const ocrResult = { id: 27853 };
      jest.spyOn(ocrResultFormService, 'getOcrResult').mockReturnValue(ocrResult);
      jest.spyOn(ocrResultService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ocrResult });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ocrResult }));
      saveSubject.complete();

      // THEN
      expect(ocrResultFormService.getOcrResult).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ocrResultService.update).toHaveBeenCalledWith(expect.objectContaining(ocrResult));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOcrResult>>();
      const ocrResult = { id: 27853 };
      jest.spyOn(ocrResultFormService, 'getOcrResult').mockReturnValue({ id: null });
      jest.spyOn(ocrResultService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ocrResult: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ocrResult }));
      saveSubject.complete();

      // THEN
      expect(ocrResultFormService.getOcrResult).toHaveBeenCalled();
      expect(ocrResultService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOcrResult>>();
      const ocrResult = { id: 27853 };
      jest.spyOn(ocrResultService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ocrResult });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ocrResultService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareOcrJob', () => {
      it('should forward to ocrJobService', () => {
        const entity = { id: 3289 };
        const entity2 = { id: 11926 };
        jest.spyOn(ocrJobService, 'compareOcrJob');
        comp.compareOcrJob(entity, entity2);
        expect(ocrJobService.compareOcrJob).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
