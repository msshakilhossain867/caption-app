package com.citomoltd.romanticstatusandcaptions;

import android.app.Activity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class AdHelper {

    private static long lastAdTime = 0;
    private static final long AD_INTERVAL = 3 * 60 * 1000; // 3 minutes interval

    private static InterstitialAd mInterstitialAd;
    private static boolean isAdLoading = false;

    private static RewardedAd mRewardedAd;
    private static boolean isRewardLoading = false;

    private static AdView mAdView;
    private static boolean isBannerAdLoading = false;

    public interface AdListener {
        void onAdClosed();
    }

    public interface RewardListener {
        void onRewardEarned();
    }

    public static void initializeAd(Activity activity) {
        loadNewAd(activity);
        loadRewardedAd(activity);
    }

    // -------- Interstitial Ad --------
    public static void showAdIfNeeded(Activity activity, AdListener listener) {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastAdTime > AD_INTERVAL) {
            if (mInterstitialAd != null) {
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        Log.d("AdHelper", "Interstitial dismissed.");
                        lastAdTime = System.currentTimeMillis();
                        if (listener != null) listener.onAdClosed();
                        loadNewAd(activity);
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        Log.d("AdHelper", "Interstitial failed: " + adError.getMessage());
                        if (listener != null) listener.onAdClosed();
                        loadNewAd(activity);
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        mInterstitialAd = null;
                    }
                });
                mInterstitialAd.show(activity);
            } else {
                Log.d("AdHelper", "Interstitial not ready, loading...");
                loadNewAd(activity);
                if (listener != null) listener.onAdClosed();
            }
        } else {
            if (listener != null) listener.onAdClosed();
        }
    }

    private static void loadNewAd(Activity activity) {
        if (isAdLoading) return;
        isAdLoading = true;

        AdRequest adRequest = new AdRequest.Builder().build();
        //test ca-app-pub-5488667449529842/8311434319
        InterstitialAd.load(activity, "id ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        isAdLoading = false;
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        mInterstitialAd = null;
                        isAdLoading = false;
                    }
                });
    }

    // -------- Rewarded Ad --------
    public static void showRewardedAd(Activity activity, RewardListener listener) {
        if (mRewardedAd != null) {
            mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    Log.d("AdHelper", "Rewarded dismissed.");
                    loadRewardedAd(activity);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    Log.d("AdHelper", "Rewarded failed: " + adError.getMessage());
                    loadRewardedAd(activity);
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    mRewardedAd = null;
                }
            });

            mRewardedAd.show(activity, rewardItem -> {
                Log.d("AdHelper", "User earned reward: " + rewardItem.getAmount());
                if (listener != null) listener.onRewardEarned();
            });

        } else {
            Toast.makeText(activity, "Please check your internet. Ad not ready!", Toast.LENGTH_SHORT).show();
            loadRewardedAd(activity);
        }
    }

    private static void loadRewardedAd(Activity activity) {
        if (isRewardLoading) return;
        isRewardLoading = true;

        AdRequest adRequest = new AdRequest.Builder().build();
        //test Ads ca-app-pub-5488667449529842/8666657538
        RewardedAd.load(activity, "ca-app-pub-3940256099942544/5224354917", adRequest,
                new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        isRewardLoading = false;
                        Log.d("AdHelper", "Rewarded Ad Loaded");
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        mRewardedAd = null;
                        isRewardLoading = false;
                        Log.d("AdHelper", "Rewarded failed: " + loadAdError.getMessage());
                    }
                });
    }

    // -------- Banner Ad --------
    public static void loadBannerAd(Activity activity, FrameLayout adContainer) {
        if (isBannerAdLoading) return;
        isBannerAdLoading = true;

        mAdView = new AdView(activity);
        mAdView.setAdSize(AdSize.BANNER);
        //test Ad ca-app-pub-3940256099942544/6300978111
        mAdView.setAdUnitId("ca-app-pub-5488667449529842/5876842668");
        adContainer.removeAllViews();
        adContainer.addView(mAdView);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() {
                isBannerAdLoading = false;
                Log.d("AdHelper", "Banner Loaded");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                isBannerAdLoading = false;
                Log.d("AdHelper", "Banner failed: " + loadAdError.getMessage());
            }
        });
    }

    public static void clearBannerAd() {
        if (mAdView != null) {
            mAdView.destroy();
            mAdView = null;
        }
    }
}
