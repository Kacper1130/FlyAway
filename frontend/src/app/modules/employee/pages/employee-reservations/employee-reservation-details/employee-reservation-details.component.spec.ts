import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeReservationDetailsComponent } from './employee-reservation-details.component';

describe('EmployeeReservationDetailsComponent', () => {
  let component: EmployeeReservationDetailsComponent;
  let fixture: ComponentFixture<EmployeeReservationDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmployeeReservationDetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmployeeReservationDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
