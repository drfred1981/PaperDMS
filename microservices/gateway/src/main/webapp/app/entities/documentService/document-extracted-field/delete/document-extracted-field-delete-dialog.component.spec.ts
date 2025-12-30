jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { DocumentExtractedFieldService } from '../service/document-extracted-field.service';

import { DocumentExtractedFieldDeleteDialogComponent } from './document-extracted-field-delete-dialog.component';

describe('DocumentExtractedField Management Delete Component', () => {
  let comp: DocumentExtractedFieldDeleteDialogComponent;
  let fixture: ComponentFixture<DocumentExtractedFieldDeleteDialogComponent>;
  let service: DocumentExtractedFieldService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DocumentExtractedFieldDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(DocumentExtractedFieldDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DocumentExtractedFieldDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DocumentExtractedFieldService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
