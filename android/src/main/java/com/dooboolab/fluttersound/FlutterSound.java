package com.dooboolab.fluttersound;

/*
 * Copyright 2018, 2019, 2020, 2021 Dooboolab.
 *
 * This file is part of Flutter-Sound.
 *
 * Flutter-Sound is free software: you can redistribute it and/or modify
 * it under the terms of the Mozilla Public License version 2 (MPL2.0),
 * as published by the Mozilla organization.
 *
 * Flutter-Sound is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * MPL General Public License for more details.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;

import com.dooboolab.TauEngine.Flauto;

public class FlutterSound implements FlutterPlugin, ActivityAware {
  private FlutterPluginBinding pluginBinding;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
    this.pluginBinding = binding;
    // Нельзя вызывать attachFlautoX здесь, т.к. Activity может быть недоступна
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    this.pluginBinding = null;
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    Activity activity = binding.getActivity();
    Context context = pluginBinding.getApplicationContext();

    // Сохраняем ссылки в Flauto (по аналогии с registerWith)
    Flauto.androidActivity = activity;
    Flauto.androidContext = context;

    // Регистрируем player и recorder
    FlutterSoundPlayerManager.attachFlautoPlayer(context, pluginBinding.getBinaryMessenger());
    FlutterSoundRecorderManager.attachFlautoRecorder(context, pluginBinding.getBinaryMessenger());
  }

  @Override
  public void onDetachedFromActivity() {
    // Очистка активности
    Flauto.androidActivity = null;
  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
    onAttachedToActivity(binding); // просто повторяем attach
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    onDetachedFromActivity(); // повторяем detach
  }
}
