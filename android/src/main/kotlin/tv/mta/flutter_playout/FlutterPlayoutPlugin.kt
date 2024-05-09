package tv.mta.flutter_playout

import android.app.Activity
import android.util.Log
import tv.mta.flutter_playout.audio.AudioPlayer
import tv.mta.flutter_playout.video.PlayerViewFactory

import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding

class FlutterPlayoutPlugin: FlutterPlugin, ActivityAware {

  private lateinit var activity : Activity

  private var playerViewFactory : PlayerViewFactory? = null

  private var audioPlayerFactory : AudioPlayer? = null

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    try {
      playerViewFactory = PlayerViewFactory.registerWith(
              flutterPluginBinding.platformViewRegistry,
              flutterPluginBinding.binaryMessenger,
              activity)
    } catch (e: Exception) {
      Log.w("FlutterPlayout", "Failed initializing playerViewFactory", e)
    }

    try {
      audioPlayerFactory = AudioPlayer.registerWith(flutterPluginBinding.binaryMessenger,
              activity, flutterPluginBinding.applicationContext)
    } catch (e: Exception) {
      Log.w("FlutterPlayout", "Failed initializing audioPlayerFactory", e)
    }
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    playerViewFactory?.onDetachActivity()
    audioPlayerFactory?.onDetachActivity()
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity
    playerViewFactory?.onAttachActivity(binding.activity)
    audioPlayerFactory?.onAttachActivity(binding.activity)
  }

  override fun onDetachedFromActivityForConfigChanges() {
    playerViewFactory?.onDetachActivity()
    audioPlayerFactory?.onDetachActivity()
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    activity = binding.activity
    playerViewFactory?.onAttachActivity(binding.activity)
    audioPlayerFactory?.onAttachActivity(binding.activity)
  }

  override fun onDetachedFromActivity() {
    playerViewFactory?.onDetachActivity()
    audioPlayerFactory?.onDetachActivity()
  }
}
