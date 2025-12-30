import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IEmailImportImportMapping, NewEmailImportImportMapping } from '../email-import-import-mapping.model';

export type PartialUpdateEmailImportImportMapping = Partial<IEmailImportImportMapping> & Pick<IEmailImportImportMapping, 'id'>;

export type EntityResponseType = HttpResponse<IEmailImportImportMapping>;
export type EntityArrayResponseType = HttpResponse<IEmailImportImportMapping[]>;

@Injectable({ providedIn: 'root' })
export class EmailImportImportMappingService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/email-import-import-mappings', 'emailimportdocumentservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor(
    'api/email-import-import-mappings/_search',
    'emailimportdocumentservice',
  );

  create(emailImportImportMapping: NewEmailImportImportMapping): Observable<EntityResponseType> {
    return this.http.post<IEmailImportImportMapping>(this.resourceUrl, emailImportImportMapping, { observe: 'response' });
  }

  update(emailImportImportMapping: IEmailImportImportMapping): Observable<EntityResponseType> {
    return this.http.put<IEmailImportImportMapping>(
      `${this.resourceUrl}/${this.getEmailImportImportMappingIdentifier(emailImportImportMapping)}`,
      emailImportImportMapping,
      { observe: 'response' },
    );
  }

  partialUpdate(emailImportImportMapping: PartialUpdateEmailImportImportMapping): Observable<EntityResponseType> {
    return this.http.patch<IEmailImportImportMapping>(
      `${this.resourceUrl}/${this.getEmailImportImportMappingIdentifier(emailImportImportMapping)}`,
      emailImportImportMapping,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEmailImportImportMapping>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEmailImportImportMapping[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEmailImportImportMapping[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IEmailImportImportMapping[]>()], asapScheduler)));
  }

  getEmailImportImportMappingIdentifier(emailImportImportMapping: Pick<IEmailImportImportMapping, 'id'>): number {
    return emailImportImportMapping.id;
  }

  compareEmailImportImportMapping(
    o1: Pick<IEmailImportImportMapping, 'id'> | null,
    o2: Pick<IEmailImportImportMapping, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getEmailImportImportMappingIdentifier(o1) === this.getEmailImportImportMappingIdentifier(o2) : o1 === o2;
  }

  addEmailImportImportMappingToCollectionIfMissing<Type extends Pick<IEmailImportImportMapping, 'id'>>(
    emailImportImportMappingCollection: Type[],
    ...emailImportImportMappingsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const emailImportImportMappings: Type[] = emailImportImportMappingsToCheck.filter(isPresent);
    if (emailImportImportMappings.length > 0) {
      const emailImportImportMappingCollectionIdentifiers = emailImportImportMappingCollection.map(emailImportImportMappingItem =>
        this.getEmailImportImportMappingIdentifier(emailImportImportMappingItem),
      );
      const emailImportImportMappingsToAdd = emailImportImportMappings.filter(emailImportImportMappingItem => {
        const emailImportImportMappingIdentifier = this.getEmailImportImportMappingIdentifier(emailImportImportMappingItem);
        if (emailImportImportMappingCollectionIdentifiers.includes(emailImportImportMappingIdentifier)) {
          return false;
        }
        emailImportImportMappingCollectionIdentifiers.push(emailImportImportMappingIdentifier);
        return true;
      });
      return [...emailImportImportMappingsToAdd, ...emailImportImportMappingCollection];
    }
    return emailImportImportMappingCollection;
  }
}
