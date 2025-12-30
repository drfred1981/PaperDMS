import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IWorkflow } from 'app/entities/workflowService/workflow/workflow.model';
import { WorkflowService } from 'app/entities/workflowService/workflow/service/workflow.service';
import { WorkflowInstanceService } from '../service/workflow-instance.service';
import { IWorkflowInstance } from '../workflow-instance.model';
import { WorkflowInstanceFormService } from './workflow-instance-form.service';

import { WorkflowInstanceUpdateComponent } from './workflow-instance-update.component';

describe('WorkflowInstance Management Update Component', () => {
  let comp: WorkflowInstanceUpdateComponent;
  let fixture: ComponentFixture<WorkflowInstanceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let workflowInstanceFormService: WorkflowInstanceFormService;
  let workflowInstanceService: WorkflowInstanceService;
  let workflowService: WorkflowService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [WorkflowInstanceUpdateComponent],
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
      .overrideTemplate(WorkflowInstanceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WorkflowInstanceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    workflowInstanceFormService = TestBed.inject(WorkflowInstanceFormService);
    workflowInstanceService = TestBed.inject(WorkflowInstanceService);
    workflowService = TestBed.inject(WorkflowService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Workflow query and add missing value', () => {
      const workflowInstance: IWorkflowInstance = { id: 21506 };
      const workflow: IWorkflow = { id: 11987 };
      workflowInstance.workflow = workflow;

      const workflowCollection: IWorkflow[] = [{ id: 11987 }];
      jest.spyOn(workflowService, 'query').mockReturnValue(of(new HttpResponse({ body: workflowCollection })));
      const additionalWorkflows = [workflow];
      const expectedCollection: IWorkflow[] = [...additionalWorkflows, ...workflowCollection];
      jest.spyOn(workflowService, 'addWorkflowToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workflowInstance });
      comp.ngOnInit();

      expect(workflowService.query).toHaveBeenCalled();
      expect(workflowService.addWorkflowToCollectionIfMissing).toHaveBeenCalledWith(
        workflowCollection,
        ...additionalWorkflows.map(expect.objectContaining),
      );
      expect(comp.workflowsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const workflowInstance: IWorkflowInstance = { id: 21506 };
      const workflow: IWorkflow = { id: 11987 };
      workflowInstance.workflow = workflow;

      activatedRoute.data = of({ workflowInstance });
      comp.ngOnInit();

      expect(comp.workflowsSharedCollection).toContainEqual(workflow);
      expect(comp.workflowInstance).toEqual(workflowInstance);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkflowInstance>>();
      const workflowInstance = { id: 15006 };
      jest.spyOn(workflowInstanceFormService, 'getWorkflowInstance').mockReturnValue(workflowInstance);
      jest.spyOn(workflowInstanceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workflowInstance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workflowInstance }));
      saveSubject.complete();

      // THEN
      expect(workflowInstanceFormService.getWorkflowInstance).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(workflowInstanceService.update).toHaveBeenCalledWith(expect.objectContaining(workflowInstance));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkflowInstance>>();
      const workflowInstance = { id: 15006 };
      jest.spyOn(workflowInstanceFormService, 'getWorkflowInstance').mockReturnValue({ id: null });
      jest.spyOn(workflowInstanceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workflowInstance: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workflowInstance }));
      saveSubject.complete();

      // THEN
      expect(workflowInstanceFormService.getWorkflowInstance).toHaveBeenCalled();
      expect(workflowInstanceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkflowInstance>>();
      const workflowInstance = { id: 15006 };
      jest.spyOn(workflowInstanceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workflowInstance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(workflowInstanceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareWorkflow', () => {
      it('should forward to workflowService', () => {
        const entity = { id: 11987 };
        const entity2 = { id: 32445 };
        jest.spyOn(workflowService, 'compareWorkflow');
        comp.compareWorkflow(entity, entity2);
        expect(workflowService.compareWorkflow).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
