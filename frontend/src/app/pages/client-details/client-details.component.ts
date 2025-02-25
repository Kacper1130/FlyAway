import {Component, inject, OnInit} from '@angular/core';
import {NewNavbarComponent} from "../../components/new-navbar/new-navbar.component";
import {ClientDto} from "../../services/models/client-dto";
import {FormsModule} from "@angular/forms";
import {MatButton} from "@angular/material/button";
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from "@angular/material/datepicker";
import {MatFormField, MatHint, MatLabel, MatSuffix} from "@angular/material/form-field";
import {MatIcon} from "@angular/material/icon";
import {MatInput} from "@angular/material/input";
import {ClientService} from "../../services/services/client.service";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";
import {MatDialog} from "@angular/material/dialog";
import {ChangePasswordComponent} from "./change-password/change-password.component";

@Component({
  selector: 'app-client-details',
  standalone: true,
  imports: [
    NewNavbarComponent,
    FormsModule,
    MatButton,
    MatDatepicker,
    MatDatepickerInput,
    MatDatepickerToggle,
    MatFormField,
    MatIcon,
    MatInput,
    MatLabel,
    MatSuffix,
    MatHint
  ],
  templateUrl: './client-details.component.html',
  styleUrl: './client-details.component.scss'
})
export class ClientDetailsComponent implements OnInit {
  clientDetails: ClientDto = {
    dayOfBirth: '',
    email: '',
    firstname: '',
    lastname: '',
    phoneNumber: ''
  }

  private readonly _snackBar = inject(MatSnackBar);
  horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  verticalPosition: MatSnackBarVerticalPosition = 'top';

  errorMsg: Array<string> = [];

  constructor(
    private readonly clientService: ClientService,
    private readonly dialog: MatDialog,
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
    this.clientService.updateClient({body: this.clientDetails}).subscribe({
      next: (res) => {
        this.clientDetails = res;
        const message = 'Your profile has been updated successfully'
        this._snackBar.open(message, 'close', {
          horizontalPosition: this.horizontalPosition,
          verticalPosition: this.verticalPosition,
          panelClass: ['snackbar-success'],
          duration: 8000
        });
      },
      error: (err) => {
        this.errorMsg = [];
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
  }

  openChangePasswordDialog(): void {
    this.dialog.open(ChangePasswordComponent, {
      width: '800px',
      disableClose: true
    });
  }

}
