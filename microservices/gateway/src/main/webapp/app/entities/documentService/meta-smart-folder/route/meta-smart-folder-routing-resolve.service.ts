import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMetaSmartFolder } from '../meta-smart-folder.model';
import { MetaSmartFolderService } from '../service/meta-smart-folder.service';

const metaSmartFolderResolve = (route: ActivatedRouteSnapshot): Observable<null | IMetaSmartFolder> => {
  const id = route.params.id;
  if (id) {
    return inject(MetaSmartFolderService)
      .find(id)
      .pipe(
        mergeMap((metaSmartFolder: HttpResponse<IMetaSmartFolder>) => {
          if (metaSmartFolder.body) {
            return of(metaSmartFolder.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default metaSmartFolderResolve;
