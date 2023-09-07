import {Component, OnInit, QueryList, ViewChildren} from "@angular/core";
import {Payment} from "@app/_models";
import {PaymentService} from "@app/_services";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {RouterLink} from "@angular/router";
import {SortableHeader, SortEvent} from "@app/_helpers";

const compare = (v1: string | number, v2: string | number) => (v1 < v2 ? -1 : v1 > v2 ? 1 : 0);

@Component({
  templateUrl: "list.component.html",
  styleUrls: ["list.component.scss"],
  imports: [
    NgForOf,
    NgIf,
    RouterLink,
    SortableHeader,
    DatePipe
  ],
  standalone: true
})
export class ListComponent implements OnInit {
  payments?: Payment[];

  @ViewChildren(SortableHeader)
  headers?: QueryList<SortableHeader>;

  constructor(private paymentService: PaymentService) {
  }

  ngOnInit(): void {
    this.paymentService.getUserPayments()
      .subscribe(payments => this.payments = payments);
  }

  onSort({column, direction}: SortEvent) {
    if (!this.payments || column === "" || !this.headers) {
      return;
    }

    this.headers
      .filter(header => header.sortable !== column)
      .forEach(header => header.direction = "");

    this.payments = this.payments.sort((p1, p2) => {
      const res = compare(p1[column as keyof Payment], p2[column as keyof Payment]);
      return direction === "asc" ? res : -res;
    });
  }

}
