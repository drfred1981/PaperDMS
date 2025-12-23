import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IImportMapping } from '../import-mapping.model';
import { ImportMappingService } from '../service/import-mapping.service';

const importMappingResolve = (route: ActivatedRouteSnapshot): Observable<null | IImportMapping> => {
  const id = route.params.id;
  if (id) {
    return inject(ImportMappingService)
      .find(id)
      .pipe(
        mergeMap((importMapping: HttpResponse<IImportMapping>) => {
          if (importMapping.body) {
            return of(importMapping.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default importMappingResolve;
