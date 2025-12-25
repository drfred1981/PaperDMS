import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOcrComparison, NewOcrComparison } from '../ocr-comparison.model';

export type PartialUpdateOcrComparison = Partial<IOcrComparison> & Pick<IOcrComparison, 'id'>;

type RestOf<T extends IOcrComparison | NewOcrComparison> = Omit<T, 'selectedDate' | 'comparisonDate'> & {
  selectedDate?: string | null;
  comparisonDate?: string | null;
};

export type RestOcrComparison = RestOf<IOcrComparison>;

export type NewRestOcrComparison = RestOf<NewOcrComparison>;

export type PartialUpdateRestOcrComparison = RestOf<PartialUpdateOcrComparison>;

export type EntityResponseType = HttpResponse<IOcrComparison>;
export type EntityArrayResponseType = HttpResponse<IOcrComparison[]>;

@Injectable({ providedIn: 'root' })
export class OcrComparisonService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ocr-comparisons', 'ocrservice');

  create(ocrComparison: NewOcrComparison): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ocrComparison);
    return this.http
      .post<RestOcrComparison>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(ocrComparison: IOcrComparison): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ocrComparison);
    return this.http
      .put<RestOcrComparison>(`${this.resourceUrl}/${this.getOcrComparisonIdentifier(ocrComparison)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(ocrComparison: PartialUpdateOcrComparison): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ocrComparison);
    return this.http
      .patch<RestOcrComparison>(`${this.resourceUrl}/${this.getOcrComparisonIdentifier(ocrComparison)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestOcrComparison>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOcrComparison[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOcrComparisonIdentifier(ocrComparison: Pick<IOcrComparison, 'id'>): number {
    return ocrComparison.id;
  }

  compareOcrComparison(o1: Pick<IOcrComparison, 'id'> | null, o2: Pick<IOcrComparison, 'id'> | null): boolean {
    return o1 && o2 ? this.getOcrComparisonIdentifier(o1) === this.getOcrComparisonIdentifier(o2) : o1 === o2;
  }

  addOcrComparisonToCollectionIfMissing<Type extends Pick<IOcrComparison, 'id'>>(
    ocrComparisonCollection: Type[],
    ...ocrComparisonsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ocrComparisons: Type[] = ocrComparisonsToCheck.filter(isPresent);
    if (ocrComparisons.length > 0) {
      const ocrComparisonCollectionIdentifiers = ocrComparisonCollection.map(ocrComparisonItem =>
        this.getOcrComparisonIdentifier(ocrComparisonItem),
      );
      const ocrComparisonsToAdd = ocrComparisons.filter(ocrComparisonItem => {
        const ocrComparisonIdentifier = this.getOcrComparisonIdentifier(ocrComparisonItem);
        if (ocrComparisonCollectionIdentifiers.includes(ocrComparisonIdentifier)) {
          return false;
        }
        ocrComparisonCollectionIdentifiers.push(ocrComparisonIdentifier);
        return true;
      });
      return [...ocrComparisonsToAdd, ...ocrComparisonCollection];
    }
    return ocrComparisonCollection;
  }

  protected convertDateFromClient<T extends IOcrComparison | NewOcrComparison | PartialUpdateOcrComparison>(ocrComparison: T): RestOf<T> {
    return {
      ...ocrComparison,
      selectedDate: ocrComparison.selectedDate?.toJSON() ?? null,
      comparisonDate: ocrComparison.comparisonDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restOcrComparison: RestOcrComparison): IOcrComparison {
    return {
      ...restOcrComparison,
      selectedDate: restOcrComparison.selectedDate ? dayjs(restOcrComparison.selectedDate) : undefined,
      comparisonDate: restOcrComparison.comparisonDate ? dayjs(restOcrComparison.comparisonDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestOcrComparison>): HttpResponse<IOcrComparison> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestOcrComparison[]>): HttpResponse<IOcrComparison[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
