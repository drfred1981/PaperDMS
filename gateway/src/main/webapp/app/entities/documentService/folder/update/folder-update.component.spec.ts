import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { FolderService } from '../service/folder.service';
import { IFolder } from '../folder.model';
import { FolderFormService } from './folder-form.service';

import { FolderUpdateComponent } from './folder-update.component';

describe('Folder Management Update Component', () => {
  let comp: FolderUpdateComponent;
  let fixture: ComponentFixture<FolderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let folderFormService: FolderFormService;
  let folderService: FolderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FolderUpdateComponent],
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
      .overrideTemplate(FolderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FolderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    folderFormService = TestBed.inject(FolderFormService);
    folderService = TestBed.inject(FolderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Folder query and add missing value', () => {
      const folder: IFolder = { id: 16447 };
      const parent: IFolder = { id: 4745 };
      folder.parent = parent;

      const folderCollection: IFolder[] = [{ id: 4745 }];
      jest.spyOn(folderService, 'query').mockReturnValue(of(new HttpResponse({ body: folderCollection })));
      const additionalFolders = [parent];
      const expectedCollection: IFolder[] = [...additionalFolders, ...folderCollection];
      jest.spyOn(folderService, 'addFolderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ folder });
      comp.ngOnInit();

      expect(folderService.query).toHaveBeenCalled();
      expect(folderService.addFolderToCollectionIfMissing).toHaveBeenCalledWith(
        folderCollection,
        ...additionalFolders.map(expect.objectContaining),
      );
      expect(comp.foldersSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const folder: IFolder = { id: 16447 };
      const parent: IFolder = { id: 4745 };
      folder.parent = parent;

      activatedRoute.data = of({ folder });
      comp.ngOnInit();

      expect(comp.foldersSharedCollection).toContainEqual(parent);
      expect(comp.folder).toEqual(folder);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFolder>>();
      const folder = { id: 4745 };
      jest.spyOn(folderFormService, 'getFolder').mockReturnValue(folder);
      jest.spyOn(folderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ folder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: folder }));
      saveSubject.complete();

      // THEN
      expect(folderFormService.getFolder).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(folderService.update).toHaveBeenCalledWith(expect.objectContaining(folder));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFolder>>();
      const folder = { id: 4745 };
      jest.spyOn(folderFormService, 'getFolder').mockReturnValue({ id: null });
      jest.spyOn(folderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ folder: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: folder }));
      saveSubject.complete();

      // THEN
      expect(folderFormService.getFolder).toHaveBeenCalled();
      expect(folderService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFolder>>();
      const folder = { id: 4745 };
      jest.spyOn(folderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ folder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(folderService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFolder', () => {
      it('should forward to folderService', () => {
        const entity = { id: 4745 };
        const entity2 = { id: 16447 };
        jest.spyOn(folderService, 'compareFolder');
        comp.compareFolder(entity, entity2);
        expect(folderService.compareFolder).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
