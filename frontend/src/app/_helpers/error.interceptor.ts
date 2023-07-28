import {inject} from '@angular/core';
import {HttpEvent, HttpHandler, HttpHandlerFn, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';

import {AuthService} from '@app/_services';

export function errorInterceptor(request: HttpRequest<any>, next: HttpHandlerFn): Observable<HttpEvent<any>> {
  const auth: AuthService = inject(AuthService);

  return next(request).pipe(catchError(error => {
    if ([401, 403].includes(error.status) && auth.userValue) {
      auth.logout();
    }
    const errorMessage = error.error?.message || error.statusText;
    console.error(errorMessage);
    return throwError(() => errorMessage);
  }))
}
