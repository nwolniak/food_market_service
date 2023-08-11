import {ItemQuantityDto, PaymentDto} from "@app/_models";

export class OrderDto {
  orderId?: string;
  orderItems?: ItemQuantityDto[];
  createdDate?: string;
  isPaid?: boolean;
  paymentDto?: PaymentDto;

  constructor(orderId?: string, orderItems?: Array<ItemQuantityDto>) {
    this.orderId = orderId;
    this.orderItems = orderItems;
  }

}
