import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { tap, catchError, map } from 'rxjs/operators';
import { Osoba } from '../interface/osoba';
import { OsobaVM } from '../interface/osoba-vm';

@Injectable({
  providedIn: 'root'
})
export class OsobaService {

  private readonly apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  getOsobas(): Observable<Osoba[]> {
    return this.http.get<Osoba[]>(`${this.apiUrl}/osoba/`)
      .pipe(
        tap(console.log),
        map(osobas => osobas.map(osoba => ({
          photoOfAPerson: "data:image/png;base64," + osoba.photoOfAPerson,
          id: osoba.id,
          name: osoba.name,
          surname: osoba.surname,
          homePhone: osoba.homePhone,
          officePhone: osoba.officePhone,
          email: osoba.email
        }))),
        catchError(this.handleError)
      );
  }

  createOsoba(osoba: Osoba): Observable<Osoba> {
    return this.http.post<Osoba>(`${this.apiUrl}/osoba/`, osoba)
    .pipe(
      tap(console.log),
      map(osoba => {
        return { ...osoba, photoOfAPerson: "data:image/png;base64," + osoba.photoOfAPerson }
      }),
      catchError(this.handleError)
    );
  }

  updateOsoba(osoba: Osoba): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/osoba/`, osoba);
  }

  deleteOsoba(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/osoba/${id}`);
  }

  findOsobaFilter(osobaVM: OsobaVM): Observable<Osoba[]> {
    return this.http.post<Osoba[]>(`${this.apiUrl}/osoba/filter`, osobaVM)
      .pipe(
        tap(console.log),
        map(osobas => osobas.map(osoba => ({
          photoOfAPerson: "data:image/png;base64," + osoba.photoOfAPerson,
          id: osoba.id,
          name: osoba.name,
          surname: osoba.surname,
          homePhone: osoba.homePhone,
          officePhone: osoba.officePhone,
          email: osoba.email
        }))),
        catchError(this.handleError)
      );
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    console.log(error);
    return throwError(`An error occurred - Error code: ${error.status}`)
  }
}
