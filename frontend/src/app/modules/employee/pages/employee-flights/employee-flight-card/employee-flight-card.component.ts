import {Component, Input} from '@angular/core';
import {CurrencyPipe, DatePipe, NgIf} from "@angular/common";
import {FlightDetailsDto} from "../../../../../services/models/flight-details-dto";
import {RouterLink} from "@angular/router";
import {MatIcon} from "@angular/material/icon";

@Component({
  selector: 'app-employee-flight-card',
  standalone: true,
  imports: [
    DatePipe,
    CurrencyPipe,
    RouterLink,
    MatIcon,
    NgIf
  ],
  templateUrl: './employee-flight-card.component.html',
  styleUrl: './employee-flight-card.component.scss'
})
export class EmployeeFlightCardComponent {
  @Input() flight!: FlightDetailsDto;

  get cabinClassPrices() {
    return Object.keys(this.flight.cabinClassPrices).map(key => ({
      className: key,
      price: this.flight.cabinClassPrices[key]
    }));
  }

}
