import {ItemQuantity, Payment} from "@app/_models";

export class Order {
  orderId: string;
  orderItems: ItemQuantity[];
  createdDate: string;
  isPaid: boolean;
  price: number;
  payment?: Payment;
  isAction?: boolean = false;

  constructor(orderId: string, orderItems: ItemQuantity[], createdDate: string, isPaid: boolean, price: number) {
    this.orderId = orderId;
    this.orderItems = orderItems;
    this.createdDate = createdDate;
    this.isPaid = isPaid;
    this.price = price;
  }

}
