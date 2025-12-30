import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IMonitoringDocumentWatch, NewMonitoringDocumentWatch } from '../monitoring-document-watch.model';

export type PartialUpdateMonitoringDocumentWatch = Partial<IMonitoringDocumentWatch> & Pick<IMonitoringDocumentWatch, 'id'>;

type RestOf<T extends IMonitoringDocumentWatch | NewMonitoringDocumentWatch> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestMonitoringDocumentWatch = RestOf<IMonitoringDocumentWatch>;

export type NewRestMonitoringDocumentWatch = RestOf<NewMonitoringDocumentWatch>;

export type PartialUpdateRestMonitoringDocumentWatch = RestOf<PartialUpdateMonitoringDocumentWatch>;

export type EntityResponseType = HttpResponse<IMonitoringDocumentWatch>;
export type EntityArrayResponseType = HttpResponse<IMonitoringDocumentWatch[]>;

@Injectable({ providedIn: 'root' })
export class MonitoringDocumentWatchService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/monitoring-document-watches', 'monitoringservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor(
    'api/monitoring-document-watches/_search',
    'monitoringservice',
  );

  create(monitoringDocumentWatch: NewMonitoringDocumentWatch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(monitoringDocumentWatch);
    return this.http
      .post<RestMonitoringDocumentWatch>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(monitoringDocumentWatch: IMonitoringDocumentWatch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(monitoringDocumentWatch);
    return this.http
      .put<RestMonitoringDocumentWatch>(`${this.resourceUrl}/${this.getMonitoringDocumentWatchIdentifier(monitoringDocumentWatch)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(monitoringDocumentWatch: PartialUpdateMonitoringDocumentWatch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(monitoringDocumentWatch);
    return this.http
      .patch<RestMonitoringDocumentWatch>(
        `${this.resourceUrl}/${this.getMonitoringDocumentWatchIdentifier(monitoringDocumentWatch)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMonitoringDocumentWatch>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMonitoringDocumentWatch[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestMonitoringDocumentWatch[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IMonitoringDocumentWatch[]>()], asapScheduler)),
    );
  }

  getMonitoringDocumentWatchIdentifier(monitoringDocumentWatch: Pick<IMonitoringDocumentWatch, 'id'>): number {
    return monitoringDocumentWatch.id;
  }

  compareMonitoringDocumentWatch(
    o1: Pick<IMonitoringDocumentWatch, 'id'> | null,
    o2: Pick<IMonitoringDocumentWatch, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getMonitoringDocumentWatchIdentifier(o1) === this.getMonitoringDocumentWatchIdentifier(o2) : o1 === o2;
  }

  addMonitoringDocumentWatchToCollectionIfMissing<Type extends Pick<IMonitoringDocumentWatch, 'id'>>(
    monitoringDocumentWatchCollection: Type[],
    ...monitoringDocumentWatchesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const monitoringDocumentWatches: Type[] = monitoringDocumentWatchesToCheck.filter(isPresent);
    if (monitoringDocumentWatches.length > 0) {
      const monitoringDocumentWatchCollectionIdentifiers = monitoringDocumentWatchCollection.map(monitoringDocumentWatchItem =>
        this.getMonitoringDocumentWatchIdentifier(monitoringDocumentWatchItem),
      );
      const monitoringDocumentWatchesToAdd = monitoringDocumentWatches.filter(monitoringDocumentWatchItem => {
        const monitoringDocumentWatchIdentifier = this.getMonitoringDocumentWatchIdentifier(monitoringDocumentWatchItem);
        if (monitoringDocumentWatchCollectionIdentifiers.includes(monitoringDocumentWatchIdentifier)) {
          return false;
        }
        monitoringDocumentWatchCollectionIdentifiers.push(monitoringDocumentWatchIdentifier);
        return true;
      });
      return [...monitoringDocumentWatchesToAdd, ...monitoringDocumentWatchCollection];
    }
    return monitoringDocumentWatchCollection;
  }

  protected convertDateFromClient<T extends IMonitoringDocumentWatch | NewMonitoringDocumentWatch | PartialUpdateMonitoringDocumentWatch>(
    monitoringDocumentWatch: T,
  ): RestOf<T> {
    return {
      ...monitoringDocumentWatch,
      createdDate: monitoringDocumentWatch.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMonitoringDocumentWatch: RestMonitoringDocumentWatch): IMonitoringDocumentWatch {
    return {
      ...restMonitoringDocumentWatch,
      createdDate: restMonitoringDocumentWatch.createdDate ? dayjs(restMonitoringDocumentWatch.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMonitoringDocumentWatch>): HttpResponse<IMonitoringDocumentWatch> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMonitoringDocumentWatch[]>): HttpResponse<IMonitoringDocumentWatch[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
