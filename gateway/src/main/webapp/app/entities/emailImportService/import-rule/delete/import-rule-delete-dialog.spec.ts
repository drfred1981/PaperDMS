import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';

import { ImportRuleService } from '../service/import-rule.service';

import { ImportRuleDeleteDialog } from './import-rule-delete-dialog';

describe('ImportRule Management Delete Component', () => {
  let comp: ImportRuleDeleteDialog;
  let fixture: ComponentFixture<ImportRuleDeleteDialog>;
  let service: ImportRuleService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), NgbActiveModal],
    });
    fixture = TestBed.createComponent(ImportRuleDeleteDialog);
    comp = fixture.componentInstance;
    service = TestBed.inject(ImportRuleService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('should call delete service on confirmDelete', () => {
      // GIVEN
      jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));
      jest.spyOn(mockActiveModal, 'close');

      // WHEN
      comp.confirmDelete(123);

      // THEN
      expect(service.delete).toHaveBeenCalledWith(123);
      expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
    });

    it('should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');
      jest.spyOn(mockActiveModal, 'close');
      jest.spyOn(mockActiveModal, 'dismiss');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
