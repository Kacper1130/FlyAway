import { Injectable } from '@angular/core';
import {JwtHelperService} from "@auth0/angular-jwt";

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  private jwtHelper: JwtHelperService = new JwtHelperService();

  set token(token: string) {
    localStorage.setItem('token', token)
  }

  get token() {
    return localStorage.getItem('token') as string;
  }

  isTokenNotValid() {
    return !this.isTokenValid();
  }

  private isTokenValid() {
    const token = this.token;
    if (!token) {
      return false;
    }
    const isTokenExpired = this.jwtHelper.isTokenExpired(token);
    if(isTokenExpired) {
      localStorage.clear();
      return false;
    }
    return true;
  }

  getFirstname() {
    const token = this.token
    return this.jwtHelper.decodeToken(token).firstname;
  }

}
