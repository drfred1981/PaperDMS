import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IAlertRule } from 'app/entities/monitoringService/alert-rule/alert-rule.model';
import { AlertRuleService } from 'app/entities/monitoringService/alert-rule/service/alert-rule.service';
import { AlertService } from '../service/alert.service';
import { IAlert } from '../alert.model';
import { AlertFormService } from './alert-form.service';

import { AlertUpdateComponent } from './alert-update.component';

describe('Alert Management Update Component', () => {
  let comp: AlertUpdateComponent;
  let fixture: ComponentFixture<AlertUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let alertFormService: AlertFormService;
  let alertService: AlertService;
  let alertRuleService: AlertRuleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AlertUpdateComponent],
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
      .overrideTemplate(AlertUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AlertUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    alertFormService = TestBed.inject(AlertFormService);
    alertService = TestBed.inject(AlertService);
    alertRuleService = TestBed.inject(AlertRuleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call AlertRule query and add missing value', () => {
      const alert: IAlert = { id: 17171 };
      const alertRule: IAlertRule = { id: 8076 };
      alert.alertRule = alertRule;

      const alertRuleCollection: IAlertRule[] = [{ id: 8076 }];
      jest.spyOn(alertRuleService, 'query').mockReturnValue(of(new HttpResponse({ body: alertRuleCollection })));
      const additionalAlertRules = [alertRule];
      const expectedCollection: IAlertRule[] = [...additionalAlertRules, ...alertRuleCollection];
      jest.spyOn(alertRuleService, 'addAlertRuleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ alert });
      comp.ngOnInit();

      expect(alertRuleService.query).toHaveBeenCalled();
      expect(alertRuleService.addAlertRuleToCollectionIfMissing).toHaveBeenCalledWith(
        alertRuleCollection,
        ...additionalAlertRules.map(expect.objectContaining),
      );
      expect(comp.alertRulesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const alert: IAlert = { id: 17171 };
      const alertRule: IAlertRule = { id: 8076 };
      alert.alertRule = alertRule;

      activatedRoute.data = of({ alert });
      comp.ngOnInit();

      expect(comp.alertRulesSharedCollection).toContainEqual(alertRule);
      expect(comp.alert).toEqual(alert);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlert>>();
      const alert = { id: 22054 };
      jest.spyOn(alertFormService, 'getAlert').mockReturnValue(alert);
      jest.spyOn(alertService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alert });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: alert }));
      saveSubject.complete();

      // THEN
      expect(alertFormService.getAlert).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(alertService.update).toHaveBeenCalledWith(expect.objectContaining(alert));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlert>>();
      const alert = { id: 22054 };
      jest.spyOn(alertFormService, 'getAlert').mockReturnValue({ id: null });
      jest.spyOn(alertService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alert: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: alert }));
      saveSubject.complete();

      // THEN
      expect(alertFormService.getAlert).toHaveBeenCalled();
      expect(alertService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlert>>();
      const alert = { id: 22054 };
      jest.spyOn(alertService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alert });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(alertService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAlertRule', () => {
      it('should forward to alertRuleService', () => {
        const entity = { id: 8076 };
        const entity2 = { id: 8795 };
        jest.spyOn(alertRuleService, 'compareAlertRule');
        comp.compareAlertRule(entity, entity2);
        expect(alertRuleService.compareAlertRule).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
