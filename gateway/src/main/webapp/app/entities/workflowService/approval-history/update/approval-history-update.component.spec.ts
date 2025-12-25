import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ApprovalHistoryService } from '../service/approval-history.service';
import { IApprovalHistory } from '../approval-history.model';
import { ApprovalHistoryFormService } from './approval-history-form.service';

import { ApprovalHistoryUpdateComponent } from './approval-history-update.component';

describe('ApprovalHistory Management Update Component', () => {
  let comp: ApprovalHistoryUpdateComponent;
  let fixture: ComponentFixture<ApprovalHistoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let approvalHistoryFormService: ApprovalHistoryFormService;
  let approvalHistoryService: ApprovalHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ApprovalHistoryUpdateComponent],
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
      .overrideTemplate(ApprovalHistoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApprovalHistoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    approvalHistoryFormService = TestBed.inject(ApprovalHistoryFormService);
    approvalHistoryService = TestBed.inject(ApprovalHistoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const approvalHistory: IApprovalHistory = { id: 10065 };

      activatedRoute.data = of({ approvalHistory });
      comp.ngOnInit();

      expect(comp.approvalHistory).toEqual(approvalHistory);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApprovalHistory>>();
      const approvalHistory = { id: 15176 };
      jest.spyOn(approvalHistoryFormService, 'getApprovalHistory').mockReturnValue(approvalHistory);
      jest.spyOn(approvalHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ approvalHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: approvalHistory }));
      saveSubject.complete();

      // THEN
      expect(approvalHistoryFormService.getApprovalHistory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(approvalHistoryService.update).toHaveBeenCalledWith(expect.objectContaining(approvalHistory));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApprovalHistory>>();
      const approvalHistory = { id: 15176 };
      jest.spyOn(approvalHistoryFormService, 'getApprovalHistory').mockReturnValue({ id: null });
      jest.spyOn(approvalHistoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ approvalHistory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: approvalHistory }));
      saveSubject.complete();

      // THEN
      expect(approvalHistoryFormService.getApprovalHistory).toHaveBeenCalled();
      expect(approvalHistoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApprovalHistory>>();
      const approvalHistory = { id: 15176 };
      jest.spyOn(approvalHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ approvalHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(approvalHistoryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
