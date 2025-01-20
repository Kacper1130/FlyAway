import {Component, Input} from '@angular/core';
import {CurrencyPipe, DatePipe, NgForOf} from "@angular/common";
import {FlightDetailsDto} from "../../../../../services/models/flight-details-dto";
import {MatButton} from "@angular/material/button";
import {RouterLink, RouterLinkActive} from "@angular/router";

@Component({
  selector: 'app-employee-flight-card',
  standalone: true,
  imports: [
    DatePipe,
    CurrencyPipe,
    NgForOf,
    MatButton,
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './employee-flight-card.component.html',
  styleUrl: './employee-flight-card.component.scss'
})
export class EmployeeFlightCardComponent {
  @Input() flight!: FlightDetailsDto;

  get flightDuration(): string {
    if (!this.flight) {
      return '';
    }

    const departure = new Date(this.flight.departureDate);
    const arrival = new Date(this.flight.arrivalDate);

    const durationMs = arrival.getTime() - departure.getTime();
    const hours = Math.floor(durationMs / (1000 * 60 * 60));
    const minutes = Math.floor((durationMs % (1000 * 60 * 60)) / (1000 * 60));

    return `${hours}h ${minutes}m`;
  }

  get cabinClassPrices() {
    return Object.keys(this.flight.cabinClassPrices).map(key => ({
      className: key,
      price: this.flight.cabinClassPrices[key]
    }));
  }

}
