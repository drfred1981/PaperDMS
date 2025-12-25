import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { SystemMetricService } from '../service/system-metric.service';
import { ISystemMetric } from '../system-metric.model';
import { SystemMetricFormService } from './system-metric-form.service';

import { SystemMetricUpdateComponent } from './system-metric-update.component';

describe('SystemMetric Management Update Component', () => {
  let comp: SystemMetricUpdateComponent;
  let fixture: ComponentFixture<SystemMetricUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let systemMetricFormService: SystemMetricFormService;
  let systemMetricService: SystemMetricService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SystemMetricUpdateComponent],
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
      .overrideTemplate(SystemMetricUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SystemMetricUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    systemMetricFormService = TestBed.inject(SystemMetricFormService);
    systemMetricService = TestBed.inject(SystemMetricService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const systemMetric: ISystemMetric = { id: 9908 };

      activatedRoute.data = of({ systemMetric });
      comp.ngOnInit();

      expect(comp.systemMetric).toEqual(systemMetric);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISystemMetric>>();
      const systemMetric = { id: 14068 };
      jest.spyOn(systemMetricFormService, 'getSystemMetric').mockReturnValue(systemMetric);
      jest.spyOn(systemMetricService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ systemMetric });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: systemMetric }));
      saveSubject.complete();

      // THEN
      expect(systemMetricFormService.getSystemMetric).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(systemMetricService.update).toHaveBeenCalledWith(expect.objectContaining(systemMetric));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISystemMetric>>();
      const systemMetric = { id: 14068 };
      jest.spyOn(systemMetricFormService, 'getSystemMetric').mockReturnValue({ id: null });
      jest.spyOn(systemMetricService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ systemMetric: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: systemMetric }));
      saveSubject.complete();

      // THEN
      expect(systemMetricFormService.getSystemMetric).toHaveBeenCalled();
      expect(systemMetricService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISystemMetric>>();
      const systemMetric = { id: 14068 };
      jest.spyOn(systemMetricService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ systemMetric });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(systemMetricService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
