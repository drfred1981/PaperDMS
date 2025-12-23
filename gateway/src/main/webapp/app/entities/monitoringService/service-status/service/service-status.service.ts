import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IServiceStatus, NewServiceStatus } from '../service-status.model';

export type PartialUpdateServiceStatus = Partial<IServiceStatus> & Pick<IServiceStatus, 'id'>;

type RestOf<T extends IServiceStatus | NewServiceStatus> = Omit<T, 'lastPing'> & {
  lastPing?: string | null;
};

export type RestServiceStatus = RestOf<IServiceStatus>;

export type NewRestServiceStatus = RestOf<NewServiceStatus>;

export type PartialUpdateRestServiceStatus = RestOf<PartialUpdateServiceStatus>;

export type EntityResponseType = HttpResponse<IServiceStatus>;
export type EntityArrayResponseType = HttpResponse<IServiceStatus[]>;

@Injectable({ providedIn: 'root' })
export class ServiceStatusService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/service-statuses', 'monitoringservice');

  create(serviceStatus: NewServiceStatus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceStatus);
    return this.http
      .post<RestServiceStatus>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(serviceStatus: IServiceStatus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceStatus);
    return this.http
      .put<RestServiceStatus>(`${this.resourceUrl}/${encodeURIComponent(this.getServiceStatusIdentifier(serviceStatus))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(serviceStatus: PartialUpdateServiceStatus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceStatus);
    return this.http
      .patch<RestServiceStatus>(`${this.resourceUrl}/${encodeURIComponent(this.getServiceStatusIdentifier(serviceStatus))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestServiceStatus>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestServiceStatus[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getServiceStatusIdentifier(serviceStatus: Pick<IServiceStatus, 'id'>): number {
    return serviceStatus.id;
  }

  compareServiceStatus(o1: Pick<IServiceStatus, 'id'> | null, o2: Pick<IServiceStatus, 'id'> | null): boolean {
    return o1 && o2 ? this.getServiceStatusIdentifier(o1) === this.getServiceStatusIdentifier(o2) : o1 === o2;
  }

  addServiceStatusToCollectionIfMissing<Type extends Pick<IServiceStatus, 'id'>>(
    serviceStatusCollection: Type[],
    ...serviceStatusesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const serviceStatuses: Type[] = serviceStatusesToCheck.filter(isPresent);
    if (serviceStatuses.length > 0) {
      const serviceStatusCollectionIdentifiers = serviceStatusCollection.map(serviceStatusItem =>
        this.getServiceStatusIdentifier(serviceStatusItem),
      );
      const serviceStatusesToAdd = serviceStatuses.filter(serviceStatusItem => {
        const serviceStatusIdentifier = this.getServiceStatusIdentifier(serviceStatusItem);
        if (serviceStatusCollectionIdentifiers.includes(serviceStatusIdentifier)) {
          return false;
        }
        serviceStatusCollectionIdentifiers.push(serviceStatusIdentifier);
        return true;
      });
      return [...serviceStatusesToAdd, ...serviceStatusCollection];
    }
    return serviceStatusCollection;
  }

  protected convertDateFromClient<T extends IServiceStatus | NewServiceStatus | PartialUpdateServiceStatus>(serviceStatus: T): RestOf<T> {
    return {
      ...serviceStatus,
      lastPing: serviceStatus.lastPing?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restServiceStatus: RestServiceStatus): IServiceStatus {
    return {
      ...restServiceStatus,
      lastPing: restServiceStatus.lastPing ? dayjs(restServiceStatus.lastPing) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestServiceStatus>): HttpResponse<IServiceStatus> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestServiceStatus[]>): HttpResponse<IServiceStatus[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
