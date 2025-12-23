import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IArchiveJob } from 'app/entities/archiveService/archive-job/archive-job.model';
import { ArchiveJobService } from 'app/entities/archiveService/archive-job/service/archive-job.service';
import { IArchiveDocument } from '../archive-document.model';
import { ArchiveDocumentService } from '../service/archive-document.service';

import { ArchiveDocumentFormService } from './archive-document-form.service';
import { ArchiveDocumentUpdate } from './archive-document-update';

describe('ArchiveDocument Management Update Component', () => {
  let comp: ArchiveDocumentUpdate;
  let fixture: ComponentFixture<ArchiveDocumentUpdate>;
  let activatedRoute: ActivatedRoute;
  let archiveDocumentFormService: ArchiveDocumentFormService;
  let archiveDocumentService: ArchiveDocumentService;
  let archiveJobService: ArchiveJobService;

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

    fixture = TestBed.createComponent(ArchiveDocumentUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    archiveDocumentFormService = TestBed.inject(ArchiveDocumentFormService);
    archiveDocumentService = TestBed.inject(ArchiveDocumentService);
    archiveJobService = TestBed.inject(ArchiveJobService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ArchiveJob query and add missing value', () => {
      const archiveDocument: IArchiveDocument = { id: 24242 };
      const archiveJob: IArchiveJob = { id: 16105 };
      archiveDocument.archiveJob = archiveJob;

      const archiveJobCollection: IArchiveJob[] = [{ id: 16105 }];
      jest.spyOn(archiveJobService, 'query').mockReturnValue(of(new HttpResponse({ body: archiveJobCollection })));
      const additionalArchiveJobs = [archiveJob];
      const expectedCollection: IArchiveJob[] = [...additionalArchiveJobs, ...archiveJobCollection];
      jest.spyOn(archiveJobService, 'addArchiveJobToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ archiveDocument });
      comp.ngOnInit();

      expect(archiveJobService.query).toHaveBeenCalled();
      expect(archiveJobService.addArchiveJobToCollectionIfMissing).toHaveBeenCalledWith(
        archiveJobCollection,
        ...additionalArchiveJobs.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.archiveJobsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const archiveDocument: IArchiveDocument = { id: 24242 };
      const archiveJob: IArchiveJob = { id: 16105 };
      archiveDocument.archiveJob = archiveJob;

      activatedRoute.data = of({ archiveDocument });
      comp.ngOnInit();

      expect(comp.archiveJobsSharedCollection()).toContainEqual(archiveJob);
      expect(comp.archiveDocument).toEqual(archiveDocument);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArchiveDocument>>();
      const archiveDocument = { id: 30053 };
      jest.spyOn(archiveDocumentFormService, 'getArchiveDocument').mockReturnValue(archiveDocument);
      jest.spyOn(archiveDocumentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ archiveDocument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: archiveDocument }));
      saveSubject.complete();

      // THEN
      expect(archiveDocumentFormService.getArchiveDocument).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(archiveDocumentService.update).toHaveBeenCalledWith(expect.objectContaining(archiveDocument));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArchiveDocument>>();
      const archiveDocument = { id: 30053 };
      jest.spyOn(archiveDocumentFormService, 'getArchiveDocument').mockReturnValue({ id: null });
      jest.spyOn(archiveDocumentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ archiveDocument: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: archiveDocument }));
      saveSubject.complete();

      // THEN
      expect(archiveDocumentFormService.getArchiveDocument).toHaveBeenCalled();
      expect(archiveDocumentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArchiveDocument>>();
      const archiveDocument = { id: 30053 };
      jest.spyOn(archiveDocumentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ archiveDocument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(archiveDocumentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareArchiveJob', () => {
      it('should forward to archiveJobService', () => {
        const entity = { id: 16105 };
        const entity2 = { id: 11882 };
        jest.spyOn(archiveJobService, 'compareArchiveJob');
        comp.compareArchiveJob(entity, entity2);
        expect(archiveJobService.compareArchiveJob).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
