import {Injectable} from '@angular/core';
import {NavigationStart, Router} from '@angular/router';
import {Observable, Subject} from 'rxjs';

@Injectable({providedIn: 'root'})
export class AlertService {
  private subject: Subject<any> = new Subject<any>();
  private showAfterRedirect: boolean = false;

  constructor(private router: Router) {
    // clear alert messages on route change unless 'showAfterRedirect' flag is true
    this.router.events.subscribe(event => {
      if (event instanceof NavigationStart) {
        if (this.showAfterRedirect) {
          this.showAfterRedirect = false;
        } else {
          this.clear();
        }
      }
    });
  }

  onAlert(): Observable<any> {
    return this.subject.asObservable();
  }

  success(message: string, showAfterRedirect: boolean = false) {
    this.showAfterRedirect = showAfterRedirect;
    this.subject.next({type: 'success', message});
  }

  error(message: string, showAfterRedirect = false) {
    this.showAfterRedirect = showAfterRedirect;
    this.subject.next({type: 'error', message});
  }

  clear() {
    // clear by calling subject.next() with null
    this.subject.next(null);
  }
}
