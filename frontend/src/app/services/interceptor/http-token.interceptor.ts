import {HttpInterceptorFn} from '@angular/common/http';
import {inject} from "@angular/core";
import {LocalStorageService} from "../token/local-storage.service";

export const httpTokenInterceptor: HttpInterceptorFn = (req, next) => {
  const localStorageService = inject(LocalStorageService);
  const userId = localStorageService.userId;
  if (userId) {
    const authReq = req.clone({
      headers: req.headers.set('X-User-ID', userId.toString())
    });
    return next(authReq);
  }
  return next(req);
};
