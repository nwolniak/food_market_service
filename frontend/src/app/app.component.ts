import {Component, OnInit} from '@angular/core';
import {User} from "@app/_models";
import {AuthService} from "@app/_services";
import {RouterLink, RouterOutlet} from "@angular/router";
import {AlertComponent} from "@app/_components";
import {NgIf} from "@angular/common";
import {FaIconLibrary, FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {faChevronLeft, faEye, faPencilSquare, faUser} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  imports: [
    RouterLink,
    AlertComponent,
    RouterOutlet,
    NgIf,
    FontAwesomeModule
  ],
  standalone: true
})
export class AppComponent implements OnInit {

  user?: User | null;

  constructor(library: FaIconLibrary,
              private auth: AuthService) {
    library.addIcons(faEye, faPencilSquare, faUser, faChevronLeft);
  }

  ngOnInit(): void {
    this.auth.user.subscribe(user => this.user = user);
  }

  logout() {
    this.auth.logout();
  }

}
