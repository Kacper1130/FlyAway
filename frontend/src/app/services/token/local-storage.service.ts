import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LocalStorageService {

  set userId(userId: string) {
    localStorage.setItem('userId', userId)
  }

  get userId() {
    const userId: string = localStorage.getItem('userId')!;
    return userId;
  }

  set role(role: string) {
    localStorage.setItem('role', role)
  }

  get role() {
    return localStorage.getItem('role') as string;
  }

  set firstname(firstname: string) {
    localStorage.setItem('firstname', firstname)
  }

  get firstname() {
    return localStorage.getItem('firstname') as string;
  }

  set email(email: string) {
    localStorage.setItem('email', email)
  }

  get email() {
    return localStorage.getItem('email') as string;
  }

  isLogged() {
    return localStorage.getItem('userId') !== null;
  }

}
