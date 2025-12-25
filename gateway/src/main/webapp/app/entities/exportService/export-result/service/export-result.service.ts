import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExportResult, NewExportResult } from '../export-result.model';

export type PartialUpdateExportResult = Partial<IExportResult> & Pick<IExportResult, 'id'>;

type RestOf<T extends IExportResult | NewExportResult> = Omit<T, 'exportedDate'> & {
  exportedDate?: string | null;
};

export type RestExportResult = RestOf<IExportResult>;

export type NewRestExportResult = RestOf<NewExportResult>;

export type PartialUpdateRestExportResult = RestOf<PartialUpdateExportResult>;

export type EntityResponseType = HttpResponse<IExportResult>;
export type EntityArrayResponseType = HttpResponse<IExportResult[]>;

@Injectable({ providedIn: 'root' })
export class ExportResultService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/export-results', 'exportservice');

  create(exportResult: NewExportResult): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exportResult);
    return this.http
      .post<RestExportResult>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(exportResult: IExportResult): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exportResult);
    return this.http
      .put<RestExportResult>(`${this.resourceUrl}/${this.getExportResultIdentifier(exportResult)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(exportResult: PartialUpdateExportResult): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exportResult);
    return this.http
      .patch<RestExportResult>(`${this.resourceUrl}/${this.getExportResultIdentifier(exportResult)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestExportResult>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestExportResult[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getExportResultIdentifier(exportResult: Pick<IExportResult, 'id'>): number {
    return exportResult.id;
  }

  compareExportResult(o1: Pick<IExportResult, 'id'> | null, o2: Pick<IExportResult, 'id'> | null): boolean {
    return o1 && o2 ? this.getExportResultIdentifier(o1) === this.getExportResultIdentifier(o2) : o1 === o2;
  }

  addExportResultToCollectionIfMissing<Type extends Pick<IExportResult, 'id'>>(
    exportResultCollection: Type[],
    ...exportResultsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const exportResults: Type[] = exportResultsToCheck.filter(isPresent);
    if (exportResults.length > 0) {
      const exportResultCollectionIdentifiers = exportResultCollection.map(exportResultItem =>
        this.getExportResultIdentifier(exportResultItem),
      );
      const exportResultsToAdd = exportResults.filter(exportResultItem => {
        const exportResultIdentifier = this.getExportResultIdentifier(exportResultItem);
        if (exportResultCollectionIdentifiers.includes(exportResultIdentifier)) {
          return false;
        }
        exportResultCollectionIdentifiers.push(exportResultIdentifier);
        return true;
      });
      return [...exportResultsToAdd, ...exportResultCollection];
    }
    return exportResultCollection;
  }

  protected convertDateFromClient<T extends IExportResult | NewExportResult | PartialUpdateExportResult>(exportResult: T): RestOf<T> {
    return {
      ...exportResult,
      exportedDate: exportResult.exportedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restExportResult: RestExportResult): IExportResult {
    return {
      ...restExportResult,
      exportedDate: restExportResult.exportedDate ? dayjs(restExportResult.exportedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestExportResult>): HttpResponse<IExportResult> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestExportResult[]>): HttpResponse<IExportResult[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
