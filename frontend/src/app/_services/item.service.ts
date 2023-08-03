import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {ItemDto} from "@app/_models";
import {environment} from "@environments/environment";
import {Observable} from "rxjs";

@Injectable({
  providedIn: "root"
})
export class ItemService {


  constructor(private http: HttpClient) {
  }

  getById(id: string): Observable<ItemDto> {
    return this.http.get<ItemDto>(`${environment.apiUrl}/items/${id}`);
  }

  getAll(): Observable<Array<ItemDto>> {
    return this.http.get<Array<ItemDto>>(`${environment.apiUrl}/items`);
  }

  postItem(item: ItemDto): Observable<ItemDto> {
    return this.http.post<ItemDto>(`${environment.apiUrl}/items`, item);
  }

  putItem(id: string, item: ItemDto): Observable<ItemDto> {
    return this.http.put<ItemDto>(`${environment.apiUrl}/items/${id}`, item);
  }

  deleteItem(id: string): Observable<Object> {
    return this.http.delete(`${environment.apiUrl}/items/${id}`);
  }

}
