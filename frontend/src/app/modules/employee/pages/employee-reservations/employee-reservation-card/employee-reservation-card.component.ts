import {Component, Input} from '@angular/core';
import {ReservationDto} from "../../../../../services/models/reservation-dto";
import {Router, RouterLink, RouterLinkActive} from "@angular/router";
import {CurrencyPipe, DatePipe, NgClass} from "@angular/common";
import {MatIcon} from "@angular/material/icon";
import {MatButton} from "@angular/material/button";
import {ReservationSummaryEmployeeDto} from "../../../../../services/models/reservation-summary-employee-dto";

@Component({
  selector: 'app-employee-reservation-card',
  standalone: true,
  imports: [
    RouterLink,
    RouterLinkActive,
    DatePipe,
    CurrencyPipe,
    NgClass,
    MatIcon,
    MatButton
  ],
  templateUrl: './employee-reservation-card.component.html',
  styleUrl: './employee-reservation-card.component.scss'
})
export class EmployeeReservationCardComponent {
  @Input() reservation!: ReservationSummaryEmployeeDto;

  getStatusColor(status: string): string {
    switch (status) {
      case 'ACTIVE': return '#00d733';
      case 'COMPLETED': return '#00ad2d'
      case 'PENDING': return '#d97706';
      default: return '#dc2626';
    }
  }

}
