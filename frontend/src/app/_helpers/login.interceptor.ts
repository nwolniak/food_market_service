import {HttpEvent, HttpHandlerFn, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "@environments/environment";

export function loginInterceptor(request: HttpRequest<any>, next: HttpHandlerFn): Observable<HttpEvent<any>> {
  return intercept();

  function intercept() {
    if (!isLoginUrl(request)) {
      return next(request);
    }
    request = request.clone({
      withCredentials: true,
    })
    return next(request);
  }

  function isLoginUrl(request: HttpRequest<any>): boolean {
    return request.url === environment.loginUrl;
  }
}
