import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { SystemHealthService } from '../service/system-health.service';
import { ISystemHealth } from '../system-health.model';
import { SystemHealthFormService } from './system-health-form.service';

import { SystemHealthUpdateComponent } from './system-health-update.component';

describe('SystemHealth Management Update Component', () => {
  let comp: SystemHealthUpdateComponent;
  let fixture: ComponentFixture<SystemHealthUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let systemHealthFormService: SystemHealthFormService;
  let systemHealthService: SystemHealthService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SystemHealthUpdateComponent],
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
      .overrideTemplate(SystemHealthUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SystemHealthUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    systemHealthFormService = TestBed.inject(SystemHealthFormService);
    systemHealthService = TestBed.inject(SystemHealthService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const systemHealth: ISystemHealth = { id: 10472 };

      activatedRoute.data = of({ systemHealth });
      comp.ngOnInit();

      expect(comp.systemHealth).toEqual(systemHealth);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISystemHealth>>();
      const systemHealth = { id: 28359 };
      jest.spyOn(systemHealthFormService, 'getSystemHealth').mockReturnValue(systemHealth);
      jest.spyOn(systemHealthService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ systemHealth });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: systemHealth }));
      saveSubject.complete();

      // THEN
      expect(systemHealthFormService.getSystemHealth).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(systemHealthService.update).toHaveBeenCalledWith(expect.objectContaining(systemHealth));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISystemHealth>>();
      const systemHealth = { id: 28359 };
      jest.spyOn(systemHealthFormService, 'getSystemHealth').mockReturnValue({ id: null });
      jest.spyOn(systemHealthService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ systemHealth: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: systemHealth }));
      saveSubject.complete();

      // THEN
      expect(systemHealthFormService.getSystemHealth).toHaveBeenCalled();
      expect(systemHealthService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISystemHealth>>();
      const systemHealth = { id: 28359 };
      jest.spyOn(systemHealthService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ systemHealth });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(systemHealthService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
