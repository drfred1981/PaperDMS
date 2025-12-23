import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApprovalHistory } from '../approval-history.model';
import { ApprovalHistoryService } from '../service/approval-history.service';

const approvalHistoryResolve = (route: ActivatedRouteSnapshot): Observable<null | IApprovalHistory> => {
  const id = route.params.id;
  if (id) {
    return inject(ApprovalHistoryService)
      .find(id)
      .pipe(
        mergeMap((approvalHistory: HttpResponse<IApprovalHistory>) => {
          if (approvalHistory.body) {
            return of(approvalHistory.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default approvalHistoryResolve;
