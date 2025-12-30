jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { SimilarityDocumentComparisonService } from '../service/similarity-document-comparison.service';

import { SimilarityDocumentComparisonDeleteDialogComponent } from './similarity-document-comparison-delete-dialog.component';

describe('SimilarityDocumentComparison Management Delete Component', () => {
  let comp: SimilarityDocumentComparisonDeleteDialogComponent;
  let fixture: ComponentFixture<SimilarityDocumentComparisonDeleteDialogComponent>;
  let service: SimilarityDocumentComparisonService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SimilarityDocumentComparisonDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(SimilarityDocumentComparisonDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SimilarityDocumentComparisonDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SimilarityDocumentComparisonService);
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
