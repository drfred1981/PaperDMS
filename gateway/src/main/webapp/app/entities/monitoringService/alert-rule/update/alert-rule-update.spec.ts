import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IAlertRule } from '../alert-rule.model';
import { AlertRuleService } from '../service/alert-rule.service';

import { AlertRuleFormService } from './alert-rule-form.service';
import { AlertRuleUpdate } from './alert-rule-update';

describe('AlertRule Management Update Component', () => {
  let comp: AlertRuleUpdate;
  let fixture: ComponentFixture<AlertRuleUpdate>;
  let activatedRoute: ActivatedRoute;
  let alertRuleFormService: AlertRuleFormService;
  let alertRuleService: AlertRuleService;

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

    fixture = TestBed.createComponent(AlertRuleUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    alertRuleFormService = TestBed.inject(AlertRuleFormService);
    alertRuleService = TestBed.inject(AlertRuleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const alertRule: IAlertRule = { id: 8795 };

      activatedRoute.data = of({ alertRule });
      comp.ngOnInit();

      expect(comp.alertRule).toEqual(alertRule);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlertRule>>();
      const alertRule = { id: 8076 };
      jest.spyOn(alertRuleFormService, 'getAlertRule').mockReturnValue(alertRule);
      jest.spyOn(alertRuleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alertRule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: alertRule }));
      saveSubject.complete();

      // THEN
      expect(alertRuleFormService.getAlertRule).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(alertRuleService.update).toHaveBeenCalledWith(expect.objectContaining(alertRule));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlertRule>>();
      const alertRule = { id: 8076 };
      jest.spyOn(alertRuleFormService, 'getAlertRule').mockReturnValue({ id: null });
      jest.spyOn(alertRuleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alertRule: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: alertRule }));
      saveSubject.complete();

      // THEN
      expect(alertRuleFormService.getAlertRule).toHaveBeenCalled();
      expect(alertRuleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlertRule>>();
      const alertRule = { id: 8076 };
      jest.spyOn(alertRuleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alertRule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(alertRuleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
