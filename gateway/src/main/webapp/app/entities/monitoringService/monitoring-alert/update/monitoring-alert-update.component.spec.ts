import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IMonitoringAlertRule } from 'app/entities/monitoringService/monitoring-alert-rule/monitoring-alert-rule.model';
import { MonitoringAlertRuleService } from 'app/entities/monitoringService/monitoring-alert-rule/service/monitoring-alert-rule.service';
import { MonitoringAlertService } from '../service/monitoring-alert.service';
import { IMonitoringAlert } from '../monitoring-alert.model';
import { MonitoringAlertFormService } from './monitoring-alert-form.service';

import { MonitoringAlertUpdateComponent } from './monitoring-alert-update.component';

describe('MonitoringAlert Management Update Component', () => {
  let comp: MonitoringAlertUpdateComponent;
  let fixture: ComponentFixture<MonitoringAlertUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let monitoringAlertFormService: MonitoringAlertFormService;
  let monitoringAlertService: MonitoringAlertService;
  let monitoringAlertRuleService: MonitoringAlertRuleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MonitoringAlertUpdateComponent],
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
      .overrideTemplate(MonitoringAlertUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MonitoringAlertUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    monitoringAlertFormService = TestBed.inject(MonitoringAlertFormService);
    monitoringAlertService = TestBed.inject(MonitoringAlertService);
    monitoringAlertRuleService = TestBed.inject(MonitoringAlertRuleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call MonitoringAlertRule query and add missing value', () => {
      const monitoringAlert: IMonitoringAlert = { id: 11544 };
      const alertRule: IMonitoringAlertRule = { id: 16699 };
      monitoringAlert.alertRule = alertRule;

      const monitoringAlertRuleCollection: IMonitoringAlertRule[] = [{ id: 16699 }];
      jest.spyOn(monitoringAlertRuleService, 'query').mockReturnValue(of(new HttpResponse({ body: monitoringAlertRuleCollection })));
      const additionalMonitoringAlertRules = [alertRule];
      const expectedCollection: IMonitoringAlertRule[] = [...additionalMonitoringAlertRules, ...monitoringAlertRuleCollection];
      jest.spyOn(monitoringAlertRuleService, 'addMonitoringAlertRuleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ monitoringAlert });
      comp.ngOnInit();

      expect(monitoringAlertRuleService.query).toHaveBeenCalled();
      expect(monitoringAlertRuleService.addMonitoringAlertRuleToCollectionIfMissing).toHaveBeenCalledWith(
        monitoringAlertRuleCollection,
        ...additionalMonitoringAlertRules.map(expect.objectContaining),
      );
      expect(comp.monitoringAlertRulesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const monitoringAlert: IMonitoringAlert = { id: 11544 };
      const alertRule: IMonitoringAlertRule = { id: 16699 };
      monitoringAlert.alertRule = alertRule;

      activatedRoute.data = of({ monitoringAlert });
      comp.ngOnInit();

      expect(comp.monitoringAlertRulesSharedCollection).toContainEqual(alertRule);
      expect(comp.monitoringAlert).toEqual(monitoringAlert);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMonitoringAlert>>();
      const monitoringAlert = { id: 15172 };
      jest.spyOn(monitoringAlertFormService, 'getMonitoringAlert').mockReturnValue(monitoringAlert);
      jest.spyOn(monitoringAlertService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ monitoringAlert });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: monitoringAlert }));
      saveSubject.complete();

      // THEN
      expect(monitoringAlertFormService.getMonitoringAlert).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(monitoringAlertService.update).toHaveBeenCalledWith(expect.objectContaining(monitoringAlert));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMonitoringAlert>>();
      const monitoringAlert = { id: 15172 };
      jest.spyOn(monitoringAlertFormService, 'getMonitoringAlert').mockReturnValue({ id: null });
      jest.spyOn(monitoringAlertService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ monitoringAlert: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: monitoringAlert }));
      saveSubject.complete();

      // THEN
      expect(monitoringAlertFormService.getMonitoringAlert).toHaveBeenCalled();
      expect(monitoringAlertService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMonitoringAlert>>();
      const monitoringAlert = { id: 15172 };
      jest.spyOn(monitoringAlertService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ monitoringAlert });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(monitoringAlertService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMonitoringAlertRule', () => {
      it('should forward to monitoringAlertRuleService', () => {
        const entity = { id: 16699 };
        const entity2 = { id: 14601 };
        jest.spyOn(monitoringAlertRuleService, 'compareMonitoringAlertRule');
        comp.compareMonitoringAlertRule(entity, entity2);
        expect(monitoringAlertRuleService.compareMonitoringAlertRule).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
