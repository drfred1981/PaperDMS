import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { MonitoringServiceStatusService } from '../service/monitoring-service-status.service';
import { IMonitoringServiceStatus } from '../monitoring-service-status.model';
import { MonitoringServiceStatusFormService } from './monitoring-service-status-form.service';

import { MonitoringServiceStatusUpdateComponent } from './monitoring-service-status-update.component';

describe('MonitoringServiceStatus Management Update Component', () => {
  let comp: MonitoringServiceStatusUpdateComponent;
  let fixture: ComponentFixture<MonitoringServiceStatusUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let monitoringServiceStatusFormService: MonitoringServiceStatusFormService;
  let monitoringServiceStatusService: MonitoringServiceStatusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MonitoringServiceStatusUpdateComponent],
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
      .overrideTemplate(MonitoringServiceStatusUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MonitoringServiceStatusUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    monitoringServiceStatusFormService = TestBed.inject(MonitoringServiceStatusFormService);
    monitoringServiceStatusService = TestBed.inject(MonitoringServiceStatusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const monitoringServiceStatus: IMonitoringServiceStatus = { id: 17949 };

      activatedRoute.data = of({ monitoringServiceStatus });
      comp.ngOnInit();

      expect(comp.monitoringServiceStatus).toEqual(monitoringServiceStatus);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMonitoringServiceStatus>>();
      const monitoringServiceStatus = { id: 22154 };
      jest.spyOn(monitoringServiceStatusFormService, 'getMonitoringServiceStatus').mockReturnValue(monitoringServiceStatus);
      jest.spyOn(monitoringServiceStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ monitoringServiceStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: monitoringServiceStatus }));
      saveSubject.complete();

      // THEN
      expect(monitoringServiceStatusFormService.getMonitoringServiceStatus).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(monitoringServiceStatusService.update).toHaveBeenCalledWith(expect.objectContaining(monitoringServiceStatus));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMonitoringServiceStatus>>();
      const monitoringServiceStatus = { id: 22154 };
      jest.spyOn(monitoringServiceStatusFormService, 'getMonitoringServiceStatus').mockReturnValue({ id: null });
      jest.spyOn(monitoringServiceStatusService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ monitoringServiceStatus: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: monitoringServiceStatus }));
      saveSubject.complete();

      // THEN
      expect(monitoringServiceStatusFormService.getMonitoringServiceStatus).toHaveBeenCalled();
      expect(monitoringServiceStatusService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMonitoringServiceStatus>>();
      const monitoringServiceStatus = { id: 22154 };
      jest.spyOn(monitoringServiceStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ monitoringServiceStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(monitoringServiceStatusService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
