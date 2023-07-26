import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";
import {AuthService} from "@app/_services";
import {environment} from "@environments/environment";

@Injectable()
export class HttpRequestInterceptor implements HttpInterceptor {

  constructor(private auth: AuthService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!this.auth.isLoggedIn() || !this.isApiUrl(request)) {
      return next.handle(request);
    }
    const user = this.auth.userValue;
    request = request.clone({
      withCredentials: true,
      setHeaders: {
        'Authorization': `Basic ${btoa(user?.username + ":" + user?.password)}`
      }
    })
    return next.handle(request);
  }

  private isApiUrl(request: HttpRequest<any>): boolean {
    return request.url.startsWith(environment.apiUrl);
  }

}
