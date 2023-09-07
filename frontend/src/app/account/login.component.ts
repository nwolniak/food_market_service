import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {AlertService, AuthService} from "@app/_services";
import {first} from "rxjs";
import {NgClass, NgIf} from "@angular/common";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
    imports: [
        ReactiveFormsModule,
        NgClass,
        NgIf,
        RouterLink,
        FontAwesomeModule
    ],
  standalone: true
})
export class LoginComponent implements OnInit {
  form!: FormGroup;
  passwordVisibility: string = "password";
  loading: boolean = false;
  submitted: boolean = false;

  constructor(private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private auth: AuthService,
              private alertService: AlertService) {
    if (this.auth.userValue) {
      this.router.navigate(["/"]);
    }
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      username: ["", Validators.required],
      password: ["", Validators.required]
    });
  }

  onSubmit(): void {
    this.submitted = true;
    this.alertService.clear();
    if (this.form.invalid) {
      return;
    }

    this.loading = true;
    this.auth.login(this.form.controls.username.value, this.form.controls.password.value)
      .pipe(first())
      .subscribe({
        next: () => {
          const returnUrl = this.route.snapshot.queryParams.returnUrl || "/";
          this.router.navigateByUrl(returnUrl);
        },
        error: error => {
          this.alertService.error(error);
          this.loading = false;
        }
      })
  }

  changePasswordVisibility() {
    if (this.passwordVisibility === "password") {
      this.passwordVisibility = "text";
    } else {
      this.passwordVisibility = "password";
    }
  }

  get controls() {
    return this.form.controls;
  }

}
