import {Component} from '@angular/core';
import {User} from "@app/_models";
import {AuthService} from "@app/_services";
import {RouterLink, RouterOutlet} from "@angular/router";
import {AlertComponent} from "@app/_components";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  imports: [
    RouterLink,
    AlertComponent,
    RouterOutlet,
    NgIf
  ],
  standalone: true
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
