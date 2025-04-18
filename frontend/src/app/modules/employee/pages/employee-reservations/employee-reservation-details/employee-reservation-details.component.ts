import {Component, OnInit} from '@angular/core';
import {ReservationDto} from "../../../../../services/models/reservation-dto";
import {ActivatedRoute, Router} from "@angular/router";
import {EmployeeReservationService} from "../../../../../services/services/employee-reservation.service";
import {CurrencyPipe, DatePipe, NgIf} from "@angular/common";
import {MatIcon} from "@angular/material/icon";
import {MatButton} from "@angular/material/button";
import {EmployeeNavbarComponent} from "../../../components/employee-navbar/employee-navbar.component";
import {ReservationDetailsEmployeeDto} from "../../../../../services/models/reservation-details-employee-dto";

@Component({
  selector: 'app-employee-reservation-details',
  standalone: true,
  imports: [
    DatePipe,
    MatIcon,
    MatButton,
    CurrencyPipe,
    NgIf,
    EmployeeNavbarComponent
  ],
  templateUrl: './employee-reservation-details.component.html',
  styleUrl: './employee-reservation-details.component.scss'
})
export class EmployeeReservationDetailsComponent implements OnInit {
  reservation: ReservationDetailsEmployeeDto | null = null

  constructor(
    private readonly route: ActivatedRoute,
    private readonly reservationService: EmployeeReservationService,
    private readonly router: Router
  ) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const reservationId = params.get('id');
      if (reservationId) {
        this.fetchReservationDetails(reservationId);
      }
    });
  }

  private fetchReservationDetails(reservationId: string) {
    this.reservationService.getReservationDetails1({id: reservationId}).subscribe({
      next: (reservation) => {
        this.reservation = reservation;
      }
    })
  }

  getStatusColor(status: string): string {
    switch (status) {
      case 'ACTIVE': return '#00d733';
      case 'COMPLETED': return '#006817'
      case 'PENDING': return '#d97706';
      default: return '#dc2626';
    }
  }

  cancelReservation() {
    const id = this.route.snapshot.paramMap.get('id')!;
    this.reservationService.cancelReservation({id: id}).subscribe({
      next: () => {
        window.location.reload();
      }
    })
  }

  navigateToFlight() {
    if (this.reservation?.flightDto?.id) {
      this.router.navigate(['/employee/flights', this.reservation.flightDto.id]);
    }
  }
}
