import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IDocumentProcess } from '../document-process.model';
import { DocumentProcessService } from '../service/document-process.service';

import { DocumentProcessFormService } from './document-process-form.service';
import { DocumentProcessUpdate } from './document-process-update';

describe('DocumentProcess Management Update Component', () => {
  let comp: DocumentProcessUpdate;
  let fixture: ComponentFixture<DocumentProcessUpdate>;
  let activatedRoute: ActivatedRoute;
  let documentProcessFormService: DocumentProcessFormService;
  let documentProcessService: DocumentProcessService;

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

    fixture = TestBed.createComponent(DocumentProcessUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentProcessFormService = TestBed.inject(DocumentProcessFormService);
    documentProcessService = TestBed.inject(DocumentProcessService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const documentProcess: IDocumentProcess = { id: 28301 };

      activatedRoute.data = of({ documentProcess });
      comp.ngOnInit();

      expect(comp.documentProcess).toEqual(documentProcess);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentProcess>>();
      const documentProcess = { id: 3659 };
      jest.spyOn(documentProcessFormService, 'getDocumentProcess').mockReturnValue(documentProcess);
      jest.spyOn(documentProcessService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentProcess });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentProcess }));
      saveSubject.complete();

      // THEN
      expect(documentProcessFormService.getDocumentProcess).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentProcessService.update).toHaveBeenCalledWith(expect.objectContaining(documentProcess));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentProcess>>();
      const documentProcess = { id: 3659 };
      jest.spyOn(documentProcessFormService, 'getDocumentProcess').mockReturnValue({ id: null });
      jest.spyOn(documentProcessService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentProcess: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentProcess }));
      saveSubject.complete();

      // THEN
      expect(documentProcessFormService.getDocumentProcess).toHaveBeenCalled();
      expect(documentProcessService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentProcess>>();
      const documentProcess = { id: 3659 };
      jest.spyOn(documentProcessService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentProcess });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentProcessService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
