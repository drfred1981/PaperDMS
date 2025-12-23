import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IScannerConfiguration, NewScannerConfiguration } from '../scanner-configuration.model';

export type PartialUpdateScannerConfiguration = Partial<IScannerConfiguration> & Pick<IScannerConfiguration, 'id'>;

type RestOf<T extends IScannerConfiguration | NewScannerConfiguration> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestScannerConfiguration = RestOf<IScannerConfiguration>;

export type NewRestScannerConfiguration = RestOf<NewScannerConfiguration>;

export type PartialUpdateRestScannerConfiguration = RestOf<PartialUpdateScannerConfiguration>;

export type EntityResponseType = HttpResponse<IScannerConfiguration>;
export type EntityArrayResponseType = HttpResponse<IScannerConfiguration[]>;

@Injectable({ providedIn: 'root' })
export class ScannerConfigurationService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/scanner-configurations', 'scanservice');

  create(scannerConfiguration: NewScannerConfiguration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(scannerConfiguration);
    return this.http
      .post<RestScannerConfiguration>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(scannerConfiguration: IScannerConfiguration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(scannerConfiguration);
    return this.http
      .put<RestScannerConfiguration>(
        `${this.resourceUrl}/${encodeURIComponent(this.getScannerConfigurationIdentifier(scannerConfiguration))}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(scannerConfiguration: PartialUpdateScannerConfiguration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(scannerConfiguration);
    return this.http
      .patch<RestScannerConfiguration>(
        `${this.resourceUrl}/${encodeURIComponent(this.getScannerConfigurationIdentifier(scannerConfiguration))}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestScannerConfiguration>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestScannerConfiguration[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getScannerConfigurationIdentifier(scannerConfiguration: Pick<IScannerConfiguration, 'id'>): number {
    return scannerConfiguration.id;
  }

  compareScannerConfiguration(o1: Pick<IScannerConfiguration, 'id'> | null, o2: Pick<IScannerConfiguration, 'id'> | null): boolean {
    return o1 && o2 ? this.getScannerConfigurationIdentifier(o1) === this.getScannerConfigurationIdentifier(o2) : o1 === o2;
  }

  addScannerConfigurationToCollectionIfMissing<Type extends Pick<IScannerConfiguration, 'id'>>(
    scannerConfigurationCollection: Type[],
    ...scannerConfigurationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const scannerConfigurations: Type[] = scannerConfigurationsToCheck.filter(isPresent);
    if (scannerConfigurations.length > 0) {
      const scannerConfigurationCollectionIdentifiers = scannerConfigurationCollection.map(scannerConfigurationItem =>
        this.getScannerConfigurationIdentifier(scannerConfigurationItem),
      );
      const scannerConfigurationsToAdd = scannerConfigurations.filter(scannerConfigurationItem => {
        const scannerConfigurationIdentifier = this.getScannerConfigurationIdentifier(scannerConfigurationItem);
        if (scannerConfigurationCollectionIdentifiers.includes(scannerConfigurationIdentifier)) {
          return false;
        }
        scannerConfigurationCollectionIdentifiers.push(scannerConfigurationIdentifier);
        return true;
      });
      return [...scannerConfigurationsToAdd, ...scannerConfigurationCollection];
    }
    return scannerConfigurationCollection;
  }

  protected convertDateFromClient<T extends IScannerConfiguration | NewScannerConfiguration | PartialUpdateScannerConfiguration>(
    scannerConfiguration: T,
  ): RestOf<T> {
    return {
      ...scannerConfiguration,
      createdDate: scannerConfiguration.createdDate?.toJSON() ?? null,
      lastModifiedDate: scannerConfiguration.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restScannerConfiguration: RestScannerConfiguration): IScannerConfiguration {
    return {
      ...restScannerConfiguration,
      createdDate: restScannerConfiguration.createdDate ? dayjs(restScannerConfiguration.createdDate) : undefined,
      lastModifiedDate: restScannerConfiguration.lastModifiedDate ? dayjs(restScannerConfiguration.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestScannerConfiguration>): HttpResponse<IScannerConfiguration> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestScannerConfiguration[]>): HttpResponse<IScannerConfiguration[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
