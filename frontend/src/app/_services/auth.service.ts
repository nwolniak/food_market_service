import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject, map, Observable} from "rxjs";
import {User} from "@app/_models";
import {Router} from "@angular/router";
import {environment} from "@environments/environment";

@Injectable({
  providedIn: "root"
})
export class AuthService {
  private userSubject: BehaviorSubject<User | null>;
  private _user: Observable<User | null>;

  constructor(private router: Router, private http: HttpClient) {
    this.userSubject = new BehaviorSubject(JSON.parse(localStorage.getItem("user")!));
    this._user = this.userSubject.asObservable();
  }

  login(username: string, password: string): Observable<User> {
    return this.http.post<User>(`${environment.apiUrl}/login`, {username, password})
      .pipe(map(user => {
        localStorage.setItem("user", JSON.stringify(user));
        this.userSubject.next(user);
        return user;
      }))
  }

  register(user: User): Observable<User> {
    return this.http.post<User>(`${environment.apiUrl}/registration`, user);
  }

  logout() {
    this.http.post(`${environment.apiUrl}/logout`, {})
      .subscribe({
        next: () => {
          localStorage.removeItem("user");
          this.userSubject.next(null);
          this.router.navigate(["/account/login"]);
        },
        error: error => {
          console.error(`Logout error: ${error}`);
        }
      });
  }


  isLoggedIn(): boolean {
    return this.userSubject.value !== null;
  }

  public get userValue(): User | null {
    return this.userSubject.value;
  }

  public get user(): Observable<User | null> {
    return this._user;
  }

}
