import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SystemHealthDetailComponent } from './system-health-detail.component';

describe('SystemHealth Management Detail Component', () => {
  let comp: SystemHealthDetailComponent;
  let fixture: ComponentFixture<SystemHealthDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SystemHealthDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./system-health-detail.component').then(m => m.SystemHealthDetailComponent),
              resolve: { systemHealth: () => of({ id: 28359 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SystemHealthDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SystemHealthDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load systemHealth on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SystemHealthDetailComponent);

      // THEN
      expect(instance.systemHealth()).toEqual(expect.objectContaining({ id: 28359 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
