import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWorkflowStep, NewWorkflowStep } from '../workflow-step.model';

export type PartialUpdateWorkflowStep = Partial<IWorkflowStep> & Pick<IWorkflowStep, 'id'>;

export type EntityResponseType = HttpResponse<IWorkflowStep>;
export type EntityArrayResponseType = HttpResponse<IWorkflowStep[]>;

@Injectable({ providedIn: 'root' })
export class WorkflowStepService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/workflow-steps', 'workflowservice');

  create(workflowStep: NewWorkflowStep): Observable<EntityResponseType> {
    return this.http.post<IWorkflowStep>(this.resourceUrl, workflowStep, { observe: 'response' });
  }

  update(workflowStep: IWorkflowStep): Observable<EntityResponseType> {
    return this.http.put<IWorkflowStep>(`${this.resourceUrl}/${this.getWorkflowStepIdentifier(workflowStep)}`, workflowStep, {
      observe: 'response',
    });
  }

  partialUpdate(workflowStep: PartialUpdateWorkflowStep): Observable<EntityResponseType> {
    return this.http.patch<IWorkflowStep>(`${this.resourceUrl}/${this.getWorkflowStepIdentifier(workflowStep)}`, workflowStep, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWorkflowStep>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWorkflowStep[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getWorkflowStepIdentifier(workflowStep: Pick<IWorkflowStep, 'id'>): number {
    return workflowStep.id;
  }

  compareWorkflowStep(o1: Pick<IWorkflowStep, 'id'> | null, o2: Pick<IWorkflowStep, 'id'> | null): boolean {
    return o1 && o2 ? this.getWorkflowStepIdentifier(o1) === this.getWorkflowStepIdentifier(o2) : o1 === o2;
  }

  addWorkflowStepToCollectionIfMissing<Type extends Pick<IWorkflowStep, 'id'>>(
    workflowStepCollection: Type[],
    ...workflowStepsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const workflowSteps: Type[] = workflowStepsToCheck.filter(isPresent);
    if (workflowSteps.length > 0) {
      const workflowStepCollectionIdentifiers = workflowStepCollection.map(workflowStepItem =>
        this.getWorkflowStepIdentifier(workflowStepItem),
      );
      const workflowStepsToAdd = workflowSteps.filter(workflowStepItem => {
        const workflowStepIdentifier = this.getWorkflowStepIdentifier(workflowStepItem);
        if (workflowStepCollectionIdentifiers.includes(workflowStepIdentifier)) {
          return false;
        }
        workflowStepCollectionIdentifiers.push(workflowStepIdentifier);
        return true;
      });
      return [...workflowStepsToAdd, ...workflowStepCollection];
    }
    return workflowStepCollection;
  }
}
