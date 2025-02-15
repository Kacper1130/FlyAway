import {Component, OnInit, ViewChild} from '@angular/core';
import {EmployeeNavbarComponent} from "../../components/employee-navbar/employee-navbar.component";
import {EmployeeFlightCardComponent} from "../employee-flights/employee-flight-card/employee-flight-card.component";
import {MatButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {NgForOf} from "@angular/common";
import {RouterLink} from "@angular/router";
import {AdminNavbarComponent} from "../../../admin/components/admin-navbar/admin-navbar.component";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {AvailableFlightComponent} from "../../../flights/components/available-flight/available-flight.component";
import {FormsModule} from "@angular/forms";
import {PageResponseDisplayReservationDto} from "../../../../services/models/page-response-display-reservation-dto";
import {EmployeeReservationService} from "../../../../services/services/employee-reservation.service";
import {EmployeeReservationCardComponent} from "./employee-reservation-card/employee-reservation-card.component";

@Component({
  selector: 'app-employee-reservations',
  standalone: true,
  imports: [
    EmployeeNavbarComponent,
    EmployeeFlightCardComponent,
    MatButton,
    MatIcon,
    NgForOf,
    RouterLink,
    AdminNavbarComponent,
    MatFormField,
    MatInput,
    MatLabel,
    MatPaginator,
    AvailableFlightComponent,
    EmployeeReservationCardComponent,
    FormsModule
  ],
  templateUrl: './employee-reservations.component.html',
  styleUrl: './employee-reservations.component.scss'
})
export class EmployeeReservationsComponent implements OnInit{

  reservations: PageResponseDisplayReservationDto = {};
  page: number = 0;
  size: number = 20;

  searchId: string = '';

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private readonly reservationService: EmployeeReservationService
  ) {
  }

  ngOnInit(): void {
    this.findReservations();
  }

  private findReservations() {
    this.reservationService.getReservations({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (reservations) => {
        this.reservations = reservations;
      }
    });
  }

  onPageChange(event: PageEvent): void {
    this.page = event.pageIndex;
    this.size = event.pageSize;
    this.findReservations();
    window.scrollTo({top: 0, behavior: 'instant'});
  }

  applyFilter() {
    if (this.searchId.trim()) {
      this.reservationService.searchReservationById({id: this.searchId}).subscribe({
        next: (reservation) => {
          this.reservations = { content: [reservation], totalElements: 1 };
        },
        error: () => {
          this.reservations = { content: [], totalElements: 0 };
        }
      });
    } else {
      this.findReservations(); // Jeśli pole puste, pobierz wszystkie
    }
  }

}
