import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IAITypePrediction } from 'app/entities/aiService/ai-type-prediction/ai-type-prediction.model';
import { AITypePredictionService } from 'app/entities/aiService/ai-type-prediction/service/ai-type-prediction.service';
import { IAILanguageDetection } from 'app/entities/aiService/ai-language-detection/ai-language-detection.model';
import { AILanguageDetectionService } from 'app/entities/aiService/ai-language-detection/service/ai-language-detection.service';
import { IAIAutoTagJob } from '../ai-auto-tag-job.model';
import { AIAutoTagJobService } from '../service/ai-auto-tag-job.service';
import { AIAutoTagJobFormService } from './ai-auto-tag-job-form.service';

import { AIAutoTagJobUpdateComponent } from './ai-auto-tag-job-update.component';

describe('AIAutoTagJob Management Update Component', () => {
  let comp: AIAutoTagJobUpdateComponent;
  let fixture: ComponentFixture<AIAutoTagJobUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let aIAutoTagJobFormService: AIAutoTagJobFormService;
  let aIAutoTagJobService: AIAutoTagJobService;
  let aITypePredictionService: AITypePredictionService;
  let aILanguageDetectionService: AILanguageDetectionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AIAutoTagJobUpdateComponent],
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
      .overrideTemplate(AIAutoTagJobUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AIAutoTagJobUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    aIAutoTagJobFormService = TestBed.inject(AIAutoTagJobFormService);
    aIAutoTagJobService = TestBed.inject(AIAutoTagJobService);
    aITypePredictionService = TestBed.inject(AITypePredictionService);
    aILanguageDetectionService = TestBed.inject(AILanguageDetectionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call aITypePrediction query and add missing value', () => {
      const aIAutoTagJob: IAIAutoTagJob = { id: 2294 };
      const aITypePrediction: IAITypePrediction = { id: 133 };
      aIAutoTagJob.aITypePrediction = aITypePrediction;

      const aITypePredictionCollection: IAITypePrediction[] = [{ id: 133 }];
      jest.spyOn(aITypePredictionService, 'query').mockReturnValue(of(new HttpResponse({ body: aITypePredictionCollection })));
      const expectedCollection: IAITypePrediction[] = [aITypePrediction, ...aITypePredictionCollection];
      jest.spyOn(aITypePredictionService, 'addAITypePredictionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ aIAutoTagJob });
      comp.ngOnInit();

      expect(aITypePredictionService.query).toHaveBeenCalled();
      expect(aITypePredictionService.addAITypePredictionToCollectionIfMissing).toHaveBeenCalledWith(
        aITypePredictionCollection,
        aITypePrediction,
      );
      expect(comp.aITypePredictionsCollection).toEqual(expectedCollection);
    });

    it('should call languagePrediction query and add missing value', () => {
      const aIAutoTagJob: IAIAutoTagJob = { id: 2294 };
      const languagePrediction: IAILanguageDetection = { id: 3507 };
      aIAutoTagJob.languagePrediction = languagePrediction;

      const languagePredictionCollection: IAILanguageDetection[] = [{ id: 3507 }];
      jest.spyOn(aILanguageDetectionService, 'query').mockReturnValue(of(new HttpResponse({ body: languagePredictionCollection })));
      const expectedCollection: IAILanguageDetection[] = [languagePrediction, ...languagePredictionCollection];
      jest.spyOn(aILanguageDetectionService, 'addAILanguageDetectionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ aIAutoTagJob });
      comp.ngOnInit();

      expect(aILanguageDetectionService.query).toHaveBeenCalled();
      expect(aILanguageDetectionService.addAILanguageDetectionToCollectionIfMissing).toHaveBeenCalledWith(
        languagePredictionCollection,
        languagePrediction,
      );
      expect(comp.languagePredictionsCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const aIAutoTagJob: IAIAutoTagJob = { id: 2294 };
      const aITypePrediction: IAITypePrediction = { id: 133 };
      aIAutoTagJob.aITypePrediction = aITypePrediction;
      const languagePrediction: IAILanguageDetection = { id: 3507 };
      aIAutoTagJob.languagePrediction = languagePrediction;

      activatedRoute.data = of({ aIAutoTagJob });
      comp.ngOnInit();

      expect(comp.aITypePredictionsCollection).toContainEqual(aITypePrediction);
      expect(comp.languagePredictionsCollection).toContainEqual(languagePrediction);
      expect(comp.aIAutoTagJob).toEqual(aIAutoTagJob);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAIAutoTagJob>>();
      const aIAutoTagJob = { id: 7774 };
      jest.spyOn(aIAutoTagJobFormService, 'getAIAutoTagJob').mockReturnValue(aIAutoTagJob);
      jest.spyOn(aIAutoTagJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aIAutoTagJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aIAutoTagJob }));
      saveSubject.complete();

      // THEN
      expect(aIAutoTagJobFormService.getAIAutoTagJob).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(aIAutoTagJobService.update).toHaveBeenCalledWith(expect.objectContaining(aIAutoTagJob));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAIAutoTagJob>>();
      const aIAutoTagJob = { id: 7774 };
      jest.spyOn(aIAutoTagJobFormService, 'getAIAutoTagJob').mockReturnValue({ id: null });
      jest.spyOn(aIAutoTagJobService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aIAutoTagJob: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aIAutoTagJob }));
      saveSubject.complete();

      // THEN
      expect(aIAutoTagJobFormService.getAIAutoTagJob).toHaveBeenCalled();
      expect(aIAutoTagJobService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAIAutoTagJob>>();
      const aIAutoTagJob = { id: 7774 };
      jest.spyOn(aIAutoTagJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aIAutoTagJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(aIAutoTagJobService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAITypePrediction', () => {
      it('should forward to aITypePredictionService', () => {
        const entity = { id: 133 };
        const entity2 = { id: 17407 };
        jest.spyOn(aITypePredictionService, 'compareAITypePrediction');
        comp.compareAITypePrediction(entity, entity2);
        expect(aITypePredictionService.compareAITypePrediction).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareAILanguageDetection', () => {
      it('should forward to aILanguageDetectionService', () => {
        const entity = { id: 3507 };
        const entity2 = { id: 26399 };
        jest.spyOn(aILanguageDetectionService, 'compareAILanguageDetection');
        comp.compareAILanguageDetection(entity, entity2);
        expect(aILanguageDetectionService.compareAILanguageDetection).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
