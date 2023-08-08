import {Component, OnInit} from "@angular/core";
import {Order} from "@app/_models";
import {OrderService} from "@app/_services";
import {ActivatedRoute} from "@angular/router";
import {NgForOf} from "@angular/common";

@Component({
  templateUrl: "list.component.html",
  imports: [
    NgForOf
  ],
  standalone: true
})
export class ListComponent implements OnInit {

  userId?: string;
  orders?: Order[];

  constructor(private orderService: OrderService,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.userId = this.route.snapshot.params["userId"];
    this.orderService.getAll()
      .subscribe(orders => this.orders = orders);
  }

}
