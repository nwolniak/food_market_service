export class Payment {
  paymentId: string;
  orderId: string;
  amount: number;
  paymentDate: string;

  constructor(paymentId: string, orderId: string, amount: number, paymentDate: string) {
    this.paymentId = paymentId;
    this.orderId = orderId;
    this.amount = amount;
    this.paymentDate = paymentDate;
  }

}
