import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { TransformWatermarkJobService } from '../service/transform-watermark-job.service';
import { ITransformWatermarkJob } from '../transform-watermark-job.model';
import { TransformWatermarkJobFormService } from './transform-watermark-job-form.service';

import { TransformWatermarkJobUpdateComponent } from './transform-watermark-job-update.component';

describe('TransformWatermarkJob Management Update Component', () => {
  let comp: TransformWatermarkJobUpdateComponent;
  let fixture: ComponentFixture<TransformWatermarkJobUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let transformWatermarkJobFormService: TransformWatermarkJobFormService;
  let transformWatermarkJobService: TransformWatermarkJobService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TransformWatermarkJobUpdateComponent],
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
      .overrideTemplate(TransformWatermarkJobUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TransformWatermarkJobUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    transformWatermarkJobFormService = TestBed.inject(TransformWatermarkJobFormService);
    transformWatermarkJobService = TestBed.inject(TransformWatermarkJobService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const transformWatermarkJob: ITransformWatermarkJob = { id: 16524 };

      activatedRoute.data = of({ transformWatermarkJob });
      comp.ngOnInit();

      expect(comp.transformWatermarkJob).toEqual(transformWatermarkJob);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransformWatermarkJob>>();
      const transformWatermarkJob = { id: 15528 };
      jest.spyOn(transformWatermarkJobFormService, 'getTransformWatermarkJob').mockReturnValue(transformWatermarkJob);
      jest.spyOn(transformWatermarkJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transformWatermarkJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transformWatermarkJob }));
      saveSubject.complete();

      // THEN
      expect(transformWatermarkJobFormService.getTransformWatermarkJob).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(transformWatermarkJobService.update).toHaveBeenCalledWith(expect.objectContaining(transformWatermarkJob));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransformWatermarkJob>>();
      const transformWatermarkJob = { id: 15528 };
      jest.spyOn(transformWatermarkJobFormService, 'getTransformWatermarkJob').mockReturnValue({ id: null });
      jest.spyOn(transformWatermarkJobService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transformWatermarkJob: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transformWatermarkJob }));
      saveSubject.complete();

      // THEN
      expect(transformWatermarkJobFormService.getTransformWatermarkJob).toHaveBeenCalled();
      expect(transformWatermarkJobService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransformWatermarkJob>>();
      const transformWatermarkJob = { id: 15528 };
      jest.spyOn(transformWatermarkJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transformWatermarkJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(transformWatermarkJobService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
