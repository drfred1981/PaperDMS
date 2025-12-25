import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAlertRule, NewAlertRule } from '../alert-rule.model';

export type PartialUpdateAlertRule = Partial<IAlertRule> & Pick<IAlertRule, 'id'>;

type RestOf<T extends IAlertRule | NewAlertRule> = Omit<T, 'lastTriggered' | 'createdDate'> & {
  lastTriggered?: string | null;
  createdDate?: string | null;
};

export type RestAlertRule = RestOf<IAlertRule>;

export type NewRestAlertRule = RestOf<NewAlertRule>;

export type PartialUpdateRestAlertRule = RestOf<PartialUpdateAlertRule>;

export type EntityResponseType = HttpResponse<IAlertRule>;
export type EntityArrayResponseType = HttpResponse<IAlertRule[]>;

@Injectable({ providedIn: 'root' })
export class AlertRuleService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/alert-rules', 'monitoringservice');

  create(alertRule: NewAlertRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alertRule);
    return this.http
      .post<RestAlertRule>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(alertRule: IAlertRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alertRule);
    return this.http
      .put<RestAlertRule>(`${this.resourceUrl}/${this.getAlertRuleIdentifier(alertRule)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(alertRule: PartialUpdateAlertRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alertRule);
    return this.http
      .patch<RestAlertRule>(`${this.resourceUrl}/${this.getAlertRuleIdentifier(alertRule)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAlertRule>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAlertRule[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAlertRuleIdentifier(alertRule: Pick<IAlertRule, 'id'>): number {
    return alertRule.id;
  }

  compareAlertRule(o1: Pick<IAlertRule, 'id'> | null, o2: Pick<IAlertRule, 'id'> | null): boolean {
    return o1 && o2 ? this.getAlertRuleIdentifier(o1) === this.getAlertRuleIdentifier(o2) : o1 === o2;
  }

  addAlertRuleToCollectionIfMissing<Type extends Pick<IAlertRule, 'id'>>(
    alertRuleCollection: Type[],
    ...alertRulesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const alertRules: Type[] = alertRulesToCheck.filter(isPresent);
    if (alertRules.length > 0) {
      const alertRuleCollectionIdentifiers = alertRuleCollection.map(alertRuleItem => this.getAlertRuleIdentifier(alertRuleItem));
      const alertRulesToAdd = alertRules.filter(alertRuleItem => {
        const alertRuleIdentifier = this.getAlertRuleIdentifier(alertRuleItem);
        if (alertRuleCollectionIdentifiers.includes(alertRuleIdentifier)) {
          return false;
        }
        alertRuleCollectionIdentifiers.push(alertRuleIdentifier);
        return true;
      });
      return [...alertRulesToAdd, ...alertRuleCollection];
    }
    return alertRuleCollection;
  }

  protected convertDateFromClient<T extends IAlertRule | NewAlertRule | PartialUpdateAlertRule>(alertRule: T): RestOf<T> {
    return {
      ...alertRule,
      lastTriggered: alertRule.lastTriggered?.toJSON() ?? null,
      createdDate: alertRule.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAlertRule: RestAlertRule): IAlertRule {
    return {
      ...restAlertRule,
      lastTriggered: restAlertRule.lastTriggered ? dayjs(restAlertRule.lastTriggered) : undefined,
      createdDate: restAlertRule.createdDate ? dayjs(restAlertRule.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAlertRule>): HttpResponse<IAlertRule> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAlertRule[]>): HttpResponse<IAlertRule[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
