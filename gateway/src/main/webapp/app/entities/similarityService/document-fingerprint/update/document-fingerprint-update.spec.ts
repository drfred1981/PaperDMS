import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IDocumentFingerprint } from '../document-fingerprint.model';
import { DocumentFingerprintService } from '../service/document-fingerprint.service';

import { DocumentFingerprintFormService } from './document-fingerprint-form.service';
import { DocumentFingerprintUpdate } from './document-fingerprint-update';

describe('DocumentFingerprint Management Update Component', () => {
  let comp: DocumentFingerprintUpdate;
  let fixture: ComponentFixture<DocumentFingerprintUpdate>;
  let activatedRoute: ActivatedRoute;
  let documentFingerprintFormService: DocumentFingerprintFormService;
  let documentFingerprintService: DocumentFingerprintService;

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

    fixture = TestBed.createComponent(DocumentFingerprintUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentFingerprintFormService = TestBed.inject(DocumentFingerprintFormService);
    documentFingerprintService = TestBed.inject(DocumentFingerprintService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const documentFingerprint: IDocumentFingerprint = { id: 19955 };

      activatedRoute.data = of({ documentFingerprint });
      comp.ngOnInit();

      expect(comp.documentFingerprint).toEqual(documentFingerprint);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentFingerprint>>();
      const documentFingerprint = { id: 26494 };
      jest.spyOn(documentFingerprintFormService, 'getDocumentFingerprint').mockReturnValue(documentFingerprint);
      jest.spyOn(documentFingerprintService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentFingerprint });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentFingerprint }));
      saveSubject.complete();

      // THEN
      expect(documentFingerprintFormService.getDocumentFingerprint).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentFingerprintService.update).toHaveBeenCalledWith(expect.objectContaining(documentFingerprint));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentFingerprint>>();
      const documentFingerprint = { id: 26494 };
      jest.spyOn(documentFingerprintFormService, 'getDocumentFingerprint').mockReturnValue({ id: null });
      jest.spyOn(documentFingerprintService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentFingerprint: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentFingerprint }));
      saveSubject.complete();

      // THEN
      expect(documentFingerprintFormService.getDocumentFingerprint).toHaveBeenCalled();
      expect(documentFingerprintService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentFingerprint>>();
      const documentFingerprint = { id: 26494 };
      jest.spyOn(documentFingerprintService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentFingerprint });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentFingerprintService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
