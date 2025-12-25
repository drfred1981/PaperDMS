import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISystemHealth, NewSystemHealth } from '../system-health.model';

export type PartialUpdateSystemHealth = Partial<ISystemHealth> & Pick<ISystemHealth, 'id'>;

type RestOf<T extends ISystemHealth | NewSystemHealth> = Omit<T, 'lastCheck'> & {
  lastCheck?: string | null;
};

export type RestSystemHealth = RestOf<ISystemHealth>;

export type NewRestSystemHealth = RestOf<NewSystemHealth>;

export type PartialUpdateRestSystemHealth = RestOf<PartialUpdateSystemHealth>;

export type EntityResponseType = HttpResponse<ISystemHealth>;
export type EntityArrayResponseType = HttpResponse<ISystemHealth[]>;

@Injectable({ providedIn: 'root' })
export class SystemHealthService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/system-healths', 'monitoringservice');

  create(systemHealth: NewSystemHealth): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(systemHealth);
    return this.http
      .post<RestSystemHealth>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(systemHealth: ISystemHealth): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(systemHealth);
    return this.http
      .put<RestSystemHealth>(`${this.resourceUrl}/${this.getSystemHealthIdentifier(systemHealth)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(systemHealth: PartialUpdateSystemHealth): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(systemHealth);
    return this.http
      .patch<RestSystemHealth>(`${this.resourceUrl}/${this.getSystemHealthIdentifier(systemHealth)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSystemHealth>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSystemHealth[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSystemHealthIdentifier(systemHealth: Pick<ISystemHealth, 'id'>): number {
    return systemHealth.id;
  }

  compareSystemHealth(o1: Pick<ISystemHealth, 'id'> | null, o2: Pick<ISystemHealth, 'id'> | null): boolean {
    return o1 && o2 ? this.getSystemHealthIdentifier(o1) === this.getSystemHealthIdentifier(o2) : o1 === o2;
  }

  addSystemHealthToCollectionIfMissing<Type extends Pick<ISystemHealth, 'id'>>(
    systemHealthCollection: Type[],
    ...systemHealthsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const systemHealths: Type[] = systemHealthsToCheck.filter(isPresent);
    if (systemHealths.length > 0) {
      const systemHealthCollectionIdentifiers = systemHealthCollection.map(systemHealthItem =>
        this.getSystemHealthIdentifier(systemHealthItem),
      );
      const systemHealthsToAdd = systemHealths.filter(systemHealthItem => {
        const systemHealthIdentifier = this.getSystemHealthIdentifier(systemHealthItem);
        if (systemHealthCollectionIdentifiers.includes(systemHealthIdentifier)) {
          return false;
        }
        systemHealthCollectionIdentifiers.push(systemHealthIdentifier);
        return true;
      });
      return [...systemHealthsToAdd, ...systemHealthCollection];
    }
    return systemHealthCollection;
  }

  protected convertDateFromClient<T extends ISystemHealth | NewSystemHealth | PartialUpdateSystemHealth>(systemHealth: T): RestOf<T> {
    return {
      ...systemHealth,
      lastCheck: systemHealth.lastCheck?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSystemHealth: RestSystemHealth): ISystemHealth {
    return {
      ...restSystemHealth,
      lastCheck: restSystemHealth.lastCheck ? dayjs(restSystemHealth.lastCheck) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSystemHealth>): HttpResponse<ISystemHealth> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSystemHealth[]>): HttpResponse<ISystemHealth[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
