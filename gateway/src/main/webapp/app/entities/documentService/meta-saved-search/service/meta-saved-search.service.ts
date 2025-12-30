import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IMetaSavedSearch, NewMetaSavedSearch } from '../meta-saved-search.model';

export type PartialUpdateMetaSavedSearch = Partial<IMetaSavedSearch> & Pick<IMetaSavedSearch, 'id'>;

type RestOf<T extends IMetaSavedSearch | NewMetaSavedSearch> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestMetaSavedSearch = RestOf<IMetaSavedSearch>;

export type NewRestMetaSavedSearch = RestOf<NewMetaSavedSearch>;

export type PartialUpdateRestMetaSavedSearch = RestOf<PartialUpdateMetaSavedSearch>;

export type EntityResponseType = HttpResponse<IMetaSavedSearch>;
export type EntityArrayResponseType = HttpResponse<IMetaSavedSearch[]>;

@Injectable({ providedIn: 'root' })
export class MetaSavedSearchService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/meta-saved-searches', 'documentservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/meta-saved-searches/_search', 'documentservice');

  create(metaSavedSearch: NewMetaSavedSearch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(metaSavedSearch);
    return this.http
      .post<RestMetaSavedSearch>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(metaSavedSearch: IMetaSavedSearch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(metaSavedSearch);
    return this.http
      .put<RestMetaSavedSearch>(`${this.resourceUrl}/${this.getMetaSavedSearchIdentifier(metaSavedSearch)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(metaSavedSearch: PartialUpdateMetaSavedSearch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(metaSavedSearch);
    return this.http
      .patch<RestMetaSavedSearch>(`${this.resourceUrl}/${this.getMetaSavedSearchIdentifier(metaSavedSearch)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMetaSavedSearch>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMetaSavedSearch[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestMetaSavedSearch[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IMetaSavedSearch[]>()], asapScheduler)),
    );
  }

  getMetaSavedSearchIdentifier(metaSavedSearch: Pick<IMetaSavedSearch, 'id'>): number {
    return metaSavedSearch.id;
  }

  compareMetaSavedSearch(o1: Pick<IMetaSavedSearch, 'id'> | null, o2: Pick<IMetaSavedSearch, 'id'> | null): boolean {
    return o1 && o2 ? this.getMetaSavedSearchIdentifier(o1) === this.getMetaSavedSearchIdentifier(o2) : o1 === o2;
  }

  addMetaSavedSearchToCollectionIfMissing<Type extends Pick<IMetaSavedSearch, 'id'>>(
    metaSavedSearchCollection: Type[],
    ...metaSavedSearchesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const metaSavedSearches: Type[] = metaSavedSearchesToCheck.filter(isPresent);
    if (metaSavedSearches.length > 0) {
      const metaSavedSearchCollectionIdentifiers = metaSavedSearchCollection.map(metaSavedSearchItem =>
        this.getMetaSavedSearchIdentifier(metaSavedSearchItem),
      );
      const metaSavedSearchesToAdd = metaSavedSearches.filter(metaSavedSearchItem => {
        const metaSavedSearchIdentifier = this.getMetaSavedSearchIdentifier(metaSavedSearchItem);
        if (metaSavedSearchCollectionIdentifiers.includes(metaSavedSearchIdentifier)) {
          return false;
        }
        metaSavedSearchCollectionIdentifiers.push(metaSavedSearchIdentifier);
        return true;
      });
      return [...metaSavedSearchesToAdd, ...metaSavedSearchCollection];
    }
    return metaSavedSearchCollection;
  }

  protected convertDateFromClient<T extends IMetaSavedSearch | NewMetaSavedSearch | PartialUpdateMetaSavedSearch>(
    metaSavedSearch: T,
  ): RestOf<T> {
    return {
      ...metaSavedSearch,
      createdDate: metaSavedSearch.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMetaSavedSearch: RestMetaSavedSearch): IMetaSavedSearch {
    return {
      ...restMetaSavedSearch,
      createdDate: restMetaSavedSearch.createdDate ? dayjs(restMetaSavedSearch.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMetaSavedSearch>): HttpResponse<IMetaSavedSearch> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMetaSavedSearch[]>): HttpResponse<IMetaSavedSearch[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
