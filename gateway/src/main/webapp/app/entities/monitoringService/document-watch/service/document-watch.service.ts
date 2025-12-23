import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IDocumentWatch, NewDocumentWatch } from '../document-watch.model';

export type PartialUpdateDocumentWatch = Partial<IDocumentWatch> & Pick<IDocumentWatch, 'id'>;

type RestOf<T extends IDocumentWatch | NewDocumentWatch> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestDocumentWatch = RestOf<IDocumentWatch>;

export type NewRestDocumentWatch = RestOf<NewDocumentWatch>;

export type PartialUpdateRestDocumentWatch = RestOf<PartialUpdateDocumentWatch>;

export type EntityResponseType = HttpResponse<IDocumentWatch>;
export type EntityArrayResponseType = HttpResponse<IDocumentWatch[]>;

@Injectable({ providedIn: 'root' })
export class DocumentWatchService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-watches', 'monitoringservice');

  create(documentWatch: NewDocumentWatch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentWatch);
    return this.http
      .post<RestDocumentWatch>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(documentWatch: IDocumentWatch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentWatch);
    return this.http
      .put<RestDocumentWatch>(`${this.resourceUrl}/${encodeURIComponent(this.getDocumentWatchIdentifier(documentWatch))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(documentWatch: PartialUpdateDocumentWatch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentWatch);
    return this.http
      .patch<RestDocumentWatch>(`${this.resourceUrl}/${encodeURIComponent(this.getDocumentWatchIdentifier(documentWatch))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDocumentWatch>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDocumentWatch[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getDocumentWatchIdentifier(documentWatch: Pick<IDocumentWatch, 'id'>): number {
    return documentWatch.id;
  }

  compareDocumentWatch(o1: Pick<IDocumentWatch, 'id'> | null, o2: Pick<IDocumentWatch, 'id'> | null): boolean {
    return o1 && o2 ? this.getDocumentWatchIdentifier(o1) === this.getDocumentWatchIdentifier(o2) : o1 === o2;
  }

  addDocumentWatchToCollectionIfMissing<Type extends Pick<IDocumentWatch, 'id'>>(
    documentWatchCollection: Type[],
    ...documentWatchesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const documentWatches: Type[] = documentWatchesToCheck.filter(isPresent);
    if (documentWatches.length > 0) {
      const documentWatchCollectionIdentifiers = documentWatchCollection.map(documentWatchItem =>
        this.getDocumentWatchIdentifier(documentWatchItem),
      );
      const documentWatchesToAdd = documentWatches.filter(documentWatchItem => {
        const documentWatchIdentifier = this.getDocumentWatchIdentifier(documentWatchItem);
        if (documentWatchCollectionIdentifiers.includes(documentWatchIdentifier)) {
          return false;
        }
        documentWatchCollectionIdentifiers.push(documentWatchIdentifier);
        return true;
      });
      return [...documentWatchesToAdd, ...documentWatchCollection];
    }
    return documentWatchCollection;
  }

  protected convertDateFromClient<T extends IDocumentWatch | NewDocumentWatch | PartialUpdateDocumentWatch>(documentWatch: T): RestOf<T> {
    return {
      ...documentWatch,
      createdDate: documentWatch.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDocumentWatch: RestDocumentWatch): IDocumentWatch {
    return {
      ...restDocumentWatch,
      createdDate: restDocumentWatch.createdDate ? dayjs(restDocumentWatch.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDocumentWatch>): HttpResponse<IDocumentWatch> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDocumentWatch[]>): HttpResponse<IDocumentWatch[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
