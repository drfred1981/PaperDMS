import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { MetaFolderService } from '../service/meta-folder.service';
import { IMetaFolder } from '../meta-folder.model';
import { MetaFolderFormService } from './meta-folder-form.service';

import { MetaFolderUpdateComponent } from './meta-folder-update.component';

describe('MetaFolder Management Update Component', () => {
  let comp: MetaFolderUpdateComponent;
  let fixture: ComponentFixture<MetaFolderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let metaFolderFormService: MetaFolderFormService;
  let metaFolderService: MetaFolderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MetaFolderUpdateComponent],
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
      .overrideTemplate(MetaFolderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MetaFolderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    metaFolderFormService = TestBed.inject(MetaFolderFormService);
    metaFolderService = TestBed.inject(MetaFolderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call MetaFolder query and add missing value', () => {
      const metaFolder: IMetaFolder = { id: 27869 };
      const parent: IMetaFolder = { id: 18281 };
      metaFolder.parent = parent;

      const metaFolderCollection: IMetaFolder[] = [{ id: 18281 }];
      jest.spyOn(metaFolderService, 'query').mockReturnValue(of(new HttpResponse({ body: metaFolderCollection })));
      const additionalMetaFolders = [parent];
      const expectedCollection: IMetaFolder[] = [...additionalMetaFolders, ...metaFolderCollection];
      jest.spyOn(metaFolderService, 'addMetaFolderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ metaFolder });
      comp.ngOnInit();

      expect(metaFolderService.query).toHaveBeenCalled();
      expect(metaFolderService.addMetaFolderToCollectionIfMissing).toHaveBeenCalledWith(
        metaFolderCollection,
        ...additionalMetaFolders.map(expect.objectContaining),
      );
      expect(comp.metaFoldersSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const metaFolder: IMetaFolder = { id: 27869 };
      const parent: IMetaFolder = { id: 18281 };
      metaFolder.parent = parent;

      activatedRoute.data = of({ metaFolder });
      comp.ngOnInit();

      expect(comp.metaFoldersSharedCollection).toContainEqual(parent);
      expect(comp.metaFolder).toEqual(metaFolder);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMetaFolder>>();
      const metaFolder = { id: 18281 };
      jest.spyOn(metaFolderFormService, 'getMetaFolder').mockReturnValue(metaFolder);
      jest.spyOn(metaFolderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ metaFolder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: metaFolder }));
      saveSubject.complete();

      // THEN
      expect(metaFolderFormService.getMetaFolder).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(metaFolderService.update).toHaveBeenCalledWith(expect.objectContaining(metaFolder));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMetaFolder>>();
      const metaFolder = { id: 18281 };
      jest.spyOn(metaFolderFormService, 'getMetaFolder').mockReturnValue({ id: null });
      jest.spyOn(metaFolderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ metaFolder: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: metaFolder }));
      saveSubject.complete();

      // THEN
      expect(metaFolderFormService.getMetaFolder).toHaveBeenCalled();
      expect(metaFolderService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMetaFolder>>();
      const metaFolder = { id: 18281 };
      jest.spyOn(metaFolderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ metaFolder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(metaFolderService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMetaFolder', () => {
      it('should forward to metaFolderService', () => {
        const entity = { id: 18281 };
        const entity2 = { id: 27869 };
        jest.spyOn(metaFolderService, 'compareMetaFolder');
        comp.compareMetaFolder(entity, entity2);
        expect(metaFolderService.compareMetaFolder).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
