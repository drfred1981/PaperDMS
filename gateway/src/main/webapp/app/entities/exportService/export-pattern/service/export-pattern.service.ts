import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IExportPattern, NewExportPattern } from '../export-pattern.model';

export type PartialUpdateExportPattern = Partial<IExportPattern> & Pick<IExportPattern, 'id'>;

type RestOf<T extends IExportPattern | NewExportPattern> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestExportPattern = RestOf<IExportPattern>;

export type NewRestExportPattern = RestOf<NewExportPattern>;

export type PartialUpdateRestExportPattern = RestOf<PartialUpdateExportPattern>;

export type EntityResponseType = HttpResponse<IExportPattern>;
export type EntityArrayResponseType = HttpResponse<IExportPattern[]>;

@Injectable({ providedIn: 'root' })
export class ExportPatternService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/export-patterns', 'exportservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/export-patterns/_search', 'exportservice');

  create(exportPattern: NewExportPattern): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exportPattern);
    return this.http
      .post<RestExportPattern>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(exportPattern: IExportPattern): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exportPattern);
    return this.http
      .put<RestExportPattern>(`${this.resourceUrl}/${this.getExportPatternIdentifier(exportPattern)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(exportPattern: PartialUpdateExportPattern): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exportPattern);
    return this.http
      .patch<RestExportPattern>(`${this.resourceUrl}/${this.getExportPatternIdentifier(exportPattern)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestExportPattern>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestExportPattern[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestExportPattern[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IExportPattern[]>()], asapScheduler)),
    );
  }

  getExportPatternIdentifier(exportPattern: Pick<IExportPattern, 'id'>): number {
    return exportPattern.id;
  }

  compareExportPattern(o1: Pick<IExportPattern, 'id'> | null, o2: Pick<IExportPattern, 'id'> | null): boolean {
    return o1 && o2 ? this.getExportPatternIdentifier(o1) === this.getExportPatternIdentifier(o2) : o1 === o2;
  }

  addExportPatternToCollectionIfMissing<Type extends Pick<IExportPattern, 'id'>>(
    exportPatternCollection: Type[],
    ...exportPatternsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const exportPatterns: Type[] = exportPatternsToCheck.filter(isPresent);
    if (exportPatterns.length > 0) {
      const exportPatternCollectionIdentifiers = exportPatternCollection.map(exportPatternItem =>
        this.getExportPatternIdentifier(exportPatternItem),
      );
      const exportPatternsToAdd = exportPatterns.filter(exportPatternItem => {
        const exportPatternIdentifier = this.getExportPatternIdentifier(exportPatternItem);
        if (exportPatternCollectionIdentifiers.includes(exportPatternIdentifier)) {
          return false;
        }
        exportPatternCollectionIdentifiers.push(exportPatternIdentifier);
        return true;
      });
      return [...exportPatternsToAdd, ...exportPatternCollection];
    }
    return exportPatternCollection;
  }

  protected convertDateFromClient<T extends IExportPattern | NewExportPattern | PartialUpdateExportPattern>(exportPattern: T): RestOf<T> {
    return {
      ...exportPattern,
      createdDate: exportPattern.createdDate?.toJSON() ?? null,
      lastModifiedDate: exportPattern.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restExportPattern: RestExportPattern): IExportPattern {
    return {
      ...restExportPattern,
      createdDate: restExportPattern.createdDate ? dayjs(restExportPattern.createdDate) : undefined,
      lastModifiedDate: restExportPattern.lastModifiedDate ? dayjs(restExportPattern.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestExportPattern>): HttpResponse<IExportPattern> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestExportPattern[]>): HttpResponse<IExportPattern[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
