import {ItemQuantity} from "@app/_models";

export class Order {
  orderId: string;
  orderItems: ItemQuantity[];
  createdDate: string;

  constructor(orderId: string, orderItems: ItemQuantity[], createdDate: string) {
    this.orderId = orderId;
    this.orderItems = orderItems;
    this.createdDate = createdDate;
  }

}
