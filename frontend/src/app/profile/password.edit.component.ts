import {AlertService, AuthService} from "@app/_services";
import {Component, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, ValidationErrors, Validators} from "@angular/forms";
import {NgClass, NgIf} from "@angular/common";
import {Router, RouterLink} from "@angular/router";

@Component({
  templateUrl: "password.edit.component.html",
  imports: [
    FormsModule,
    NgIf,
    ReactiveFormsModule,
    RouterLink,
    NgClass
  ],
  standalone: true
})
export class PasswordEditComponent implements OnInit {

  form!: FormGroup;
  submitting: boolean = false;
  submitted: boolean = false;

  constructor(private formBuilder: FormBuilder,
              private router: Router,
              private auth: AuthService,
              private alertService: AlertService) {
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      new_password: ['', Validators.required],
      new_password_confirm: ['', Validators.required]
    }, {
      validators: [this.createNewPasswordValidator()]
    });
  }

  createNewPasswordValidator() {
    return (form: FormGroup): ValidationErrors | null => {
      const newPassword = form.get("new_password")?.value;
      const newPasswordConfirm = form.get("new_password_confirm")?.value;
      if (!newPassword || !newPasswordConfirm) {
        return null;
      }
      return newPassword === newPasswordConfirm ? null : {differentPasswords: true};
    }
  }

  onSubmit() {
    this.submitted = true;
    this.alertService.clear();

    if (this.form.invalid) {
      return;
    }

    this.submitting = true;
    this.auth.changePassword(this.form.value["new_password"])
      .subscribe({
        next: () => {
          this.alertService.success("Password changed", true);
          this.router.navigateByUrl("/profile");
        },
        error: err => {
          console.log(err)
          this.alertService.error(err);
          this.submitting = false;
        }
      });
  }

  get controls() {
    return this.form.controls;
  }

  protected readonly console = console;
}
