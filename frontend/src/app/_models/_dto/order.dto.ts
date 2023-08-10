import {ItemQuantityDto} from "@app/_models";

export class OrderDto {
  orderId?: string;
  orderItems?: ItemQuantityDto[];
  createdDate?: string;

  constructor(orderId?: string, orderItems?: Array<ItemQuantityDto>) {
    this.orderId = orderId;
    this.orderItems = orderItems;
  }

}
