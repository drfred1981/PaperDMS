import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { IContract } from 'app/entities/businessDocService/contract/contract.model';
import { ContractService } from 'app/entities/businessDocService/contract/service/contract.service';
import { ClauseType } from 'app/entities/enumerations/clause-type.model';
import { AlertErrorModel } from 'app/shared/alert/alert-error.model';
import SharedModule from 'app/shared/shared.module';
import { IContractClause } from '../contract-clause.model';
import { ContractClauseService } from '../service/contract-clause.service';

import { ContractClauseFormGroup, ContractClauseFormService } from './contract-clause-form.service';

@Component({
  selector: 'jhi-contract-clause-update',
  templateUrl: './contract-clause-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class ContractClauseUpdate implements OnInit {
  isSaving = false;
  contractClause: IContractClause | null = null;
  clauseTypeValues = Object.keys(ClauseType);

  contractsSharedCollection = signal<IContract[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected contractClauseService = inject(ContractClauseService);
  protected contractClauseFormService = inject(ContractClauseFormService);
  protected contractService = inject(ContractService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ContractClauseFormGroup = this.contractClauseFormService.createContractClauseFormGroup();

  compareContract = (o1: IContract | null, o2: IContract | null): boolean => this.contractService.compareContract(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contractClause }) => {
      this.contractClause = contractClause;
      if (contractClause) {
        this.updateForm(contractClause);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertErrorModel>('gatewayApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contractClause = this.contractClauseFormService.getContractClause(this.editForm);
    if (contractClause.id === null) {
      this.subscribeToSaveResponse(this.contractClauseService.create(contractClause));
    } else {
      this.subscribeToSaveResponse(this.contractClauseService.update(contractClause));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContractClause>>): void {
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

  protected updateForm(contractClause: IContractClause): void {
    this.contractClause = contractClause;
    this.contractClauseFormService.resetForm(this.editForm, contractClause);

    this.contractsSharedCollection.set(
      this.contractService.addContractToCollectionIfMissing<IContract>(this.contractsSharedCollection(), contractClause.contract),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contractService
      .query()
      .pipe(map((res: HttpResponse<IContract[]>) => res.body ?? []))
      .pipe(
        map((contracts: IContract[]) =>
          this.contractService.addContractToCollectionIfMissing<IContract>(contracts, this.contractClause?.contract),
        ),
      )
      .subscribe((contracts: IContract[]) => this.contractsSharedCollection.set(contracts));
  }
}
