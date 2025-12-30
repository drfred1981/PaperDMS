import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { SearchSemanticService } from '../service/search-semantic.service';
import { ISearchSemantic } from '../search-semantic.model';
import { SearchSemanticFormService } from './search-semantic-form.service';

import { SearchSemanticUpdateComponent } from './search-semantic-update.component';

describe('SearchSemantic Management Update Component', () => {
  let comp: SearchSemanticUpdateComponent;
  let fixture: ComponentFixture<SearchSemanticUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let searchSemanticFormService: SearchSemanticFormService;
  let searchSemanticService: SearchSemanticService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SearchSemanticUpdateComponent],
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
      .overrideTemplate(SearchSemanticUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SearchSemanticUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    searchSemanticFormService = TestBed.inject(SearchSemanticFormService);
    searchSemanticService = TestBed.inject(SearchSemanticService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const searchSemantic: ISearchSemantic = { id: 9131 };

      activatedRoute.data = of({ searchSemantic });
      comp.ngOnInit();

      expect(comp.searchSemantic).toEqual(searchSemantic);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISearchSemantic>>();
      const searchSemantic = { id: 4712 };
      jest.spyOn(searchSemanticFormService, 'getSearchSemantic').mockReturnValue(searchSemantic);
      jest.spyOn(searchSemanticService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ searchSemantic });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: searchSemantic }));
      saveSubject.complete();

      // THEN
      expect(searchSemanticFormService.getSearchSemantic).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(searchSemanticService.update).toHaveBeenCalledWith(expect.objectContaining(searchSemantic));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISearchSemantic>>();
      const searchSemantic = { id: 4712 };
      jest.spyOn(searchSemanticFormService, 'getSearchSemantic').mockReturnValue({ id: null });
      jest.spyOn(searchSemanticService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ searchSemantic: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: searchSemantic }));
      saveSubject.complete();

      // THEN
      expect(searchSemanticFormService.getSearchSemantic).toHaveBeenCalled();
      expect(searchSemanticService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISearchSemantic>>();
      const searchSemantic = { id: 4712 };
      jest.spyOn(searchSemanticService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ searchSemantic });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(searchSemanticService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
