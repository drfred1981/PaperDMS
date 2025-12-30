import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { MonitoringSystemHealthService } from '../service/monitoring-system-health.service';
import { IMonitoringSystemHealth } from '../monitoring-system-health.model';
import { MonitoringSystemHealthFormService } from './monitoring-system-health-form.service';

import { MonitoringSystemHealthUpdateComponent } from './monitoring-system-health-update.component';

describe('MonitoringSystemHealth Management Update Component', () => {
  let comp: MonitoringSystemHealthUpdateComponent;
  let fixture: ComponentFixture<MonitoringSystemHealthUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let monitoringSystemHealthFormService: MonitoringSystemHealthFormService;
  let monitoringSystemHealthService: MonitoringSystemHealthService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MonitoringSystemHealthUpdateComponent],
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
      .overrideTemplate(MonitoringSystemHealthUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MonitoringSystemHealthUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    monitoringSystemHealthFormService = TestBed.inject(MonitoringSystemHealthFormService);
    monitoringSystemHealthService = TestBed.inject(MonitoringSystemHealthService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const monitoringSystemHealth: IMonitoringSystemHealth = { id: 11934 };

      activatedRoute.data = of({ monitoringSystemHealth });
      comp.ngOnInit();

      expect(comp.monitoringSystemHealth).toEqual(monitoringSystemHealth);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMonitoringSystemHealth>>();
      const monitoringSystemHealth = { id: 14622 };
      jest.spyOn(monitoringSystemHealthFormService, 'getMonitoringSystemHealth').mockReturnValue(monitoringSystemHealth);
      jest.spyOn(monitoringSystemHealthService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ monitoringSystemHealth });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: monitoringSystemHealth }));
      saveSubject.complete();

      // THEN
      expect(monitoringSystemHealthFormService.getMonitoringSystemHealth).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(monitoringSystemHealthService.update).toHaveBeenCalledWith(expect.objectContaining(monitoringSystemHealth));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMonitoringSystemHealth>>();
      const monitoringSystemHealth = { id: 14622 };
      jest.spyOn(monitoringSystemHealthFormService, 'getMonitoringSystemHealth').mockReturnValue({ id: null });
      jest.spyOn(monitoringSystemHealthService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ monitoringSystemHealth: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: monitoringSystemHealth }));
      saveSubject.complete();

      // THEN
      expect(monitoringSystemHealthFormService.getMonitoringSystemHealth).toHaveBeenCalled();
      expect(monitoringSystemHealthService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMonitoringSystemHealth>>();
      const monitoringSystemHealth = { id: 14622 };
      jest.spyOn(monitoringSystemHealthService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ monitoringSystemHealth });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(monitoringSystemHealthService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
