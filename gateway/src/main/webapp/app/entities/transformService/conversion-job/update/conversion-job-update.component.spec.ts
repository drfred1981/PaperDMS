import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ConversionJobService } from '../service/conversion-job.service';
import { IConversionJob } from '../conversion-job.model';
import { ConversionJobFormService } from './conversion-job-form.service';

import { ConversionJobUpdateComponent } from './conversion-job-update.component';

describe('ConversionJob Management Update Component', () => {
  let comp: ConversionJobUpdateComponent;
  let fixture: ComponentFixture<ConversionJobUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let conversionJobFormService: ConversionJobFormService;
  let conversionJobService: ConversionJobService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ConversionJobUpdateComponent],
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
      .overrideTemplate(ConversionJobUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConversionJobUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    conversionJobFormService = TestBed.inject(ConversionJobFormService);
    conversionJobService = TestBed.inject(ConversionJobService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const conversionJob: IConversionJob = { id: 1993 };

      activatedRoute.data = of({ conversionJob });
      comp.ngOnInit();

      expect(comp.conversionJob).toEqual(conversionJob);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConversionJob>>();
      const conversionJob = { id: 479 };
      jest.spyOn(conversionJobFormService, 'getConversionJob').mockReturnValue(conversionJob);
      jest.spyOn(conversionJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conversionJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: conversionJob }));
      saveSubject.complete();

      // THEN
      expect(conversionJobFormService.getConversionJob).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(conversionJobService.update).toHaveBeenCalledWith(expect.objectContaining(conversionJob));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConversionJob>>();
      const conversionJob = { id: 479 };
      jest.spyOn(conversionJobFormService, 'getConversionJob').mockReturnValue({ id: null });
      jest.spyOn(conversionJobService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conversionJob: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: conversionJob }));
      saveSubject.complete();

      // THEN
      expect(conversionJobFormService.getConversionJob).toHaveBeenCalled();
      expect(conversionJobService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConversionJob>>();
      const conversionJob = { id: 479 };
      jest.spyOn(conversionJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conversionJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(conversionJobService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
