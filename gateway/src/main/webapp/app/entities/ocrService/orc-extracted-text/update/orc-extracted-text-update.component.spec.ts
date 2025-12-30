import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IOcrJob } from 'app/entities/ocrService/ocr-job/ocr-job.model';
import { OcrJobService } from 'app/entities/ocrService/ocr-job/service/ocr-job.service';
import { OrcExtractedTextService } from '../service/orc-extracted-text.service';
import { IOrcExtractedText } from '../orc-extracted-text.model';
import { OrcExtractedTextFormService } from './orc-extracted-text-form.service';

import { OrcExtractedTextUpdateComponent } from './orc-extracted-text-update.component';

describe('OrcExtractedText Management Update Component', () => {
  let comp: OrcExtractedTextUpdateComponent;
  let fixture: ComponentFixture<OrcExtractedTextUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let orcExtractedTextFormService: OrcExtractedTextFormService;
  let orcExtractedTextService: OrcExtractedTextService;
  let ocrJobService: OcrJobService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [OrcExtractedTextUpdateComponent],
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
      .overrideTemplate(OrcExtractedTextUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrcExtractedTextUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orcExtractedTextFormService = TestBed.inject(OrcExtractedTextFormService);
    orcExtractedTextService = TestBed.inject(OrcExtractedTextService);
    ocrJobService = TestBed.inject(OcrJobService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call OcrJob query and add missing value', () => {
      const orcExtractedText: IOrcExtractedText = { id: 28138 };
      const job: IOcrJob = { id: 3289 };
      orcExtractedText.job = job;

      const ocrJobCollection: IOcrJob[] = [{ id: 3289 }];
      jest.spyOn(ocrJobService, 'query').mockReturnValue(of(new HttpResponse({ body: ocrJobCollection })));
      const additionalOcrJobs = [job];
      const expectedCollection: IOcrJob[] = [...additionalOcrJobs, ...ocrJobCollection];
      jest.spyOn(ocrJobService, 'addOcrJobToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orcExtractedText });
      comp.ngOnInit();

      expect(ocrJobService.query).toHaveBeenCalled();
      expect(ocrJobService.addOcrJobToCollectionIfMissing).toHaveBeenCalledWith(
        ocrJobCollection,
        ...additionalOcrJobs.map(expect.objectContaining),
      );
      expect(comp.ocrJobsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const orcExtractedText: IOrcExtractedText = { id: 28138 };
      const job: IOcrJob = { id: 3289 };
      orcExtractedText.job = job;

      activatedRoute.data = of({ orcExtractedText });
      comp.ngOnInit();

      expect(comp.ocrJobsSharedCollection).toContainEqual(job);
      expect(comp.orcExtractedText).toEqual(orcExtractedText);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrcExtractedText>>();
      const orcExtractedText = { id: 23372 };
      jest.spyOn(orcExtractedTextFormService, 'getOrcExtractedText').mockReturnValue(orcExtractedText);
      jest.spyOn(orcExtractedTextService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orcExtractedText });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orcExtractedText }));
      saveSubject.complete();

      // THEN
      expect(orcExtractedTextFormService.getOrcExtractedText).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orcExtractedTextService.update).toHaveBeenCalledWith(expect.objectContaining(orcExtractedText));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrcExtractedText>>();
      const orcExtractedText = { id: 23372 };
      jest.spyOn(orcExtractedTextFormService, 'getOrcExtractedText').mockReturnValue({ id: null });
      jest.spyOn(orcExtractedTextService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orcExtractedText: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orcExtractedText }));
      saveSubject.complete();

      // THEN
      expect(orcExtractedTextFormService.getOrcExtractedText).toHaveBeenCalled();
      expect(orcExtractedTextService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrcExtractedText>>();
      const orcExtractedText = { id: 23372 };
      jest.spyOn(orcExtractedTextService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orcExtractedText });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orcExtractedTextService.update).toHaveBeenCalled();
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
