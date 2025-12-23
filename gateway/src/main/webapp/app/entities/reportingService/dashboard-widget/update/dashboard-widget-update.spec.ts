import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IDashboard } from 'app/entities/reportingService/dashboard/dashboard.model';
import { DashboardService } from 'app/entities/reportingService/dashboard/service/dashboard.service';
import { IDashboardWidget } from '../dashboard-widget.model';
import { DashboardWidgetService } from '../service/dashboard-widget.service';

import { DashboardWidgetFormService } from './dashboard-widget-form.service';
import { DashboardWidgetUpdate } from './dashboard-widget-update';

describe('DashboardWidget Management Update Component', () => {
  let comp: DashboardWidgetUpdate;
  let fixture: ComponentFixture<DashboardWidgetUpdate>;
  let activatedRoute: ActivatedRoute;
  let dashboardWidgetFormService: DashboardWidgetFormService;
  let dashboardWidgetService: DashboardWidgetService;
  let dashboardService: DashboardService;

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

    fixture = TestBed.createComponent(DashboardWidgetUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dashboardWidgetFormService = TestBed.inject(DashboardWidgetFormService);
    dashboardWidgetService = TestBed.inject(DashboardWidgetService);
    dashboardService = TestBed.inject(DashboardService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Dashboard query and add missing value', () => {
      const dashboardWidget: IDashboardWidget = { id: 30011 };
      const dashboard: IDashboard = { id: 22202 };
      dashboardWidget.dashboard = dashboard;

      const dashboardCollection: IDashboard[] = [{ id: 22202 }];
      jest.spyOn(dashboardService, 'query').mockReturnValue(of(new HttpResponse({ body: dashboardCollection })));
      const additionalDashboards = [dashboard];
      const expectedCollection: IDashboard[] = [...additionalDashboards, ...dashboardCollection];
      jest.spyOn(dashboardService, 'addDashboardToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ dashboardWidget });
      comp.ngOnInit();

      expect(dashboardService.query).toHaveBeenCalled();
      expect(dashboardService.addDashboardToCollectionIfMissing).toHaveBeenCalledWith(
        dashboardCollection,
        ...additionalDashboards.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.dashboardsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const dashboardWidget: IDashboardWidget = { id: 30011 };
      const dashboard: IDashboard = { id: 22202 };
      dashboardWidget.dashboard = dashboard;

      activatedRoute.data = of({ dashboardWidget });
      comp.ngOnInit();

      expect(comp.dashboardsSharedCollection()).toContainEqual(dashboard);
      expect(comp.dashboardWidget).toEqual(dashboardWidget);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDashboardWidget>>();
      const dashboardWidget = { id: 6668 };
      jest.spyOn(dashboardWidgetFormService, 'getDashboardWidget').mockReturnValue(dashboardWidget);
      jest.spyOn(dashboardWidgetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dashboardWidget });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dashboardWidget }));
      saveSubject.complete();

      // THEN
      expect(dashboardWidgetFormService.getDashboardWidget).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(dashboardWidgetService.update).toHaveBeenCalledWith(expect.objectContaining(dashboardWidget));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDashboardWidget>>();
      const dashboardWidget = { id: 6668 };
      jest.spyOn(dashboardWidgetFormService, 'getDashboardWidget').mockReturnValue({ id: null });
      jest.spyOn(dashboardWidgetService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dashboardWidget: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dashboardWidget }));
      saveSubject.complete();

      // THEN
      expect(dashboardWidgetFormService.getDashboardWidget).toHaveBeenCalled();
      expect(dashboardWidgetService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDashboardWidget>>();
      const dashboardWidget = { id: 6668 };
      jest.spyOn(dashboardWidgetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dashboardWidget });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dashboardWidgetService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDashboard', () => {
      it('should forward to dashboardService', () => {
        const entity = { id: 22202 };
        const entity2 = { id: 21947 };
        jest.spyOn(dashboardService, 'compareDashboard');
        comp.compareDashboard(entity, entity2);
        expect(dashboardService.compareDashboard).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
