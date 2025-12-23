import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IScheduledReport } from 'app/entities/reportingService/scheduled-report/scheduled-report.model';
import { ScheduledReportService } from 'app/entities/reportingService/scheduled-report/service/scheduled-report.service';
import { IReportExecution } from '../report-execution.model';
import { ReportExecutionService } from '../service/report-execution.service';

import { ReportExecutionFormService } from './report-execution-form.service';
import { ReportExecutionUpdate } from './report-execution-update';

describe('ReportExecution Management Update Component', () => {
  let comp: ReportExecutionUpdate;
  let fixture: ComponentFixture<ReportExecutionUpdate>;
  let activatedRoute: ActivatedRoute;
  let reportExecutionFormService: ReportExecutionFormService;
  let reportExecutionService: ReportExecutionService;
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

    fixture = TestBed.createComponent(ReportExecutionUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reportExecutionFormService = TestBed.inject(ReportExecutionFormService);
    reportExecutionService = TestBed.inject(ReportExecutionService);
    scheduledReportService = TestBed.inject(ScheduledReportService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ScheduledReport query and add missing value', () => {
      const reportExecution: IReportExecution = { id: 29743 };
      const scheduledReport: IScheduledReport = { id: 28944 };
      reportExecution.scheduledReport = scheduledReport;

      const scheduledReportCollection: IScheduledReport[] = [{ id: 28944 }];
      jest.spyOn(scheduledReportService, 'query').mockReturnValue(of(new HttpResponse({ body: scheduledReportCollection })));
      const additionalScheduledReports = [scheduledReport];
      const expectedCollection: IScheduledReport[] = [...additionalScheduledReports, ...scheduledReportCollection];
      jest.spyOn(scheduledReportService, 'addScheduledReportToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reportExecution });
      comp.ngOnInit();

      expect(scheduledReportService.query).toHaveBeenCalled();
      expect(scheduledReportService.addScheduledReportToCollectionIfMissing).toHaveBeenCalledWith(
        scheduledReportCollection,
        ...additionalScheduledReports.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.scheduledReportsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const reportExecution: IReportExecution = { id: 29743 };
      const scheduledReport: IScheduledReport = { id: 28944 };
      reportExecution.scheduledReport = scheduledReport;

      activatedRoute.data = of({ reportExecution });
      comp.ngOnInit();

      expect(comp.scheduledReportsSharedCollection()).toContainEqual(scheduledReport);
      expect(comp.reportExecution).toEqual(reportExecution);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportExecution>>();
      const reportExecution = { id: 23917 };
      jest.spyOn(reportExecutionFormService, 'getReportExecution').mockReturnValue(reportExecution);
      jest.spyOn(reportExecutionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportExecution });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportExecution }));
      saveSubject.complete();

      // THEN
      expect(reportExecutionFormService.getReportExecution).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reportExecutionService.update).toHaveBeenCalledWith(expect.objectContaining(reportExecution));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportExecution>>();
      const reportExecution = { id: 23917 };
      jest.spyOn(reportExecutionFormService, 'getReportExecution').mockReturnValue({ id: null });
      jest.spyOn(reportExecutionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportExecution: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportExecution }));
      saveSubject.complete();

      // THEN
      expect(reportExecutionFormService.getReportExecution).toHaveBeenCalled();
      expect(reportExecutionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportExecution>>();
      const reportExecution = { id: 23917 };
      jest.spyOn(reportExecutionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportExecution });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reportExecutionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareScheduledReport', () => {
      it('should forward to scheduledReportService', () => {
        const entity = { id: 28944 };
        const entity2 = { id: 3093 };
        jest.spyOn(scheduledReportService, 'compareScheduledReport');
        comp.compareScheduledReport(entity, entity2);
        expect(scheduledReportService.compareScheduledReport).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
