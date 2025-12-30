import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { TransformConversionJobService } from '../service/transform-conversion-job.service';
import { ITransformConversionJob } from '../transform-conversion-job.model';
import { TransformConversionJobFormService } from './transform-conversion-job-form.service';

import { TransformConversionJobUpdateComponent } from './transform-conversion-job-update.component';

describe('TransformConversionJob Management Update Component', () => {
  let comp: TransformConversionJobUpdateComponent;
  let fixture: ComponentFixture<TransformConversionJobUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let transformConversionJobFormService: TransformConversionJobFormService;
  let transformConversionJobService: TransformConversionJobService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TransformConversionJobUpdateComponent],
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
      .overrideTemplate(TransformConversionJobUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TransformConversionJobUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    transformConversionJobFormService = TestBed.inject(TransformConversionJobFormService);
    transformConversionJobService = TestBed.inject(TransformConversionJobService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const transformConversionJob: ITransformConversionJob = { id: 9217 };

      activatedRoute.data = of({ transformConversionJob });
      comp.ngOnInit();

      expect(comp.transformConversionJob).toEqual(transformConversionJob);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransformConversionJob>>();
      const transformConversionJob = { id: 1719 };
      jest.spyOn(transformConversionJobFormService, 'getTransformConversionJob').mockReturnValue(transformConversionJob);
      jest.spyOn(transformConversionJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transformConversionJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transformConversionJob }));
      saveSubject.complete();

      // THEN
      expect(transformConversionJobFormService.getTransformConversionJob).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(transformConversionJobService.update).toHaveBeenCalledWith(expect.objectContaining(transformConversionJob));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransformConversionJob>>();
      const transformConversionJob = { id: 1719 };
      jest.spyOn(transformConversionJobFormService, 'getTransformConversionJob').mockReturnValue({ id: null });
      jest.spyOn(transformConversionJobService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transformConversionJob: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transformConversionJob }));
      saveSubject.complete();

      // THEN
      expect(transformConversionJobFormService.getTransformConversionJob).toHaveBeenCalled();
      expect(transformConversionJobService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransformConversionJob>>();
      const transformConversionJob = { id: 1719 };
      jest.spyOn(transformConversionJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transformConversionJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(transformConversionJobService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
