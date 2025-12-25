import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IScannedPage, NewScannedPage } from '../scanned-page.model';

export type PartialUpdateScannedPage = Partial<IScannedPage> & Pick<IScannedPage, 'id'>;

type RestOf<T extends IScannedPage | NewScannedPage> = Omit<T, 'scannedDate'> & {
  scannedDate?: string | null;
};

export type RestScannedPage = RestOf<IScannedPage>;

export type NewRestScannedPage = RestOf<NewScannedPage>;

export type PartialUpdateRestScannedPage = RestOf<PartialUpdateScannedPage>;

export type EntityResponseType = HttpResponse<IScannedPage>;
export type EntityArrayResponseType = HttpResponse<IScannedPage[]>;

@Injectable({ providedIn: 'root' })
export class ScannedPageService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/scanned-pages', 'scanservice');

  create(scannedPage: NewScannedPage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(scannedPage);
    return this.http
      .post<RestScannedPage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(scannedPage: IScannedPage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(scannedPage);
    return this.http
      .put<RestScannedPage>(`${this.resourceUrl}/${this.getScannedPageIdentifier(scannedPage)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(scannedPage: PartialUpdateScannedPage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(scannedPage);
    return this.http
      .patch<RestScannedPage>(`${this.resourceUrl}/${this.getScannedPageIdentifier(scannedPage)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestScannedPage>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestScannedPage[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getScannedPageIdentifier(scannedPage: Pick<IScannedPage, 'id'>): number {
    return scannedPage.id;
  }

  compareScannedPage(o1: Pick<IScannedPage, 'id'> | null, o2: Pick<IScannedPage, 'id'> | null): boolean {
    return o1 && o2 ? this.getScannedPageIdentifier(o1) === this.getScannedPageIdentifier(o2) : o1 === o2;
  }

  addScannedPageToCollectionIfMissing<Type extends Pick<IScannedPage, 'id'>>(
    scannedPageCollection: Type[],
    ...scannedPagesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const scannedPages: Type[] = scannedPagesToCheck.filter(isPresent);
    if (scannedPages.length > 0) {
      const scannedPageCollectionIdentifiers = scannedPageCollection.map(scannedPageItem => this.getScannedPageIdentifier(scannedPageItem));
      const scannedPagesToAdd = scannedPages.filter(scannedPageItem => {
        const scannedPageIdentifier = this.getScannedPageIdentifier(scannedPageItem);
        if (scannedPageCollectionIdentifiers.includes(scannedPageIdentifier)) {
          return false;
        }
        scannedPageCollectionIdentifiers.push(scannedPageIdentifier);
        return true;
      });
      return [...scannedPagesToAdd, ...scannedPageCollection];
    }
    return scannedPageCollection;
  }

  protected convertDateFromClient<T extends IScannedPage | NewScannedPage | PartialUpdateScannedPage>(scannedPage: T): RestOf<T> {
    return {
      ...scannedPage,
      scannedDate: scannedPage.scannedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restScannedPage: RestScannedPage): IScannedPage {
    return {
      ...restScannedPage,
      scannedDate: restScannedPage.scannedDate ? dayjs(restScannedPage.scannedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestScannedPage>): HttpResponse<IScannedPage> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestScannedPage[]>): HttpResponse<IScannedPage[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
