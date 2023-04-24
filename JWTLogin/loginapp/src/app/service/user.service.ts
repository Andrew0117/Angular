import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';
import { User } from '../model/user';
import { environment } from '../environments/environment';
import { JwtResponse } from '../model/jwtResponse';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  public apiUrl = environment.apiUrl;
  private jwtHelper = new JwtHelperService();

  constructor(private http: HttpClient) { }

  login(user: User): Observable<HttpResponse<JwtResponse>> {
    return this.http.post<JwtResponse>(`${this.apiUrl}/api/auth/authenticate`, user, { observe: 'response' });
  }

  addTokenToCache(token: string): void {
    localStorage.setItem('token', token);
  }

  getTokenFromCache(): string {
    return localStorage.getItem('token') || '';
  }

  logOut(): void {
    localStorage.removeItem('user');
    localStorage.removeItem('token');
  }

  getTokenExpirationDate(): Date | null {
    return this.jwtHelper.getTokenExpirationDate(this.getTokenFromCache());
  }

  isUserLoggedIn(): boolean {
    if (this.getTokenFromCache() &&
      this.jwtHelper.decodeToken(this.getTokenFromCache()).sub &&
      !this.jwtHelper.isTokenExpired(this.getTokenFromCache())) {
      return true;
    } else {
      this.logOut();
      return false;
    }
  }
}
