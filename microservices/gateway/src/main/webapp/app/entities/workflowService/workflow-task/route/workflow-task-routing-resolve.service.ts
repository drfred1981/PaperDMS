import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWorkflowTask } from '../workflow-task.model';
import { WorkflowTaskService } from '../service/workflow-task.service';

const workflowTaskResolve = (route: ActivatedRouteSnapshot): Observable<null | IWorkflowTask> => {
  const id = route.params.id;
  if (id) {
    return inject(WorkflowTaskService)
      .find(id)
      .pipe(
        mergeMap((workflowTask: HttpResponse<IWorkflowTask>) => {
          if (workflowTask.body) {
            return of(workflowTask.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default workflowTaskResolve;
