import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentStatistics } from '../document-statistics.model';
import { DocumentStatisticsService } from '../service/document-statistics.service';

const documentStatisticsResolve = (route: ActivatedRouteSnapshot): Observable<null | IDocumentStatistics> => {
  const id = route.params.id;
  if (id) {
    return inject(DocumentStatisticsService)
      .find(id)
      .pipe(
        mergeMap((documentStatistics: HttpResponse<IDocumentStatistics>) => {
          if (documentStatistics.body) {
            return of(documentStatistics.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default documentStatisticsResolve;
