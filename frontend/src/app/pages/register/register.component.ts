import {ChangeDetectionStrategy, Component, inject, signal} from '@angular/core';
import {Router} from "@angular/router";
import {AuthenticationService} from "../../servicesOLD/services/authentication.service";
import {RegistrationRequest} from "../../servicesOLD/models/registration-request";
import {NgForOf, NgIf} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {MatFormField, MatFormFieldModule, MatHint, MatLabel, MatSuffix} from "@angular/material/form-field";
import {MatInput, MatInputModule} from "@angular/material/input";
import {MatIcon} from "@angular/material/icon";
import {MatAnchor, MatButton, MatIconButton} from "@angular/material/button";
import {MatDatepickerModule} from '@angular/material/datepicker';
import {NavbarComponent} from "../../components/navbar/navbar.component";
import {NewNavbarComponent} from "../../components/new-navbar/new-navbar.component";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition,} from '@angular/material/snack-bar';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    NgForOf,
    FormsModule,
    NgIf,
    MatLabel,
    MatFormField,
    MatInput,
    MatHint,
    MatIcon,
    MatIconButton,
    MatSuffix,
    MatAnchor,
    MatButton,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    NavbarComponent,
    NewNavbarComponent
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  registerRequest: RegistrationRequest = {
    email: '',
    password: '',
    firstname: '',
    lastname: '',
    dayOfBirth: '',
    phoneNumber: ''
  };
  errorMsg: Array<string> = [];

  private _snackBar = inject(MatSnackBar);
  horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  verticalPosition: MatSnackBarVerticalPosition = 'top';

  constructor(
    private router: Router,
    private authService: AuthenticationService
  ) {
  }

  register() {
    this.errorMsg = [];
    this.authService.register({
      body: this.registerRequest
    }).subscribe({
      next: () => {
        this.router.navigate(['']);
        const message = 'Your account has been created successfully.\n Please check your email to activate your account.'
        this._snackBar.open(message, 'close', {
          horizontalPosition: this.horizontalPosition,
          verticalPosition: this.verticalPosition,
          panelClass: ['snackbar-success'],
          duration: 8000
        });
      },
      error: (err) => {
        console.log(err.error.errors);
        if (err.error.errors) {
          const errorMessages = Object.values(err.error.errors)
            .map(value => {
              if (typeof value === 'string') {
                return value.replace(/\.,/g, '\n').replace('.','')
              }
              return '';
            })
            .join('\n');
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

  login() {
    this.router.navigate(['login'])
  }

  hide = signal(true);

  clickEvent(event: MouseEvent) {
    this.hide.set(!this.hide());
    event.stopPropagation();
  }


}
