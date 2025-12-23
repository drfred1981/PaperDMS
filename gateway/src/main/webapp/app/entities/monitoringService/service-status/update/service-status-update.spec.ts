import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ServiceStatusService } from '../service/service-status.service';
import { IServiceStatus } from '../service-status.model';

import { ServiceStatusFormService } from './service-status-form.service';
import { ServiceStatusUpdate } from './service-status-update';

describe('ServiceStatus Management Update Component', () => {
  let comp: ServiceStatusUpdate;
  let fixture: ComponentFixture<ServiceStatusUpdate>;
  let activatedRoute: ActivatedRoute;
  let serviceStatusFormService: ServiceStatusFormService;
  let serviceStatusService: ServiceStatusService;

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

    fixture = TestBed.createComponent(ServiceStatusUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    serviceStatusFormService = TestBed.inject(ServiceStatusFormService);
    serviceStatusService = TestBed.inject(ServiceStatusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const serviceStatus: IServiceStatus = { id: 24994 };

      activatedRoute.data = of({ serviceStatus });
      comp.ngOnInit();

      expect(comp.serviceStatus).toEqual(serviceStatus);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServiceStatus>>();
      const serviceStatus = { id: 16780 };
      jest.spyOn(serviceStatusFormService, 'getServiceStatus').mockReturnValue(serviceStatus);
      jest.spyOn(serviceStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: serviceStatus }));
      saveSubject.complete();

      // THEN
      expect(serviceStatusFormService.getServiceStatus).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(serviceStatusService.update).toHaveBeenCalledWith(expect.objectContaining(serviceStatus));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServiceStatus>>();
      const serviceStatus = { id: 16780 };
      jest.spyOn(serviceStatusFormService, 'getServiceStatus').mockReturnValue({ id: null });
      jest.spyOn(serviceStatusService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceStatus: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: serviceStatus }));
      saveSubject.complete();

      // THEN
      expect(serviceStatusFormService.getServiceStatus).toHaveBeenCalled();
      expect(serviceStatusService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServiceStatus>>();
      const serviceStatus = { id: 16780 };
      jest.spyOn(serviceStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(serviceStatusService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
