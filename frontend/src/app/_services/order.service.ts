import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {AuthService} from "@app/_services/auth.service";
import {Cart, Item, ItemQuantity, Order, OrderDto, Payment} from "@app/_models";
import {environment} from "@environments/environment";
import {BehaviorSubject, concatAll, concatMap, from, map, Observable, tap, toArray} from "rxjs";
import {ItemService} from "@app/_services/item.service";

@Injectable({providedIn: "root"})
export class OrderService {

  private ordersSubject: BehaviorSubject<Order[] | undefined>;
  private _orders: Observable<Order[] | undefined>;

  constructor(private http: HttpClient,
              private auth: AuthService,
              private itemService: ItemService) {
    this.ordersSubject = new BehaviorSubject<Order[] | undefined>(undefined);
    this._orders = this.ordersSubject.asObservable();
  }

  getById(orderId: string): Observable<Order> {
    return this.http.get<OrderDto>(`${environment.apiUrl}/orders/${orderId}`)
      .pipe(
        concatMap(orderDto => this.mapDtoToOrder(orderDto)),
        map(order => {
          console.log(`Get order ${order}`)
          return order;
        })
      );
  }

  getUserOrders() {
    return this.http.get<OrderDto[]>(`${environment.apiUrl}/orders`)
      .pipe(
        concatMap((orderDtoList: OrderDto[]) => orderDtoList.map(orderDto => this.mapDtoToOrder(orderDto))),
        concatAll(),
        toArray(),
        map(orders => {
          console.log("Get orders");
          this.ordersSubject.next(orders);
          return orders;
        })
      );
  }

  getAll(): Observable<Order[]> {
    return this.http.get<OrderDto[]>(`${environment.apiUrl}/allOrders`)
      .pipe(
        concatMap((orderDtoList: OrderDto[]) => orderDtoList.map(orderDto => this.mapDtoToOrder(orderDto))),
        concatAll(),
        toArray(),
        map(orders => {
          console.log("Get orders");
          this.ordersSubject.next(orders);
          return orders;
        })
      );
  }

  postOrder(cartId: string): Observable<Order> {
    return this.http.post<OrderDto>(`${environment.apiUrl}/orders`, {cartId})
      .pipe(
        concatMap(orderDto => this.mapDtoToOrder(orderDto)),
        map(order => {
          console.log(`Post order ${order}`)
          this.ordersValue?.push(order);
          this.ordersSubject.next(this.ordersValue);
          return order;
        })
      );
  }

  deleteOrder(orderId: string) {
    return this.http.delete(`${environment.apiUrl}/orders/${orderId}`)
      .pipe(map(() => {
        console.log(`Deleted ${orderId} order`)
        const orders = this.ordersValue?.filter(order => order.orderId !== orderId);
        this.ordersSubject.next(orders);
      }));
  }

  mapDtoToOrder(orderDto: OrderDto): Observable<Order> {
    return this.mapCartItems(orderDto)
      .pipe(
        toArray(),
        map(itemQuantities => {
          return {
            orderId: orderDto.orderId!,
            orderItems: itemQuantities,
            createdDate: orderDto.createdDate!,
            price: this.orderPrice(itemQuantities),
            isPaid: orderDto.isPaid!,
            payment: new Payment(orderDto.paymentDto?.paymentId!,
              orderDto.paymentDto?.orderId!,
              orderDto.paymentDto?.amount!,
              orderDto.paymentDto?.paymentDate!)
          }
        })
      )
  }

  mapCartItems(orderDto: OrderDto): Observable<ItemQuantity> {
    return from(orderDto.orderItems!)
      .pipe(concatMap(orderItem =>
        from(this.itemService.getById(orderItem.itemId))
          .pipe(map(itemDto => {
            return {
              item: new Item(itemDto),
              quantity: orderItem.quantity
            }
          }))))
  }

  private orderPrice(orderItems: ItemQuantity[]): number {
    return orderItems
      .map(orderItem => parseFloat((orderItem.quantity * parseFloat(orderItem.item.unitPrice)).toFixed(2)))
      .reduce((previousValue, currentValue) => previousValue + currentValue);
  }

  get orders(): Observable<Order[] | undefined> {
    return this._orders;
  }

  get ordersValue(): Order[] | undefined {
    return this.ordersSubject.value;
  }

}
