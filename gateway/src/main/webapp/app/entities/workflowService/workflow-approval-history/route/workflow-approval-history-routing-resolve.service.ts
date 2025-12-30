import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWorkflowApprovalHistory } from '../workflow-approval-history.model';
import { WorkflowApprovalHistoryService } from '../service/workflow-approval-history.service';

const workflowApprovalHistoryResolve = (route: ActivatedRouteSnapshot): Observable<null | IWorkflowApprovalHistory> => {
  const id = route.params.id;
  if (id) {
    return inject(WorkflowApprovalHistoryService)
      .find(id)
      .pipe(
        mergeMap((workflowApprovalHistory: HttpResponse<IWorkflowApprovalHistory>) => {
          if (workflowApprovalHistory.body) {
            return of(workflowApprovalHistory.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default workflowApprovalHistoryResolve;
