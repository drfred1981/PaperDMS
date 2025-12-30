import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IWorkflowInstance } from 'app/entities/workflowService/workflow-instance/workflow-instance.model';
import { WorkflowInstanceService } from 'app/entities/workflowService/workflow-instance/service/workflow-instance.service';
import { WorkflowApprovalHistoryService } from '../service/workflow-approval-history.service';
import { IWorkflowApprovalHistory } from '../workflow-approval-history.model';
import { WorkflowApprovalHistoryFormService } from './workflow-approval-history-form.service';

import { WorkflowApprovalHistoryUpdateComponent } from './workflow-approval-history-update.component';

describe('WorkflowApprovalHistory Management Update Component', () => {
  let comp: WorkflowApprovalHistoryUpdateComponent;
  let fixture: ComponentFixture<WorkflowApprovalHistoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let workflowApprovalHistoryFormService: WorkflowApprovalHistoryFormService;
  let workflowApprovalHistoryService: WorkflowApprovalHistoryService;
  let workflowInstanceService: WorkflowInstanceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [WorkflowApprovalHistoryUpdateComponent],
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
      .overrideTemplate(WorkflowApprovalHistoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WorkflowApprovalHistoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    workflowApprovalHistoryFormService = TestBed.inject(WorkflowApprovalHistoryFormService);
    workflowApprovalHistoryService = TestBed.inject(WorkflowApprovalHistoryService);
    workflowInstanceService = TestBed.inject(WorkflowInstanceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call WorkflowInstance query and add missing value', () => {
      const workflowApprovalHistory: IWorkflowApprovalHistory = { id: 22100 };
      const workflowInstance: IWorkflowInstance = { id: 15006 };
      workflowApprovalHistory.workflowInstance = workflowInstance;

      const workflowInstanceCollection: IWorkflowInstance[] = [{ id: 15006 }];
      jest.spyOn(workflowInstanceService, 'query').mockReturnValue(of(new HttpResponse({ body: workflowInstanceCollection })));
      const additionalWorkflowInstances = [workflowInstance];
      const expectedCollection: IWorkflowInstance[] = [...additionalWorkflowInstances, ...workflowInstanceCollection];
      jest.spyOn(workflowInstanceService, 'addWorkflowInstanceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workflowApprovalHistory });
      comp.ngOnInit();

      expect(workflowInstanceService.query).toHaveBeenCalled();
      expect(workflowInstanceService.addWorkflowInstanceToCollectionIfMissing).toHaveBeenCalledWith(
        workflowInstanceCollection,
        ...additionalWorkflowInstances.map(expect.objectContaining),
      );
      expect(comp.workflowInstancesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const workflowApprovalHistory: IWorkflowApprovalHistory = { id: 22100 };
      const workflowInstance: IWorkflowInstance = { id: 15006 };
      workflowApprovalHistory.workflowInstance = workflowInstance;

      activatedRoute.data = of({ workflowApprovalHistory });
      comp.ngOnInit();

      expect(comp.workflowInstancesSharedCollection).toContainEqual(workflowInstance);
      expect(comp.workflowApprovalHistory).toEqual(workflowApprovalHistory);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkflowApprovalHistory>>();
      const workflowApprovalHistory = { id: 17441 };
      jest.spyOn(workflowApprovalHistoryFormService, 'getWorkflowApprovalHistory').mockReturnValue(workflowApprovalHistory);
      jest.spyOn(workflowApprovalHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workflowApprovalHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workflowApprovalHistory }));
      saveSubject.complete();

      // THEN
      expect(workflowApprovalHistoryFormService.getWorkflowApprovalHistory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(workflowApprovalHistoryService.update).toHaveBeenCalledWith(expect.objectContaining(workflowApprovalHistory));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkflowApprovalHistory>>();
      const workflowApprovalHistory = { id: 17441 };
      jest.spyOn(workflowApprovalHistoryFormService, 'getWorkflowApprovalHistory').mockReturnValue({ id: null });
      jest.spyOn(workflowApprovalHistoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workflowApprovalHistory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workflowApprovalHistory }));
      saveSubject.complete();

      // THEN
      expect(workflowApprovalHistoryFormService.getWorkflowApprovalHistory).toHaveBeenCalled();
      expect(workflowApprovalHistoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkflowApprovalHistory>>();
      const workflowApprovalHistory = { id: 17441 };
      jest.spyOn(workflowApprovalHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workflowApprovalHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(workflowApprovalHistoryService.update).toHaveBeenCalled();
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
  });
});
