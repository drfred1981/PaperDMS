import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { ISemanticSearch, NewSemanticSearch } from '../semantic-search.model';

export type PartialUpdateSemanticSearch = Partial<ISemanticSearch> & Pick<ISemanticSearch, 'id'>;

type RestOf<T extends ISemanticSearch | NewSemanticSearch> = Omit<T, 'searchDate'> & {
  searchDate?: string | null;
};

export type RestSemanticSearch = RestOf<ISemanticSearch>;

export type NewRestSemanticSearch = RestOf<NewSemanticSearch>;

export type PartialUpdateRestSemanticSearch = RestOf<PartialUpdateSemanticSearch>;

export type EntityResponseType = HttpResponse<ISemanticSearch>;
export type EntityArrayResponseType = HttpResponse<ISemanticSearch[]>;

@Injectable({ providedIn: 'root' })
export class SemanticSearchService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/semantic-searches', 'searchservice');

  create(semanticSearch: NewSemanticSearch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(semanticSearch);
    return this.http
      .post<RestSemanticSearch>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(semanticSearch: ISemanticSearch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(semanticSearch);
    return this.http
      .put<RestSemanticSearch>(`${this.resourceUrl}/${encodeURIComponent(this.getSemanticSearchIdentifier(semanticSearch))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(semanticSearch: PartialUpdateSemanticSearch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(semanticSearch);
    return this.http
      .patch<RestSemanticSearch>(`${this.resourceUrl}/${encodeURIComponent(this.getSemanticSearchIdentifier(semanticSearch))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSemanticSearch>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSemanticSearch[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getSemanticSearchIdentifier(semanticSearch: Pick<ISemanticSearch, 'id'>): number {
    return semanticSearch.id;
  }

  compareSemanticSearch(o1: Pick<ISemanticSearch, 'id'> | null, o2: Pick<ISemanticSearch, 'id'> | null): boolean {
    return o1 && o2 ? this.getSemanticSearchIdentifier(o1) === this.getSemanticSearchIdentifier(o2) : o1 === o2;
  }

  addSemanticSearchToCollectionIfMissing<Type extends Pick<ISemanticSearch, 'id'>>(
    semanticSearchCollection: Type[],
    ...semanticSearchesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const semanticSearches: Type[] = semanticSearchesToCheck.filter(isPresent);
    if (semanticSearches.length > 0) {
      const semanticSearchCollectionIdentifiers = semanticSearchCollection.map(semanticSearchItem =>
        this.getSemanticSearchIdentifier(semanticSearchItem),
      );
      const semanticSearchesToAdd = semanticSearches.filter(semanticSearchItem => {
        const semanticSearchIdentifier = this.getSemanticSearchIdentifier(semanticSearchItem);
        if (semanticSearchCollectionIdentifiers.includes(semanticSearchIdentifier)) {
          return false;
        }
        semanticSearchCollectionIdentifiers.push(semanticSearchIdentifier);
        return true;
      });
      return [...semanticSearchesToAdd, ...semanticSearchCollection];
    }
    return semanticSearchCollection;
  }

  protected convertDateFromClient<T extends ISemanticSearch | NewSemanticSearch | PartialUpdateSemanticSearch>(
    semanticSearch: T,
  ): RestOf<T> {
    return {
      ...semanticSearch,
      searchDate: semanticSearch.searchDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSemanticSearch: RestSemanticSearch): ISemanticSearch {
    return {
      ...restSemanticSearch,
      searchDate: restSemanticSearch.searchDate ? dayjs(restSemanticSearch.searchDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSemanticSearch>): HttpResponse<ISemanticSearch> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSemanticSearch[]>): HttpResponse<ISemanticSearch[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
