import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ContractStatus } from 'app/entities/enumerations/contract-status.model';
import { ContractType } from 'app/entities/enumerations/contract-type.model';
import SharedModule from 'app/shared/shared.module';
import { IContract } from '../contract.model';
import { ContractService } from '../service/contract.service';

import { ContractFormGroup, ContractFormService } from './contract-form.service';

@Component({
  selector: 'jhi-contract-update',
  templateUrl: './contract-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class ContractUpdate implements OnInit {
  isSaving = false;
  contract: IContract | null = null;
  contractTypeValues = Object.keys(ContractType);
  contractStatusValues = Object.keys(ContractStatus);

  protected contractService = inject(ContractService);
  protected contractFormService = inject(ContractFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ContractFormGroup = this.contractFormService.createContractFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contract }) => {
      this.contract = contract;
      if (contract) {
        this.updateForm(contract);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contract = this.contractFormService.getContract(this.editForm);
    if (contract.id === null) {
      this.subscribeToSaveResponse(this.contractService.create(contract));
    } else {
      this.subscribeToSaveResponse(this.contractService.update(contract));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContract>>): void {
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

  protected updateForm(contract: IContract): void {
    this.contract = contract;
    this.contractFormService.resetForm(this.editForm, contract);
  }
}
