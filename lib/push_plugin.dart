import 'dart:async';

import 'package:flutter/services.dart';

class PushPlugin {
  factory PushPlugin() {
    if (_instance == null) {
      final MethodChannel methodChannel = const MethodChannel('push_plugin');
      final EventChannel eventChannel = const EventChannel('push_event');
      _instance = new PushPlugin.private(methodChannel, eventChannel);
    }
    return _instance;
  }

  PushPlugin.private(this._methodChannel, this._eventChannel);

  final MethodChannel _methodChannel;

  final EventChannel _eventChannel;

  static PushPlugin _instance;

  Stream<dynamic> _listener;

  Future<String> get platformVersion async {
    final String version =
        await _methodChannel.invokeMethod('getPlatformVersion');
    return version;
  }

  Stream<dynamic> get onMessage {
    if (_listener == null) {
      _listener = _eventChannel
          .receiveBroadcastStream()
          .asyncMap((dynamic event) => _parseMsg(event));
    }
    return _listener;
  }

  dynamic _parseMsg(event) {
    return event;
  }

  void bindAccount(String uid) {
    _methodChannel.invokeMethod(
      'bindAccount',
      <String, Object>{'uid': uid},
    );
  }

  void unbindAccount() {
    _methodChannel.invokeMethod('unbindAccount');
  }
}
