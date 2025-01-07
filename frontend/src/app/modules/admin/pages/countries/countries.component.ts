import {Component, OnInit} from '@angular/core';
import {AdminNavbarComponent} from "../../components/admin-navbar/admin-navbar.component";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {Router} from "@angular/router";
import {CountryService} from "../../../../services/services/country.service";
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatNoDataRow,
  MatRow,
  MatRowDef,
  MatTable,
  MatTableDataSource,
} from "@angular/material/table";
import {MatSlideToggle} from "@angular/material/slide-toggle";
import {FormsModule} from "@angular/forms";
import {MatIcon} from "@angular/material/icon";

@Component({
  selector: 'app-countries',
  standalone: true,
  imports: [
    AdminNavbarComponent,
    MatFormField,
    MatInput,
    MatTable,
    MatColumnDef,
    MatHeaderCell,
    MatCell,
    MatCellDef,
    MatHeaderCellDef,
    MatNoDataRow,
    MatLabel,
    MatRow,
    MatHeaderRow,
    MatRowDef,
    MatHeaderRowDef,
    MatSlideToggle,
    FormsModule,
    MatIcon
  ],
  templateUrl: './countries.component.html',
  styleUrl: './countries.component.scss'
})
export class CountriesComponent implements OnInit {

  displayedColumns: string[] = ['name', 'enabled'];
  dataSource = new MatTableDataSource();

  constructor(
    private readonly countryService: CountryService,
    private readonly router: Router
  ) {
  }

  ngOnInit(): void {
    this.findAllCountries();
  }

  private findAllCountries() {
    this.countryService.getAllCountries().subscribe({
      next: (res) => {
        console.log(res)
        this.dataSource.data = res;
      }
    })
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  onToggleChange(country: any) {
    this.countryService.switchCountryStatus(country).subscribe({
      next: () => {

      }
    })
    console.log("switched {}", country.name);
  }
}
