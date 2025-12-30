import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IReportingDashboard } from 'app/entities/reportingService/reporting-dashboard/reporting-dashboard.model';
import { ReportingDashboardService } from 'app/entities/reportingService/reporting-dashboard/service/reporting-dashboard.service';
import { ReportingDashboardWidgetService } from '../service/reporting-dashboard-widget.service';
import { IReportingDashboardWidget } from '../reporting-dashboard-widget.model';
import { ReportingDashboardWidgetFormService } from './reporting-dashboard-widget-form.service';

import { ReportingDashboardWidgetUpdateComponent } from './reporting-dashboard-widget-update.component';

describe('ReportingDashboardWidget Management Update Component', () => {
  let comp: ReportingDashboardWidgetUpdateComponent;
  let fixture: ComponentFixture<ReportingDashboardWidgetUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reportingDashboardWidgetFormService: ReportingDashboardWidgetFormService;
  let reportingDashboardWidgetService: ReportingDashboardWidgetService;
  let reportingDashboardService: ReportingDashboardService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReportingDashboardWidgetUpdateComponent],
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
      .overrideTemplate(ReportingDashboardWidgetUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReportingDashboardWidgetUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reportingDashboardWidgetFormService = TestBed.inject(ReportingDashboardWidgetFormService);
    reportingDashboardWidgetService = TestBed.inject(ReportingDashboardWidgetService);
    reportingDashboardService = TestBed.inject(ReportingDashboardService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ReportingDashboard query and add missing value', () => {
      const reportingDashboardWidget: IReportingDashboardWidget = { id: 25774 };
      const dashboar: IReportingDashboard = { id: 5841 };
      reportingDashboardWidget.dashboar = dashboar;

      const reportingDashboardCollection: IReportingDashboard[] = [{ id: 5841 }];
      jest.spyOn(reportingDashboardService, 'query').mockReturnValue(of(new HttpResponse({ body: reportingDashboardCollection })));
      const additionalReportingDashboards = [dashboar];
      const expectedCollection: IReportingDashboard[] = [...additionalReportingDashboards, ...reportingDashboardCollection];
      jest.spyOn(reportingDashboardService, 'addReportingDashboardToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reportingDashboardWidget });
      comp.ngOnInit();

      expect(reportingDashboardService.query).toHaveBeenCalled();
      expect(reportingDashboardService.addReportingDashboardToCollectionIfMissing).toHaveBeenCalledWith(
        reportingDashboardCollection,
        ...additionalReportingDashboards.map(expect.objectContaining),
      );
      expect(comp.reportingDashboardsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const reportingDashboardWidget: IReportingDashboardWidget = { id: 25774 };
      const dashboar: IReportingDashboard = { id: 5841 };
      reportingDashboardWidget.dashboar = dashboar;

      activatedRoute.data = of({ reportingDashboardWidget });
      comp.ngOnInit();

      expect(comp.reportingDashboardsSharedCollection).toContainEqual(dashboar);
      expect(comp.reportingDashboardWidget).toEqual(reportingDashboardWidget);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportingDashboardWidget>>();
      const reportingDashboardWidget = { id: 12221 };
      jest.spyOn(reportingDashboardWidgetFormService, 'getReportingDashboardWidget').mockReturnValue(reportingDashboardWidget);
      jest.spyOn(reportingDashboardWidgetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportingDashboardWidget });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportingDashboardWidget }));
      saveSubject.complete();

      // THEN
      expect(reportingDashboardWidgetFormService.getReportingDashboardWidget).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reportingDashboardWidgetService.update).toHaveBeenCalledWith(expect.objectContaining(reportingDashboardWidget));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportingDashboardWidget>>();
      const reportingDashboardWidget = { id: 12221 };
      jest.spyOn(reportingDashboardWidgetFormService, 'getReportingDashboardWidget').mockReturnValue({ id: null });
      jest.spyOn(reportingDashboardWidgetService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportingDashboardWidget: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportingDashboardWidget }));
      saveSubject.complete();

      // THEN
      expect(reportingDashboardWidgetFormService.getReportingDashboardWidget).toHaveBeenCalled();
      expect(reportingDashboardWidgetService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportingDashboardWidget>>();
      const reportingDashboardWidget = { id: 12221 };
      jest.spyOn(reportingDashboardWidgetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportingDashboardWidget });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reportingDashboardWidgetService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareReportingDashboard', () => {
      it('should forward to reportingDashboardService', () => {
        const entity = { id: 5841 };
        const entity2 = { id: 30933 };
        jest.spyOn(reportingDashboardService, 'compareReportingDashboard');
        comp.compareReportingDashboard(entity, entity2);
        expect(reportingDashboardService.compareReportingDashboard).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
