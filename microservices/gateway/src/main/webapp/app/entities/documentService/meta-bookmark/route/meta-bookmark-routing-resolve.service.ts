import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMetaBookmark } from '../meta-bookmark.model';
import { MetaBookmarkService } from '../service/meta-bookmark.service';

const metaBookmarkResolve = (route: ActivatedRouteSnapshot): Observable<null | IMetaBookmark> => {
  const id = route.params.id;
  if (id) {
    return inject(MetaBookmarkService)
      .find(id)
      .pipe(
        mergeMap((metaBookmark: HttpResponse<IMetaBookmark>) => {
          if (metaBookmark.body) {
            return of(metaBookmark.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default metaBookmarkResolve;
