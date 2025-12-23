import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IOcrJob } from '../ocr-job.model';
import { OcrJobService } from '../service/ocr-job.service';

import { OcrJobFormService } from './ocr-job-form.service';
import { OcrJobUpdate } from './ocr-job-update';

describe('OcrJob Management Update Component', () => {
  let comp: OcrJobUpdate;
  let fixture: ComponentFixture<OcrJobUpdate>;
  let activatedRoute: ActivatedRoute;
  let ocrJobFormService: OcrJobFormService;
  let ocrJobService: OcrJobService;

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

    fixture = TestBed.createComponent(OcrJobUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ocrJobFormService = TestBed.inject(OcrJobFormService);
    ocrJobService = TestBed.inject(OcrJobService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const ocrJob: IOcrJob = { id: 11926 };

      activatedRoute.data = of({ ocrJob });
      comp.ngOnInit();

      expect(comp.ocrJob).toEqual(ocrJob);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOcrJob>>();
      const ocrJob = { id: 3289 };
      jest.spyOn(ocrJobFormService, 'getOcrJob').mockReturnValue(ocrJob);
      jest.spyOn(ocrJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ocrJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ocrJob }));
      saveSubject.complete();

      // THEN
      expect(ocrJobFormService.getOcrJob).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ocrJobService.update).toHaveBeenCalledWith(expect.objectContaining(ocrJob));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOcrJob>>();
      const ocrJob = { id: 3289 };
      jest.spyOn(ocrJobFormService, 'getOcrJob').mockReturnValue({ id: null });
      jest.spyOn(ocrJobService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ocrJob: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ocrJob }));
      saveSubject.complete();

      // THEN
      expect(ocrJobFormService.getOcrJob).toHaveBeenCalled();
      expect(ocrJobService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOcrJob>>();
      const ocrJob = { id: 3289 };
      jest.spyOn(ocrJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ocrJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ocrJobService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
