import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { WorkflowTaskService } from '../service/workflow-task.service';
import { IWorkflowTask } from '../workflow-task.model';

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
