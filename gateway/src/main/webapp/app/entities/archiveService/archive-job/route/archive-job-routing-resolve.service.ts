import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IArchiveJob } from '../archive-job.model';
import { ArchiveJobService } from '../service/archive-job.service';

const archiveJobResolve = (route: ActivatedRouteSnapshot): Observable<null | IArchiveJob> => {
  const id = route.params.id;
  if (id) {
    return inject(ArchiveJobService)
      .find(id)
      .pipe(
        mergeMap((archiveJob: HttpResponse<IArchiveJob>) => {
          if (archiveJob.body) {
            return of(archiveJob.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default archiveJobResolve;
