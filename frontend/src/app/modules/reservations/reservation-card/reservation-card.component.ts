import {Component, EventEmitter, Input, Output} from '@angular/core';
import {CurrencyPipe, DatePipe, NgClass} from "@angular/common";
import {ReservationDto} from "../../../services/models/reservation-dto";

@Component({
  selector: 'app-reservation-card',
  standalone: true,
  imports: [
    NgClass,
    DatePipe,
    CurrencyPipe
  ],
  templateUrl: './reservation-card.component.html',
  styleUrl: './reservation-card.component.scss'
})
export class ReservationCardComponent {
  @Input() reservation!: ReservationDto;
  @Output() cardClick = new EventEmitter<ReservationDto>();

  get duration(): string {
    const departure = new Date(this.reservation.flightDto!.departureDate);
    const arrival = new Date(this.reservation.flightDto!.arrivalDate);
    const diff = arrival.getTime() - departure.getTime();
    const hours = Math.floor(diff / (1000 * 60 * 60));
    const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
    return `${hours}h ${minutes}m`;
  }

  onCardClick(): void {
    this.cardClick.emit(this.reservation);
  }
}
