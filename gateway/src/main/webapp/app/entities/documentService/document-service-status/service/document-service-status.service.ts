import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { isPresent } from 'app/core/util/operators';
import { IDocumentServiceStatus, NewDocumentServiceStatus } from '../document-service-status.model';

export type PartialUpdateDocumentServiceStatus = Partial<IDocumentServiceStatus> & Pick<IDocumentServiceStatus, 'id'>;

type RestOf<T extends IDocumentServiceStatus | NewDocumentServiceStatus> = Omit<
  T,
  'lastProcessedDate' | 'processingStartDate' | 'processingEndDate' | 'updatedDate'
> & {
  lastProcessedDate?: string | null;
  processingStartDate?: string | null;
  processingEndDate?: string | null;
  updatedDate?: string | null;
};

export type RestDocumentServiceStatus = RestOf<IDocumentServiceStatus>;

export type NewRestDocumentServiceStatus = RestOf<NewDocumentServiceStatus>;

export type PartialUpdateRestDocumentServiceStatus = RestOf<PartialUpdateDocumentServiceStatus>;

export type EntityResponseType = HttpResponse<IDocumentServiceStatus>;
export type EntityArrayResponseType = HttpResponse<IDocumentServiceStatus[]>;

@Injectable({ providedIn: 'root' })
export class DocumentServiceStatusService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-service-statuses', 'documentservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/document-service-statuses/_search', 'documentservice');

  create(documentServiceStatus: NewDocumentServiceStatus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentServiceStatus);
    return this.http
      .post<RestDocumentServiceStatus>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(documentServiceStatus: IDocumentServiceStatus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentServiceStatus);
    return this.http
      .put<RestDocumentServiceStatus>(
        `${this.resourceUrl}/${encodeURIComponent(this.getDocumentServiceStatusIdentifier(documentServiceStatus))}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(documentServiceStatus: PartialUpdateDocumentServiceStatus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentServiceStatus);
    return this.http
      .patch<RestDocumentServiceStatus>(
        `${this.resourceUrl}/${encodeURIComponent(this.getDocumentServiceStatusIdentifier(documentServiceStatus))}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDocumentServiceStatus>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDocumentServiceStatus[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestDocumentServiceStatus[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IDocumentServiceStatus[]>()], asapScheduler)),
    );
  }

  getDocumentServiceStatusIdentifier(documentServiceStatus: Pick<IDocumentServiceStatus, 'id'>): number {
    return documentServiceStatus.id;
  }

  compareDocumentServiceStatus(o1: Pick<IDocumentServiceStatus, 'id'> | null, o2: Pick<IDocumentServiceStatus, 'id'> | null): boolean {
    return o1 && o2 ? this.getDocumentServiceStatusIdentifier(o1) === this.getDocumentServiceStatusIdentifier(o2) : o1 === o2;
  }

  addDocumentServiceStatusToCollectionIfMissing<Type extends Pick<IDocumentServiceStatus, 'id'>>(
    documentServiceStatusCollection: Type[],
    ...documentServiceStatusesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const documentServiceStatuses: Type[] = documentServiceStatusesToCheck.filter(isPresent);
    if (documentServiceStatuses.length > 0) {
      const documentServiceStatusCollectionIdentifiers = documentServiceStatusCollection.map(documentServiceStatusItem =>
        this.getDocumentServiceStatusIdentifier(documentServiceStatusItem),
      );
      const documentServiceStatusesToAdd = documentServiceStatuses.filter(documentServiceStatusItem => {
        const documentServiceStatusIdentifier = this.getDocumentServiceStatusIdentifier(documentServiceStatusItem);
        if (documentServiceStatusCollectionIdentifiers.includes(documentServiceStatusIdentifier)) {
          return false;
        }
        documentServiceStatusCollectionIdentifiers.push(documentServiceStatusIdentifier);
        return true;
      });
      return [...documentServiceStatusesToAdd, ...documentServiceStatusCollection];
    }
    return documentServiceStatusCollection;
  }

  protected convertDateFromClient<T extends IDocumentServiceStatus | NewDocumentServiceStatus | PartialUpdateDocumentServiceStatus>(
    documentServiceStatus: T,
  ): RestOf<T> {
    return {
      ...documentServiceStatus,
      lastProcessedDate: documentServiceStatus.lastProcessedDate?.toJSON() ?? null,
      processingStartDate: documentServiceStatus.processingStartDate?.toJSON() ?? null,
      processingEndDate: documentServiceStatus.processingEndDate?.toJSON() ?? null,
      updatedDate: documentServiceStatus.updatedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDocumentServiceStatus: RestDocumentServiceStatus): IDocumentServiceStatus {
    return {
      ...restDocumentServiceStatus,
      lastProcessedDate: restDocumentServiceStatus.lastProcessedDate ? dayjs(restDocumentServiceStatus.lastProcessedDate) : undefined,
      processingStartDate: restDocumentServiceStatus.processingStartDate ? dayjs(restDocumentServiceStatus.processingStartDate) : undefined,
      processingEndDate: restDocumentServiceStatus.processingEndDate ? dayjs(restDocumentServiceStatus.processingEndDate) : undefined,
      updatedDate: restDocumentServiceStatus.updatedDate ? dayjs(restDocumentServiceStatus.updatedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDocumentServiceStatus>): HttpResponse<IDocumentServiceStatus> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDocumentServiceStatus[]>): HttpResponse<IDocumentServiceStatus[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
