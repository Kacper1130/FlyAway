import {Component, Input} from '@angular/core';
import {CurrencyPipe, DatePipe, NgIf} from "@angular/common";
import {RouterLink} from "@angular/router";
import {MatIcon} from "@angular/material/icon";
import {FlightSummaryEmployeeDto} from "../../../../../services/models/flight-summary-employee-dto";

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
  @Input() flight!: FlightSummaryEmployeeDto;

  get cabinClassPrices() {
    return Object.keys(this.flight.cabinClassPrices!).map(key => ({
      className: key,
      price: this.flight.cabinClassPrices![key]
    }));
  }

}
