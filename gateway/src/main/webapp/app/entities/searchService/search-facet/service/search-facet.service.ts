import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISearchFacet, NewSearchFacet } from '../search-facet.model';

export type PartialUpdateSearchFacet = Partial<ISearchFacet> & Pick<ISearchFacet, 'id'>;

export type EntityResponseType = HttpResponse<ISearchFacet>;
export type EntityArrayResponseType = HttpResponse<ISearchFacet[]>;

@Injectable({ providedIn: 'root' })
export class SearchFacetService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/search-facets', 'searchservice');

  create(searchFacet: NewSearchFacet): Observable<EntityResponseType> {
    return this.http.post<ISearchFacet>(this.resourceUrl, searchFacet, { observe: 'response' });
  }

  update(searchFacet: ISearchFacet): Observable<EntityResponseType> {
    return this.http.put<ISearchFacet>(`${this.resourceUrl}/${this.getSearchFacetIdentifier(searchFacet)}`, searchFacet, {
      observe: 'response',
    });
  }

  partialUpdate(searchFacet: PartialUpdateSearchFacet): Observable<EntityResponseType> {
    return this.http.patch<ISearchFacet>(`${this.resourceUrl}/${this.getSearchFacetIdentifier(searchFacet)}`, searchFacet, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISearchFacet>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISearchFacet[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSearchFacetIdentifier(searchFacet: Pick<ISearchFacet, 'id'>): number {
    return searchFacet.id;
  }

  compareSearchFacet(o1: Pick<ISearchFacet, 'id'> | null, o2: Pick<ISearchFacet, 'id'> | null): boolean {
    return o1 && o2 ? this.getSearchFacetIdentifier(o1) === this.getSearchFacetIdentifier(o2) : o1 === o2;
  }

  addSearchFacetToCollectionIfMissing<Type extends Pick<ISearchFacet, 'id'>>(
    searchFacetCollection: Type[],
    ...searchFacetsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const searchFacets: Type[] = searchFacetsToCheck.filter(isPresent);
    if (searchFacets.length > 0) {
      const searchFacetCollectionIdentifiers = searchFacetCollection.map(searchFacetItem => this.getSearchFacetIdentifier(searchFacetItem));
      const searchFacetsToAdd = searchFacets.filter(searchFacetItem => {
        const searchFacetIdentifier = this.getSearchFacetIdentifier(searchFacetItem);
        if (searchFacetCollectionIdentifiers.includes(searchFacetIdentifier)) {
          return false;
        }
        searchFacetCollectionIdentifiers.push(searchFacetIdentifier);
        return true;
      });
      return [...searchFacetsToAdd, ...searchFacetCollection];
    }
    return searchFacetCollection;
  }
}
