import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IWorkflowInstance } from 'app/entities/workflowService/workflow-instance/workflow-instance.model';
import { WorkflowInstanceService } from 'app/entities/workflowService/workflow-instance/service/workflow-instance.service';
import { IWorkflowStep } from 'app/entities/workflowService/workflow-step/workflow-step.model';
import { WorkflowStepService } from 'app/entities/workflowService/workflow-step/service/workflow-step.service';
import { IWorkflowTask } from '../workflow-task.model';
import { WorkflowTaskService } from '../service/workflow-task.service';
import { WorkflowTaskFormService } from './workflow-task-form.service';

import { WorkflowTaskUpdateComponent } from './workflow-task-update.component';

describe('WorkflowTask Management Update Component', () => {
  let comp: WorkflowTaskUpdateComponent;
  let fixture: ComponentFixture<WorkflowTaskUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let workflowTaskFormService: WorkflowTaskFormService;
  let workflowTaskService: WorkflowTaskService;
  let workflowInstanceService: WorkflowInstanceService;
  let workflowStepService: WorkflowStepService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [WorkflowTaskUpdateComponent],
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
      .overrideTemplate(WorkflowTaskUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WorkflowTaskUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    workflowTaskFormService = TestBed.inject(WorkflowTaskFormService);
    workflowTaskService = TestBed.inject(WorkflowTaskService);
    workflowInstanceService = TestBed.inject(WorkflowInstanceService);
    workflowStepService = TestBed.inject(WorkflowStepService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call WorkflowInstance query and add missing value', () => {
      const workflowTask: IWorkflowTask = { id: 13181 };
      const instance: IWorkflowInstance = { id: 15006 };
      workflowTask.instance = instance;

      const workflowInstanceCollection: IWorkflowInstance[] = [{ id: 15006 }];
      jest.spyOn(workflowInstanceService, 'query').mockReturnValue(of(new HttpResponse({ body: workflowInstanceCollection })));
      const additionalWorkflowInstances = [instance];
      const expectedCollection: IWorkflowInstance[] = [...additionalWorkflowInstances, ...workflowInstanceCollection];
      jest.spyOn(workflowInstanceService, 'addWorkflowInstanceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workflowTask });
      comp.ngOnInit();

      expect(workflowInstanceService.query).toHaveBeenCalled();
      expect(workflowInstanceService.addWorkflowInstanceToCollectionIfMissing).toHaveBeenCalledWith(
        workflowInstanceCollection,
        ...additionalWorkflowInstances.map(expect.objectContaining),
      );
      expect(comp.workflowInstancesSharedCollection).toEqual(expectedCollection);
    });

    it('should call WorkflowStep query and add missing value', () => {
      const workflowTask: IWorkflowTask = { id: 13181 };
      const step: IWorkflowStep = { id: 22856 };
      workflowTask.step = step;

      const workflowStepCollection: IWorkflowStep[] = [{ id: 22856 }];
      jest.spyOn(workflowStepService, 'query').mockReturnValue(of(new HttpResponse({ body: workflowStepCollection })));
      const additionalWorkflowSteps = [step];
      const expectedCollection: IWorkflowStep[] = [...additionalWorkflowSteps, ...workflowStepCollection];
      jest.spyOn(workflowStepService, 'addWorkflowStepToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workflowTask });
      comp.ngOnInit();

      expect(workflowStepService.query).toHaveBeenCalled();
      expect(workflowStepService.addWorkflowStepToCollectionIfMissing).toHaveBeenCalledWith(
        workflowStepCollection,
        ...additionalWorkflowSteps.map(expect.objectContaining),
      );
      expect(comp.workflowStepsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const workflowTask: IWorkflowTask = { id: 13181 };
      const instance: IWorkflowInstance = { id: 15006 };
      workflowTask.instance = instance;
      const step: IWorkflowStep = { id: 22856 };
      workflowTask.step = step;

      activatedRoute.data = of({ workflowTask });
      comp.ngOnInit();

      expect(comp.workflowInstancesSharedCollection).toContainEqual(instance);
      expect(comp.workflowStepsSharedCollection).toContainEqual(step);
      expect(comp.workflowTask).toEqual(workflowTask);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkflowTask>>();
      const workflowTask = { id: 2349 };
      jest.spyOn(workflowTaskFormService, 'getWorkflowTask').mockReturnValue(workflowTask);
      jest.spyOn(workflowTaskService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workflowTask });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workflowTask }));
      saveSubject.complete();

      // THEN
      expect(workflowTaskFormService.getWorkflowTask).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(workflowTaskService.update).toHaveBeenCalledWith(expect.objectContaining(workflowTask));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkflowTask>>();
      const workflowTask = { id: 2349 };
      jest.spyOn(workflowTaskFormService, 'getWorkflowTask').mockReturnValue({ id: null });
      jest.spyOn(workflowTaskService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workflowTask: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workflowTask }));
      saveSubject.complete();

      // THEN
      expect(workflowTaskFormService.getWorkflowTask).toHaveBeenCalled();
      expect(workflowTaskService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkflowTask>>();
      const workflowTask = { id: 2349 };
      jest.spyOn(workflowTaskService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workflowTask });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(workflowTaskService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareWorkflowInstance', () => {
      it('should forward to workflowInstanceService', () => {
        const entity = { id: 15006 };
        const entity2 = { id: 21506 };
        jest.spyOn(workflowInstanceService, 'compareWorkflowInstance');
        comp.compareWorkflowInstance(entity, entity2);
        expect(workflowInstanceService.compareWorkflowInstance).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareWorkflowStep', () => {
      it('should forward to workflowStepService', () => {
        const entity = { id: 22856 };
        const entity2 = { id: 27664 };
        jest.spyOn(workflowStepService, 'compareWorkflowStep');
        comp.compareWorkflowStep(entity, entity2);
        expect(workflowStepService.compareWorkflowStep).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
