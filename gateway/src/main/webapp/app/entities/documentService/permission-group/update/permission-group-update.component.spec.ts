import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { PermissionGroupService } from '../service/permission-group.service';
import { IPermissionGroup } from '../permission-group.model';
import { PermissionGroupFormService } from './permission-group-form.service';

import { PermissionGroupUpdateComponent } from './permission-group-update.component';

describe('PermissionGroup Management Update Component', () => {
  let comp: PermissionGroupUpdateComponent;
  let fixture: ComponentFixture<PermissionGroupUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let permissionGroupFormService: PermissionGroupFormService;
  let permissionGroupService: PermissionGroupService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PermissionGroupUpdateComponent],
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
      .overrideTemplate(PermissionGroupUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PermissionGroupUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    permissionGroupFormService = TestBed.inject(PermissionGroupFormService);
    permissionGroupService = TestBed.inject(PermissionGroupService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const permissionGroup: IPermissionGroup = { id: 13105 };

      activatedRoute.data = of({ permissionGroup });
      comp.ngOnInit();

      expect(comp.permissionGroup).toEqual(permissionGroup);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPermissionGroup>>();
      const permissionGroup = { id: 20701 };
      jest.spyOn(permissionGroupFormService, 'getPermissionGroup').mockReturnValue(permissionGroup);
      jest.spyOn(permissionGroupService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ permissionGroup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: permissionGroup }));
      saveSubject.complete();

      // THEN
      expect(permissionGroupFormService.getPermissionGroup).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(permissionGroupService.update).toHaveBeenCalledWith(expect.objectContaining(permissionGroup));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPermissionGroup>>();
      const permissionGroup = { id: 20701 };
      jest.spyOn(permissionGroupFormService, 'getPermissionGroup').mockReturnValue({ id: null });
      jest.spyOn(permissionGroupService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ permissionGroup: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: permissionGroup }));
      saveSubject.complete();

      // THEN
      expect(permissionGroupFormService.getPermissionGroup).toHaveBeenCalled();
      expect(permissionGroupService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPermissionGroup>>();
      const permissionGroup = { id: 20701 };
      jest.spyOn(permissionGroupService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ permissionGroup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(permissionGroupService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
