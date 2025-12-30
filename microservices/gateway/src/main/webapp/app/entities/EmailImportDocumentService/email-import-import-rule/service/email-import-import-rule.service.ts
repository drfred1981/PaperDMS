import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IEmailImportImportRule, NewEmailImportImportRule } from '../email-import-import-rule.model';

export type PartialUpdateEmailImportImportRule = Partial<IEmailImportImportRule> & Pick<IEmailImportImportRule, 'id'>;

type RestOf<T extends IEmailImportImportRule | NewEmailImportImportRule> = Omit<T, 'lastMatchDate' | 'createdDate' | 'lastModifiedDate'> & {
  lastMatchDate?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestEmailImportImportRule = RestOf<IEmailImportImportRule>;

export type NewRestEmailImportImportRule = RestOf<NewEmailImportImportRule>;

export type PartialUpdateRestEmailImportImportRule = RestOf<PartialUpdateEmailImportImportRule>;

export type EntityResponseType = HttpResponse<IEmailImportImportRule>;
export type EntityArrayResponseType = HttpResponse<IEmailImportImportRule[]>;

@Injectable({ providedIn: 'root' })
export class EmailImportImportRuleService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/email-import-import-rules', 'emailimportdocumentservice');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor(
    'api/email-import-import-rules/_search',
    'emailimportdocumentservice',
  );

  create(emailImportImportRule: NewEmailImportImportRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(emailImportImportRule);
    return this.http
      .post<RestEmailImportImportRule>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(emailImportImportRule: IEmailImportImportRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(emailImportImportRule);
    return this.http
      .put<RestEmailImportImportRule>(`${this.resourceUrl}/${this.getEmailImportImportRuleIdentifier(emailImportImportRule)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(emailImportImportRule: PartialUpdateEmailImportImportRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(emailImportImportRule);
    return this.http
      .patch<RestEmailImportImportRule>(`${this.resourceUrl}/${this.getEmailImportImportRuleIdentifier(emailImportImportRule)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEmailImportImportRule>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEmailImportImportRule[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestEmailImportImportRule[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IEmailImportImportRule[]>()], asapScheduler)),
    );
  }

  getEmailImportImportRuleIdentifier(emailImportImportRule: Pick<IEmailImportImportRule, 'id'>): number {
    return emailImportImportRule.id;
  }

  compareEmailImportImportRule(o1: Pick<IEmailImportImportRule, 'id'> | null, o2: Pick<IEmailImportImportRule, 'id'> | null): boolean {
    return o1 && o2 ? this.getEmailImportImportRuleIdentifier(o1) === this.getEmailImportImportRuleIdentifier(o2) : o1 === o2;
  }

  addEmailImportImportRuleToCollectionIfMissing<Type extends Pick<IEmailImportImportRule, 'id'>>(
    emailImportImportRuleCollection: Type[],
    ...emailImportImportRulesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const emailImportImportRules: Type[] = emailImportImportRulesToCheck.filter(isPresent);
    if (emailImportImportRules.length > 0) {
      const emailImportImportRuleCollectionIdentifiers = emailImportImportRuleCollection.map(emailImportImportRuleItem =>
        this.getEmailImportImportRuleIdentifier(emailImportImportRuleItem),
      );
      const emailImportImportRulesToAdd = emailImportImportRules.filter(emailImportImportRuleItem => {
        const emailImportImportRuleIdentifier = this.getEmailImportImportRuleIdentifier(emailImportImportRuleItem);
        if (emailImportImportRuleCollectionIdentifiers.includes(emailImportImportRuleIdentifier)) {
          return false;
        }
        emailImportImportRuleCollectionIdentifiers.push(emailImportImportRuleIdentifier);
        return true;
      });
      return [...emailImportImportRulesToAdd, ...emailImportImportRuleCollection];
    }
    return emailImportImportRuleCollection;
  }

  protected convertDateFromClient<T extends IEmailImportImportRule | NewEmailImportImportRule | PartialUpdateEmailImportImportRule>(
    emailImportImportRule: T,
  ): RestOf<T> {
    return {
      ...emailImportImportRule,
      lastMatchDate: emailImportImportRule.lastMatchDate?.toJSON() ?? null,
      createdDate: emailImportImportRule.createdDate?.toJSON() ?? null,
      lastModifiedDate: emailImportImportRule.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restEmailImportImportRule: RestEmailImportImportRule): IEmailImportImportRule {
    return {
      ...restEmailImportImportRule,
      lastMatchDate: restEmailImportImportRule.lastMatchDate ? dayjs(restEmailImportImportRule.lastMatchDate) : undefined,
      createdDate: restEmailImportImportRule.createdDate ? dayjs(restEmailImportImportRule.createdDate) : undefined,
      lastModifiedDate: restEmailImportImportRule.lastModifiedDate ? dayjs(restEmailImportImportRule.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEmailImportImportRule>): HttpResponse<IEmailImportImportRule> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEmailImportImportRule[]>): HttpResponse<IEmailImportImportRule[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
