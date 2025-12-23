import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IDashboard } from '../dashboard.model';
import { DashboardService } from '../service/dashboard.service';

import { DashboardFormService } from './dashboard-form.service';
import { DashboardUpdate } from './dashboard-update';

describe('Dashboard Management Update Component', () => {
  let comp: DashboardUpdate;
  let fixture: ComponentFixture<DashboardUpdate>;
  let activatedRoute: ActivatedRoute;
  let dashboardFormService: DashboardFormService;
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

    fixture = TestBed.createComponent(DashboardUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dashboardFormService = TestBed.inject(DashboardFormService);
    dashboardService = TestBed.inject(DashboardService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const dashboard: IDashboard = { id: 21947 };

      activatedRoute.data = of({ dashboard });
      comp.ngOnInit();

      expect(comp.dashboard).toEqual(dashboard);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDashboard>>();
      const dashboard = { id: 22202 };
      jest.spyOn(dashboardFormService, 'getDashboard').mockReturnValue(dashboard);
      jest.spyOn(dashboardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dashboard });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dashboard }));
      saveSubject.complete();

      // THEN
      expect(dashboardFormService.getDashboard).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(dashboardService.update).toHaveBeenCalledWith(expect.objectContaining(dashboard));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDashboard>>();
      const dashboard = { id: 22202 };
      jest.spyOn(dashboardFormService, 'getDashboard').mockReturnValue({ id: null });
      jest.spyOn(dashboardService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dashboard: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dashboard }));
      saveSubject.complete();

      // THEN
      expect(dashboardFormService.getDashboard).toHaveBeenCalled();
      expect(dashboardService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDashboard>>();
      const dashboard = { id: 22202 };
      jest.spyOn(dashboardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dashboard });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dashboardService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
