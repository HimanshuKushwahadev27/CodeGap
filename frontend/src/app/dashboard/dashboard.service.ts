import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { DashboardData, Platform, PlatformAccount, UserProfile } from './dashboard.models';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private readonly http = inject(HttpClient);

  upsertProfile(profile: { displayName: string; email: string }): Observable<UserProfile> {
    return this.http.post<UserProfile>('/api/users/profile', profile);
  }

  getDashboard(userId: string): Observable<DashboardData> {
    return this.http.get<DashboardData>('/api/analysis/dashboard', { params: { userId } });
  }

  getAccounts(userId: string): Observable<PlatformAccount[]> {
    return this.http.get<PlatformAccount[]>(`/api/platform-accounts/user/${userId}`);
  }

  linkAccount(userId: string, platform: Platform, handle: string): Observable<PlatformAccount> {
    return this.http.post<PlatformAccount>('/api/platform-accounts', { userId, platform, handle });
  }

  unlinkAccount(accountId: string, userId: string): Observable<void> {
    return this.http.delete<void>(`/api/platform-accounts/${accountId}/user/${userId}`);
  }

  syncAccount(accountId: string): Observable<void> {
    return this.http.post<void>(`/api/platform-accounts/${accountId}/sync`, {});
  }
}
