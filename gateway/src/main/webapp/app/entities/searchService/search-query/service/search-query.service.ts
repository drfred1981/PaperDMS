import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { ISearchQuery, NewSearchQuery } from '../search-query.model';

export type PartialUpdateSearchQuery = Partial<ISearchQuery> & Pick<ISearchQuery, 'id'>;

type RestOf<T extends ISearchQuery | NewSearchQuery> = Omit<T, 'searchDate'> & {
  searchDate?: string | null;
};

export type RestSearchQuery = RestOf<ISearchQuery>;

export type NewRestSearchQuery = RestOf<NewSearchQuery>;

export type PartialUpdateRestSearchQuery = RestOf<PartialUpdateSearchQuery>;

export type EntityResponseType = HttpResponse<ISearchQuery>;
export type EntityArrayResponseType = HttpResponse<ISearchQuery[]>;

@Injectable({ providedIn: 'root' })
export class SearchQueryService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/search-queries', 'searchservice');

  create(searchQuery: NewSearchQuery): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(searchQuery);
    return this.http
      .post<RestSearchQuery>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(searchQuery: ISearchQuery): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(searchQuery);
    return this.http
      .put<RestSearchQuery>(`${this.resourceUrl}/${encodeURIComponent(this.getSearchQueryIdentifier(searchQuery))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(searchQuery: PartialUpdateSearchQuery): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(searchQuery);
    return this.http
      .patch<RestSearchQuery>(`${this.resourceUrl}/${encodeURIComponent(this.getSearchQueryIdentifier(searchQuery))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSearchQuery>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSearchQuery[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getSearchQueryIdentifier(searchQuery: Pick<ISearchQuery, 'id'>): number {
    return searchQuery.id;
  }

  compareSearchQuery(o1: Pick<ISearchQuery, 'id'> | null, o2: Pick<ISearchQuery, 'id'> | null): boolean {
    return o1 && o2 ? this.getSearchQueryIdentifier(o1) === this.getSearchQueryIdentifier(o2) : o1 === o2;
  }

  addSearchQueryToCollectionIfMissing<Type extends Pick<ISearchQuery, 'id'>>(
    searchQueryCollection: Type[],
    ...searchQueriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const searchQueries: Type[] = searchQueriesToCheck.filter(isPresent);
    if (searchQueries.length > 0) {
      const searchQueryCollectionIdentifiers = searchQueryCollection.map(searchQueryItem => this.getSearchQueryIdentifier(searchQueryItem));
      const searchQueriesToAdd = searchQueries.filter(searchQueryItem => {
        const searchQueryIdentifier = this.getSearchQueryIdentifier(searchQueryItem);
        if (searchQueryCollectionIdentifiers.includes(searchQueryIdentifier)) {
          return false;
        }
        searchQueryCollectionIdentifiers.push(searchQueryIdentifier);
        return true;
      });
      return [...searchQueriesToAdd, ...searchQueryCollection];
    }
    return searchQueryCollection;
  }

  protected convertDateFromClient<T extends ISearchQuery | NewSearchQuery | PartialUpdateSearchQuery>(searchQuery: T): RestOf<T> {
    return {
      ...searchQuery,
      searchDate: searchQuery.searchDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSearchQuery: RestSearchQuery): ISearchQuery {
    return {
      ...restSearchQuery,
      searchDate: restSearchQuery.searchDate ? dayjs(restSearchQuery.searchDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSearchQuery>): HttpResponse<ISearchQuery> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSearchQuery[]>): HttpResponse<ISearchQuery[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
