import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDocumentType } from 'app/entities/documentService/document-type/document-type.model';
import { DocumentTypeService } from 'app/entities/documentService/document-type/service/document-type.service';
import { DocumentTypeFieldService } from '../service/document-type-field.service';
import { IDocumentTypeField } from '../document-type-field.model';
import { DocumentTypeFieldFormService } from './document-type-field-form.service';

import { DocumentTypeFieldUpdateComponent } from './document-type-field-update.component';

describe('DocumentTypeField Management Update Component', () => {
  let comp: DocumentTypeFieldUpdateComponent;
  let fixture: ComponentFixture<DocumentTypeFieldUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentTypeFieldFormService: DocumentTypeFieldFormService;
  let documentTypeFieldService: DocumentTypeFieldService;
  let documentTypeService: DocumentTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DocumentTypeFieldUpdateComponent],
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
      .overrideTemplate(DocumentTypeFieldUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentTypeFieldUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentTypeFieldFormService = TestBed.inject(DocumentTypeFieldFormService);
    documentTypeFieldService = TestBed.inject(DocumentTypeFieldService);
    documentTypeService = TestBed.inject(DocumentTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call DocumentType query and add missing value', () => {
      const documentTypeField: IDocumentTypeField = { id: 30737 };
      const documentType: IDocumentType = { id: 9974 };
      documentTypeField.documentType = documentType;

      const documentTypeCollection: IDocumentType[] = [{ id: 9974 }];
      jest.spyOn(documentTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: documentTypeCollection })));
      const additionalDocumentTypes = [documentType];
      const expectedCollection: IDocumentType[] = [...additionalDocumentTypes, ...documentTypeCollection];
      jest.spyOn(documentTypeService, 'addDocumentTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ documentTypeField });
      comp.ngOnInit();

      expect(documentTypeService.query).toHaveBeenCalled();
      expect(documentTypeService.addDocumentTypeToCollectionIfMissing).toHaveBeenCalledWith(
        documentTypeCollection,
        ...additionalDocumentTypes.map(expect.objectContaining),
      );
      expect(comp.documentTypesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const documentTypeField: IDocumentTypeField = { id: 30737 };
      const documentType: IDocumentType = { id: 9974 };
      documentTypeField.documentType = documentType;

      activatedRoute.data = of({ documentTypeField });
      comp.ngOnInit();

      expect(comp.documentTypesSharedCollection).toContainEqual(documentType);
      expect(comp.documentTypeField).toEqual(documentTypeField);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentTypeField>>();
      const documentTypeField = { id: 31753 };
      jest.spyOn(documentTypeFieldFormService, 'getDocumentTypeField').mockReturnValue(documentTypeField);
      jest.spyOn(documentTypeFieldService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentTypeField });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentTypeField }));
      saveSubject.complete();

      // THEN
      expect(documentTypeFieldFormService.getDocumentTypeField).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentTypeFieldService.update).toHaveBeenCalledWith(expect.objectContaining(documentTypeField));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentTypeField>>();
      const documentTypeField = { id: 31753 };
      jest.spyOn(documentTypeFieldFormService, 'getDocumentTypeField').mockReturnValue({ id: null });
      jest.spyOn(documentTypeFieldService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentTypeField: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentTypeField }));
      saveSubject.complete();

      // THEN
      expect(documentTypeFieldFormService.getDocumentTypeField).toHaveBeenCalled();
      expect(documentTypeFieldService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentTypeField>>();
      const documentTypeField = { id: 31753 };
      jest.spyOn(documentTypeFieldService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentTypeField });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentTypeFieldService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDocumentType', () => {
      it('should forward to documentTypeService', () => {
        const entity = { id: 9974 };
        const entity2 = { id: 29456 };
        jest.spyOn(documentTypeService, 'compareDocumentType');
        comp.compareDocumentType(entity, entity2);
        expect(documentTypeService.compareDocumentType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
