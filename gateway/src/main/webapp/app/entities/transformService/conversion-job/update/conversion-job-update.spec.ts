import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IConversionJob } from '../conversion-job.model';
import { ConversionJobService } from '../service/conversion-job.service';

import { ConversionJobFormService } from './conversion-job-form.service';
import { ConversionJobUpdate } from './conversion-job-update';

describe('ConversionJob Management Update Component', () => {
  let comp: ConversionJobUpdate;
  let fixture: ComponentFixture<ConversionJobUpdate>;
  let activatedRoute: ActivatedRoute;
  let conversionJobFormService: ConversionJobFormService;
  let conversionJobService: ConversionJobService;

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

    fixture = TestBed.createComponent(ConversionJobUpdate);
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
