import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/services/authentication.service";
import {AuthenticationRequest} from "../../services/models/authentication-request";
import {FormsModule} from "@angular/forms";
import {LocalStorageService} from "../../services/token/local-storage.service";
import {MatButtonModule} from '@angular/material/button';
import {MatFormFieldModule} from '@angular/material/form-field';
import {ChangeDetectionStrategy, Component, inject, signal} from '@angular/core';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {NewNavbarComponent} from "../../components/new-navbar/new-navbar.component";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition,} from '@angular/material/snack-bar';
import {MatSelectModule} from '@angular/material/select';
import {AuthenticationResponse} from "../../services/models/authentication-response";


@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    NewNavbarComponent,
    MatSelectModule,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  authRequest: AuthenticationRequest = {email: '', password: ''}
  errorMsg: Array<string> = [];

  private _snackBar = inject(MatSnackBar);
  horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  verticalPosition: MatSnackBarVerticalPosition = 'top';

  constructor(
    private router: Router,
    private authService: AuthenticationService,
    private localStorageService: LocalStorageService
  ) {
  }

  login() {
    this.errorMsg = [];
    this.authService.authenticate({
      body: this.authRequest
    }).subscribe({
      next: (res: AuthenticationResponse) => {
        // console.log(res.token)
        // console.log(res)
        this.localStorageService.userId! = res.userId;
        this.localStorageService.role! = res.role;
        this.localStorageService.email! = res.email;
        this.localStorageService.firstname! = res.firstname;
        const role = res.role;
        console.log(role);
        if (role === 'ROLE_ADMIN') {
          this.router.navigate(['admin']);
        } else if (role === 'ROLE_EMPLOYEE') {
          this.router.navigate(['employee']);
        } else {
          this.router.navigate(['']);
        }
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
    });
  }

  register() {
    this.router.navigate(['register'])
  }

  hide = signal(true);

  clickEvent(event: MouseEvent) {
    this.hide.set(!this.hide());
    event.stopPropagation();
  }


}
