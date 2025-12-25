import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmailImport, NewEmailImport } from '../email-import.model';

export type PartialUpdateEmailImport = Partial<IEmailImport> & Pick<IEmailImport, 'id'>;

type RestOf<T extends IEmailImport | NewEmailImport> = Omit<T, 'receivedDate' | 'processedDate'> & {
  receivedDate?: string | null;
  processedDate?: string | null;
};

export type RestEmailImport = RestOf<IEmailImport>;

export type NewRestEmailImport = RestOf<NewEmailImport>;

export type PartialUpdateRestEmailImport = RestOf<PartialUpdateEmailImport>;

export type EntityResponseType = HttpResponse<IEmailImport>;
export type EntityArrayResponseType = HttpResponse<IEmailImport[]>;

@Injectable({ providedIn: 'root' })
export class EmailImportService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/email-imports', 'emailimportservice');

  create(emailImport: NewEmailImport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(emailImport);
    return this.http
      .post<RestEmailImport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(emailImport: IEmailImport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(emailImport);
    return this.http
      .put<RestEmailImport>(`${this.resourceUrl}/${this.getEmailImportIdentifier(emailImport)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(emailImport: PartialUpdateEmailImport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(emailImport);
    return this.http
      .patch<RestEmailImport>(`${this.resourceUrl}/${this.getEmailImportIdentifier(emailImport)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEmailImport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEmailImport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEmailImportIdentifier(emailImport: Pick<IEmailImport, 'id'>): number {
    return emailImport.id;
  }

  compareEmailImport(o1: Pick<IEmailImport, 'id'> | null, o2: Pick<IEmailImport, 'id'> | null): boolean {
    return o1 && o2 ? this.getEmailImportIdentifier(o1) === this.getEmailImportIdentifier(o2) : o1 === o2;
  }

  addEmailImportToCollectionIfMissing<Type extends Pick<IEmailImport, 'id'>>(
    emailImportCollection: Type[],
    ...emailImportsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const emailImports: Type[] = emailImportsToCheck.filter(isPresent);
    if (emailImports.length > 0) {
      const emailImportCollectionIdentifiers = emailImportCollection.map(emailImportItem => this.getEmailImportIdentifier(emailImportItem));
      const emailImportsToAdd = emailImports.filter(emailImportItem => {
        const emailImportIdentifier = this.getEmailImportIdentifier(emailImportItem);
        if (emailImportCollectionIdentifiers.includes(emailImportIdentifier)) {
          return false;
        }
        emailImportCollectionIdentifiers.push(emailImportIdentifier);
        return true;
      });
      return [...emailImportsToAdd, ...emailImportCollection];
    }
    return emailImportCollection;
  }

  protected convertDateFromClient<T extends IEmailImport | NewEmailImport | PartialUpdateEmailImport>(emailImport: T): RestOf<T> {
    return {
      ...emailImport,
      receivedDate: emailImport.receivedDate?.toJSON() ?? null,
      processedDate: emailImport.processedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restEmailImport: RestEmailImport): IEmailImport {
    return {
      ...restEmailImport,
      receivedDate: restEmailImport.receivedDate ? dayjs(restEmailImport.receivedDate) : undefined,
      processedDate: restEmailImport.processedDate ? dayjs(restEmailImport.processedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEmailImport>): HttpResponse<IEmailImport> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEmailImport[]>): HttpResponse<IEmailImport[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
