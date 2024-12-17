import { Component } from '@angular/core';
import {EmployeeNavbarComponent} from "../../../components/employee-navbar/employee-navbar.component";
import {FlightDetailsDto} from "../../../../../services/models/flight-details-dto";
import {ActivatedRoute} from "@angular/router";
import {FlightService} from "../../../../../services/services/flight.service";
import {DatePipe, KeyValuePipe, NgForOf, NgIf} from "@angular/common";
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-employee-flight-details',
  standalone: true,
  imports: [
    EmployeeNavbarComponent,
    NgIf,
    DatePipe,
    NgForOf,
    FormsModule,
    KeyValuePipe
  ],
  templateUrl: './employee-flight-details.component.html',
  styleUrl: './employee-flight-details.component.scss'
})
export class EmployeeFlightDetailsComponent {
  flight: FlightDetailsDto | null = null;
  loading = true;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private flightService: FlightService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const flightId = params.get('id');
      if (flightId) {
        this.fetchFlightDetails(flightId);
      }
    });
  }

  fetchFlightDetails(flightId: string): void {
    this.flightService.getFlightDetails({id: flightId}).subscribe({
      next: (flightDetails) => {
        this.flight = flightDetails;
        this.loading = false;
      },
      error: (err) => {
        this.error = err.error.message;
        this.loading = false;
        console.error('Error fetching flight details', err);
      }
    });
  }

  getTotalAvailableSeats(): number {
    if (!this.flight?.aircraft) return 0;
    return this.flight.aircraft.totalSeats -
      (this.flight.reservations?.length || 0);
  }

}
