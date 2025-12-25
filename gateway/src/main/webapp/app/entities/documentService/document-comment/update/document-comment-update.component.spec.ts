import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { DocumentCommentService } from '../service/document-comment.service';
import { IDocumentComment } from '../document-comment.model';
import { DocumentCommentFormService } from './document-comment-form.service';

import { DocumentCommentUpdateComponent } from './document-comment-update.component';

describe('DocumentComment Management Update Component', () => {
  let comp: DocumentCommentUpdateComponent;
  let fixture: ComponentFixture<DocumentCommentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentCommentFormService: DocumentCommentFormService;
  let documentCommentService: DocumentCommentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DocumentCommentUpdateComponent],
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
      .overrideTemplate(DocumentCommentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentCommentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentCommentFormService = TestBed.inject(DocumentCommentFormService);
    documentCommentService = TestBed.inject(DocumentCommentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call DocumentComment query and add missing value', () => {
      const documentComment: IDocumentComment = { id: 7332 };
      const parentComment: IDocumentComment = { id: 5301 };
      documentComment.parentComment = parentComment;

      const documentCommentCollection: IDocumentComment[] = [{ id: 5301 }];
      jest.spyOn(documentCommentService, 'query').mockReturnValue(of(new HttpResponse({ body: documentCommentCollection })));
      const additionalDocumentComments = [parentComment];
      const expectedCollection: IDocumentComment[] = [...additionalDocumentComments, ...documentCommentCollection];
      jest.spyOn(documentCommentService, 'addDocumentCommentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ documentComment });
      comp.ngOnInit();

      expect(documentCommentService.query).toHaveBeenCalled();
      expect(documentCommentService.addDocumentCommentToCollectionIfMissing).toHaveBeenCalledWith(
        documentCommentCollection,
        ...additionalDocumentComments.map(expect.objectContaining),
      );
      expect(comp.documentCommentsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const documentComment: IDocumentComment = { id: 7332 };
      const parentComment: IDocumentComment = { id: 5301 };
      documentComment.parentComment = parentComment;

      activatedRoute.data = of({ documentComment });
      comp.ngOnInit();

      expect(comp.documentCommentsSharedCollection).toContainEqual(parentComment);
      expect(comp.documentComment).toEqual(documentComment);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentComment>>();
      const documentComment = { id: 5301 };
      jest.spyOn(documentCommentFormService, 'getDocumentComment').mockReturnValue(documentComment);
      jest.spyOn(documentCommentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentComment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentComment }));
      saveSubject.complete();

      // THEN
      expect(documentCommentFormService.getDocumentComment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentCommentService.update).toHaveBeenCalledWith(expect.objectContaining(documentComment));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentComment>>();
      const documentComment = { id: 5301 };
      jest.spyOn(documentCommentFormService, 'getDocumentComment').mockReturnValue({ id: null });
      jest.spyOn(documentCommentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentComment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentComment }));
      saveSubject.complete();

      // THEN
      expect(documentCommentFormService.getDocumentComment).toHaveBeenCalled();
      expect(documentCommentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentComment>>();
      const documentComment = { id: 5301 };
      jest.spyOn(documentCommentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentComment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentCommentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDocumentComment', () => {
      it('should forward to documentCommentService', () => {
        const entity = { id: 5301 };
        const entity2 = { id: 7332 };
        jest.spyOn(documentCommentService, 'compareDocumentComment');
        comp.compareDocumentComment(entity, entity2);
        expect(documentCommentService.compareDocumentComment).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
