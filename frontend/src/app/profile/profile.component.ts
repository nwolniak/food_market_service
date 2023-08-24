import {Component, OnInit} from "@angular/core";
import {NgIf, NgOptimizedImage} from "@angular/common";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {AuthService} from "@app/_services";
import {User} from "@app/_models";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {RouterLink} from "@angular/router";

@Component({
  templateUrl: "profile.component.html",
  imports: [
    NgOptimizedImage,
    FormsModule,
    NgIf,
    ReactiveFormsModule,
    FontAwesomeModule,
    RouterLink
  ],
  standalone: true
})
export class ProfileComponent implements OnInit{

  user?: User | null;
  passwordVisibility: string = "password";

  constructor(private auth: AuthService) {
  }

  ngOnInit(): void {
    this.auth.user.subscribe(user => this.user = user);
  }

  changePasswordVisibility() {
    if (this.passwordVisibility === "password") {
      this.passwordVisibility = "text";
    } else {
      this.passwordVisibility = "password";
    }
  }
}
