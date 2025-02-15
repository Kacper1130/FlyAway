import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeReservationCardComponent } from './employee-reservation-card.component';

describe('EmployeeReservationCardComponent', () => {
  let component: EmployeeReservationCardComponent;
  let fixture: ComponentFixture<EmployeeReservationCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmployeeReservationCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmployeeReservationCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
