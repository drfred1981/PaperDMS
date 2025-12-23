import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IScheduledReport } from '../scheduled-report.model';
import { ScheduledReportService } from '../service/scheduled-report.service';

import { ScheduledReportFormService } from './scheduled-report-form.service';
import { ScheduledReportUpdate } from './scheduled-report-update';

describe('ScheduledReport Management Update Component', () => {
  let comp: ScheduledReportUpdate;
  let fixture: ComponentFixture<ScheduledReportUpdate>;
  let activatedRoute: ActivatedRoute;
  let scheduledReportFormService: ScheduledReportFormService;
  let scheduledReportService: ScheduledReportService;

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

    fixture = TestBed.createComponent(ScheduledReportUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    scheduledReportFormService = TestBed.inject(ScheduledReportFormService);
    scheduledReportService = TestBed.inject(ScheduledReportService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const scheduledReport: IScheduledReport = { id: 3093 };

      activatedRoute.data = of({ scheduledReport });
      comp.ngOnInit();

      expect(comp.scheduledReport).toEqual(scheduledReport);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IScheduledReport>>();
      const scheduledReport = { id: 28944 };
      jest.spyOn(scheduledReportFormService, 'getScheduledReport').mockReturnValue(scheduledReport);
      jest.spyOn(scheduledReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scheduledReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: scheduledReport }));
      saveSubject.complete();

      // THEN
      expect(scheduledReportFormService.getScheduledReport).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(scheduledReportService.update).toHaveBeenCalledWith(expect.objectContaining(scheduledReport));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IScheduledReport>>();
      const scheduledReport = { id: 28944 };
      jest.spyOn(scheduledReportFormService, 'getScheduledReport').mockReturnValue({ id: null });
      jest.spyOn(scheduledReportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scheduledReport: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: scheduledReport }));
      saveSubject.complete();

      // THEN
      expect(scheduledReportFormService.getScheduledReport).toHaveBeenCalled();
      expect(scheduledReportService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IScheduledReport>>();
      const scheduledReport = { id: 28944 };
      jest.spyOn(scheduledReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scheduledReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(scheduledReportService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
