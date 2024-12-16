import {Component, Input} from '@angular/core';
import {CurrencyPipe, DatePipe, NgClass, NgForOf} from "@angular/common";
import {FlightDto} from "../../../../services/models/flight-dto";

@Component({
  selector: 'app-flight',
  standalone: true,
  imports: [
    DatePipe,
    NgForOf,
    NgClass,
    CurrencyPipe
  ],
  templateUrl: './flight.component.html',
  styleUrl: './flight.component.scss'
})
export class FlightComponent {
  @Input() flight!: FlightDto;

  // Helper methods to get dynamic keys
  aircraftSeatClassKeys(): string[] {
    return Object.keys(this.flight.aircraft.seatClassRanges || {});
  }

  cabinClassPriceKeys(): string[] {
    return Object.keys(this.flight.cabinClassPrices || {});
  }
}
