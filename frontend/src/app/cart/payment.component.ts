import {Component, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {PaymentService} from "@app/_services";

@Component({
  templateUrl: "payment.component.html",
  standalone: true
})
export class PaymentComponent implements OnInit {
  form!: FormGroup;
  id?: string;
  submitting: boolean = false;
  submitted: boolean = false;

  constructor(private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private paymentService: PaymentService
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

}
