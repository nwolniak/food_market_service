import {Component, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {AlertService, ItemsService} from "@app/_services";
import {first} from "rxjs";

@Component({
  templateUrl: "add.component.html"
})
export class AddComponent implements OnInit {
  form!: FormGroup;
  id?: string;
  submitting: boolean = false;
  submitted: boolean = false;

  constructor(private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private itemsService: ItemsService,
              private alertService: AlertService
  ) {
  }

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];

    this.form = this.formBuilder.group({
      name: ['', Validators.required],
      category: ['', Validators.required],
      unitType: ['', Validators.required],
      unitPrice: ['', Validators.required],
      description: ['', Validators.required],
    });
  }

  onSubmit() {
    this.submitted = true;
    this.alertService.clear();

    if (this.form.invalid) {
      return;
    }

    this.submitting = true;
    this.itemsService.postItem(this.form.value)
      .pipe(first())
      .subscribe({
        next: () => {
          this.alertService.success("Item added", true);
          this.router.navigateByUrl("/items");
        },
        error: error => {
          this.alertService.error(error);
          this.submitting = false;
        }
      })
  }

  get controls() {
    return this.form.controls;
  }

}
