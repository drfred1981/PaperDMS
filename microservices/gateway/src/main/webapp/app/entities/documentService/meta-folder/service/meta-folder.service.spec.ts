import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMetaFolder } from '../meta-folder.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../meta-folder.test-samples';

import { MetaFolderService, RestMetaFolder } from './meta-folder.service';

const requireRestSample: RestMetaFolder = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('MetaFolder Service', () => {
  let service: MetaFolderService;
  let httpMock: HttpTestingController;
  let expectedResult: IMetaFolder | IMetaFolder[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MetaFolderService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a MetaFolder', () => {
      const metaFolder = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(metaFolder).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MetaFolder', () => {
      const metaFolder = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(metaFolder).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MetaFolder', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MetaFolder', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MetaFolder', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a MetaFolder', () => {
      const queryObject: any = {
        page: 0,
        size: 20,
        query: '',
        sort: [],
      };
      service.search(queryObject).subscribe(() => expectedResult);

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(null, { status: 500, statusText: 'Internal Server Error' });
      expect(expectedResult).toBe(null);
    });

    describe('addMetaFolderToCollectionIfMissing', () => {
      it('should add a MetaFolder to an empty array', () => {
        const metaFolder: IMetaFolder = sampleWithRequiredData;
        expectedResult = service.addMetaFolderToCollectionIfMissing([], metaFolder);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(metaFolder);
      });

      it('should not add a MetaFolder to an array that contains it', () => {
        const metaFolder: IMetaFolder = sampleWithRequiredData;
        const metaFolderCollection: IMetaFolder[] = [
          {
            ...metaFolder,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMetaFolderToCollectionIfMissing(metaFolderCollection, metaFolder);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MetaFolder to an array that doesn't contain it", () => {
        const metaFolder: IMetaFolder = sampleWithRequiredData;
        const metaFolderCollection: IMetaFolder[] = [sampleWithPartialData];
        expectedResult = service.addMetaFolderToCollectionIfMissing(metaFolderCollection, metaFolder);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(metaFolder);
      });

      it('should add only unique MetaFolder to an array', () => {
        const metaFolderArray: IMetaFolder[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const metaFolderCollection: IMetaFolder[] = [sampleWithRequiredData];
        expectedResult = service.addMetaFolderToCollectionIfMissing(metaFolderCollection, ...metaFolderArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const metaFolder: IMetaFolder = sampleWithRequiredData;
        const metaFolder2: IMetaFolder = sampleWithPartialData;
        expectedResult = service.addMetaFolderToCollectionIfMissing([], metaFolder, metaFolder2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(metaFolder);
        expect(expectedResult).toContain(metaFolder2);
      });

      it('should accept null and undefined values', () => {
        const metaFolder: IMetaFolder = sampleWithRequiredData;
        expectedResult = service.addMetaFolderToCollectionIfMissing([], null, metaFolder, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(metaFolder);
      });

      it('should return initial array if no MetaFolder is added', () => {
        const metaFolderCollection: IMetaFolder[] = [sampleWithRequiredData];
        expectedResult = service.addMetaFolderToCollectionIfMissing(metaFolderCollection, undefined, null);
        expect(expectedResult).toEqual(metaFolderCollection);
      });
    });

    describe('compareMetaFolder', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMetaFolder(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 18281 };
        const entity2 = null;

        const compareResult1 = service.compareMetaFolder(entity1, entity2);
        const compareResult2 = service.compareMetaFolder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 18281 };
        const entity2 = { id: 27869 };

        const compareResult1 = service.compareMetaFolder(entity1, entity2);
        const compareResult2 = service.compareMetaFolder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 18281 };
        const entity2 = { id: 18281 };

        const compareResult1 = service.compareMetaFolder(entity1, entity2);
        const compareResult2 = service.compareMetaFolder(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
