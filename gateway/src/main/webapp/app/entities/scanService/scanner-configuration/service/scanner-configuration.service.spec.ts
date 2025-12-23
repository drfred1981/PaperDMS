import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IScannerConfiguration } from '../scanner-configuration.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../scanner-configuration.test-samples';

import { RestScannerConfiguration, ScannerConfigurationService } from './scanner-configuration.service';

const requireRestSample: RestScannerConfiguration = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('ScannerConfiguration Service', () => {
  let service: ScannerConfigurationService;
  let httpMock: HttpTestingController;
  let expectedResult: IScannerConfiguration | IScannerConfiguration[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ScannerConfigurationService);
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

    it('should create a ScannerConfiguration', () => {
      const scannerConfiguration = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(scannerConfiguration).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ScannerConfiguration', () => {
      const scannerConfiguration = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(scannerConfiguration).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ScannerConfiguration', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ScannerConfiguration', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ScannerConfiguration', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addScannerConfigurationToCollectionIfMissing', () => {
      it('should add a ScannerConfiguration to an empty array', () => {
        const scannerConfiguration: IScannerConfiguration = sampleWithRequiredData;
        expectedResult = service.addScannerConfigurationToCollectionIfMissing([], scannerConfiguration);
        expect(expectedResult).toEqual([scannerConfiguration]);
      });

      it('should not add a ScannerConfiguration to an array that contains it', () => {
        const scannerConfiguration: IScannerConfiguration = sampleWithRequiredData;
        const scannerConfigurationCollection: IScannerConfiguration[] = [
          {
            ...scannerConfiguration,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addScannerConfigurationToCollectionIfMissing(scannerConfigurationCollection, scannerConfiguration);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ScannerConfiguration to an array that doesn't contain it", () => {
        const scannerConfiguration: IScannerConfiguration = sampleWithRequiredData;
        const scannerConfigurationCollection: IScannerConfiguration[] = [sampleWithPartialData];
        expectedResult = service.addScannerConfigurationToCollectionIfMissing(scannerConfigurationCollection, scannerConfiguration);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(scannerConfiguration);
      });

      it('should add only unique ScannerConfiguration to an array', () => {
        const scannerConfigurationArray: IScannerConfiguration[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const scannerConfigurationCollection: IScannerConfiguration[] = [sampleWithRequiredData];
        expectedResult = service.addScannerConfigurationToCollectionIfMissing(scannerConfigurationCollection, ...scannerConfigurationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const scannerConfiguration: IScannerConfiguration = sampleWithRequiredData;
        const scannerConfiguration2: IScannerConfiguration = sampleWithPartialData;
        expectedResult = service.addScannerConfigurationToCollectionIfMissing([], scannerConfiguration, scannerConfiguration2);
        expect(expectedResult).toEqual([scannerConfiguration, scannerConfiguration2]);
      });

      it('should accept null and undefined values', () => {
        const scannerConfiguration: IScannerConfiguration = sampleWithRequiredData;
        expectedResult = service.addScannerConfigurationToCollectionIfMissing([], null, scannerConfiguration, undefined);
        expect(expectedResult).toEqual([scannerConfiguration]);
      });

      it('should return initial array if no ScannerConfiguration is added', () => {
        const scannerConfigurationCollection: IScannerConfiguration[] = [sampleWithRequiredData];
        expectedResult = service.addScannerConfigurationToCollectionIfMissing(scannerConfigurationCollection, undefined, null);
        expect(expectedResult).toEqual(scannerConfigurationCollection);
      });
    });

    describe('compareScannerConfiguration', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareScannerConfiguration(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 13848 };
        const entity2 = null;

        const compareResult1 = service.compareScannerConfiguration(entity1, entity2);
        const compareResult2 = service.compareScannerConfiguration(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 13848 };
        const entity2 = { id: 17334 };

        const compareResult1 = service.compareScannerConfiguration(entity1, entity2);
        const compareResult2 = service.compareScannerConfiguration(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 13848 };
        const entity2 = { id: 13848 };

        const compareResult1 = service.compareScannerConfiguration(entity1, entity2);
        const compareResult2 = service.compareScannerConfiguration(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
