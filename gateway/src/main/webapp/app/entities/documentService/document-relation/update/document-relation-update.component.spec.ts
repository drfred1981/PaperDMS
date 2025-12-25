import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { DocumentRelationService } from '../service/document-relation.service';
import { IDocumentRelation } from '../document-relation.model';
import { DocumentRelationFormService } from './document-relation-form.service';

import { DocumentRelationUpdateComponent } from './document-relation-update.component';

describe('DocumentRelation Management Update Component', () => {
  let comp: DocumentRelationUpdateComponent;
  let fixture: ComponentFixture<DocumentRelationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentRelationFormService: DocumentRelationFormService;
  let documentRelationService: DocumentRelationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DocumentRelationUpdateComponent],
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
      .overrideTemplate(DocumentRelationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentRelationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentRelationFormService = TestBed.inject(DocumentRelationFormService);
    documentRelationService = TestBed.inject(DocumentRelationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const documentRelation: IDocumentRelation = { id: 4779 };

      activatedRoute.data = of({ documentRelation });
      comp.ngOnInit();

      expect(comp.documentRelation).toEqual(documentRelation);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentRelation>>();
      const documentRelation = { id: 21039 };
      jest.spyOn(documentRelationFormService, 'getDocumentRelation').mockReturnValue(documentRelation);
      jest.spyOn(documentRelationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentRelation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentRelation }));
      saveSubject.complete();

      // THEN
      expect(documentRelationFormService.getDocumentRelation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentRelationService.update).toHaveBeenCalledWith(expect.objectContaining(documentRelation));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentRelation>>();
      const documentRelation = { id: 21039 };
      jest.spyOn(documentRelationFormService, 'getDocumentRelation').mockReturnValue({ id: null });
      jest.spyOn(documentRelationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentRelation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentRelation }));
      saveSubject.complete();

      // THEN
      expect(documentRelationFormService.getDocumentRelation).toHaveBeenCalled();
      expect(documentRelationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentRelation>>();
      const documentRelation = { id: 21039 };
      jest.spyOn(documentRelationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentRelation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentRelationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
