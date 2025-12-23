import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IAutoTagJob } from 'app/entities/aiService/auto-tag-job/auto-tag-job.model';
import { AutoTagJobService } from 'app/entities/aiService/auto-tag-job/service/auto-tag-job.service';
import { TagPredictionService } from '../service/tag-prediction.service';
import { ITagPrediction } from '../tag-prediction.model';

import { TagPredictionFormService } from './tag-prediction-form.service';
import { TagPredictionUpdate } from './tag-prediction-update';

describe('TagPrediction Management Update Component', () => {
  let comp: TagPredictionUpdate;
  let fixture: ComponentFixture<TagPredictionUpdate>;
  let activatedRoute: ActivatedRoute;
  let tagPredictionFormService: TagPredictionFormService;
  let tagPredictionService: TagPredictionService;
  let autoTagJobService: AutoTagJobService;

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

    fixture = TestBed.createComponent(TagPredictionUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tagPredictionFormService = TestBed.inject(TagPredictionFormService);
    tagPredictionService = TestBed.inject(TagPredictionService);
    autoTagJobService = TestBed.inject(AutoTagJobService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call AutoTagJob query and add missing value', () => {
      const tagPrediction: ITagPrediction = { id: 7515 };
      const job: IAutoTagJob = { id: 5115 };
      tagPrediction.job = job;

      const autoTagJobCollection: IAutoTagJob[] = [{ id: 5115 }];
      jest.spyOn(autoTagJobService, 'query').mockReturnValue(of(new HttpResponse({ body: autoTagJobCollection })));
      const additionalAutoTagJobs = [job];
      const expectedCollection: IAutoTagJob[] = [...additionalAutoTagJobs, ...autoTagJobCollection];
      jest.spyOn(autoTagJobService, 'addAutoTagJobToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tagPrediction });
      comp.ngOnInit();

      expect(autoTagJobService.query).toHaveBeenCalled();
      expect(autoTagJobService.addAutoTagJobToCollectionIfMissing).toHaveBeenCalledWith(
        autoTagJobCollection,
        ...additionalAutoTagJobs.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.autoTagJobsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const tagPrediction: ITagPrediction = { id: 7515 };
      const job: IAutoTagJob = { id: 5115 };
      tagPrediction.job = job;

      activatedRoute.data = of({ tagPrediction });
      comp.ngOnInit();

      expect(comp.autoTagJobsSharedCollection()).toContainEqual(job);
      expect(comp.tagPrediction).toEqual(tagPrediction);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITagPrediction>>();
      const tagPrediction = { id: 7335 };
      jest.spyOn(tagPredictionFormService, 'getTagPrediction').mockReturnValue(tagPrediction);
      jest.spyOn(tagPredictionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tagPrediction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tagPrediction }));
      saveSubject.complete();

      // THEN
      expect(tagPredictionFormService.getTagPrediction).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tagPredictionService.update).toHaveBeenCalledWith(expect.objectContaining(tagPrediction));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITagPrediction>>();
      const tagPrediction = { id: 7335 };
      jest.spyOn(tagPredictionFormService, 'getTagPrediction').mockReturnValue({ id: null });
      jest.spyOn(tagPredictionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tagPrediction: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tagPrediction }));
      saveSubject.complete();

      // THEN
      expect(tagPredictionFormService.getTagPrediction).toHaveBeenCalled();
      expect(tagPredictionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITagPrediction>>();
      const tagPrediction = { id: 7335 };
      jest.spyOn(tagPredictionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tagPrediction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tagPredictionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAutoTagJob', () => {
      it('should forward to autoTagJobService', () => {
        const entity = { id: 5115 };
        const entity2 = { id: 3881 };
        jest.spyOn(autoTagJobService, 'compareAutoTagJob');
        comp.compareAutoTagJob(entity, entity2);
        expect(autoTagJobService.compareAutoTagJob).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
