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
import {MatButton, MatIconButton} from "@angular/material/button";
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
    MatTooltip
  ],
  templateUrl: './airports.component.html',
  styleUrl: './airports.component.scss'
})
export class AirportsComponent implements OnInit {
  countries: Country[] = [];

  private readonly _snackBar = inject(MatSnackBar);
  horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  verticalPosition: MatSnackBarVerticalPosition = 'top';

  constructor(
    private readonly countryService: CountryService,
    private readonly airportService: AirportService,
    private readonly dialog: MatDialog
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

  // deleteAirport(airport: any, country: any) {
  //   this.airportService.deleteAirport(airport).subscribe({
  //     next: () => {
  //       country.airports = country.airports.filter((a: Airport) => a !== airport);
  //     }
  //   })
  // }

  editAirport(airport: Airport, country: Country) {
    const dialogRef = this.dialog.open(AddAirportFormComponent, {
      maxWidth: '100vw',
      maxHeight: '100vh',
      height: '80%',
      width: '60%',
      data: {
        airport: airport,
        country: country.name
      }
    });

    dialogRef.afterClosed().subscribe((result: CreateAirportDto) => {
      if(result) {
        this.airportService.updateAirport({id:airport.id!, body:result}).subscribe({
          next: (updatedAirport) => {
            if (country.name !== result.country){
              this.ngOnInit()
            } else {
              const airportIndex = country.airports?.findIndex(a => a.id === airport.id);
              if (airportIndex !== undefined && airportIndex !== -1 && country.airports) {
                country.airports[airportIndex] = updatedAirport;
                country.airports = [...country.airports];
              }
            }
            const message = `Airport has been updated successfully`
            this._snackBar.open(message, 'close', {
              horizontalPosition: this.horizontalPosition,
              verticalPosition: this.verticalPosition,
              panelClass: ['snackbar-success'],
              duration: 9000
            });
            // this.ngOnInit();
          },
          error: (err) => {
            this._snackBar.open(
              err.error.message || 'Error updating airport', 'Close', {
                horizontalPosition: this.horizontalPosition,
                verticalPosition: this.verticalPosition,
                panelClass: ['snackbar-error'],
                duration: 5000
              }
            );
          }
        })
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
      if(result) {
        this.airportService.createAirport({body: result}).subscribe({
          next: (createdAirport: Airport) => {
            const country = this.countries.find(c => c.name === result.country);
            console.log("found {}", country)
            if (country) {
              // console.log(country.airports);
              // console.log("pushing {}", createdAirport);
              // country.airports?.push(createdAirport);
              country.airports = [...(country.airports ?? []), createdAirport];
            }
            const message = `Airport has been created successfully`
            this._snackBar.open(message, 'close', {
              horizontalPosition: this.horizontalPosition,
              verticalPosition: this.verticalPosition,
              panelClass: ['snackbar-success'],
              duration: 9000
            });
          },
          error: (err) => {
            this._snackBar.open(
              err.error.message || 'Error creating airport', 'Close', {
                horizontalPosition: this.horizontalPosition,
                verticalPosition: this.verticalPosition,
                panelClass: ['snackbar-error'],
                duration: 5000
              }
            );
          }
        })
      }
    });
  }
}
