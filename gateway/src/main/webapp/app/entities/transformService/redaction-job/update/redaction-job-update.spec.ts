import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IRedactionJob } from '../redaction-job.model';
import { RedactionJobService } from '../service/redaction-job.service';

import { RedactionJobFormService } from './redaction-job-form.service';
import { RedactionJobUpdate } from './redaction-job-update';

describe('RedactionJob Management Update Component', () => {
  let comp: RedactionJobUpdate;
  let fixture: ComponentFixture<RedactionJobUpdate>;
  let activatedRoute: ActivatedRoute;
  let redactionJobFormService: RedactionJobFormService;
  let redactionJobService: RedactionJobService;

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

    fixture = TestBed.createComponent(RedactionJobUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    redactionJobFormService = TestBed.inject(RedactionJobFormService);
    redactionJobService = TestBed.inject(RedactionJobService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const redactionJob: IRedactionJob = { id: 1724 };

      activatedRoute.data = of({ redactionJob });
      comp.ngOnInit();

      expect(comp.redactionJob).toEqual(redactionJob);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRedactionJob>>();
      const redactionJob = { id: 3246 };
      jest.spyOn(redactionJobFormService, 'getRedactionJob').mockReturnValue(redactionJob);
      jest.spyOn(redactionJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ redactionJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: redactionJob }));
      saveSubject.complete();

      // THEN
      expect(redactionJobFormService.getRedactionJob).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(redactionJobService.update).toHaveBeenCalledWith(expect.objectContaining(redactionJob));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRedactionJob>>();
      const redactionJob = { id: 3246 };
      jest.spyOn(redactionJobFormService, 'getRedactionJob').mockReturnValue({ id: null });
      jest.spyOn(redactionJobService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ redactionJob: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: redactionJob }));
      saveSubject.complete();

      // THEN
      expect(redactionJobFormService.getRedactionJob).toHaveBeenCalled();
      expect(redactionJobService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRedactionJob>>();
      const redactionJob = { id: 3246 };
      jest.spyOn(redactionJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ redactionJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(redactionJobService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
