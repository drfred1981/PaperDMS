import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IArchiveJob } from '../archive-job.model';
import { ArchiveJobService } from '../service/archive-job.service';

import { ArchiveJobFormService } from './archive-job-form.service';
import { ArchiveJobUpdate } from './archive-job-update';

describe('ArchiveJob Management Update Component', () => {
  let comp: ArchiveJobUpdate;
  let fixture: ComponentFixture<ArchiveJobUpdate>;
  let activatedRoute: ActivatedRoute;
  let archiveJobFormService: ArchiveJobFormService;
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

    fixture = TestBed.createComponent(ArchiveJobUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    archiveJobFormService = TestBed.inject(ArchiveJobFormService);
    archiveJobService = TestBed.inject(ArchiveJobService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const archiveJob: IArchiveJob = { id: 11882 };

      activatedRoute.data = of({ archiveJob });
      comp.ngOnInit();

      expect(comp.archiveJob).toEqual(archiveJob);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArchiveJob>>();
      const archiveJob = { id: 16105 };
      jest.spyOn(archiveJobFormService, 'getArchiveJob').mockReturnValue(archiveJob);
      jest.spyOn(archiveJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ archiveJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: archiveJob }));
      saveSubject.complete();

      // THEN
      expect(archiveJobFormService.getArchiveJob).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(archiveJobService.update).toHaveBeenCalledWith(expect.objectContaining(archiveJob));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArchiveJob>>();
      const archiveJob = { id: 16105 };
      jest.spyOn(archiveJobFormService, 'getArchiveJob').mockReturnValue({ id: null });
      jest.spyOn(archiveJobService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ archiveJob: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: archiveJob }));
      saveSubject.complete();

      // THEN
      expect(archiveJobFormService.getArchiveJob).toHaveBeenCalled();
      expect(archiveJobService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArchiveJob>>();
      const archiveJob = { id: 16105 };
      jest.spyOn(archiveJobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ archiveJob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(archiveJobService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
