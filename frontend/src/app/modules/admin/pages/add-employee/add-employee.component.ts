import {Component, inject} from '@angular/core';
import {AdminNavbarComponent} from "../../components/admin-navbar/admin-navbar.component";
import {FormsModule} from "@angular/forms";
import {MatAnchor, MatButton, MatIconButton} from "@angular/material/button";
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from "@angular/material/datepicker";
import {MatFormField, MatLabel, MatSuffix} from "@angular/material/form-field";
import {MatIcon} from "@angular/material/icon";
import {MatInput} from "@angular/material/input";
import {PaginatorModule} from "primeng/paginator";
import {AddEmployeeDto} from "../../../../services/models/add-employee-dto";
import {Router} from "@angular/router";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";
import {EmployeeCredentialsDto} from "../../../../services/models/employee-credentials-dto";
import {NgIf} from "@angular/common";
import {MatCard, MatCardContent, MatCardHeader, MatCardSubtitle, MatCardTitle} from "@angular/material/card";
import {EmployeeService} from "../../../../services/services/employee.service";

@Component({
  selector: 'app-add-employee',
  standalone: true,
  imports: [
    AdminNavbarComponent,
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
    MatSuffix,
    PaginatorModule,
    NgIf,
    MatCard,
    MatCardHeader,
    MatCardContent,
    MatCardTitle,
    MatCardSubtitle
  ],
  templateUrl: './add-employee.component.html',
  styleUrl: './add-employee.component.scss'
})
export class AddEmployeeComponent {
  addEmployeeDto: AddEmployeeDto = {
    email: '',
    firstname: '',
    lastname: '',
    phoneNumber: '',
  }

  employeeCredentials: EmployeeCredentialsDto = {
    email: '',
    firstname: '',
    lastname: '',
    password: ''
  }

  errorMsg: Array<string> = [];

  private _snackBar = inject(MatSnackBar);
  horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  verticalPosition: MatSnackBarVerticalPosition = 'top';

  constructor(
    private router: Router,
    private employeeService: EmployeeService
  ) {
  }

  createEmployee() {
    this.errorMsg = [];
    if (!this.addEmployeeDto.email?.endsWith("@flyaway.com")) {
      this.addEmployeeDto.email += '@flyaway.com';
    }
    this.employeeService.createEmployee({
      body: this.addEmployeeDto
    }).subscribe({
        next: (res) => {
          this.employeeCredentials = res;
          this.addEmployeeDto = {
            email: '',
            firstname: '',
            lastname: '',
            phoneNumber: '',
          };
        },
        error: (err) => {
          console.log(err.error.errors);
          if (this.addEmployeeDto.email && this.addEmployeeDto.email.endsWith("@flyaway.com")) {
            this.addEmployeeDto.email = this.addEmployeeDto.email.replace(/@flyaway\.com$/, '');
          }
          if (err.error.errors) {
            const errorMessages = Object.values(err.error.errors)
              .map(value => {
                if (typeof value === 'string') {
                  return value.replace(/\.,/g, '\n').replace('.', '')
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
      }
    )
  }
}
