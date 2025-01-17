import {Component, Inject, OnInit} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from "@angular/material/dialog";
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatSlideToggle} from "@angular/material/slide-toggle";
import {MatButton} from "@angular/material/button";
import {MatInput} from "@angular/material/input";
import {AsyncPipe, NgIf} from "@angular/common";
import {MatOption, MatSelect} from "@angular/material/select";
import {Observable, startWith} from "rxjs";
import {MatAutocomplete, MatAutocompleteTrigger} from "@angular/material/autocomplete";
import {map} from "rxjs/operators";
import {CreateAirportDto} from "../../../../../services/models/create-airport-dto";
import {CountryService} from "../../../../../services/services/country.service";
import {Airport} from "../../../../../services/models/airport";

@Component({
  selector: 'app-add-airport-form',
  standalone: true,
  imports: [
    MatLabel,
    MatFormField,
    MatSlideToggle,
    FormsModule,
    MatButton,
    MatInput,
    ReactiveFormsModule,
    NgIf,
    MatDialogActions,
    MatDialogTitle,
    MatDialogContent,
    MatError,
    MatSelect,
    MatOption,
    MatAutocompleteTrigger,
    MatAutocomplete,
    AsyncPipe
  ],
  templateUrl: './add-airport-form.component.html',
  styleUrl: './add-airport-form.component.scss'
})
export class AddAirportFormComponent implements OnInit {
  country = new FormControl('', Validators.required);
  filteredOptions: Observable<string[]> | undefined;

  airportForm: FormGroup;

  countryList: string[] = [];

  constructor(
    private readonly fb: FormBuilder,
    public dialogRef: MatDialogRef<AddAirportFormComponent>,
    private readonly countryService: CountryService,
    @Inject(MAT_DIALOG_DATA) public data?: { airport: Airport; country: string }
  ) {
    if (data?.country) {
      this.country.setValue(data.country);
      this.checkCountryEnabled(data.country);
    }
    this.airportForm = this.fb.group({
      name: [data?.airport?.name || '', Validators.required],
      country: this.country,
      city: [data?.airport?.city || '', Validators.required],
      IATACode: [data?.airport?.IATACode || '', [Validators.required, Validators.minLength(3), Validators.maxLength(3)]],
      enabled: [{value: data?.airport?.enabled ?? false, disabled: true}],
    });
  }

  ngOnInit() {
    this.filteredOptions = this.country.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value || '')),
    );
    this.findAllCountries();

    this.country.valueChanges.subscribe({
      next: (c) => {
        this.checkCountryEnabled(c as string);
      }
    });
  }

  private checkCountryEnabled(c: string) {
    this.countryService.isCountryEnabled({countryName: c}).subscribe({
      next: (res) => {
        if (res) {
          // console.log("petla if {}", this.country.value);
          this.airportForm.get('enabled')?.enable();
        } else {
          // console.log("petla else {}", this.country.value);
          this.airportForm.get('enabled')?.disable();
        }
      }
    })
  }

  private isCountryEnabled() {
    this.countryService.isCountryEnabled({countryName: this.country.value as string}).pipe(
      map(response => response)
    );
  }

  private findAllCountries() {
    this.countryService.getAllCountriesNames().subscribe({
      next: (res) => {
        this.countryList = res;
        res.sort();
      }
    })
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();
    return this.countryList.filter(option => option.toLowerCase().includes(filterValue));
  }

  onSubmit() {
    if (this.airportForm.valid) {
      const newAirport: CreateAirportDto = this.airportForm.value;
      this.dialogRef.close(newAirport);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }


}
