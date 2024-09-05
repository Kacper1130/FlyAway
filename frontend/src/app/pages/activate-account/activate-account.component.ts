import {Component, inject, OnInit} from '@angular/core';
import {AuthenticationService} from "../../services/services/authentication.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";

@Component({
  selector: 'app-activate-account',
  standalone: true,
  imports: [],
  templateUrl: './activate-account.component.html',
  styleUrl: './activate-account.component.scss'
})
export class ActivateAccountComponent implements OnInit {

  private _snackBar = inject(MatSnackBar);
  horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  verticalPosition: MatSnackBarVerticalPosition = 'top';

  constructor(
    private router: Router,
    private authService: AuthenticationService,
    private activatedRoute: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
    console.log("ngOnInit called");
    this. activateAccount();
  }

  private activateAccount() {
    console.log("activateAccount called");
    const token = this.activatedRoute.snapshot.queryParams['token'];
    this.authService.confirmUserAccount({
      token
    }).subscribe({
      next: () => {
        const message = 'Verification complete! You can now log in.'
        this._snackBar.open(message, 'close', {
          horizontalPosition: this.horizontalPosition,
          verticalPosition: this.verticalPosition,
          panelClass: ['snackbar-success'],
          duration: 7000
        });
        this.router.navigate(['login']);
      },
      error: (err) => {
        this._snackBar.open(err.error.message, 'close', {
          horizontalPosition: this.horizontalPosition,
          verticalPosition: this.verticalPosition,
          panelClass: ['snackbar-error'],
          duration: 7000
        });
        this.router.navigate(['']);
      }
    })
  }
}
