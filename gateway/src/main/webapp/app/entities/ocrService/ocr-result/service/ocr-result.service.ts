import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOcrResult, NewOcrResult } from '../ocr-result.model';

export type PartialUpdateOcrResult = Partial<IOcrResult> & Pick<IOcrResult, 'id'>;

type RestOf<T extends IOcrResult | NewOcrResult> = Omit<T, 'processedDate'> & {
  processedDate?: string | null;
};

export type RestOcrResult = RestOf<IOcrResult>;

export type NewRestOcrResult = RestOf<NewOcrResult>;

export type PartialUpdateRestOcrResult = RestOf<PartialUpdateOcrResult>;

export type EntityResponseType = HttpResponse<IOcrResult>;
export type EntityArrayResponseType = HttpResponse<IOcrResult[]>;

@Injectable({ providedIn: 'root' })
export class OcrResultService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ocr-results', 'ocrservice');

  create(ocrResult: NewOcrResult): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ocrResult);
    return this.http
      .post<RestOcrResult>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(ocrResult: IOcrResult): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ocrResult);
    return this.http
      .put<RestOcrResult>(`${this.resourceUrl}/${this.getOcrResultIdentifier(ocrResult)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(ocrResult: PartialUpdateOcrResult): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ocrResult);
    return this.http
      .patch<RestOcrResult>(`${this.resourceUrl}/${this.getOcrResultIdentifier(ocrResult)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestOcrResult>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOcrResult[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOcrResultIdentifier(ocrResult: Pick<IOcrResult, 'id'>): number {
    return ocrResult.id;
  }

  compareOcrResult(o1: Pick<IOcrResult, 'id'> | null, o2: Pick<IOcrResult, 'id'> | null): boolean {
    return o1 && o2 ? this.getOcrResultIdentifier(o1) === this.getOcrResultIdentifier(o2) : o1 === o2;
  }

  addOcrResultToCollectionIfMissing<Type extends Pick<IOcrResult, 'id'>>(
    ocrResultCollection: Type[],
    ...ocrResultsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ocrResults: Type[] = ocrResultsToCheck.filter(isPresent);
    if (ocrResults.length > 0) {
      const ocrResultCollectionIdentifiers = ocrResultCollection.map(ocrResultItem => this.getOcrResultIdentifier(ocrResultItem));
      const ocrResultsToAdd = ocrResults.filter(ocrResultItem => {
        const ocrResultIdentifier = this.getOcrResultIdentifier(ocrResultItem);
        if (ocrResultCollectionIdentifiers.includes(ocrResultIdentifier)) {
          return false;
        }
        ocrResultCollectionIdentifiers.push(ocrResultIdentifier);
        return true;
      });
      return [...ocrResultsToAdd, ...ocrResultCollection];
    }
    return ocrResultCollection;
  }

  protected convertDateFromClient<T extends IOcrResult | NewOcrResult | PartialUpdateOcrResult>(ocrResult: T): RestOf<T> {
    return {
      ...ocrResult,
      processedDate: ocrResult.processedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restOcrResult: RestOcrResult): IOcrResult {
    return {
      ...restOcrResult,
      processedDate: restOcrResult.processedDate ? dayjs(restOcrResult.processedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestOcrResult>): HttpResponse<IOcrResult> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestOcrResult[]>): HttpResponse<IOcrResult[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
