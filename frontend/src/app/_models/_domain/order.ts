import {ItemQuantity} from "@app/_models";

export class Order {
  orderId: string;
  orderItems: ItemQuantity[];

  constructor(orderId: string, orderItems: ItemQuantity[]) {
    this.orderId = orderId;
    this.orderItems = orderItems;
  }

}
