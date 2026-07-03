package com.resona.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

private const val GITHUB_REPO_URL = "https://github.com/Dev-Aditya-More/Resona"
private const val PRIVACY_POLICY_URL = "$GITHUB_REPO_URL/blob/master/Privacy_Policy.md"
private const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id="

fun openUrl(context: Context, url: String) {
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    } catch (_: ActivityNotFoundException) {
        // No browser available; silently ignore.
    }
}

fun openGithubRepo(context: Context) = openUrl(context, GITHUB_REPO_URL)

fun openPrivacyPolicy(context: Context) = openUrl(context, PRIVACY_POLICY_URL)

fun openPlayStoreListing(context: Context, packageName: String = context.packageName) {
    try {
        context.startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")).apply {
                setPackage("com.android.vending")
            }
        )
    } catch (_: ActivityNotFoundException) {
        openUrl(context, "$PLAY_STORE_URL$packageName")
    }
}

fun shareApp(context: Context, packageName: String = context.packageName) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, "Check out Resona, an offline music player: $PLAY_STORE_URL$packageName")
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share Resona"))
}
