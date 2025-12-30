import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IImageConversionHistory, NewImageConversionHistory } from '../image-conversion-history.model';

export type PartialUpdateImageConversionHistory = Partial<IImageConversionHistory> & Pick<IImageConversionHistory, 'id'>;

type RestOf<T extends IImageConversionHistory | NewImageConversionHistory> = Omit<T, 'archivedAt'> & {
  archivedAt?: string | null;
};

export type RestImageConversionHistory = RestOf<IImageConversionHistory>;

export type NewRestImageConversionHistory = RestOf<NewImageConversionHistory>;

export type PartialUpdateRestImageConversionHistory = RestOf<PartialUpdateImageConversionHistory>;

export type EntityResponseType = HttpResponse<IImageConversionHistory>;
export type EntityArrayResponseType = HttpResponse<IImageConversionHistory[]>;

@Injectable({ providedIn: 'root' })
export class ImageConversionHistoryService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/image-conversion-histories', 'pdftoimageservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/image-conversion-histories/_search', 'pdftoimageservice');

  create(imageConversionHistory: NewImageConversionHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(imageConversionHistory);
    return this.http
      .post<RestImageConversionHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(imageConversionHistory: IImageConversionHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(imageConversionHistory);
    return this.http
      .put<RestImageConversionHistory>(`${this.resourceUrl}/${this.getImageConversionHistoryIdentifier(imageConversionHistory)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(imageConversionHistory: PartialUpdateImageConversionHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(imageConversionHistory);
    return this.http
      .patch<RestImageConversionHistory>(`${this.resourceUrl}/${this.getImageConversionHistoryIdentifier(imageConversionHistory)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestImageConversionHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestImageConversionHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestImageConversionHistory[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IImageConversionHistory[]>()], asapScheduler)),
    );
  }

  getImageConversionHistoryIdentifier(imageConversionHistory: Pick<IImageConversionHistory, 'id'>): number {
    return imageConversionHistory.id;
  }

  compareImageConversionHistory(o1: Pick<IImageConversionHistory, 'id'> | null, o2: Pick<IImageConversionHistory, 'id'> | null): boolean {
    return o1 && o2 ? this.getImageConversionHistoryIdentifier(o1) === this.getImageConversionHistoryIdentifier(o2) : o1 === o2;
  }

  addImageConversionHistoryToCollectionIfMissing<Type extends Pick<IImageConversionHistory, 'id'>>(
    imageConversionHistoryCollection: Type[],
    ...imageConversionHistoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const imageConversionHistories: Type[] = imageConversionHistoriesToCheck.filter(isPresent);
    if (imageConversionHistories.length > 0) {
      const imageConversionHistoryCollectionIdentifiers = imageConversionHistoryCollection.map(imageConversionHistoryItem =>
        this.getImageConversionHistoryIdentifier(imageConversionHistoryItem),
      );
      const imageConversionHistoriesToAdd = imageConversionHistories.filter(imageConversionHistoryItem => {
        const imageConversionHistoryIdentifier = this.getImageConversionHistoryIdentifier(imageConversionHistoryItem);
        if (imageConversionHistoryCollectionIdentifiers.includes(imageConversionHistoryIdentifier)) {
          return false;
        }
        imageConversionHistoryCollectionIdentifiers.push(imageConversionHistoryIdentifier);
        return true;
      });
      return [...imageConversionHistoriesToAdd, ...imageConversionHistoryCollection];
    }
    return imageConversionHistoryCollection;
  }

  protected convertDateFromClient<T extends IImageConversionHistory | NewImageConversionHistory | PartialUpdateImageConversionHistory>(
    imageConversionHistory: T,
  ): RestOf<T> {
    return {
      ...imageConversionHistory,
      archivedAt: imageConversionHistory.archivedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restImageConversionHistory: RestImageConversionHistory): IImageConversionHistory {
    return {
      ...restImageConversionHistory,
      archivedAt: restImageConversionHistory.archivedAt ? dayjs(restImageConversionHistory.archivedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestImageConversionHistory>): HttpResponse<IImageConversionHistory> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestImageConversionHistory[]>): HttpResponse<IImageConversionHistory[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
