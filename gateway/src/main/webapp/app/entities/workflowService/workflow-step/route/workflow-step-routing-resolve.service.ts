import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { WorkflowStepService } from '../service/workflow-step.service';
import { IWorkflowStep } from '../workflow-step.model';

const workflowStepResolve = (route: ActivatedRouteSnapshot): Observable<null | IWorkflowStep> => {
  const id = route.params.id;
  if (id) {
    return inject(WorkflowStepService)
      .find(id)
      .pipe(
        mergeMap((workflowStep: HttpResponse<IWorkflowStep>) => {
          if (workflowStep.body) {
            return of(workflowStep.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default workflowStepResolve;
