import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPermissionGroup } from '../permission-group.model';
import { PermissionGroupService } from '../service/permission-group.service';

const permissionGroupResolve = (route: ActivatedRouteSnapshot): Observable<null | IPermissionGroup> => {
  const id = route.params.id;
  if (id) {
    return inject(PermissionGroupService)
      .find(id)
      .pipe(
        mergeMap((permissionGroup: HttpResponse<IPermissionGroup>) => {
          if (permissionGroup.body) {
            return of(permissionGroup.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default permissionGroupResolve;
