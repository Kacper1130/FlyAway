import {Component, EventEmitter, Input, Output} from '@angular/core';
import {DatePipe, NgClass} from "@angular/common";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {ReservationSummaryClientDto} from "../../../services/models/reservation-summary-client-dto";

@Component({
  selector: 'app-reservation-card',
  standalone: true,
  imports: [
    NgClass,
    DatePipe,
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './reservation-card.component.html',
  styleUrl: './reservation-card.component.scss'
})
export class ReservationCardComponent {
  @Input() reservation!: ReservationSummaryClientDto;
  @Output() cardClick = new EventEmitter<ReservationSummaryClientDto>();

  get duration(): string {
    const departure = new Date(this.reservation.flightDto!.departureDate!);
    const arrival = new Date(this.reservation.flightDto!.arrivalDate!);
    const diff = arrival.getTime() - departure.getTime();
    const hours = Math.floor(diff / (1000 * 60 * 60));
    const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
    return `${hours}h ${minutes}m`;
  }

}
