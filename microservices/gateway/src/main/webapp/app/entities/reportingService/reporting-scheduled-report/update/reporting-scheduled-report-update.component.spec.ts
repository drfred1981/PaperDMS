import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ReportingScheduledReportService } from '../service/reporting-scheduled-report.service';
import { IReportingScheduledReport } from '../reporting-scheduled-report.model';
import { ReportingScheduledReportFormService } from './reporting-scheduled-report-form.service';

import { ReportingScheduledReportUpdateComponent } from './reporting-scheduled-report-update.component';

describe('ReportingScheduledReport Management Update Component', () => {
  let comp: ReportingScheduledReportUpdateComponent;
  let fixture: ComponentFixture<ReportingScheduledReportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reportingScheduledReportFormService: ReportingScheduledReportFormService;
  let reportingScheduledReportService: ReportingScheduledReportService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReportingScheduledReportUpdateComponent],
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
      .overrideTemplate(ReportingScheduledReportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReportingScheduledReportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reportingScheduledReportFormService = TestBed.inject(ReportingScheduledReportFormService);
    reportingScheduledReportService = TestBed.inject(ReportingScheduledReportService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const reportingScheduledReport: IReportingScheduledReport = { id: 16309 };

      activatedRoute.data = of({ reportingScheduledReport });
      comp.ngOnInit();

      expect(comp.reportingScheduledReport).toEqual(reportingScheduledReport);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportingScheduledReport>>();
      const reportingScheduledReport = { id: 25144 };
      jest.spyOn(reportingScheduledReportFormService, 'getReportingScheduledReport').mockReturnValue(reportingScheduledReport);
      jest.spyOn(reportingScheduledReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportingScheduledReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportingScheduledReport }));
      saveSubject.complete();

      // THEN
      expect(reportingScheduledReportFormService.getReportingScheduledReport).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reportingScheduledReportService.update).toHaveBeenCalledWith(expect.objectContaining(reportingScheduledReport));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportingScheduledReport>>();
      const reportingScheduledReport = { id: 25144 };
      jest.spyOn(reportingScheduledReportFormService, 'getReportingScheduledReport').mockReturnValue({ id: null });
      jest.spyOn(reportingScheduledReportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportingScheduledReport: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportingScheduledReport }));
      saveSubject.complete();

      // THEN
      expect(reportingScheduledReportFormService.getReportingScheduledReport).toHaveBeenCalled();
      expect(reportingScheduledReportService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportingScheduledReport>>();
      const reportingScheduledReport = { id: 25144 };
      jest.spyOn(reportingScheduledReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportingScheduledReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reportingScheduledReportService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
