import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IManualChapter, NewManualChapter } from '../manual-chapter.model';

export type PartialUpdateManualChapter = Partial<IManualChapter> & Pick<IManualChapter, 'id'>;

export type EntityResponseType = HttpResponse<IManualChapter>;
export type EntityArrayResponseType = HttpResponse<IManualChapter[]>;

@Injectable({ providedIn: 'root' })
export class ManualChapterService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/manual-chapters', 'businessdocservice');

  create(manualChapter: NewManualChapter): Observable<EntityResponseType> {
    return this.http.post<IManualChapter>(this.resourceUrl, manualChapter, { observe: 'response' });
  }

  update(manualChapter: IManualChapter): Observable<EntityResponseType> {
    return this.http.put<IManualChapter>(`${this.resourceUrl}/${this.getManualChapterIdentifier(manualChapter)}`, manualChapter, {
      observe: 'response',
    });
  }

  partialUpdate(manualChapter: PartialUpdateManualChapter): Observable<EntityResponseType> {
    return this.http.patch<IManualChapter>(`${this.resourceUrl}/${this.getManualChapterIdentifier(manualChapter)}`, manualChapter, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IManualChapter>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IManualChapter[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getManualChapterIdentifier(manualChapter: Pick<IManualChapter, 'id'>): number {
    return manualChapter.id;
  }

  compareManualChapter(o1: Pick<IManualChapter, 'id'> | null, o2: Pick<IManualChapter, 'id'> | null): boolean {
    return o1 && o2 ? this.getManualChapterIdentifier(o1) === this.getManualChapterIdentifier(o2) : o1 === o2;
  }

  addManualChapterToCollectionIfMissing<Type extends Pick<IManualChapter, 'id'>>(
    manualChapterCollection: Type[],
    ...manualChaptersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const manualChapters: Type[] = manualChaptersToCheck.filter(isPresent);
    if (manualChapters.length > 0) {
      const manualChapterCollectionIdentifiers = manualChapterCollection.map(manualChapterItem =>
        this.getManualChapterIdentifier(manualChapterItem),
      );
      const manualChaptersToAdd = manualChapters.filter(manualChapterItem => {
        const manualChapterIdentifier = this.getManualChapterIdentifier(manualChapterItem);
        if (manualChapterCollectionIdentifiers.includes(manualChapterIdentifier)) {
          return false;
        }
        manualChapterCollectionIdentifiers.push(manualChapterIdentifier);
        return true;
      });
      return [...manualChaptersToAdd, ...manualChapterCollection];
    }
    return manualChapterCollection;
  }
}
