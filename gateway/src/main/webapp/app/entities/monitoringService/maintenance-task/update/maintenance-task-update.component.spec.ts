import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { MaintenanceTaskService } from '../service/maintenance-task.service';
import { IMaintenanceTask } from '../maintenance-task.model';
import { MaintenanceTaskFormService } from './maintenance-task-form.service';

import { MaintenanceTaskUpdateComponent } from './maintenance-task-update.component';

describe('MaintenanceTask Management Update Component', () => {
  let comp: MaintenanceTaskUpdateComponent;
  let fixture: ComponentFixture<MaintenanceTaskUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let maintenanceTaskFormService: MaintenanceTaskFormService;
  let maintenanceTaskService: MaintenanceTaskService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MaintenanceTaskUpdateComponent],
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
      .overrideTemplate(MaintenanceTaskUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MaintenanceTaskUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    maintenanceTaskFormService = TestBed.inject(MaintenanceTaskFormService);
    maintenanceTaskService = TestBed.inject(MaintenanceTaskService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const maintenanceTask: IMaintenanceTask = { id: 10127 };

      activatedRoute.data = of({ maintenanceTask });
      comp.ngOnInit();

      expect(comp.maintenanceTask).toEqual(maintenanceTask);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaintenanceTask>>();
      const maintenanceTask = { id: 23869 };
      jest.spyOn(maintenanceTaskFormService, 'getMaintenanceTask').mockReturnValue(maintenanceTask);
      jest.spyOn(maintenanceTaskService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ maintenanceTask });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: maintenanceTask }));
      saveSubject.complete();

      // THEN
      expect(maintenanceTaskFormService.getMaintenanceTask).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(maintenanceTaskService.update).toHaveBeenCalledWith(expect.objectContaining(maintenanceTask));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaintenanceTask>>();
      const maintenanceTask = { id: 23869 };
      jest.spyOn(maintenanceTaskFormService, 'getMaintenanceTask').mockReturnValue({ id: null });
      jest.spyOn(maintenanceTaskService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ maintenanceTask: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: maintenanceTask }));
      saveSubject.complete();

      // THEN
      expect(maintenanceTaskFormService.getMaintenanceTask).toHaveBeenCalled();
      expect(maintenanceTaskService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaintenanceTask>>();
      const maintenanceTask = { id: 23869 };
      jest.spyOn(maintenanceTaskService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ maintenanceTask });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(maintenanceTaskService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
