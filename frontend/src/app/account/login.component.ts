import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {AlertService, AuthService} from "@app/_services";
import {first} from "rxjs";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  form!: FormGroup;
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

  get controls() {
    return this.form.controls;
  }

}
