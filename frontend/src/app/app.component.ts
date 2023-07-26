import {Component} from '@angular/core';
import {User} from "@app/_models";
import {AuthService} from "@app/_services";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  user?: User | null;

  constructor(private auth: AuthService) {
    this.auth.user.subscribe(user => this.user = user);
  }

  logout() {
    this.auth.logout();
  }

}
