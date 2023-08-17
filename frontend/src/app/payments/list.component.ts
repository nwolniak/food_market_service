import {Component, Directive, EventEmitter, Input, OnInit, Output, QueryList, ViewChildren} from "@angular/core";
import {Payment} from "@app/_models";
import {PaymentService} from "@app/_services";
import {NgForOf, NgIf} from "@angular/common";
import {RouterLink} from "@angular/router";

export type SortColumn = keyof Payment | "";
export type SortDirection = "asc" | "desc" | "";
const rotate: { [key: string]: SortDirection } = {asc: "desc", desc: "asc", "": "desc"};

const compare = (v1: string | number, v2: string | number) => (v1 < v2 ? -1 : v1 > v2 ? 1 : 0);

export interface SortEvent {
  column: SortColumn,
  direction: SortDirection
}

@Directive({
  selector: "th[sortable]",
  standalone: true,
  host: {
    "[class.asc]": 'direction === "asc"',
    "[class.desc]": 'direction === "desc"',
    "(click)": "rotate()",
  },
})
export class SortableHeader {
  @Input() sortable: SortColumn = ""
  @Input() direction: SortDirection = "";
  @Output() sort = new EventEmitter<SortEvent>();

  rotate() {
    this.direction = rotate[this.direction];
    this.sort.emit({column: this.sortable, direction: this.direction});
  }
}

@Component({
  templateUrl: "list.component.html",
  styleUrls: ["list.component.scss"],
  imports: [
    NgForOf,
    NgIf,
    RouterLink,
    SortableHeader
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
    this.paymentService.getPayments()
      .subscribe(payments => this.payments = payments);
  }

  onSort({column, direction}: SortEvent) {
    if (!this.payments || column === "" || !this.headers) {
      return;
    }

    this.headers
      .filter(header => header.sortable !== column)
      .forEach(header => header.direction = "");

    this.payments = [...this.payments].sort((p1, p2) => {
      const res = compare(p1[column], p2[column]);
      return direction === "asc" ? res : -res;
    });
  }

}
