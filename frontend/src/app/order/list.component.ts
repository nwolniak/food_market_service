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

  protected readonly parseFloat = parseFloat;

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

  orderPrice(order: Order): number {
    return order.orderItems
      .map(orderItem => orderItem.quantity * parseFloat(orderItem.item.unitPrice))
      .reduce((previousValue, currentValue) => previousValue + currentValue);
  }

}
