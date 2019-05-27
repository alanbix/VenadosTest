package com.alanbarrera.venadostest.utils;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;

import com.alanbarrera.venadostest.R;
import com.alanbarrera.venadostest.models.Game;

import java.util.Calendar;

public class CalendarUtil
{
    public static void addGameEventToCalendar(Context context, Game game)
    {
        String eventTitle;

        if(game.isLocal())
            eventTitle = context.getString(R.string.venados_name) + " vs. " + game.getOpponent();
        else
            eventTitle = game.getOpponent() + " vs. " + context.getString(R.string.venados_name);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(game.getDatetime());

        Intent calIntent = new Intent(Intent.ACTION_INSERT);
        calIntent.setType("vnd.android.cursor.item/event");
        calIntent.putExtra(CalendarContract.Events.TITLE, eventTitle);
        calIntent.putExtra(CalendarContract.Events.DESCRIPTION, context.getString(R.string.event_description));
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendar.getTimeInMillis());
        context.startActivity(calIntent);
    }
}
