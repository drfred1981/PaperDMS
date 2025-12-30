import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IReportingScheduledReport } from 'app/entities/reportingService/reporting-scheduled-report/reporting-scheduled-report.model';
import { ReportingScheduledReportService } from 'app/entities/reportingService/reporting-scheduled-report/service/reporting-scheduled-report.service';
import { ReportingExecutionService } from '../service/reporting-execution.service';
import { IReportingExecution } from '../reporting-execution.model';
import { ReportingExecutionFormService } from './reporting-execution-form.service';

import { ReportingExecutionUpdateComponent } from './reporting-execution-update.component';

describe('ReportingExecution Management Update Component', () => {
  let comp: ReportingExecutionUpdateComponent;
  let fixture: ComponentFixture<ReportingExecutionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reportingExecutionFormService: ReportingExecutionFormService;
  let reportingExecutionService: ReportingExecutionService;
  let reportingScheduledReportService: ReportingScheduledReportService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReportingExecutionUpdateComponent],
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
      .overrideTemplate(ReportingExecutionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReportingExecutionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reportingExecutionFormService = TestBed.inject(ReportingExecutionFormService);
    reportingExecutionService = TestBed.inject(ReportingExecutionService);
    reportingScheduledReportService = TestBed.inject(ReportingScheduledReportService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ReportingScheduledReport query and add missing value', () => {
      const reportingExecution: IReportingExecution = { id: 8451 };
      const scheduledReport: IReportingScheduledReport = { id: 25144 };
      reportingExecution.scheduledReport = scheduledReport;

      const reportingScheduledReportCollection: IReportingScheduledReport[] = [{ id: 25144 }];
      jest
        .spyOn(reportingScheduledReportService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: reportingScheduledReportCollection })));
      const additionalReportingScheduledReports = [scheduledReport];
      const expectedCollection: IReportingScheduledReport[] = [
        ...additionalReportingScheduledReports,
        ...reportingScheduledReportCollection,
      ];
      jest.spyOn(reportingScheduledReportService, 'addReportingScheduledReportToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reportingExecution });
      comp.ngOnInit();

      expect(reportingScheduledReportService.query).toHaveBeenCalled();
      expect(reportingScheduledReportService.addReportingScheduledReportToCollectionIfMissing).toHaveBeenCalledWith(
        reportingScheduledReportCollection,
        ...additionalReportingScheduledReports.map(expect.objectContaining),
      );
      expect(comp.reportingScheduledReportsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const reportingExecution: IReportingExecution = { id: 8451 };
      const scheduledReport: IReportingScheduledReport = { id: 25144 };
      reportingExecution.scheduledReport = scheduledReport;

      activatedRoute.data = of({ reportingExecution });
      comp.ngOnInit();

      expect(comp.reportingScheduledReportsSharedCollection).toContainEqual(scheduledReport);
      expect(comp.reportingExecution).toEqual(reportingExecution);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportingExecution>>();
      const reportingExecution = { id: 27701 };
      jest.spyOn(reportingExecutionFormService, 'getReportingExecution').mockReturnValue(reportingExecution);
      jest.spyOn(reportingExecutionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportingExecution });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportingExecution }));
      saveSubject.complete();

      // THEN
      expect(reportingExecutionFormService.getReportingExecution).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reportingExecutionService.update).toHaveBeenCalledWith(expect.objectContaining(reportingExecution));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportingExecution>>();
      const reportingExecution = { id: 27701 };
      jest.spyOn(reportingExecutionFormService, 'getReportingExecution').mockReturnValue({ id: null });
      jest.spyOn(reportingExecutionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportingExecution: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportingExecution }));
      saveSubject.complete();

      // THEN
      expect(reportingExecutionFormService.getReportingExecution).toHaveBeenCalled();
      expect(reportingExecutionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportingExecution>>();
      const reportingExecution = { id: 27701 };
      jest.spyOn(reportingExecutionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportingExecution });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reportingExecutionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareReportingScheduledReport', () => {
      it('should forward to reportingScheduledReportService', () => {
        const entity = { id: 25144 };
        const entity2 = { id: 16309 };
        jest.spyOn(reportingScheduledReportService, 'compareReportingScheduledReport');
        comp.compareReportingScheduledReport(entity, entity2);
        expect(reportingScheduledReportService.compareReportingScheduledReport).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
