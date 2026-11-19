import { Component, effect, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../auth/service/auth.service';
import { DashboardService } from './dashboard.service';
import { DashboardData, Platform, PlatformAccount, SyncStatus, UserProfile } from './dashboard.models';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard {
  protected readonly authService = inject(AuthService);
  private readonly dashboardService = inject(DashboardService);

  protected readonly profile = signal<UserProfile | null>(null);
  protected readonly accounts = signal<PlatformAccount[]>([]);
  protected readonly dashboard = signal<DashboardData | null>(null);
  protected readonly loading = signal(false);
  protected readonly saving = signal(false);
  protected readonly error = signal('');
  protected readonly platform = signal<Platform>('CODEFORCES');
  protected handle = '';

  protected readonly platforms: { value: Platform; label: string; icon: string }[] = [
    { value: 'CODEFORCES', label: 'Codeforces', icon: 'pi-code' },
    { value: 'LEETCODE', label: 'LeetCode', icon: 'pi-bolt' },
    { value: 'GFG', label: 'GeeksForGeeks', icon: 'pi-book' }
  ];

  constructor() {
    effect(() => {
      if (this.authService.isAuthenticated() && !this.profile()) {
        void this.loadDashboard();
      }
    });
  }

  protected login(): void {
    this.authService.login();
  }

  protected async loadDashboard(): Promise<void> {
    const claims = this.authService.getIdentityClaims();
    if (!claims?.['sub']) {
      return;
    }

    this.loading.set(true);
    this.error.set('');
    try {
      const currentProfile = await this.dashboardService.upsertProfile({
        displayName: claims['preferred_username'] ?? claims['name'] ?? 'Developer',
        email: claims['email'] ?? ''
      }).toPromise();
      if (!currentProfile) {
        throw new Error('Profile could not be loaded.');
      }
      this.profile.set(currentProfile);
      const [summary, linkedAccounts] = await Promise.all([
        this.dashboardService.getDashboard(currentProfile.id).toPromise(),
        this.dashboardService.getAccounts(currentProfile.id).toPromise()
      ]);
      this.dashboard.set(summary ?? null);
      this.accounts.set(linkedAccounts ?? []);
    } catch {
      this.error.set('We could not load your CodeGap workspace. Please try again.');
    } finally {
      this.loading.set(false);
    }
  }

  protected async linkAccount(): Promise<void> {
    const currentProfile = this.profile();
    const trimmedHandle = this.handle.trim();
    if (!currentProfile || !trimmedHandle) {
      return;
    }

    this.saving.set(true);
    this.error.set('');
    try {
      const account = await this.dashboardService
        .linkAccount(currentProfile.id, this.platform(), trimmedHandle)
        .toPromise();
      if (account) {
        this.accounts.update((accounts) => {
          const withoutPlatform = accounts.filter((item) => item.platform !== account.platform);
          return [...withoutPlatform, account];
        });
      }
      this.handle = '';
    } catch {
      this.error.set('That account could not be linked. Check the handle and try again.');
    } finally {
      this.saving.set(false);
    }
  }

  protected async unlinkAccount(account: PlatformAccount): Promise<void> {
    const currentProfile = this.profile();
    if (!currentProfile) return;

    try {
      await this.dashboardService.unlinkAccount(account.id, currentProfile.id).toPromise();
      this.accounts.update((accounts) => accounts.filter((item) => item.id !== account.id));
    } catch {
      this.error.set('The account could not be removed. Please try again.');
    }
  }

  protected async syncAccount(account: PlatformAccount): Promise<void> {
    try {
      await this.dashboardService.syncAccount(account.id).toPromise();
      this.accounts.update((accounts) =>
        accounts.map((item) => item.id === account.id ? { ...item, syncStatus: 'PENDING' as SyncStatus } : item)
      );
    } catch {
      this.error.set('Sync could not be requested. Please try again.');
    }
  }

  protected platformLabel(platform: Platform): string {
    return this.platforms.find((item) => item.value === platform)?.label ?? platform;
  }

  protected platformIcon(platform: Platform): string {
    return this.platforms.find((item) => item.value === platform)?.icon ?? 'pi-code';
  }

  protected statusLabel(status: SyncStatus): string {
    return status.charAt(0) + status.slice(1).toLowerCase();
  }

  protected formatDate(value: string | null): string {
    return value ? new Date(value).toLocaleDateString() : 'Not synced yet';
  }
}
