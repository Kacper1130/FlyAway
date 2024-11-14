import {Component, inject, OnInit} from '@angular/core';
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
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef,
  MatTable
} from "@angular/material/table";
import {MatButton, MatFabButton, MatIconButton, MatMiniFabButton} from "@angular/material/button";
import {NgForOf} from "@angular/common";
import {Country} from "../../../../services/models/country";
import {CountryService} from "../../../../services/services/country.service";
import {AirportService} from "../../../../services/services/airport.service";
import {MatSlideToggle} from "@angular/material/slide-toggle";
import {FormsModule} from "@angular/forms";
import {MatIcon} from "@angular/material/icon";
import {Airport} from "../../../../services/models/airport";
import {AddAirportFormComponent} from "./add-airport-form/add-airport-form.component";
import {MatDialog} from "@angular/material/dialog";
import {MatTooltip} from "@angular/material/tooltip";
import {RouterLink} from "@angular/router";
import {CreateAirportDto} from "../../../../services/models/create-airport-dto";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";

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
    FormsModule,
    MatIcon,
    MatIconButton,
    MatTooltip,
    MatMiniFabButton,
    MatFabButton,
    RouterLink
  ],
  templateUrl: './airports.component.html',
  styleUrl: './airports.component.scss'
})
export class AirportsComponent implements OnInit {
  countries: Country[] = [];

  private readonly _snackBar = inject(MatSnackBar);
  horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  verticalPosition: MatSnackBarVerticalPosition = 'top';
  errorMsg: Array<string> = [];

  constructor(
    private readonly countryService: CountryService,
    private readonly airportService: AirportService,
    private dialog: MatDialog
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
    this.airportService.switchAirportStatus(airport).subscribe();
  }

  deleteAirport(airport: any, country: any) {
    this.airportService.deleteAirport(airport).subscribe({
      next: () => {
        country.airports = country.airports.filter((a: Airport) => a !== airport);
      }
    })
  }

  addAirport() {
    const dialogRef = this.dialog.open(AddAirportFormComponent, {
      maxWidth: '100vw',
      maxHeight: '100vh',
      height: '80%',
      width: '60%',
    });

    dialogRef.afterClosed().subscribe((result: CreateAirportDto) => {
      console.log(result);
      this.airportService.addAirport({body: result}).subscribe({
        next: () => {
          console.log("success");
        },
        error: (err) => {
          if (err.error.errors) {
            const errorMessages = Object.values(err.error.errors).join('\n');
            this.errorMsg.push(errorMessages);
            this._snackBar.open(this.errorMsg.toString(), 'close', {
              horizontalPosition: this.horizontalPosition,
              verticalPosition: this.verticalPosition,
              panelClass: ['snackbar-error'],
              duration: 5000
            });
          } else if (err.error.message) {
            this._snackBar.open(err.error.message.toString(), 'close', {
              horizontalPosition: this.horizontalPosition,
              verticalPosition: this.verticalPosition,
              panelClass: ['snackbar-error'],
              duration: 5000
            });
          }
        }
      })
    });
  }
}
