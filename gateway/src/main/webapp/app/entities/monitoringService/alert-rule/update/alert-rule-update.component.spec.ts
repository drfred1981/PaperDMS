import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { AlertRuleService } from '../service/alert-rule.service';
import { IAlertRule } from '../alert-rule.model';
import { AlertRuleFormService } from './alert-rule-form.service';

import { AlertRuleUpdateComponent } from './alert-rule-update.component';

describe('AlertRule Management Update Component', () => {
  let comp: AlertRuleUpdateComponent;
  let fixture: ComponentFixture<AlertRuleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let alertRuleFormService: AlertRuleFormService;
  let alertRuleService: AlertRuleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AlertRuleUpdateComponent],
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
      .overrideTemplate(AlertRuleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AlertRuleUpdateComponent);
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
