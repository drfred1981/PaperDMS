import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ISearchIndex, NewSearchIndex } from '../search-index.model';

export type PartialUpdateSearchIndex = Partial<ISearchIndex> & Pick<ISearchIndex, 'id'>;

type RestOf<T extends ISearchIndex | NewSearchIndex> = Omit<T, 'indexedDate' | 'lastUpdated'> & {
  indexedDate?: string | null;
  lastUpdated?: string | null;
};

export type RestSearchIndex = RestOf<ISearchIndex>;

export type NewRestSearchIndex = RestOf<NewSearchIndex>;

export type PartialUpdateRestSearchIndex = RestOf<PartialUpdateSearchIndex>;

export type EntityResponseType = HttpResponse<ISearchIndex>;
export type EntityArrayResponseType = HttpResponse<ISearchIndex[]>;

@Injectable({ providedIn: 'root' })
export class SearchIndexService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/search-indices', 'searchservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/search-indices/_search', 'searchservice');

  create(searchIndex: NewSearchIndex): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(searchIndex);
    return this.http
      .post<RestSearchIndex>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(searchIndex: ISearchIndex): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(searchIndex);
    return this.http
      .put<RestSearchIndex>(`${this.resourceUrl}/${this.getSearchIndexIdentifier(searchIndex)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(searchIndex: PartialUpdateSearchIndex): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(searchIndex);
    return this.http
      .patch<RestSearchIndex>(`${this.resourceUrl}/${this.getSearchIndexIdentifier(searchIndex)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSearchIndex>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSearchIndex[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestSearchIndex[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<ISearchIndex[]>()], asapScheduler)),
    );
  }

  getSearchIndexIdentifier(searchIndex: Pick<ISearchIndex, 'id'>): number {
    return searchIndex.id;
  }

  compareSearchIndex(o1: Pick<ISearchIndex, 'id'> | null, o2: Pick<ISearchIndex, 'id'> | null): boolean {
    return o1 && o2 ? this.getSearchIndexIdentifier(o1) === this.getSearchIndexIdentifier(o2) : o1 === o2;
  }

  addSearchIndexToCollectionIfMissing<Type extends Pick<ISearchIndex, 'id'>>(
    searchIndexCollection: Type[],
    ...searchIndicesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const searchIndices: Type[] = searchIndicesToCheck.filter(isPresent);
    if (searchIndices.length > 0) {
      const searchIndexCollectionIdentifiers = searchIndexCollection.map(searchIndexItem => this.getSearchIndexIdentifier(searchIndexItem));
      const searchIndicesToAdd = searchIndices.filter(searchIndexItem => {
        const searchIndexIdentifier = this.getSearchIndexIdentifier(searchIndexItem);
        if (searchIndexCollectionIdentifiers.includes(searchIndexIdentifier)) {
          return false;
        }
        searchIndexCollectionIdentifiers.push(searchIndexIdentifier);
        return true;
      });
      return [...searchIndicesToAdd, ...searchIndexCollection];
    }
    return searchIndexCollection;
  }

  protected convertDateFromClient<T extends ISearchIndex | NewSearchIndex | PartialUpdateSearchIndex>(searchIndex: T): RestOf<T> {
    return {
      ...searchIndex,
      indexedDate: searchIndex.indexedDate?.toJSON() ?? null,
      lastUpdated: searchIndex.lastUpdated?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSearchIndex: RestSearchIndex): ISearchIndex {
    return {
      ...restSearchIndex,
      indexedDate: restSearchIndex.indexedDate ? dayjs(restSearchIndex.indexedDate) : undefined,
      lastUpdated: restSearchIndex.lastUpdated ? dayjs(restSearchIndex.lastUpdated) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSearchIndex>): HttpResponse<ISearchIndex> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSearchIndex[]>): HttpResponse<ISearchIndex[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
