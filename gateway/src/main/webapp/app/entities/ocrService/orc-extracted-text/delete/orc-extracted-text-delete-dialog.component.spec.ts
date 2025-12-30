jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { OrcExtractedTextService } from '../service/orc-extracted-text.service';

import { OrcExtractedTextDeleteDialogComponent } from './orc-extracted-text-delete-dialog.component';

describe('OrcExtractedText Management Delete Component', () => {
  let comp: OrcExtractedTextDeleteDialogComponent;
  let fixture: ComponentFixture<OrcExtractedTextDeleteDialogComponent>;
  let service: OrcExtractedTextService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [OrcExtractedTextDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(OrcExtractedTextDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OrcExtractedTextDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(OrcExtractedTextService);
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
