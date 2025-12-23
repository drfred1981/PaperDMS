import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IOcrJob } from 'app/entities/ocrService/ocr-job/ocr-job.model';
import { OcrJobService } from 'app/entities/ocrService/ocr-job/service/ocr-job.service';
import { IExtractedText } from '../extracted-text.model';
import { ExtractedTextService } from '../service/extracted-text.service';

import { ExtractedTextFormService } from './extracted-text-form.service';
import { ExtractedTextUpdate } from './extracted-text-update';

describe('ExtractedText Management Update Component', () => {
  let comp: ExtractedTextUpdate;
  let fixture: ComponentFixture<ExtractedTextUpdate>;
  let activatedRoute: ActivatedRoute;
  let extractedTextFormService: ExtractedTextFormService;
  let extractedTextService: ExtractedTextService;
  let ocrJobService: OcrJobService;

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

    fixture = TestBed.createComponent(ExtractedTextUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    extractedTextFormService = TestBed.inject(ExtractedTextFormService);
    extractedTextService = TestBed.inject(ExtractedTextService);
    ocrJobService = TestBed.inject(OcrJobService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call OcrJob query and add missing value', () => {
      const extractedText: IExtractedText = { id: 28181 };
      const job: IOcrJob = { id: 3289 };
      extractedText.job = job;

      const ocrJobCollection: IOcrJob[] = [{ id: 3289 }];
      jest.spyOn(ocrJobService, 'query').mockReturnValue(of(new HttpResponse({ body: ocrJobCollection })));
      const additionalOcrJobs = [job];
      const expectedCollection: IOcrJob[] = [...additionalOcrJobs, ...ocrJobCollection];
      jest.spyOn(ocrJobService, 'addOcrJobToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ extractedText });
      comp.ngOnInit();

      expect(ocrJobService.query).toHaveBeenCalled();
      expect(ocrJobService.addOcrJobToCollectionIfMissing).toHaveBeenCalledWith(
        ocrJobCollection,
        ...additionalOcrJobs.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.ocrJobsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const extractedText: IExtractedText = { id: 28181 };
      const job: IOcrJob = { id: 3289 };
      extractedText.job = job;

      activatedRoute.data = of({ extractedText });
      comp.ngOnInit();

      expect(comp.ocrJobsSharedCollection()).toContainEqual(job);
      expect(comp.extractedText).toEqual(extractedText);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExtractedText>>();
      const extractedText = { id: 16480 };
      jest.spyOn(extractedTextFormService, 'getExtractedText').mockReturnValue(extractedText);
      jest.spyOn(extractedTextService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ extractedText });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: extractedText }));
      saveSubject.complete();

      // THEN
      expect(extractedTextFormService.getExtractedText).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(extractedTextService.update).toHaveBeenCalledWith(expect.objectContaining(extractedText));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExtractedText>>();
      const extractedText = { id: 16480 };
      jest.spyOn(extractedTextFormService, 'getExtractedText').mockReturnValue({ id: null });
      jest.spyOn(extractedTextService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ extractedText: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: extractedText }));
      saveSubject.complete();

      // THEN
      expect(extractedTextFormService.getExtractedText).toHaveBeenCalled();
      expect(extractedTextService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExtractedText>>();
      const extractedText = { id: 16480 };
      jest.spyOn(extractedTextService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ extractedText });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(extractedTextService.update).toHaveBeenCalled();
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
