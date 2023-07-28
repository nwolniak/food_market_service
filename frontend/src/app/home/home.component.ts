import {Component} from '@angular/core';
import {AuthService} from "@app/_services";
import {User} from "@app/_models";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  standalone: true
})
export class HomeComponent {

  user: User | null;

  constructor(private auth: AuthService) {
    this.user = this.auth.userValue;
  }

}
