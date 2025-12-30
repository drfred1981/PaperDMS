import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IDocumentAudit, NewDocumentAudit } from '../document-audit.model';

export type PartialUpdateDocumentAudit = Partial<IDocumentAudit> & Pick<IDocumentAudit, 'id'>;

type RestOf<T extends IDocumentAudit | NewDocumentAudit> = Omit<T, 'actionDate'> & {
  actionDate?: string | null;
};

export type RestDocumentAudit = RestOf<IDocumentAudit>;

export type NewRestDocumentAudit = RestOf<NewDocumentAudit>;

export type PartialUpdateRestDocumentAudit = RestOf<PartialUpdateDocumentAudit>;

export type EntityResponseType = HttpResponse<IDocumentAudit>;
export type EntityArrayResponseType = HttpResponse<IDocumentAudit[]>;

@Injectable({ providedIn: 'root' })
export class DocumentAuditService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-audits', 'documentservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/document-audits/_search', 'documentservice');

  create(documentAudit: NewDocumentAudit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentAudit);
    return this.http
      .post<RestDocumentAudit>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(documentAudit: IDocumentAudit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentAudit);
    return this.http
      .put<RestDocumentAudit>(`${this.resourceUrl}/${this.getDocumentAuditIdentifier(documentAudit)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(documentAudit: PartialUpdateDocumentAudit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentAudit);
    return this.http
      .patch<RestDocumentAudit>(`${this.resourceUrl}/${this.getDocumentAuditIdentifier(documentAudit)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDocumentAudit>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDocumentAudit[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestDocumentAudit[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IDocumentAudit[]>()], asapScheduler)),
    );
  }

  getDocumentAuditIdentifier(documentAudit: Pick<IDocumentAudit, 'id'>): number {
    return documentAudit.id;
  }

  compareDocumentAudit(o1: Pick<IDocumentAudit, 'id'> | null, o2: Pick<IDocumentAudit, 'id'> | null): boolean {
    return o1 && o2 ? this.getDocumentAuditIdentifier(o1) === this.getDocumentAuditIdentifier(o2) : o1 === o2;
  }

  addDocumentAuditToCollectionIfMissing<Type extends Pick<IDocumentAudit, 'id'>>(
    documentAuditCollection: Type[],
    ...documentAuditsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const documentAudits: Type[] = documentAuditsToCheck.filter(isPresent);
    if (documentAudits.length > 0) {
      const documentAuditCollectionIdentifiers = documentAuditCollection.map(documentAuditItem =>
        this.getDocumentAuditIdentifier(documentAuditItem),
      );
      const documentAuditsToAdd = documentAudits.filter(documentAuditItem => {
        const documentAuditIdentifier = this.getDocumentAuditIdentifier(documentAuditItem);
        if (documentAuditCollectionIdentifiers.includes(documentAuditIdentifier)) {
          return false;
        }
        documentAuditCollectionIdentifiers.push(documentAuditIdentifier);
        return true;
      });
      return [...documentAuditsToAdd, ...documentAuditCollection];
    }
    return documentAuditCollection;
  }

  protected convertDateFromClient<T extends IDocumentAudit | NewDocumentAudit | PartialUpdateDocumentAudit>(documentAudit: T): RestOf<T> {
    return {
      ...documentAudit,
      actionDate: documentAudit.actionDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDocumentAudit: RestDocumentAudit): IDocumentAudit {
    return {
      ...restDocumentAudit,
      actionDate: restDocumentAudit.actionDate ? dayjs(restDocumentAudit.actionDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDocumentAudit>): HttpResponse<IDocumentAudit> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDocumentAudit[]>): HttpResponse<IDocumentAudit[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
