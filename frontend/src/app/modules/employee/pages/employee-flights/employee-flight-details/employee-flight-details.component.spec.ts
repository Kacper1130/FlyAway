import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeFlightDetailsComponent } from './employee-flight-details.component';

describe('EmployeeFlightDetailsComponent', () => {
  let component: EmployeeFlightDetailsComponent;
  let fixture: ComponentFixture<EmployeeFlightDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmployeeFlightDetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmployeeFlightDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
