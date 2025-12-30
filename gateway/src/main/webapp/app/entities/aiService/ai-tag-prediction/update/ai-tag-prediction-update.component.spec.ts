import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IAIAutoTagJob } from 'app/entities/aiService/ai-auto-tag-job/ai-auto-tag-job.model';
import { AIAutoTagJobService } from 'app/entities/aiService/ai-auto-tag-job/service/ai-auto-tag-job.service';
import { AITagPredictionService } from '../service/ai-tag-prediction.service';
import { IAITagPrediction } from '../ai-tag-prediction.model';
import { AITagPredictionFormService } from './ai-tag-prediction-form.service';

import { AITagPredictionUpdateComponent } from './ai-tag-prediction-update.component';

describe('AITagPrediction Management Update Component', () => {
  let comp: AITagPredictionUpdateComponent;
  let fixture: ComponentFixture<AITagPredictionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let aITagPredictionFormService: AITagPredictionFormService;
  let aITagPredictionService: AITagPredictionService;
  let aIAutoTagJobService: AIAutoTagJobService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AITagPredictionUpdateComponent],
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
      .overrideTemplate(AITagPredictionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AITagPredictionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    aITagPredictionFormService = TestBed.inject(AITagPredictionFormService);
    aITagPredictionService = TestBed.inject(AITagPredictionService);
    aIAutoTagJobService = TestBed.inject(AIAutoTagJobService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call AIAutoTagJob query and add missing value', () => {
      const aITagPrediction: IAITagPrediction = { id: 21155 };
      const job: IAIAutoTagJob = { id: 7774 };
      aITagPrediction.job = job;

      const aIAutoTagJobCollection: IAIAutoTagJob[] = [{ id: 7774 }];
      jest.spyOn(aIAutoTagJobService, 'query').mockReturnValue(of(new HttpResponse({ body: aIAutoTagJobCollection })));
      const additionalAIAutoTagJobs = [job];
      const expectedCollection: IAIAutoTagJob[] = [...additionalAIAutoTagJobs, ...aIAutoTagJobCollection];
      jest.spyOn(aIAutoTagJobService, 'addAIAutoTagJobToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ aITagPrediction });
      comp.ngOnInit();

      expect(aIAutoTagJobService.query).toHaveBeenCalled();
      expect(aIAutoTagJobService.addAIAutoTagJobToCollectionIfMissing).toHaveBeenCalledWith(
        aIAutoTagJobCollection,
        ...additionalAIAutoTagJobs.map(expect.objectContaining),
      );
      expect(comp.aIAutoTagJobsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const aITagPrediction: IAITagPrediction = { id: 21155 };
      const job: IAIAutoTagJob = { id: 7774 };
      aITagPrediction.job = job;

      activatedRoute.data = of({ aITagPrediction });
      comp.ngOnInit();

      expect(comp.aIAutoTagJobsSharedCollection).toContainEqual(job);
      expect(comp.aITagPrediction).toEqual(aITagPrediction);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAITagPrediction>>();
      const aITagPrediction = { id: 13214 };
      jest.spyOn(aITagPredictionFormService, 'getAITagPrediction').mockReturnValue(aITagPrediction);
      jest.spyOn(aITagPredictionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aITagPrediction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aITagPrediction }));
      saveSubject.complete();

      // THEN
      expect(aITagPredictionFormService.getAITagPrediction).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(aITagPredictionService.update).toHaveBeenCalledWith(expect.objectContaining(aITagPrediction));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAITagPrediction>>();
      const aITagPrediction = { id: 13214 };
      jest.spyOn(aITagPredictionFormService, 'getAITagPrediction').mockReturnValue({ id: null });
      jest.spyOn(aITagPredictionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aITagPrediction: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aITagPrediction }));
      saveSubject.complete();

      // THEN
      expect(aITagPredictionFormService.getAITagPrediction).toHaveBeenCalled();
      expect(aITagPredictionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAITagPrediction>>();
      const aITagPrediction = { id: 13214 };
      jest.spyOn(aITagPredictionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aITagPrediction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(aITagPredictionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAIAutoTagJob', () => {
      it('should forward to aIAutoTagJobService', () => {
        const entity = { id: 7774 };
        const entity2 = { id: 2294 };
        jest.spyOn(aIAutoTagJobService, 'compareAIAutoTagJob');
        comp.compareAIAutoTagJob(entity, entity2);
        expect(aIAutoTagJobService.compareAIAutoTagJob).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
