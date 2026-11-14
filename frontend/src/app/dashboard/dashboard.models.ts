export type Platform = 'CODEFORCES' | 'LEETCODE' | 'GFG';
export type SyncStatus = 'PENDING' | 'SYNCING' | 'SYNCED' | 'FAILED';

export interface UserProfile {
  id: string;
  keycloakId: string;
  displayName: string;
  email: string;
}

export interface PlatformAccount {
  id: string;
  platform: Platform;
  handle: string;
  verified: boolean;
  lastSyncedAt: string | null;
  syncStatus: SyncStatus;
}

export interface DashboardData {
  overview: {
    totalPlatformsLinked: number;
    strengthScore: number;
    weakestTopic: string;
  };
  topics: unknown[];
  recommendations: unknown[];
  emptyState: boolean;
}
