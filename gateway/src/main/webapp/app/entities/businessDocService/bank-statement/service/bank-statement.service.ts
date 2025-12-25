import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBankStatement, NewBankStatement } from '../bank-statement.model';

export type PartialUpdateBankStatement = Partial<IBankStatement> & Pick<IBankStatement, 'id'>;

type RestOf<T extends IBankStatement | NewBankStatement> = Omit<
  T,
  'statementDate' | 'statementPeriodStart' | 'statementPeriodEnd' | 'createdDate'
> & {
  statementDate?: string | null;
  statementPeriodStart?: string | null;
  statementPeriodEnd?: string | null;
  createdDate?: string | null;
};

export type RestBankStatement = RestOf<IBankStatement>;

export type NewRestBankStatement = RestOf<NewBankStatement>;

export type PartialUpdateRestBankStatement = RestOf<PartialUpdateBankStatement>;

export type EntityResponseType = HttpResponse<IBankStatement>;
export type EntityArrayResponseType = HttpResponse<IBankStatement[]>;

@Injectable({ providedIn: 'root' })
export class BankStatementService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bank-statements', 'businessdocservice');

  create(bankStatement: NewBankStatement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bankStatement);
    return this.http
      .post<RestBankStatement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(bankStatement: IBankStatement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bankStatement);
    return this.http
      .put<RestBankStatement>(`${this.resourceUrl}/${this.getBankStatementIdentifier(bankStatement)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(bankStatement: PartialUpdateBankStatement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bankStatement);
    return this.http
      .patch<RestBankStatement>(`${this.resourceUrl}/${this.getBankStatementIdentifier(bankStatement)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBankStatement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBankStatement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBankStatementIdentifier(bankStatement: Pick<IBankStatement, 'id'>): number {
    return bankStatement.id;
  }

  compareBankStatement(o1: Pick<IBankStatement, 'id'> | null, o2: Pick<IBankStatement, 'id'> | null): boolean {
    return o1 && o2 ? this.getBankStatementIdentifier(o1) === this.getBankStatementIdentifier(o2) : o1 === o2;
  }

  addBankStatementToCollectionIfMissing<Type extends Pick<IBankStatement, 'id'>>(
    bankStatementCollection: Type[],
    ...bankStatementsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const bankStatements: Type[] = bankStatementsToCheck.filter(isPresent);
    if (bankStatements.length > 0) {
      const bankStatementCollectionIdentifiers = bankStatementCollection.map(bankStatementItem =>
        this.getBankStatementIdentifier(bankStatementItem),
      );
      const bankStatementsToAdd = bankStatements.filter(bankStatementItem => {
        const bankStatementIdentifier = this.getBankStatementIdentifier(bankStatementItem);
        if (bankStatementCollectionIdentifiers.includes(bankStatementIdentifier)) {
          return false;
        }
        bankStatementCollectionIdentifiers.push(bankStatementIdentifier);
        return true;
      });
      return [...bankStatementsToAdd, ...bankStatementCollection];
    }
    return bankStatementCollection;
  }

  protected convertDateFromClient<T extends IBankStatement | NewBankStatement | PartialUpdateBankStatement>(bankStatement: T): RestOf<T> {
    return {
      ...bankStatement,
      statementDate: bankStatement.statementDate?.format(DATE_FORMAT) ?? null,
      statementPeriodStart: bankStatement.statementPeriodStart?.format(DATE_FORMAT) ?? null,
      statementPeriodEnd: bankStatement.statementPeriodEnd?.format(DATE_FORMAT) ?? null,
      createdDate: bankStatement.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restBankStatement: RestBankStatement): IBankStatement {
    return {
      ...restBankStatement,
      statementDate: restBankStatement.statementDate ? dayjs(restBankStatement.statementDate) : undefined,
      statementPeriodStart: restBankStatement.statementPeriodStart ? dayjs(restBankStatement.statementPeriodStart) : undefined,
      statementPeriodEnd: restBankStatement.statementPeriodEnd ? dayjs(restBankStatement.statementPeriodEnd) : undefined,
      createdDate: restBankStatement.createdDate ? dayjs(restBankStatement.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBankStatement>): HttpResponse<IBankStatement> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBankStatement[]>): HttpResponse<IBankStatement[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
