import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ISearchQuery } from 'app/entities/searchService/search-query/search-query.model';
import { SearchQueryService } from 'app/entities/searchService/search-query/service/search-query.service';
import { SearchFacetService } from '../service/search-facet.service';
import { ISearchFacet } from '../search-facet.model';
import { SearchFacetFormService } from './search-facet-form.service';

import { SearchFacetUpdateComponent } from './search-facet-update.component';

describe('SearchFacet Management Update Component', () => {
  let comp: SearchFacetUpdateComponent;
  let fixture: ComponentFixture<SearchFacetUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let searchFacetFormService: SearchFacetFormService;
  let searchFacetService: SearchFacetService;
  let searchQueryService: SearchQueryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SearchFacetUpdateComponent],
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
      .overrideTemplate(SearchFacetUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SearchFacetUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    searchFacetFormService = TestBed.inject(SearchFacetFormService);
    searchFacetService = TestBed.inject(SearchFacetService);
    searchQueryService = TestBed.inject(SearchQueryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call SearchQuery query and add missing value', () => {
      const searchFacet: ISearchFacet = { id: 20050 };
      const searchQuery: ISearchQuery = { id: 14684 };
      searchFacet.searchQuery = searchQuery;

      const searchQueryCollection: ISearchQuery[] = [{ id: 14684 }];
      jest.spyOn(searchQueryService, 'query').mockReturnValue(of(new HttpResponse({ body: searchQueryCollection })));
      const additionalSearchQueries = [searchQuery];
      const expectedCollection: ISearchQuery[] = [...additionalSearchQueries, ...searchQueryCollection];
      jest.spyOn(searchQueryService, 'addSearchQueryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ searchFacet });
      comp.ngOnInit();

      expect(searchQueryService.query).toHaveBeenCalled();
      expect(searchQueryService.addSearchQueryToCollectionIfMissing).toHaveBeenCalledWith(
        searchQueryCollection,
        ...additionalSearchQueries.map(expect.objectContaining),
      );
      expect(comp.searchQueriesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const searchFacet: ISearchFacet = { id: 20050 };
      const searchQuery: ISearchQuery = { id: 14684 };
      searchFacet.searchQuery = searchQuery;

      activatedRoute.data = of({ searchFacet });
      comp.ngOnInit();

      expect(comp.searchQueriesSharedCollection).toContainEqual(searchQuery);
      expect(comp.searchFacet).toEqual(searchFacet);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISearchFacet>>();
      const searchFacet = { id: 5218 };
      jest.spyOn(searchFacetFormService, 'getSearchFacet').mockReturnValue(searchFacet);
      jest.spyOn(searchFacetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ searchFacet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: searchFacet }));
      saveSubject.complete();

      // THEN
      expect(searchFacetFormService.getSearchFacet).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(searchFacetService.update).toHaveBeenCalledWith(expect.objectContaining(searchFacet));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISearchFacet>>();
      const searchFacet = { id: 5218 };
      jest.spyOn(searchFacetFormService, 'getSearchFacet').mockReturnValue({ id: null });
      jest.spyOn(searchFacetService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ searchFacet: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: searchFacet }));
      saveSubject.complete();

      // THEN
      expect(searchFacetFormService.getSearchFacet).toHaveBeenCalled();
      expect(searchFacetService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISearchFacet>>();
      const searchFacet = { id: 5218 };
      jest.spyOn(searchFacetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ searchFacet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(searchFacetService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSearchQuery', () => {
      it('should forward to searchQueryService', () => {
        const entity = { id: 14684 };
        const entity2 = { id: 23738 };
        jest.spyOn(searchQueryService, 'compareSearchQuery');
        comp.compareSearchQuery(entity, entity2);
        expect(searchQueryService.compareSearchQuery).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
