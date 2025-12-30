import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { TransformMergeJobService } from '../service/transform-merge-job.service';
import { ITransformMergeJob } from '../transform-merge-job.model';
import { TransformMergeJobFormService } from './transform-merge-job-form.service';

import { TransformMergeJobUpdateComponent } from './transform-merge-job-update.component';

describe('TransformMergeJob Management Update Component', () => {
  let comp: TransformMergeJobUpdateComponent;
  let fixture: ComponentFixture<TransformMergeJobUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let transformMergeJobFormService: TransformMergeJobFormService;
  let transformMergeJobService: TransformMergeJobService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TransformMergeJobUpdateComponent],
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
      .overrideTemplate(TransformMergeJobUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TransformMergeJobUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    transformMergeJobFormService = TestBed.inject(TransformMergeJobFormService);
    transformMergeJobService = TestBed.inject(TransformMergeJobService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const transformMergeJob: ITransformMergeJob = { id: 17170 };

      activatedRoute.data = of({ transformMergeJob });
      comp.ngOnInit();

      expect(comp.transformMergeJob).toEqual(transformMergeJob);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransformMergeJob>>();
      const transformMergeJob = { id: 7858 };
      jest.spyOn(transformMergeJobFormService, 'getTransformMergeJob').mockReturnValue(transformMergeJob);
      jest.spyOn(transformMergeJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transformMergeJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transformMergeJob }));
      saveSubject.complete();

      // THEN
      expect(transformMergeJobFormService.getTransformMergeJob).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(transformMergeJobService.update).toHaveBeenCalledWith(expect.objectContaining(transformMergeJob));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransformMergeJob>>();
      const transformMergeJob = { id: 7858 };
      jest.spyOn(transformMergeJobFormService, 'getTransformMergeJob').mockReturnValue({ id: null });
      jest.spyOn(transformMergeJobService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transformMergeJob: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transformMergeJob }));
      saveSubject.complete();

      // THEN
      expect(transformMergeJobFormService.getTransformMergeJob).toHaveBeenCalled();
      expect(transformMergeJobService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransformMergeJob>>();
      const transformMergeJob = { id: 7858 };
      jest.spyOn(transformMergeJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transformMergeJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(transformMergeJobService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
