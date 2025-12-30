import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { MonitoringAlertRuleService } from '../service/monitoring-alert-rule.service';
import { IMonitoringAlertRule } from '../monitoring-alert-rule.model';
import { MonitoringAlertRuleFormService } from './monitoring-alert-rule-form.service';

import { MonitoringAlertRuleUpdateComponent } from './monitoring-alert-rule-update.component';

describe('MonitoringAlertRule Management Update Component', () => {
  let comp: MonitoringAlertRuleUpdateComponent;
  let fixture: ComponentFixture<MonitoringAlertRuleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let monitoringAlertRuleFormService: MonitoringAlertRuleFormService;
  let monitoringAlertRuleService: MonitoringAlertRuleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MonitoringAlertRuleUpdateComponent],
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
      .overrideTemplate(MonitoringAlertRuleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MonitoringAlertRuleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    monitoringAlertRuleFormService = TestBed.inject(MonitoringAlertRuleFormService);
    monitoringAlertRuleService = TestBed.inject(MonitoringAlertRuleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const monitoringAlertRule: IMonitoringAlertRule = { id: 14601 };

      activatedRoute.data = of({ monitoringAlertRule });
      comp.ngOnInit();

      expect(comp.monitoringAlertRule).toEqual(monitoringAlertRule);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMonitoringAlertRule>>();
      const monitoringAlertRule = { id: 16699 };
      jest.spyOn(monitoringAlertRuleFormService, 'getMonitoringAlertRule').mockReturnValue(monitoringAlertRule);
      jest.spyOn(monitoringAlertRuleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ monitoringAlertRule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: monitoringAlertRule }));
      saveSubject.complete();

      // THEN
      expect(monitoringAlertRuleFormService.getMonitoringAlertRule).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(monitoringAlertRuleService.update).toHaveBeenCalledWith(expect.objectContaining(monitoringAlertRule));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMonitoringAlertRule>>();
      const monitoringAlertRule = { id: 16699 };
      jest.spyOn(monitoringAlertRuleFormService, 'getMonitoringAlertRule').mockReturnValue({ id: null });
      jest.spyOn(monitoringAlertRuleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ monitoringAlertRule: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: monitoringAlertRule }));
      saveSubject.complete();

      // THEN
      expect(monitoringAlertRuleFormService.getMonitoringAlertRule).toHaveBeenCalled();
      expect(monitoringAlertRuleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMonitoringAlertRule>>();
      const monitoringAlertRule = { id: 16699 };
      jest.spyOn(monitoringAlertRuleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ monitoringAlertRule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(monitoringAlertRuleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
