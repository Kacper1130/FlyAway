import {Component, OnInit} from '@angular/core';
import {AdminNavbarComponent} from "../../components/admin-navbar/admin-navbar.component";
import {
  MatAccordion,
  MatExpansionPanel,
  MatExpansionPanelHeader,
  MatExpansionPanelTitle
} from "@angular/material/expansion";
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef, MatTable
} from "@angular/material/table";
import {MatButton} from "@angular/material/button";
import {NgForOf} from "@angular/common";
import {Country} from "../../../../services/models/country";
import {CountryService} from "../../../../services/services/country.service";
import {AirportService} from "../../../../services/services/airport.service";
import {MatSlideToggle} from "@angular/material/slide-toggle";
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-airports',
  standalone: true,
  imports: [
    AdminNavbarComponent,
    MatAccordion,
    MatExpansionPanel,
    MatExpansionPanelTitle,
    MatColumnDef,
    MatHeaderCell,
    MatCell,
    MatHeaderCellDef,
    MatCellDef,
    MatButton,
    MatHeaderRow,
    MatHeaderRowDef,
    MatRow,
    MatRowDef,
    MatTable,
    NgForOf,
    MatExpansionPanel,
    MatExpansionPanelHeader,
    MatSlideToggle,
    FormsModule
  ],
  templateUrl: './airports.component.html',
  styleUrl: './airports.component.scss'
})
export class AirportsComponent implements OnInit {
  countries: Country[] = [];

  constructor(
    private readonly countryService: CountryService,
    private readonly airportService: AirportService
  ) {
  }

  ngOnInit(): void {
    this.countryService.getAllEnabledCountries().subscribe({
      next: (res) => {
        this.countries = res;
      }
    })
  }

  onToggleChange(airport: any) {
    this.airportService.switchAirportStatus(airport).subscribe({
      next: () => {

      }
    })
  }
}
