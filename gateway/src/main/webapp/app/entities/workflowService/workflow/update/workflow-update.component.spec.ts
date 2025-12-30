import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { WorkflowService } from '../service/workflow.service';
import { IWorkflow } from '../workflow.model';
import { WorkflowFormService } from './workflow-form.service';

import { WorkflowUpdateComponent } from './workflow-update.component';

describe('Workflow Management Update Component', () => {
  let comp: WorkflowUpdateComponent;
  let fixture: ComponentFixture<WorkflowUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let workflowFormService: WorkflowFormService;
  let workflowService: WorkflowService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [WorkflowUpdateComponent],
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
      .overrideTemplate(WorkflowUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WorkflowUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    workflowFormService = TestBed.inject(WorkflowFormService);
    workflowService = TestBed.inject(WorkflowService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const workflow: IWorkflow = { id: 32445 };

      activatedRoute.data = of({ workflow });
      comp.ngOnInit();

      expect(comp.workflow).toEqual(workflow);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkflow>>();
      const workflow = { id: 11987 };
      jest.spyOn(workflowFormService, 'getWorkflow').mockReturnValue(workflow);
      jest.spyOn(workflowService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workflow });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workflow }));
      saveSubject.complete();

      // THEN
      expect(workflowFormService.getWorkflow).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(workflowService.update).toHaveBeenCalledWith(expect.objectContaining(workflow));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkflow>>();
      const workflow = { id: 11987 };
      jest.spyOn(workflowFormService, 'getWorkflow').mockReturnValue({ id: null });
      jest.spyOn(workflowService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workflow: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workflow }));
      saveSubject.complete();

      // THEN
      expect(workflowFormService.getWorkflow).toHaveBeenCalled();
      expect(workflowService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkflow>>();
      const workflow = { id: 11987 };
      jest.spyOn(workflowService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workflow });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(workflowService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
