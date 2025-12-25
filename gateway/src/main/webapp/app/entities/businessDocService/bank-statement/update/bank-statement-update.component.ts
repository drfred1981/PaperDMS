import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { StatementStatus } from 'app/entities/enumerations/statement-status.model';
import { IBankStatement } from '../bank-statement.model';
import { BankStatementService } from '../service/bank-statement.service';
import { BankStatementFormGroup, BankStatementFormService } from './bank-statement-form.service';

@Component({
  selector: 'jhi-bank-statement-update',
  templateUrl: './bank-statement-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BankStatementUpdateComponent implements OnInit {
  isSaving = false;
  bankStatement: IBankStatement | null = null;
  statementStatusValues = Object.keys(StatementStatus);

  protected bankStatementService = inject(BankStatementService);
  protected bankStatementFormService = inject(BankStatementFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BankStatementFormGroup = this.bankStatementFormService.createBankStatementFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bankStatement }) => {
      this.bankStatement = bankStatement;
      if (bankStatement) {
        this.updateForm(bankStatement);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bankStatement = this.bankStatementFormService.getBankStatement(this.editForm);
    if (bankStatement.id !== null) {
      this.subscribeToSaveResponse(this.bankStatementService.update(bankStatement));
    } else {
      this.subscribeToSaveResponse(this.bankStatementService.create(bankStatement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBankStatement>>): void {
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

  protected updateForm(bankStatement: IBankStatement): void {
    this.bankStatement = bankStatement;
    this.bankStatementFormService.resetForm(this.editForm, bankStatement);
  }
}
