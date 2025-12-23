import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IImportRule, NewImportRule } from '../import-rule.model';

export type PartialUpdateImportRule = Partial<IImportRule> & Pick<IImportRule, 'id'>;

type RestOf<T extends IImportRule | NewImportRule> = Omit<T, 'lastMatchDate' | 'createdDate' | 'lastModifiedDate'> & {
  lastMatchDate?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestImportRule = RestOf<IImportRule>;

export type NewRestImportRule = RestOf<NewImportRule>;

export type PartialUpdateRestImportRule = RestOf<PartialUpdateImportRule>;

export type EntityResponseType = HttpResponse<IImportRule>;
export type EntityArrayResponseType = HttpResponse<IImportRule[]>;

@Injectable({ providedIn: 'root' })
export class ImportRuleService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/import-rules', 'emailimportservice');

  create(importRule: NewImportRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(importRule);
    return this.http
      .post<RestImportRule>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(importRule: IImportRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(importRule);
    return this.http
      .put<RestImportRule>(`${this.resourceUrl}/${encodeURIComponent(this.getImportRuleIdentifier(importRule))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(importRule: PartialUpdateImportRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(importRule);
    return this.http
      .patch<RestImportRule>(`${this.resourceUrl}/${encodeURIComponent(this.getImportRuleIdentifier(importRule))}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestImportRule>(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestImportRule[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${encodeURIComponent(id)}`, { observe: 'response' });
  }

  getImportRuleIdentifier(importRule: Pick<IImportRule, 'id'>): number {
    return importRule.id;
  }

  compareImportRule(o1: Pick<IImportRule, 'id'> | null, o2: Pick<IImportRule, 'id'> | null): boolean {
    return o1 && o2 ? this.getImportRuleIdentifier(o1) === this.getImportRuleIdentifier(o2) : o1 === o2;
  }

  addImportRuleToCollectionIfMissing<Type extends Pick<IImportRule, 'id'>>(
    importRuleCollection: Type[],
    ...importRulesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const importRules: Type[] = importRulesToCheck.filter(isPresent);
    if (importRules.length > 0) {
      const importRuleCollectionIdentifiers = importRuleCollection.map(importRuleItem => this.getImportRuleIdentifier(importRuleItem));
      const importRulesToAdd = importRules.filter(importRuleItem => {
        const importRuleIdentifier = this.getImportRuleIdentifier(importRuleItem);
        if (importRuleCollectionIdentifiers.includes(importRuleIdentifier)) {
          return false;
        }
        importRuleCollectionIdentifiers.push(importRuleIdentifier);
        return true;
      });
      return [...importRulesToAdd, ...importRuleCollection];
    }
    return importRuleCollection;
  }

  protected convertDateFromClient<T extends IImportRule | NewImportRule | PartialUpdateImportRule>(importRule: T): RestOf<T> {
    return {
      ...importRule,
      lastMatchDate: importRule.lastMatchDate?.toJSON() ?? null,
      createdDate: importRule.createdDate?.toJSON() ?? null,
      lastModifiedDate: importRule.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restImportRule: RestImportRule): IImportRule {
    return {
      ...restImportRule,
      lastMatchDate: restImportRule.lastMatchDate ? dayjs(restImportRule.lastMatchDate) : undefined,
      createdDate: restImportRule.createdDate ? dayjs(restImportRule.createdDate) : undefined,
      lastModifiedDate: restImportRule.lastModifiedDate ? dayjs(restImportRule.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestImportRule>): HttpResponse<IImportRule> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestImportRule[]>): HttpResponse<IImportRule[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
