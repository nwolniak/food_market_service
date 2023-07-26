import {Injectable} from '@angular/core';
import {HttpRequest, HttpHandler, HttpEvent, HttpInterceptor} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';

import {AuthService} from '@app/_services';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  constructor(private auth: AuthService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(catchError(error => {
      if ([401, 403].includes(error.status) && this.auth.userValue) {
        this.auth.logout();
      }

      const errorMessage = error.error?.message || error.statusText;
      console.error(errorMessage);
      return throwError(() => errorMessage);
    }))
  }
}
