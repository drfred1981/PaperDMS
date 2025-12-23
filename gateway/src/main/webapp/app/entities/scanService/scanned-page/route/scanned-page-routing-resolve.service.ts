import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IScannedPage } from '../scanned-page.model';
import { ScannedPageService } from '../service/scanned-page.service';

const scannedPageResolve = (route: ActivatedRouteSnapshot): Observable<null | IScannedPage> => {
  const id = route.params.id;
  if (id) {
    return inject(ScannedPageService)
      .find(id)
      .pipe(
        mergeMap((scannedPage: HttpResponse<IScannedPage>) => {
          if (scannedPage.body) {
            return of(scannedPage.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default scannedPageResolve;
