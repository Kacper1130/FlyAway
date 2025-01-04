import {Component, OnInit, ViewChild} from '@angular/core';
import {NavbarComponent} from "../../../../components/navbar/navbar.component";
import {NewNavbarComponent} from "../../../../components/new-navbar/new-navbar.component";
import {FlightService} from "../../../../services/services/flight.service";
import {Router} from "@angular/router";
import {NgForOf} from "@angular/common";
import {AvailableFlightComponent} from "../../components/available-flight/available-flight.component";
import {PageResponseFlightDto} from "../../../../services/models/page-response-flight-dto";
import {MatPaginator, PageEvent} from "@angular/material/paginator";

@Component({
  selector: 'app-flights',
  standalone: true,
  imports: [
    NavbarComponent,
    NewNavbarComponent,
    NgForOf,
    AvailableFlightComponent,
    MatPaginator
  ],
  templateUrl: './flights.component.html',
  styleUrl: './flights.component.scss'
})
export class FlightsComponent implements OnInit{
  flights: PageResponseFlightDto = {};
  page: number = 0;
  size: number = 5;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private readonly flightService: FlightService,
    private readonly router: Router
  ) {
  }

  ngOnInit(): void {
    this.findFlights();
  }

  private findFlights() {
    this.flightService.getFlights({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (flights) => {
       this.flights = flights;
      }
    });
  }

  onPageChange(event: PageEvent): void {
    this.page = event.pageIndex;
    this.size = event.pageSize;
    this.findFlights();
    window.scrollTo({ top: 0, behavior: 'instant' });
  }

}
