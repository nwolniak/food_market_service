import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {AlertService, AuthService} from "@app/_services";
import {first} from "rxjs";
import {NgClass, NgIf} from "@angular/common";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
  imports: [
    ReactiveFormsModule,
    NgClass,
    NgIf,
    RouterLink,
    FontAwesomeModule
  ],
  standalone: true
})
export class RegisterComponent implements OnInit {
  form!: FormGroup;
  passwordVisibility: string = "password";
  loading: boolean = false;
  submitted: boolean = false;


  constructor(private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private auth: AuthService,
              private alertServiec: AlertService) {
    if (this.auth.userValue) {
      this.router.navigate(["/"]);
    }
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      name: ["", Validators.required],
      username: ["", Validators.required],
      password: ["", [Validators.required, Validators.minLength(6)]],
      email: ["", [Validators.required, Validators.email]]
    })
  }

  onSubmit() {
    this.submitted = true;
    this.alertServiec.clear();
    if (this.form.invalid) {
      return;
    }

    this.loading = true;
    this.auth.register(this.form.value)
      .pipe(first())
      .subscribe({
        next: () => {
          this.router.navigate(["/login"], {queryParams: {registered: true}})
        },
        error: error => {
          this.alertServiec.error(error);
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
