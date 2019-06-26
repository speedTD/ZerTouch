package com.assistive.assistivetouch.until

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import com.google.android.gms.ads.*
import com.znitenda.A
import com.znitenda.zah
import com.znitenda.zu

object Ads {

    private var interstitialAd: InterstitialAd? = null

    interface OnAdsListener {
        fun onError()

        fun onAdLoaded()

        fun onAdOpened()

        fun onAdClose()
    }

    fun initBanner(activity: Activity, relativeLayout: RelativeLayout, onAdsListener: OnAdsListener) {
        try {
            Log.e("ADS_ERROR", "initBanner: ${zu.s(activity)} ${TextUtils.isEmpty(zah.getBannerAds(activity))} ${zah.getBannerAds(activity)}")

            if (zu.s(activity) && !TextUtils.isEmpty(zah.getBannerAds(activity))) {
                Log.e("ADS_ERROR", "initBanner: show Ads")
                val adView = AdView(activity)
                adView.adSize = AdSize.BANNER
                adView.adUnitId = zah.getBannerAds(activity)
                val adRequest = AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build()
                adView.adListener = object : AdListener() {
                    override fun onAdFailedToLoad(i: Int) {
                        Log.e("ADS_ERROR", i.toString())
//                        Toast.makeText(activity, "BANNER_ADS_ERROR: $i", Toast.LENGTH_SHORT).show()
                        onAdsListener.onError()
                        super.onAdFailedToLoad(i)
                    }

                    override fun onAdLoaded() {
                        onAdsListener.onAdLoaded()
                        super.onAdLoaded()
                    }

                    override fun onAdOpened() {
                        onAdsListener.onAdOpened()
                        super.onAdOpened()
                    }

                    override fun onAdClosed() {
                        onAdsListener.onAdClose()
                        super.onAdClosed()
                    }
                }
                adView.loadAd(adRequest)

                val rLParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT)
                rLParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1)
                relativeLayout.addView(adView, rLParams)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun loadBannerAds(mActivity: Activity, layoutAds: RelativeLayout) {
        Log.e("ADS_ERROR", "loadBannerAds: show Ads")
        initBanner(mActivity, layoutAds, object : OnAdsListener {
            override fun onError() {
                layoutAds.visibility = View.GONE
            }

            override fun onAdLoaded() {
                layoutAds.visibility = View.VISIBLE
            }

            override fun onAdOpened() {
                layoutAds.visibility = View.VISIBLE
            }

            override fun onAdClose() {
                layoutAds.visibility = View.GONE
                loadBannerAds(mActivity, layoutAds)
            }
        })
    }

    fun largeBanner(activity: Activity, relativeLayout: RelativeLayout) {
        if (zu.s(activity) && !TextUtils.isEmpty(zah.getBannerAds(activity))) {
            val adView = AdView(activity)
            adView.adSize = AdSize.LARGE_BANNER
            adView.adUnitId = zah.getBannerAds(activity)
            val adRequest = AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build()
            adView.loadAd(adRequest)

            val rLParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
            rLParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1)
            relativeLayout.addView(adView, rLParams)
        }
    }

    fun loadFullScreenAds(activity: Activity) {
        A.f(activity)

    }


    private fun showInterstitial() {
        if (interstitialAd!!.isLoaded) {
            interstitialAd!!.show()
        }
    }
}