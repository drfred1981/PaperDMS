import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IWorkflow } from 'app/entities/workflowService/workflow/workflow.model';
import { WorkflowService } from 'app/entities/workflowService/workflow/service/workflow.service';
import { WorkflowStepService } from '../service/workflow-step.service';
import { IWorkflowStep } from '../workflow-step.model';
import { WorkflowStepFormService } from './workflow-step-form.service';

import { WorkflowStepUpdateComponent } from './workflow-step-update.component';

describe('WorkflowStep Management Update Component', () => {
  let comp: WorkflowStepUpdateComponent;
  let fixture: ComponentFixture<WorkflowStepUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let workflowStepFormService: WorkflowStepFormService;
  let workflowStepService: WorkflowStepService;
  let workflowService: WorkflowService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [WorkflowStepUpdateComponent],
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
      .overrideTemplate(WorkflowStepUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WorkflowStepUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    workflowStepFormService = TestBed.inject(WorkflowStepFormService);
    workflowStepService = TestBed.inject(WorkflowStepService);
    workflowService = TestBed.inject(WorkflowService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Workflow query and add missing value', () => {
      const workflowStep: IWorkflowStep = { id: 27664 };
      const workflow: IWorkflow = { id: 11987 };
      workflowStep.workflow = workflow;

      const workflowCollection: IWorkflow[] = [{ id: 11987 }];
      jest.spyOn(workflowService, 'query').mockReturnValue(of(new HttpResponse({ body: workflowCollection })));
      const additionalWorkflows = [workflow];
      const expectedCollection: IWorkflow[] = [...additionalWorkflows, ...workflowCollection];
      jest.spyOn(workflowService, 'addWorkflowToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workflowStep });
      comp.ngOnInit();

      expect(workflowService.query).toHaveBeenCalled();
      expect(workflowService.addWorkflowToCollectionIfMissing).toHaveBeenCalledWith(
        workflowCollection,
        ...additionalWorkflows.map(expect.objectContaining),
      );
      expect(comp.workflowsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const workflowStep: IWorkflowStep = { id: 27664 };
      const workflow: IWorkflow = { id: 11987 };
      workflowStep.workflow = workflow;

      activatedRoute.data = of({ workflowStep });
      comp.ngOnInit();

      expect(comp.workflowsSharedCollection).toContainEqual(workflow);
      expect(comp.workflowStep).toEqual(workflowStep);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkflowStep>>();
      const workflowStep = { id: 22856 };
      jest.spyOn(workflowStepFormService, 'getWorkflowStep').mockReturnValue(workflowStep);
      jest.spyOn(workflowStepService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workflowStep });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workflowStep }));
      saveSubject.complete();

      // THEN
      expect(workflowStepFormService.getWorkflowStep).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(workflowStepService.update).toHaveBeenCalledWith(expect.objectContaining(workflowStep));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkflowStep>>();
      const workflowStep = { id: 22856 };
      jest.spyOn(workflowStepFormService, 'getWorkflowStep').mockReturnValue({ id: null });
      jest.spyOn(workflowStepService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workflowStep: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workflowStep }));
      saveSubject.complete();

      // THEN
      expect(workflowStepFormService.getWorkflowStep).toHaveBeenCalled();
      expect(workflowStepService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkflowStep>>();
      const workflowStep = { id: 22856 };
      jest.spyOn(workflowStepService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workflowStep });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(workflowStepService.update).toHaveBeenCalled();
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
