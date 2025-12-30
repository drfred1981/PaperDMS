import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IMonitoringAlertRule, NewMonitoringAlertRule } from '../monitoring-alert-rule.model';

export type PartialUpdateMonitoringAlertRule = Partial<IMonitoringAlertRule> & Pick<IMonitoringAlertRule, 'id'>;

type RestOf<T extends IMonitoringAlertRule | NewMonitoringAlertRule> = Omit<T, 'lastTriggered' | 'createdDate'> & {
  lastTriggered?: string | null;
  createdDate?: string | null;
};

export type RestMonitoringAlertRule = RestOf<IMonitoringAlertRule>;

export type NewRestMonitoringAlertRule = RestOf<NewMonitoringAlertRule>;

export type PartialUpdateRestMonitoringAlertRule = RestOf<PartialUpdateMonitoringAlertRule>;

export type EntityResponseType = HttpResponse<IMonitoringAlertRule>;
export type EntityArrayResponseType = HttpResponse<IMonitoringAlertRule[]>;

@Injectable({ providedIn: 'root' })
export class MonitoringAlertRuleService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/monitoring-alert-rules', 'monitoringservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/monitoring-alert-rules/_search', 'monitoringservice');

  create(monitoringAlertRule: NewMonitoringAlertRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(monitoringAlertRule);
    return this.http
      .post<RestMonitoringAlertRule>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(monitoringAlertRule: IMonitoringAlertRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(monitoringAlertRule);
    return this.http
      .put<RestMonitoringAlertRule>(`${this.resourceUrl}/${this.getMonitoringAlertRuleIdentifier(monitoringAlertRule)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(monitoringAlertRule: PartialUpdateMonitoringAlertRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(monitoringAlertRule);
    return this.http
      .patch<RestMonitoringAlertRule>(`${this.resourceUrl}/${this.getMonitoringAlertRuleIdentifier(monitoringAlertRule)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMonitoringAlertRule>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMonitoringAlertRule[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestMonitoringAlertRule[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IMonitoringAlertRule[]>()], asapScheduler)),
    );
  }

  getMonitoringAlertRuleIdentifier(monitoringAlertRule: Pick<IMonitoringAlertRule, 'id'>): number {
    return monitoringAlertRule.id;
  }

  compareMonitoringAlertRule(o1: Pick<IMonitoringAlertRule, 'id'> | null, o2: Pick<IMonitoringAlertRule, 'id'> | null): boolean {
    return o1 && o2 ? this.getMonitoringAlertRuleIdentifier(o1) === this.getMonitoringAlertRuleIdentifier(o2) : o1 === o2;
  }

  addMonitoringAlertRuleToCollectionIfMissing<Type extends Pick<IMonitoringAlertRule, 'id'>>(
    monitoringAlertRuleCollection: Type[],
    ...monitoringAlertRulesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const monitoringAlertRules: Type[] = monitoringAlertRulesToCheck.filter(isPresent);
    if (monitoringAlertRules.length > 0) {
      const monitoringAlertRuleCollectionIdentifiers = monitoringAlertRuleCollection.map(monitoringAlertRuleItem =>
        this.getMonitoringAlertRuleIdentifier(monitoringAlertRuleItem),
      );
      const monitoringAlertRulesToAdd = monitoringAlertRules.filter(monitoringAlertRuleItem => {
        const monitoringAlertRuleIdentifier = this.getMonitoringAlertRuleIdentifier(monitoringAlertRuleItem);
        if (monitoringAlertRuleCollectionIdentifiers.includes(monitoringAlertRuleIdentifier)) {
          return false;
        }
        monitoringAlertRuleCollectionIdentifiers.push(monitoringAlertRuleIdentifier);
        return true;
      });
      return [...monitoringAlertRulesToAdd, ...monitoringAlertRuleCollection];
    }
    return monitoringAlertRuleCollection;
  }

  protected convertDateFromClient<T extends IMonitoringAlertRule | NewMonitoringAlertRule | PartialUpdateMonitoringAlertRule>(
    monitoringAlertRule: T,
  ): RestOf<T> {
    return {
      ...monitoringAlertRule,
      lastTriggered: monitoringAlertRule.lastTriggered?.toJSON() ?? null,
      createdDate: monitoringAlertRule.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMonitoringAlertRule: RestMonitoringAlertRule): IMonitoringAlertRule {
    return {
      ...restMonitoringAlertRule,
      lastTriggered: restMonitoringAlertRule.lastTriggered ? dayjs(restMonitoringAlertRule.lastTriggered) : undefined,
      createdDate: restMonitoringAlertRule.createdDate ? dayjs(restMonitoringAlertRule.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMonitoringAlertRule>): HttpResponse<IMonitoringAlertRule> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMonitoringAlertRule[]>): HttpResponse<IMonitoringAlertRule[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
