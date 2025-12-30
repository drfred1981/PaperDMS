import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IImageConversionBatch, NewImageConversionBatch } from '../image-conversion-batch.model';

export type PartialUpdateImageConversionBatch = Partial<IImageConversionBatch> & Pick<IImageConversionBatch, 'id'>;

type RestOf<T extends IImageConversionBatch | NewImageConversionBatch> = Omit<T, 'createdAt' | 'startedAt' | 'completedAt'> & {
  createdAt?: string | null;
  startedAt?: string | null;
  completedAt?: string | null;
};

export type RestImageConversionBatch = RestOf<IImageConversionBatch>;

export type NewRestImageConversionBatch = RestOf<NewImageConversionBatch>;

export type PartialUpdateRestImageConversionBatch = RestOf<PartialUpdateImageConversionBatch>;

export type EntityResponseType = HttpResponse<IImageConversionBatch>;
export type EntityArrayResponseType = HttpResponse<IImageConversionBatch[]>;

@Injectable({ providedIn: 'root' })
export class ImageConversionBatchService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/image-conversion-batches', 'pdftoimageservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/image-conversion-batches/_search', 'pdftoimageservice');

  create(imageConversionBatch: NewImageConversionBatch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(imageConversionBatch);
    return this.http
      .post<RestImageConversionBatch>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(imageConversionBatch: IImageConversionBatch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(imageConversionBatch);
    return this.http
      .put<RestImageConversionBatch>(`${this.resourceUrl}/${this.getImageConversionBatchIdentifier(imageConversionBatch)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(imageConversionBatch: PartialUpdateImageConversionBatch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(imageConversionBatch);
    return this.http
      .patch<RestImageConversionBatch>(`${this.resourceUrl}/${this.getImageConversionBatchIdentifier(imageConversionBatch)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestImageConversionBatch>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestImageConversionBatch[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestImageConversionBatch[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IImageConversionBatch[]>()], asapScheduler)),
    );
  }

  getImageConversionBatchIdentifier(imageConversionBatch: Pick<IImageConversionBatch, 'id'>): number {
    return imageConversionBatch.id;
  }

  compareImageConversionBatch(o1: Pick<IImageConversionBatch, 'id'> | null, o2: Pick<IImageConversionBatch, 'id'> | null): boolean {
    return o1 && o2 ? this.getImageConversionBatchIdentifier(o1) === this.getImageConversionBatchIdentifier(o2) : o1 === o2;
  }

  addImageConversionBatchToCollectionIfMissing<Type extends Pick<IImageConversionBatch, 'id'>>(
    imageConversionBatchCollection: Type[],
    ...imageConversionBatchesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const imageConversionBatches: Type[] = imageConversionBatchesToCheck.filter(isPresent);
    if (imageConversionBatches.length > 0) {
      const imageConversionBatchCollectionIdentifiers = imageConversionBatchCollection.map(imageConversionBatchItem =>
        this.getImageConversionBatchIdentifier(imageConversionBatchItem),
      );
      const imageConversionBatchesToAdd = imageConversionBatches.filter(imageConversionBatchItem => {
        const imageConversionBatchIdentifier = this.getImageConversionBatchIdentifier(imageConversionBatchItem);
        if (imageConversionBatchCollectionIdentifiers.includes(imageConversionBatchIdentifier)) {
          return false;
        }
        imageConversionBatchCollectionIdentifiers.push(imageConversionBatchIdentifier);
        return true;
      });
      return [...imageConversionBatchesToAdd, ...imageConversionBatchCollection];
    }
    return imageConversionBatchCollection;
  }

  protected convertDateFromClient<T extends IImageConversionBatch | NewImageConversionBatch | PartialUpdateImageConversionBatch>(
    imageConversionBatch: T,
  ): RestOf<T> {
    return {
      ...imageConversionBatch,
      createdAt: imageConversionBatch.createdAt?.toJSON() ?? null,
      startedAt: imageConversionBatch.startedAt?.toJSON() ?? null,
      completedAt: imageConversionBatch.completedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restImageConversionBatch: RestImageConversionBatch): IImageConversionBatch {
    return {
      ...restImageConversionBatch,
      createdAt: restImageConversionBatch.createdAt ? dayjs(restImageConversionBatch.createdAt) : undefined,
      startedAt: restImageConversionBatch.startedAt ? dayjs(restImageConversionBatch.startedAt) : undefined,
      completedAt: restImageConversionBatch.completedAt ? dayjs(restImageConversionBatch.completedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestImageConversionBatch>): HttpResponse<IImageConversionBatch> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestImageConversionBatch[]>): HttpResponse<IImageConversionBatch[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
