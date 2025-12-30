import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { TransformRedactionJobService } from '../service/transform-redaction-job.service';
import { ITransformRedactionJob } from '../transform-redaction-job.model';
import { TransformRedactionJobFormService } from './transform-redaction-job-form.service';

import { TransformRedactionJobUpdateComponent } from './transform-redaction-job-update.component';

describe('TransformRedactionJob Management Update Component', () => {
  let comp: TransformRedactionJobUpdateComponent;
  let fixture: ComponentFixture<TransformRedactionJobUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let transformRedactionJobFormService: TransformRedactionJobFormService;
  let transformRedactionJobService: TransformRedactionJobService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TransformRedactionJobUpdateComponent],
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
      .overrideTemplate(TransformRedactionJobUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TransformRedactionJobUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    transformRedactionJobFormService = TestBed.inject(TransformRedactionJobFormService);
    transformRedactionJobService = TestBed.inject(TransformRedactionJobService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const transformRedactionJob: ITransformRedactionJob = { id: 28775 };

      activatedRoute.data = of({ transformRedactionJob });
      comp.ngOnInit();

      expect(comp.transformRedactionJob).toEqual(transformRedactionJob);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransformRedactionJob>>();
      const transformRedactionJob = { id: 32171 };
      jest.spyOn(transformRedactionJobFormService, 'getTransformRedactionJob').mockReturnValue(transformRedactionJob);
      jest.spyOn(transformRedactionJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transformRedactionJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transformRedactionJob }));
      saveSubject.complete();

      // THEN
      expect(transformRedactionJobFormService.getTransformRedactionJob).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(transformRedactionJobService.update).toHaveBeenCalledWith(expect.objectContaining(transformRedactionJob));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransformRedactionJob>>();
      const transformRedactionJob = { id: 32171 };
      jest.spyOn(transformRedactionJobFormService, 'getTransformRedactionJob').mockReturnValue({ id: null });
      jest.spyOn(transformRedactionJobService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transformRedactionJob: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transformRedactionJob }));
      saveSubject.complete();

      // THEN
      expect(transformRedactionJobFormService.getTransformRedactionJob).toHaveBeenCalled();
      expect(transformRedactionJobService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransformRedactionJob>>();
      const transformRedactionJob = { id: 32171 };
      jest.spyOn(transformRedactionJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transformRedactionJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(transformRedactionJobService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
