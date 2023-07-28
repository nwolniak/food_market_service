import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "@environments/environment";

@Injectable()
export class LoginInterceptor implements HttpInterceptor {

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!this.isLoginUrl(request)) {
      return next.handle(request);
    }
    request = request.clone({
      withCredentials: true,
    })
    return next.handle(request);
  }

  private isLoginUrl(request: HttpRequest<any>): boolean {
    return request.url === environment.loginUrl;
  }

}
