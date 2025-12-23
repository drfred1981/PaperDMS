import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBankStatement } from 'app/entities/businessDocService/bank-statement/bank-statement.model';
import { BankStatementService } from 'app/entities/businessDocService/bank-statement/service/bank-statement.service';
import SharedModule from 'app/shared/shared.module';
import { IBankTransaction } from '../bank-transaction.model';
import { BankTransactionService } from '../service/bank-transaction.service';

import { BankTransactionFormGroup, BankTransactionFormService } from './bank-transaction-form.service';

@Component({
  selector: 'jhi-bank-transaction-update',
  templateUrl: './bank-transaction-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class BankTransactionUpdate implements OnInit {
  isSaving = false;
  bankTransaction: IBankTransaction | null = null;

  bankStatementsSharedCollection = signal<IBankStatement[]>([]);

  protected bankTransactionService = inject(BankTransactionService);
  protected bankTransactionFormService = inject(BankTransactionFormService);
  protected bankStatementService = inject(BankStatementService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BankTransactionFormGroup = this.bankTransactionFormService.createBankTransactionFormGroup();

  compareBankStatement = (o1: IBankStatement | null, o2: IBankStatement | null): boolean =>
    this.bankStatementService.compareBankStatement(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bankTransaction }) => {
      this.bankTransaction = bankTransaction;
      if (bankTransaction) {
        this.updateForm(bankTransaction);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bankTransaction = this.bankTransactionFormService.getBankTransaction(this.editForm);
    if (bankTransaction.id === null) {
      this.subscribeToSaveResponse(this.bankTransactionService.create(bankTransaction));
    } else {
      this.subscribeToSaveResponse(this.bankTransactionService.update(bankTransaction));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBankTransaction>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(bankTransaction: IBankTransaction): void {
    this.bankTransaction = bankTransaction;
    this.bankTransactionFormService.resetForm(this.editForm, bankTransaction);

    this.bankStatementsSharedCollection.set(
      this.bankStatementService.addBankStatementToCollectionIfMissing<IBankStatement>(
        this.bankStatementsSharedCollection(),
        bankTransaction.statement,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.bankStatementService
      .query()
      .pipe(map((res: HttpResponse<IBankStatement[]>) => res.body ?? []))
      .pipe(
        map((bankStatements: IBankStatement[]) =>
          this.bankStatementService.addBankStatementToCollectionIfMissing<IBankStatement>(bankStatements, this.bankTransaction?.statement),
        ),
      )
      .subscribe((bankStatements: IBankStatement[]) => this.bankStatementsSharedCollection.set(bankStatements));
  }
}
