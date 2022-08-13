package com.abhishek.audioplayer.player;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.abhishek.audioplayer.MPConstants;
import com.abhishek.audioplayer.MainActivity;
import com.abhishek.audioplayer.R;
import com.abhishek.audioplayer.helper.MusicLibraryHelper;
import com.abhishek.audioplayer.model.Music;

public class PlayerNotificationManager {

    private final NotificationManager notificationManager;
    private final PlayerService playerService;
    private NotificationCompat.Builder notificationBuilder;

    PlayerNotificationManager(@NonNull final PlayerService playerService) {
        this.playerService = playerService;
        notificationManager = (NotificationManager) playerService.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public final NotificationManager getNotificationManager() {
        return notificationManager;
    }

    private PendingIntent playerAction(@NonNull final String action) {
        final Intent pauseIntent = new Intent();
        pauseIntent.setAction(action);

        return PendingIntent.getBroadcast(playerService, MPConstants.REQUEST_CODE, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private int getDominantColor(Bitmap bitmap) {
        if (bitmap == null) return Color.BLACK;

        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }

    public Notification createNotification() {
        final Music song = playerService.getPlayerManager().getCurrentMusic();
        notificationBuilder = new NotificationCompat.Builder(playerService, MPConstants.CHANNEL_ID);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        final Intent openPlayerIntent = new Intent(playerService, MainActivity.class);
        openPlayerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent contentIntent = PendingIntent.getActivity(playerService, MPConstants.REQUEST_CODE,
                openPlayerIntent, 0);

        Bitmap albumArt = MusicLibraryHelper.getThumbnail(playerService.getApplicationContext(), song.albumArt);

        notificationBuilder
                .setShowWhen(false)
                .setSmallIcon(R.drawable.ic_notif_music_note)
                .setContentTitle(song.title)
                .setContentText(song.artist)
                .setProgress(100, playerService.getPlayerManager().getCurrentPosition(), true)
                .setColor(getDominantColor(albumArt))
                .setColorized(false)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setLargeIcon(albumArt)
                .addAction(notificationAction(MPConstants.PREV_ACTION))
                .addAction(notificationAction(MPConstants.PLAY_PAUSE_ACTION))
                .addAction(notificationAction(MPConstants.NEXT_ACTION))
                .addAction(notificationAction(MPConstants.CLOSE_ACTION))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        notificationBuilder.setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1, 2));
        return notificationBuilder.build();
    }

    @SuppressLint("RestrictedApi")
    public void updateNotification() {
        if (notificationBuilder == null)
            return;

        notificationBuilder.setOngoing(playerService.getPlayerManager().isPlaying());
        PlayerManager playerManager = playerService.getPlayerManager();
        Music song = playerManager.getCurrentMusic();
        Bitmap albumArt = MusicLibraryHelper.getThumbnail(playerService.getApplicationContext(),
                song.albumArt);

        if (notificationBuilder.mActions.size() > 0)
            notificationBuilder.mActions.set(1, notificationAction(MPConstants.PLAY_PAUSE_ACTION));

        notificationBuilder
                .setLargeIcon(albumArt)
                .setColor(getDominantColor(albumArt));

        notificationBuilder
                .setContentTitle(song.title)
                .setContentText(song.artist)
                .setColorized(false)
                .setSubText(song.album);


        NotificationManagerCompat.from(playerService).notify(MPConstants.NOTIFICATION_ID, notificationBuilder.build());
    }

    @NonNull
    private NotificationCompat.Action notificationAction(@NonNull final String action) {
        int icon = R.drawable.ic_controls_pause;

        switch (action) {
            case MPConstants.PREV_ACTION:
                icon = R.drawable.ic_controls_prev;
                break;
            case MPConstants.PLAY_PAUSE_ACTION:
                icon = playerService.getPlayerManager().isPlaying() ? R.drawable.ic_controls_pause : R.drawable.ic_controls_play;
                break;
            case MPConstants.NEXT_ACTION:
                icon = R.drawable.ic_controls_next;
                break;
            case MPConstants.CLOSE_ACTION:
                icon = R.drawable.ic_close;
                break;
        }
        return new NotificationCompat.Action.Builder(icon, action, playerAction(action)).build();
    }

    @RequiresApi(26)
    private void createNotificationChannel() {

        if (notificationManager.getNotificationChannel(MPConstants.CHANNEL_ID) == null) {
            final NotificationChannel notificationChannel =
                    new NotificationChannel(MPConstants.CHANNEL_ID,
                            playerService.getString(R.string.app_name),
                            NotificationManager.IMPORTANCE_LOW);

            notificationChannel.setDescription(
                    playerService.getString(R.string.app_name));

            notificationChannel.enableLights(false);
            notificationChannel.enableVibration(false);
            notificationChannel.setShowBadge(false);

            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
