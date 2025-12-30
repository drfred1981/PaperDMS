import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDocumentType } from 'app/entities/documentService/document-type/document-type.model';
import { DocumentTypeService } from 'app/entities/documentService/document-type/service/document-type.service';
import { DocumentTemplateService } from '../service/document-template.service';
import { IDocumentTemplate } from '../document-template.model';
import { DocumentTemplateFormService } from './document-template-form.service';

import { DocumentTemplateUpdateComponent } from './document-template-update.component';

describe('DocumentTemplate Management Update Component', () => {
  let comp: DocumentTemplateUpdateComponent;
  let fixture: ComponentFixture<DocumentTemplateUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentTemplateFormService: DocumentTemplateFormService;
  let documentTemplateService: DocumentTemplateService;
  let documentTypeService: DocumentTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DocumentTemplateUpdateComponent],
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
      .overrideTemplate(DocumentTemplateUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentTemplateUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentTemplateFormService = TestBed.inject(DocumentTemplateFormService);
    documentTemplateService = TestBed.inject(DocumentTemplateService);
    documentTypeService = TestBed.inject(DocumentTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call DocumentType query and add missing value', () => {
      const documentTemplate: IDocumentTemplate = { id: 10432 };
      const documentType: IDocumentType = { id: 9974 };
      documentTemplate.documentType = documentType;

      const documentTypeCollection: IDocumentType[] = [{ id: 9974 }];
      jest.spyOn(documentTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: documentTypeCollection })));
      const additionalDocumentTypes = [documentType];
      const expectedCollection: IDocumentType[] = [...additionalDocumentTypes, ...documentTypeCollection];
      jest.spyOn(documentTypeService, 'addDocumentTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ documentTemplate });
      comp.ngOnInit();

      expect(documentTypeService.query).toHaveBeenCalled();
      expect(documentTypeService.addDocumentTypeToCollectionIfMissing).toHaveBeenCalledWith(
        documentTypeCollection,
        ...additionalDocumentTypes.map(expect.objectContaining),
      );
      expect(comp.documentTypesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const documentTemplate: IDocumentTemplate = { id: 10432 };
      const documentType: IDocumentType = { id: 9974 };
      documentTemplate.documentType = documentType;

      activatedRoute.data = of({ documentTemplate });
      comp.ngOnInit();

      expect(comp.documentTypesSharedCollection).toContainEqual(documentType);
      expect(comp.documentTemplate).toEqual(documentTemplate);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentTemplate>>();
      const documentTemplate = { id: 25776 };
      jest.spyOn(documentTemplateFormService, 'getDocumentTemplate').mockReturnValue(documentTemplate);
      jest.spyOn(documentTemplateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentTemplate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentTemplate }));
      saveSubject.complete();

      // THEN
      expect(documentTemplateFormService.getDocumentTemplate).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentTemplateService.update).toHaveBeenCalledWith(expect.objectContaining(documentTemplate));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentTemplate>>();
      const documentTemplate = { id: 25776 };
      jest.spyOn(documentTemplateFormService, 'getDocumentTemplate').mockReturnValue({ id: null });
      jest.spyOn(documentTemplateService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentTemplate: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentTemplate }));
      saveSubject.complete();

      // THEN
      expect(documentTemplateFormService.getDocumentTemplate).toHaveBeenCalled();
      expect(documentTemplateService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentTemplate>>();
      const documentTemplate = { id: 25776 };
      jest.spyOn(documentTemplateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentTemplate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentTemplateService.update).toHaveBeenCalled();
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
