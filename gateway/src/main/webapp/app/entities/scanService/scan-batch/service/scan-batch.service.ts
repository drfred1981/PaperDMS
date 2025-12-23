import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IScanBatch, NewScanBatch } from '../scan-batch.model';

export type PartialUpdateScanBatch = Partial<IScanBatch> & Pick<IScanBatch, 'id'>;

type RestOf<T extends IScanBatch | NewScanBatch> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestScanBatch = RestOf<IScanBatch>;

export type NewRestScanBatch = RestOf<NewScanBatch>;

export type PartialUpdateRestScanBatch = RestOf<PartialUpdateScanBatch>;

export type EntityResponseType = HttpResponse<IScanBatch>;
export type EntityArrayResponseType = HttpResponse<IScanBatch[]>;

@Injectable({ providedIn: 'root' })
export class ScanBatchService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/scan-batches', 'scanservice');

  create(scanBatch: NewScanBatch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(scanBatch);
    return this.http
      .post<RestScanBatch>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(scanBatch: IScanBatch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(scanBatch);
    return this.http
      .put<RestScanBatch>(`${this.resourceUrl}/${encodeURIComponent(this.getScanBatchIdentifier(scanBatch))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(scanBatch: PartialUpdateScanBatch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(scanBatch);
    return this.http
      .patch<RestScanBatch>(`${this.resourceUrl}/${encodeURIComponent(this.getScanBatchIdentifier(scanBatch))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestScanBatch>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestScanBatch[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getScanBatchIdentifier(scanBatch: Pick<IScanBatch, 'id'>): number {
    return scanBatch.id;
  }

  compareScanBatch(o1: Pick<IScanBatch, 'id'> | null, o2: Pick<IScanBatch, 'id'> | null): boolean {
    return o1 && o2 ? this.getScanBatchIdentifier(o1) === this.getScanBatchIdentifier(o2) : o1 === o2;
  }

  addScanBatchToCollectionIfMissing<Type extends Pick<IScanBatch, 'id'>>(
    scanBatchCollection: Type[],
    ...scanBatchesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const scanBatches: Type[] = scanBatchesToCheck.filter(isPresent);
    if (scanBatches.length > 0) {
      const scanBatchCollectionIdentifiers = scanBatchCollection.map(scanBatchItem => this.getScanBatchIdentifier(scanBatchItem));
      const scanBatchesToAdd = scanBatches.filter(scanBatchItem => {
        const scanBatchIdentifier = this.getScanBatchIdentifier(scanBatchItem);
        if (scanBatchCollectionIdentifiers.includes(scanBatchIdentifier)) {
          return false;
        }
        scanBatchCollectionIdentifiers.push(scanBatchIdentifier);
        return true;
      });
      return [...scanBatchesToAdd, ...scanBatchCollection];
    }
    return scanBatchCollection;
  }

  protected convertDateFromClient<T extends IScanBatch | NewScanBatch | PartialUpdateScanBatch>(scanBatch: T): RestOf<T> {
    return {
      ...scanBatch,
      createdDate: scanBatch.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restScanBatch: RestScanBatch): IScanBatch {
    return {
      ...restScanBatch,
      createdDate: restScanBatch.createdDate ? dayjs(restScanBatch.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestScanBatch>): HttpResponse<IScanBatch> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestScanBatch[]>): HttpResponse<IScanBatch[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
