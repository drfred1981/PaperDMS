import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPermissionGroup } from 'app/entities/documentService/permission-group/permission-group.model';
import { PermissionGroupService } from 'app/entities/documentService/permission-group/service/permission-group.service';
import { DocumentPermissionService } from '../service/document-permission.service';
import { IDocumentPermission } from '../document-permission.model';
import { DocumentPermissionFormService } from './document-permission-form.service';

import { DocumentPermissionUpdateComponent } from './document-permission-update.component';

describe('DocumentPermission Management Update Component', () => {
  let comp: DocumentPermissionUpdateComponent;
  let fixture: ComponentFixture<DocumentPermissionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentPermissionFormService: DocumentPermissionFormService;
  let documentPermissionService: DocumentPermissionService;
  let permissionGroupService: PermissionGroupService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DocumentPermissionUpdateComponent],
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
      .overrideTemplate(DocumentPermissionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentPermissionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentPermissionFormService = TestBed.inject(DocumentPermissionFormService);
    documentPermissionService = TestBed.inject(DocumentPermissionService);
    permissionGroupService = TestBed.inject(PermissionGroupService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call PermissionGroup query and add missing value', () => {
      const documentPermission: IDocumentPermission = { id: 25975 };
      const permissionGroup: IPermissionGroup = { id: 20701 };
      documentPermission.permissionGroup = permissionGroup;

      const permissionGroupCollection: IPermissionGroup[] = [{ id: 20701 }];
      jest.spyOn(permissionGroupService, 'query').mockReturnValue(of(new HttpResponse({ body: permissionGroupCollection })));
      const additionalPermissionGroups = [permissionGroup];
      const expectedCollection: IPermissionGroup[] = [...additionalPermissionGroups, ...permissionGroupCollection];
      jest.spyOn(permissionGroupService, 'addPermissionGroupToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ documentPermission });
      comp.ngOnInit();

      expect(permissionGroupService.query).toHaveBeenCalled();
      expect(permissionGroupService.addPermissionGroupToCollectionIfMissing).toHaveBeenCalledWith(
        permissionGroupCollection,
        ...additionalPermissionGroups.map(expect.objectContaining),
      );
      expect(comp.permissionGroupsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const documentPermission: IDocumentPermission = { id: 25975 };
      const permissionGroup: IPermissionGroup = { id: 20701 };
      documentPermission.permissionGroup = permissionGroup;

      activatedRoute.data = of({ documentPermission });
      comp.ngOnInit();

      expect(comp.permissionGroupsSharedCollection).toContainEqual(permissionGroup);
      expect(comp.documentPermission).toEqual(documentPermission);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentPermission>>();
      const documentPermission = { id: 5224 };
      jest.spyOn(documentPermissionFormService, 'getDocumentPermission').mockReturnValue(documentPermission);
      jest.spyOn(documentPermissionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentPermission });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentPermission }));
      saveSubject.complete();

      // THEN
      expect(documentPermissionFormService.getDocumentPermission).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentPermissionService.update).toHaveBeenCalledWith(expect.objectContaining(documentPermission));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentPermission>>();
      const documentPermission = { id: 5224 };
      jest.spyOn(documentPermissionFormService, 'getDocumentPermission').mockReturnValue({ id: null });
      jest.spyOn(documentPermissionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentPermission: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentPermission }));
      saveSubject.complete();

      // THEN
      expect(documentPermissionFormService.getDocumentPermission).toHaveBeenCalled();
      expect(documentPermissionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentPermission>>();
      const documentPermission = { id: 5224 };
      jest.spyOn(documentPermissionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentPermission });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentPermissionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePermissionGroup', () => {
      it('should forward to permissionGroupService', () => {
        const entity = { id: 20701 };
        const entity2 = { id: 13105 };
        jest.spyOn(permissionGroupService, 'comparePermissionGroup');
        comp.comparePermissionGroup(entity, entity2);
        expect(permissionGroupService.comparePermissionGroup).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
