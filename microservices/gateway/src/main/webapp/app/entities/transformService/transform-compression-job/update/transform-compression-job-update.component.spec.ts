import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { TransformCompressionJobService } from '../service/transform-compression-job.service';
import { ITransformCompressionJob } from '../transform-compression-job.model';
import { TransformCompressionJobFormService } from './transform-compression-job-form.service';

import { TransformCompressionJobUpdateComponent } from './transform-compression-job-update.component';

describe('TransformCompressionJob Management Update Component', () => {
  let comp: TransformCompressionJobUpdateComponent;
  let fixture: ComponentFixture<TransformCompressionJobUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let transformCompressionJobFormService: TransformCompressionJobFormService;
  let transformCompressionJobService: TransformCompressionJobService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TransformCompressionJobUpdateComponent],
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
      .overrideTemplate(TransformCompressionJobUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TransformCompressionJobUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    transformCompressionJobFormService = TestBed.inject(TransformCompressionJobFormService);
    transformCompressionJobService = TestBed.inject(TransformCompressionJobService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const transformCompressionJob: ITransformCompressionJob = { id: 1867 };

      activatedRoute.data = of({ transformCompressionJob });
      comp.ngOnInit();

      expect(comp.transformCompressionJob).toEqual(transformCompressionJob);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransformCompressionJob>>();
      const transformCompressionJob = { id: 13851 };
      jest.spyOn(transformCompressionJobFormService, 'getTransformCompressionJob').mockReturnValue(transformCompressionJob);
      jest.spyOn(transformCompressionJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transformCompressionJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transformCompressionJob }));
      saveSubject.complete();

      // THEN
      expect(transformCompressionJobFormService.getTransformCompressionJob).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(transformCompressionJobService.update).toHaveBeenCalledWith(expect.objectContaining(transformCompressionJob));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransformCompressionJob>>();
      const transformCompressionJob = { id: 13851 };
      jest.spyOn(transformCompressionJobFormService, 'getTransformCompressionJob').mockReturnValue({ id: null });
      jest.spyOn(transformCompressionJobService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transformCompressionJob: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transformCompressionJob }));
      saveSubject.complete();

      // THEN
      expect(transformCompressionJobFormService.getTransformCompressionJob).toHaveBeenCalled();
      expect(transformCompressionJobService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransformCompressionJob>>();
      const transformCompressionJob = { id: 13851 };
      jest.spyOn(transformCompressionJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transformCompressionJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(transformCompressionJobService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
