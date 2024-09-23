import {Component, OnInit} from '@angular/core';
import {NewNavbarComponent} from "../../components/new-navbar/new-navbar.component";
import {ClientDto} from "../../services/models/client-dto";
import {FormsModule} from "@angular/forms";
import {MatAnchor, MatButton, MatIconButton} from "@angular/material/button";
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from "@angular/material/datepicker";
import {MatFormField, MatLabel, MatSuffix} from "@angular/material/form-field";
import {MatIcon} from "@angular/material/icon";
import {MatInput} from "@angular/material/input";
import {ClientService} from "../../services/services/client.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-client-details',
  standalone: true,
  imports: [
    NewNavbarComponent,
    FormsModule,
    MatAnchor,
    MatButton,
    MatDatepicker,
    MatDatepickerInput,
    MatDatepickerToggle,
    MatFormField,
    MatIcon,
    MatIconButton,
    MatInput,
    MatLabel,
    MatSuffix
  ],
  templateUrl: './client-details.component.html',
  styleUrl: './client-details.component.scss'
})
export class ClientDetailsComponent implements OnInit {
  clientDetails: ClientDto = {}

  constructor(
    private clientService: ClientService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.loadClient();
  }

  private loadClient() {
    this.clientService.getClient()
      .subscribe({
        next: (response) => {
          this.clientDetails = response
        }
      });
  }

  submit() {

  }
}
