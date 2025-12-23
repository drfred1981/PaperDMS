import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBookmark } from '../bookmark.model';
import { BookmarkService } from '../service/bookmark.service';

const bookmarkResolve = (route: ActivatedRouteSnapshot): Observable<null | IBookmark> => {
  const id = route.params.id;
  if (id) {
    return inject(BookmarkService)
      .find(id)
      .pipe(
        mergeMap((bookmark: HttpResponse<IBookmark>) => {
          if (bookmark.body) {
            return of(bookmark.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default bookmarkResolve;
