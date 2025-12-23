import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IImportMapping, NewImportMapping } from '../import-mapping.model';

export type PartialUpdateImportMapping = Partial<IImportMapping> & Pick<IImportMapping, 'id'>;

export type EntityResponseType = HttpResponse<IImportMapping>;
export type EntityArrayResponseType = HttpResponse<IImportMapping[]>;

@Injectable({ providedIn: 'root' })
export class ImportMappingService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/import-mappings', 'emailimportservice');

  create(importMapping: NewImportMapping): Observable<EntityResponseType> {
    return this.http.post<IImportMapping>(this.resourceUrl, importMapping, { observe: 'response' });
  }

  update(importMapping: IImportMapping): Observable<EntityResponseType> {
    return this.http.put<IImportMapping>(
      `${this.resourceUrl}/${encodeURIComponent(this.getImportMappingIdentifier(importMapping))}`,
      importMapping,
      { observe: 'response' },
    );
  }

  partialUpdate(importMapping: PartialUpdateImportMapping): Observable<EntityResponseType> {
    return this.http.patch<IImportMapping>(
      `${this.resourceUrl}/${encodeURIComponent(this.getImportMappingIdentifier(importMapping))}`,
      importMapping,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IImportMapping>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IImportMapping[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getImportMappingIdentifier(importMapping: Pick<IImportMapping, 'id'>): number {
    return importMapping.id;
  }

  compareImportMapping(o1: Pick<IImportMapping, 'id'> | null, o2: Pick<IImportMapping, 'id'> | null): boolean {
    return o1 && o2 ? this.getImportMappingIdentifier(o1) === this.getImportMappingIdentifier(o2) : o1 === o2;
  }

  addImportMappingToCollectionIfMissing<Type extends Pick<IImportMapping, 'id'>>(
    importMappingCollection: Type[],
    ...importMappingsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const importMappings: Type[] = importMappingsToCheck.filter(isPresent);
    if (importMappings.length > 0) {
      const importMappingCollectionIdentifiers = importMappingCollection.map(importMappingItem =>
        this.getImportMappingIdentifier(importMappingItem),
      );
      const importMappingsToAdd = importMappings.filter(importMappingItem => {
        const importMappingIdentifier = this.getImportMappingIdentifier(importMappingItem);
        if (importMappingCollectionIdentifiers.includes(importMappingIdentifier)) {
          return false;
        }
        importMappingCollectionIdentifiers.push(importMappingIdentifier);
        return true;
      });
      return [...importMappingsToAdd, ...importMappingCollection];
    }
    return importMappingCollection;
  }
}
