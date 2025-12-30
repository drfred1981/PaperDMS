import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IDocumentStatistics, NewDocumentStatistics } from '../document-statistics.model';

export type PartialUpdateDocumentStatistics = Partial<IDocumentStatistics> & Pick<IDocumentStatistics, 'id'>;

type RestOf<T extends IDocumentStatistics | NewDocumentStatistics> = Omit<T, 'lastUpdated'> & {
  lastUpdated?: string | null;
};

export type RestDocumentStatistics = RestOf<IDocumentStatistics>;

export type NewRestDocumentStatistics = RestOf<NewDocumentStatistics>;

export type PartialUpdateRestDocumentStatistics = RestOf<PartialUpdateDocumentStatistics>;

export type EntityResponseType = HttpResponse<IDocumentStatistics>;
export type EntityArrayResponseType = HttpResponse<IDocumentStatistics[]>;

@Injectable({ providedIn: 'root' })
export class DocumentStatisticsService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-statistics', 'documentservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/document-statistics/_search', 'documentservice');

  create(documentStatistics: NewDocumentStatistics): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentStatistics);
    return this.http
      .post<RestDocumentStatistics>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(documentStatistics: IDocumentStatistics): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentStatistics);
    return this.http
      .put<RestDocumentStatistics>(`${this.resourceUrl}/${this.getDocumentStatisticsIdentifier(documentStatistics)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(documentStatistics: PartialUpdateDocumentStatistics): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentStatistics);
    return this.http
      .patch<RestDocumentStatistics>(`${this.resourceUrl}/${this.getDocumentStatisticsIdentifier(documentStatistics)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDocumentStatistics>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDocumentStatistics[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestDocumentStatistics[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IDocumentStatistics[]>()], asapScheduler)),
    );
  }

  getDocumentStatisticsIdentifier(documentStatistics: Pick<IDocumentStatistics, 'id'>): number {
    return documentStatistics.id;
  }

  compareDocumentStatistics(o1: Pick<IDocumentStatistics, 'id'> | null, o2: Pick<IDocumentStatistics, 'id'> | null): boolean {
    return o1 && o2 ? this.getDocumentStatisticsIdentifier(o1) === this.getDocumentStatisticsIdentifier(o2) : o1 === o2;
  }

  addDocumentStatisticsToCollectionIfMissing<Type extends Pick<IDocumentStatistics, 'id'>>(
    documentStatisticsCollection: Type[],
    ...documentStatisticsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const documentStatistics: Type[] = documentStatisticsToCheck.filter(isPresent);
    if (documentStatistics.length > 0) {
      const documentStatisticsCollectionIdentifiers = documentStatisticsCollection.map(documentStatisticsItem =>
        this.getDocumentStatisticsIdentifier(documentStatisticsItem),
      );
      const documentStatisticsToAdd = documentStatistics.filter(documentStatisticsItem => {
        const documentStatisticsIdentifier = this.getDocumentStatisticsIdentifier(documentStatisticsItem);
        if (documentStatisticsCollectionIdentifiers.includes(documentStatisticsIdentifier)) {
          return false;
        }
        documentStatisticsCollectionIdentifiers.push(documentStatisticsIdentifier);
        return true;
      });
      return [...documentStatisticsToAdd, ...documentStatisticsCollection];
    }
    return documentStatisticsCollection;
  }

  protected convertDateFromClient<T extends IDocumentStatistics | NewDocumentStatistics | PartialUpdateDocumentStatistics>(
    documentStatistics: T,
  ): RestOf<T> {
    return {
      ...documentStatistics,
      lastUpdated: documentStatistics.lastUpdated?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDocumentStatistics: RestDocumentStatistics): IDocumentStatistics {
    return {
      ...restDocumentStatistics,
      lastUpdated: restDocumentStatistics.lastUpdated ? dayjs(restDocumentStatistics.lastUpdated) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDocumentStatistics>): HttpResponse<IDocumentStatistics> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDocumentStatistics[]>): HttpResponse<IDocumentStatistics[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
