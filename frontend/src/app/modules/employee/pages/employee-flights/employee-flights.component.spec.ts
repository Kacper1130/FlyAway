import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeFlightsComponent } from './employee-flights.component';

describe('EmployeeFlightsComponent', () => {
  let component: EmployeeFlightsComponent;
  let fixture: ComponentFixture<EmployeeFlightsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmployeeFlightsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmployeeFlightsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
