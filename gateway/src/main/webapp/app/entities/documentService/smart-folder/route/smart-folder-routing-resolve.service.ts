import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { SmartFolderService } from '../service/smart-folder.service';
import { ISmartFolder } from '../smart-folder.model';

const smartFolderResolve = (route: ActivatedRouteSnapshot): Observable<null | ISmartFolder> => {
  const id = route.params.id;
  if (id) {
    return inject(SmartFolderService)
      .find(id)
      .pipe(
        mergeMap((smartFolder: HttpResponse<ISmartFolder>) => {
          if (smartFolder.body) {
            return of(smartFolder.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default smartFolderResolve;
