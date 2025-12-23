import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IApprovalHistory } from '../approval-history.model';
import { ApprovalHistoryService } from '../service/approval-history.service';

import { ApprovalHistoryFormService } from './approval-history-form.service';
import { ApprovalHistoryUpdate } from './approval-history-update';

describe('ApprovalHistory Management Update Component', () => {
  let comp: ApprovalHistoryUpdate;
  let fixture: ComponentFixture<ApprovalHistoryUpdate>;
  let activatedRoute: ActivatedRoute;
  let approvalHistoryFormService: ApprovalHistoryFormService;
  let approvalHistoryService: ApprovalHistoryService;

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

    fixture = TestBed.createComponent(ApprovalHistoryUpdate);
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
