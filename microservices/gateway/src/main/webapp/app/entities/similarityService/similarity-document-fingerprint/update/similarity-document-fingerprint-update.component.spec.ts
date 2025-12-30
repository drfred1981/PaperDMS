import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { SimilarityDocumentFingerprintService } from '../service/similarity-document-fingerprint.service';
import { ISimilarityDocumentFingerprint } from '../similarity-document-fingerprint.model';
import { SimilarityDocumentFingerprintFormService } from './similarity-document-fingerprint-form.service';

import { SimilarityDocumentFingerprintUpdateComponent } from './similarity-document-fingerprint-update.component';

describe('SimilarityDocumentFingerprint Management Update Component', () => {
  let comp: SimilarityDocumentFingerprintUpdateComponent;
  let fixture: ComponentFixture<SimilarityDocumentFingerprintUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let similarityDocumentFingerprintFormService: SimilarityDocumentFingerprintFormService;
  let similarityDocumentFingerprintService: SimilarityDocumentFingerprintService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SimilarityDocumentFingerprintUpdateComponent],
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
      .overrideTemplate(SimilarityDocumentFingerprintUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SimilarityDocumentFingerprintUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    similarityDocumentFingerprintFormService = TestBed.inject(SimilarityDocumentFingerprintFormService);
    similarityDocumentFingerprintService = TestBed.inject(SimilarityDocumentFingerprintService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const similarityDocumentFingerprint: ISimilarityDocumentFingerprint = { id: 14373 };

      activatedRoute.data = of({ similarityDocumentFingerprint });
      comp.ngOnInit();

      expect(comp.similarityDocumentFingerprint).toEqual(similarityDocumentFingerprint);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISimilarityDocumentFingerprint>>();
      const similarityDocumentFingerprint = { id: 22662 };
      jest
        .spyOn(similarityDocumentFingerprintFormService, 'getSimilarityDocumentFingerprint')
        .mockReturnValue(similarityDocumentFingerprint);
      jest.spyOn(similarityDocumentFingerprintService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ similarityDocumentFingerprint });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: similarityDocumentFingerprint }));
      saveSubject.complete();

      // THEN
      expect(similarityDocumentFingerprintFormService.getSimilarityDocumentFingerprint).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(similarityDocumentFingerprintService.update).toHaveBeenCalledWith(expect.objectContaining(similarityDocumentFingerprint));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISimilarityDocumentFingerprint>>();
      const similarityDocumentFingerprint = { id: 22662 };
      jest.spyOn(similarityDocumentFingerprintFormService, 'getSimilarityDocumentFingerprint').mockReturnValue({ id: null });
      jest.spyOn(similarityDocumentFingerprintService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ similarityDocumentFingerprint: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: similarityDocumentFingerprint }));
      saveSubject.complete();

      // THEN
      expect(similarityDocumentFingerprintFormService.getSimilarityDocumentFingerprint).toHaveBeenCalled();
      expect(similarityDocumentFingerprintService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISimilarityDocumentFingerprint>>();
      const similarityDocumentFingerprint = { id: 22662 };
      jest.spyOn(similarityDocumentFingerprintService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ similarityDocumentFingerprint });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(similarityDocumentFingerprintService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
