import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMetaPermissionGroup } from '../meta-permission-group.model';
import { MetaPermissionGroupService } from '../service/meta-permission-group.service';

const metaPermissionGroupResolve = (route: ActivatedRouteSnapshot): Observable<null | IMetaPermissionGroup> => {
  const id = route.params.id;
  if (id) {
    return inject(MetaPermissionGroupService)
      .find(id)
      .pipe(
        mergeMap((metaPermissionGroup: HttpResponse<IMetaPermissionGroup>) => {
          if (metaPermissionGroup.body) {
            return of(metaPermissionGroup.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default metaPermissionGroupResolve;
