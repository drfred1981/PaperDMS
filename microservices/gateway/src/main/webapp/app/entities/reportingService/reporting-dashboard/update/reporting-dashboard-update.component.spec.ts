import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ReportingDashboardService } from '../service/reporting-dashboard.service';
import { IReportingDashboard } from '../reporting-dashboard.model';
import { ReportingDashboardFormService } from './reporting-dashboard-form.service';

import { ReportingDashboardUpdateComponent } from './reporting-dashboard-update.component';

describe('ReportingDashboard Management Update Component', () => {
  let comp: ReportingDashboardUpdateComponent;
  let fixture: ComponentFixture<ReportingDashboardUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reportingDashboardFormService: ReportingDashboardFormService;
  let reportingDashboardService: ReportingDashboardService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReportingDashboardUpdateComponent],
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
      .overrideTemplate(ReportingDashboardUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReportingDashboardUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reportingDashboardFormService = TestBed.inject(ReportingDashboardFormService);
    reportingDashboardService = TestBed.inject(ReportingDashboardService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const reportingDashboard: IReportingDashboard = { id: 30933 };

      activatedRoute.data = of({ reportingDashboard });
      comp.ngOnInit();

      expect(comp.reportingDashboard).toEqual(reportingDashboard);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportingDashboard>>();
      const reportingDashboard = { id: 5841 };
      jest.spyOn(reportingDashboardFormService, 'getReportingDashboard').mockReturnValue(reportingDashboard);
      jest.spyOn(reportingDashboardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportingDashboard });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportingDashboard }));
      saveSubject.complete();

      // THEN
      expect(reportingDashboardFormService.getReportingDashboard).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reportingDashboardService.update).toHaveBeenCalledWith(expect.objectContaining(reportingDashboard));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportingDashboard>>();
      const reportingDashboard = { id: 5841 };
      jest.spyOn(reportingDashboardFormService, 'getReportingDashboard').mockReturnValue({ id: null });
      jest.spyOn(reportingDashboardService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportingDashboard: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportingDashboard }));
      saveSubject.complete();

      // THEN
      expect(reportingDashboardFormService.getReportingDashboard).toHaveBeenCalled();
      expect(reportingDashboardService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportingDashboard>>();
      const reportingDashboard = { id: 5841 };
      jest.spyOn(reportingDashboardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportingDashboard });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reportingDashboardService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
