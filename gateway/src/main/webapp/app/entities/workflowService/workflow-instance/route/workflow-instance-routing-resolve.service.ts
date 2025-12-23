import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { WorkflowInstanceService } from '../service/workflow-instance.service';
import { IWorkflowInstance } from '../workflow-instance.model';

const workflowInstanceResolve = (route: ActivatedRouteSnapshot): Observable<null | IWorkflowInstance> => {
  const id = route.params.id;
  if (id) {
    return inject(WorkflowInstanceService)
      .find(id)
      .pipe(
        mergeMap((workflowInstance: HttpResponse<IWorkflowInstance>) => {
          if (workflowInstance.body) {
            return of(workflowInstance.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default workflowInstanceResolve;
