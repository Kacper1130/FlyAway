import {Component, Input} from '@angular/core';
import {FlightDto} from "../../../../services/models/flight-dto";
import {DatePipe, NgClass} from "@angular/common";

@Component({
  selector: 'app-available-flight',
  standalone: true,
  imports: [
    DatePipe,
    NgClass
  ],
  templateUrl: './available-flight.component.html',
  styleUrl: './available-flight.component.scss'
})
export class AvailableFlightComponent {
  @Input() flight!: FlightDto;

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

}
