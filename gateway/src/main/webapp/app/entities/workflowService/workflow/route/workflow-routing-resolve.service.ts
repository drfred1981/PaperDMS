import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWorkflow } from '../workflow.model';
import { WorkflowService } from '../service/workflow.service';

const workflowResolve = (route: ActivatedRouteSnapshot): Observable<null | IWorkflow> => {
  const id = route.params.id;
  if (id) {
    return inject(WorkflowService)
      .find(id)
      .pipe(
        mergeMap((workflow: HttpResponse<IWorkflow>) => {
          if (workflow.body) {
            return of(workflow.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default workflowResolve;
