import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFolder } from '../folder.model';
import { FolderService } from '../service/folder.service';

const folderResolve = (route: ActivatedRouteSnapshot): Observable<null | IFolder> => {
  const id = route.params.id;
  if (id) {
    return inject(FolderService)
      .find(id)
      .pipe(
        mergeMap((folder: HttpResponse<IFolder>) => {
          if (folder.body) {
            return of(folder.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default folderResolve;
