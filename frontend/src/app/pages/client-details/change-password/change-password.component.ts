import {Component, inject, signal} from '@angular/core';
import {
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from "@angular/material/dialog";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatError, MatFormField, MatFormFieldModule, MatLabel, MatSuffix} from "@angular/material/form-field";
import {MatIcon} from "@angular/material/icon";
import {NgIf} from "@angular/common";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";
import {AuthenticationService} from "../../../services/services/authentication.service";
import {MatInput, MatInputModule} from "@angular/material/input";
import {MatButton, MatIconButton} from "@angular/material/button";
import {ChangePasswordRequest} from "../../../services/models/change-password-request";
import {MatDatepickerModule} from "@angular/material/datepicker";

@Component({
  selector: 'app-change-password',
  standalone: true,
  imports: [
    MatDialogContent,
    ReactiveFormsModule,
    MatFormField,
    MatIcon,
    NgIf,
    MatDialogActions,
    MatLabel,
    MatError,
    MatDialogTitle,
    MatInput,
    MatButton,
    MatDialogClose,
    MatIconButton,
    FormsModule,
    NgIf,
    MatLabel,
    MatFormField,
    MatInput,
    MatIcon,
    MatIconButton,
    MatSuffix,
    MatButton,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule
  ],
  templateUrl: './change-password.component.html',
  styleUrl: './change-password.component.scss'
})
export class ChangePasswordComponent {
  passwordForm: FormGroup;
  isLoading = false;

  errorMsg: Array<string> = [];

  private readonly _snackBar = inject(MatSnackBar);
  horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  verticalPosition: MatSnackBarVerticalPosition = 'top';

  hideNewPassword = signal(true);
  hideConfirmPassword = signal(true);

  constructor(
    private readonly fb: FormBuilder,
    private readonly dialogRef: MatDialogRef<ChangePasswordComponent>,
    private readonly authService: AuthenticationService,
    private readonly snackBar: MatSnackBar
  ) {
    this.passwordForm = this.fb.group({
      currentPassword: ['', Validators.required],
      newPassword: ['', Validators.required,],
      confirmPassword: ['', Validators.required]
    });
  }

  changePassword() {
    this.errorMsg = [];
    if (this.passwordForm.invalid) return;

    this.isLoading = true;
    const changePasswordRequest: ChangePasswordRequest = {
      oldPassword: this.passwordForm.value.currentPassword,
      newPassword1: this.passwordForm.value.newPassword,
      newPassword2: this.passwordForm.value.confirmPassword,
    }

    this.authService.changePassword({body: changePasswordRequest}).subscribe({
      next: () => {
        this.errorMsg = [];
        this.isLoading = false;
        this.snackBar.open('Password changed successfully', 'Close', {
          horizontalPosition: this.horizontalPosition,
          verticalPosition: this.verticalPosition,
          duration: 5000,
          panelClass: ['snackbar-success']
        });
        this.dialogRef.close(true);
      },
      error: (err) => {
        this.isLoading = false;
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
    });
  }

  toggleNewPassword(event: MouseEvent) {
    this.hideNewPassword.set(!this.hideNewPassword());
    event.stopPropagation();
  }

  toggleConfirmPassword(event: MouseEvent) {
    this.hideConfirmPassword.set(!this.hideConfirmPassword());
    event.stopPropagation();
  }

}
