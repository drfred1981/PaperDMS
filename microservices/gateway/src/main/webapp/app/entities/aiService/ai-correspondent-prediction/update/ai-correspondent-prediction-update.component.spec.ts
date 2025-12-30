import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IAIAutoTagJob } from 'app/entities/aiService/ai-auto-tag-job/ai-auto-tag-job.model';
import { AIAutoTagJobService } from 'app/entities/aiService/ai-auto-tag-job/service/ai-auto-tag-job.service';
import { AICorrespondentPredictionService } from '../service/ai-correspondent-prediction.service';
import { IAICorrespondentPrediction } from '../ai-correspondent-prediction.model';
import { AICorrespondentPredictionFormService } from './ai-correspondent-prediction-form.service';

import { AICorrespondentPredictionUpdateComponent } from './ai-correspondent-prediction-update.component';

describe('AICorrespondentPrediction Management Update Component', () => {
  let comp: AICorrespondentPredictionUpdateComponent;
  let fixture: ComponentFixture<AICorrespondentPredictionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let aICorrespondentPredictionFormService: AICorrespondentPredictionFormService;
  let aICorrespondentPredictionService: AICorrespondentPredictionService;
  let aIAutoTagJobService: AIAutoTagJobService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AICorrespondentPredictionUpdateComponent],
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
      .overrideTemplate(AICorrespondentPredictionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AICorrespondentPredictionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    aICorrespondentPredictionFormService = TestBed.inject(AICorrespondentPredictionFormService);
    aICorrespondentPredictionService = TestBed.inject(AICorrespondentPredictionService);
    aIAutoTagJobService = TestBed.inject(AIAutoTagJobService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call AIAutoTagJob query and add missing value', () => {
      const aICorrespondentPrediction: IAICorrespondentPrediction = { id: 2709 };
      const job: IAIAutoTagJob = { id: 7774 };
      aICorrespondentPrediction.job = job;

      const aIAutoTagJobCollection: IAIAutoTagJob[] = [{ id: 7774 }];
      jest.spyOn(aIAutoTagJobService, 'query').mockReturnValue(of(new HttpResponse({ body: aIAutoTagJobCollection })));
      const additionalAIAutoTagJobs = [job];
      const expectedCollection: IAIAutoTagJob[] = [...additionalAIAutoTagJobs, ...aIAutoTagJobCollection];
      jest.spyOn(aIAutoTagJobService, 'addAIAutoTagJobToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ aICorrespondentPrediction });
      comp.ngOnInit();

      expect(aIAutoTagJobService.query).toHaveBeenCalled();
      expect(aIAutoTagJobService.addAIAutoTagJobToCollectionIfMissing).toHaveBeenCalledWith(
        aIAutoTagJobCollection,
        ...additionalAIAutoTagJobs.map(expect.objectContaining),
      );
      expect(comp.aIAutoTagJobsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const aICorrespondentPrediction: IAICorrespondentPrediction = { id: 2709 };
      const job: IAIAutoTagJob = { id: 7774 };
      aICorrespondentPrediction.job = job;

      activatedRoute.data = of({ aICorrespondentPrediction });
      comp.ngOnInit();

      expect(comp.aIAutoTagJobsSharedCollection).toContainEqual(job);
      expect(comp.aICorrespondentPrediction).toEqual(aICorrespondentPrediction);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAICorrespondentPrediction>>();
      const aICorrespondentPrediction = { id: 18878 };
      jest.spyOn(aICorrespondentPredictionFormService, 'getAICorrespondentPrediction').mockReturnValue(aICorrespondentPrediction);
      jest.spyOn(aICorrespondentPredictionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aICorrespondentPrediction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aICorrespondentPrediction }));
      saveSubject.complete();

      // THEN
      expect(aICorrespondentPredictionFormService.getAICorrespondentPrediction).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(aICorrespondentPredictionService.update).toHaveBeenCalledWith(expect.objectContaining(aICorrespondentPrediction));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAICorrespondentPrediction>>();
      const aICorrespondentPrediction = { id: 18878 };
      jest.spyOn(aICorrespondentPredictionFormService, 'getAICorrespondentPrediction').mockReturnValue({ id: null });
      jest.spyOn(aICorrespondentPredictionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aICorrespondentPrediction: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aICorrespondentPrediction }));
      saveSubject.complete();

      // THEN
      expect(aICorrespondentPredictionFormService.getAICorrespondentPrediction).toHaveBeenCalled();
      expect(aICorrespondentPredictionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAICorrespondentPrediction>>();
      const aICorrespondentPrediction = { id: 18878 };
      jest.spyOn(aICorrespondentPredictionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aICorrespondentPrediction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(aICorrespondentPredictionService.update).toHaveBeenCalled();
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
