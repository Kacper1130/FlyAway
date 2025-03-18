import {Component, OnInit, ViewChild} from '@angular/core';
import {NavbarComponent} from "../../../../components/navbar/navbar.component";
import {NewNavbarComponent} from "../../../../components/new-navbar/new-navbar.component";
import {FlightService} from "../../../../services/services/flight.service";
import {Router} from "@angular/router";
import {NgForOf} from "@angular/common";
import {AvailableFlightComponent} from "../../components/available-flight/available-flight.component";
import {PageResponseFlightDto} from "../../../../services/models/page-response-flight-dto";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {MatExpansionPanel, MatExpansionPanelHeader, MatExpansionPanelTitle} from "@angular/material/expansion";
import {FormBuilder, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {MatFormField, MatFormFieldModule, MatHint, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {
  MatDatepicker,
  MatDatepickerInput,
  MatDatepickerModule,
  MatDatepickerToggle,
  MatDateRangeInput,
  MatDateRangePicker
} from "@angular/material/datepicker";
import {MatOption, MatSelect} from "@angular/material/select";
import {MatButton, MatIconButton} from "@angular/material/button";
import {provideNativeDateAdapter} from "@angular/material/core";
import {AiChatWidgetComponent} from "../../../../components/ai-chat-widget/ai-chat-widget.component";

@Component({
  selector: 'app-flights',
  standalone: true,
    imports: [
        NavbarComponent,
        NewNavbarComponent,
        NgForOf,
        AvailableFlightComponent,
        MatPaginator,
        MatExpansionPanel,
        MatExpansionPanelHeader,
        MatExpansionPanelTitle,
        ReactiveFormsModule,
        MatFormField,
        MatInput,
        MatDatepickerToggle,
        MatDatepicker,
        MatSelect,
        MatOption,
        MatButton,
        MatDatepickerInput,
        MatLabel,
        MatIconButton,
        MatDateRangeInput,
        MatDateRangePicker,
        MatHint,
        MatFormFieldModule,
        MatDatepickerModule,
        AiChatWidgetComponent,

    ],
  providers: [provideNativeDateAdapter()],
  // changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './flights.component.html',
  styleUrl: './flights.component.scss'
})
export class FlightsComponent implements OnInit {
  flights: PageResponseFlightDto = {};
  page: number = 0;
  size: number = 5;
  filterForm: FormGroup;
  private activeFilters: any = null;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private readonly flightService: FlightService,
    private readonly router: Router,
    private readonly fb: FormBuilder
  ) {
    this.filterForm = this.fb.group({
      departureCity: [''],
      arrivalCity: [''],
      dateRange: this.fb.group({
        departureDate: [null],
        arrivalDate: [null]
      }),
      class: ['']
    });
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
    if (this.activeFilters) {
      this.flightService.getFlightsByFilter({
        page: this.page,
        size: this.size,
        filters: this.activeFilters
      }).subscribe({
        next: (res) => {
          this.flights = res;
        }
      });
    } else {
      this.findFlights();
    }
    window.scrollTo({top: 0, behavior: 'instant'});
  }

  applyFilters() {
    // console.log(this.filterForm.value);
    const formValue = this.filterForm.value;
    const filteredFormValue = this.filterEmptyValues(formValue);

    this.activeFilters = filteredFormValue;

    this.page = 0;
    if (this.paginator) {
      this.paginator.pageIndex = 0;
    }

    this.flightService.getFlightsByFilter({
      page: this.page,
      size: this.size,
      filters: filteredFormValue
    }).subscribe({
      next: (res) => {
        this.flights = res;
      }
    });
  }

  filterEmptyValues(obj: any) {
    const filtered: any = {};
    console.log(obj);
    Object.keys(obj).forEach((key) => {
      const value = obj[key];

      if (key === 'dateRange' && value) {
        if (value.departureDate) filtered['departureDate'] = this.convertDateToLocalDateFormat(value.departureDate);
        if (value.arrivalDate) filtered['arrivalDate'] = this.convertDateToLocalDateFormat(value.arrivalDate);
      } else if (value !== '' && value !== null) {
        filtered[key] = value;
      }
    });

    return filtered;
  }

  convertDateToLocalDateFormat(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  resetFilters() {
    this.filterForm.reset();
    this.page = 0;
    this.activeFilters = null;
    this.findFlights();
  }

}
