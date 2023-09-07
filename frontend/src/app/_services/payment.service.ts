import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Payment, PaymentDto} from "@app/_models";
import {map, Observable} from "rxjs";
import {environment} from "@environments/environment";

@Injectable({providedIn: "root"})
export class PaymentService {

  constructor(private http: HttpClient) {
  }

  getPayment(paymentId: string): Observable<Payment> {
    return this.http.get<PaymentDto>(`${environment.apiUrl}/payments/${paymentId}`)
      .pipe(map(paymentDto => this.fromDto(paymentDto)));
  }

  getPayments(): Observable<Payment[]> {
    return this.http.get<PaymentDto[]>(`${environment.apiUrl}/allPayments`)
      .pipe(map(paymentDtoList => paymentDtoList.map(paymentDto => this.fromDto(paymentDto))));
  }

  getUserPayments(): Observable<Payment[]> {
    return this.http.get<PaymentDto[]>(`${environment.apiUrl}/payments`)
      .pipe(map(paymentDtoList => paymentDtoList.map(paymentDto => this.fromDto(paymentDto))));
  }

  addPayment(paymentDto: PaymentDto): Observable<Payment> {
    return this.http.post<PaymentDto>(`${environment.apiUrl}/payments`, paymentDto)
      .pipe(map(paymentDto => this.fromDto(paymentDto)));
  }

  fromDto(paymentDto: PaymentDto): Payment {
    return new Payment(paymentDto.paymentId!,
      paymentDto.orderId!,
      paymentDto.amount!,
      paymentDto.paymentDate!);
  }

  toDto(payment: Payment): PaymentDto {
    return {
      paymentId: payment.paymentId,
      orderId: payment.orderId,
      amount: payment.amount,
      paymentDate: payment.paymentDate,
    };
  }

}
