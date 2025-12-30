import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMetaFolder } from '../meta-folder.model';
import { MetaFolderService } from '../service/meta-folder.service';

const metaFolderResolve = (route: ActivatedRouteSnapshot): Observable<null | IMetaFolder> => {
  const id = route.params.id;
  if (id) {
    return inject(MetaFolderService)
      .find(id)
      .pipe(
        mergeMap((metaFolder: HttpResponse<IMetaFolder>) => {
          if (metaFolder.body) {
            return of(metaFolder.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default metaFolderResolve;
