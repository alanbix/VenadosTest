package com.alanbarrera.venadostest.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.widget.TextView;

import com.alanbarrera.venadostest.R;
import com.alanbarrera.venadostest.models.Game;
import com.alanbarrera.venadostest.models.Player;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class Utilities
{
    /**
     * Add a game event to the calendar
     * @param context Context.
     * @param game A Game object
     */
    public static void addGameEventToCalendar(Context context, Game game)
    {
        // Declare the event title.
        String eventTitle;

        // Set the title according which is the local team.
        if(game.isLocal())
            eventTitle = context.getString(R.string.venados_name) + " vs. " + game.getOpponent();
        else
            eventTitle = game.getOpponent() + " vs. " + context.getString(R.string.venados_name);

        // Set calendar time.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(game.getDatetime());

        // Start the calendar app and pass the event data.
        Intent calIntent = new Intent(Intent.ACTION_INSERT);
        calIntent.setType("vnd.android.cursor.item/event");
        calIntent.putExtra(CalendarContract.Events.TITLE, eventTitle);
        calIntent.putExtra(CalendarContract.Events.DESCRIPTION, context.getString(R.string.event_description));
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendar.getTimeInMillis());
        context.startActivity(calIntent);
    }

    /**
     * Show a dialog containing the player full information.
     * @param context Context
     * @param player A player
     */
    public static void showPlayerDetails(Context context, Player player)
    {
        // Create a dialog and set its view.
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.player_details);

        // Get view elements.
        CircleImageView pdImage = dialog.findViewById(R.id.pd_image);
        TextView pdName = dialog.findViewById(R.id.pd_name);
        TextView pdPosition = dialog.findViewById(R.id.pd_position);
        TextView pdNumber = dialog.findViewById(R.id.pd_number);
        TextView pdBirthday = dialog.findViewById(R.id.pd_birthday);
        TextView pdBirthPlace = dialog.findViewById(R.id.pd_birth_place);
        TextView pdWeight = dialog.findViewById(R.id.pd_weight);
        TextView pdHeight = dialog.findViewById(R.id.pd_height);
        TextView pdLastTeam = dialog.findViewById(R.id.pd_last_team);

        // Set view elements properties
        Glide.with(dialog.getContext()).load(player.getImage()).into(pdImage);
        String fullName = player.getName() + " " + player.getFirstSurname() + " " + player.getSecondSurname();
        pdName.setText(fullName);
        pdPosition.setText(player.getPosition());
        pdNumber.setText(String.valueOf(player.getNumber()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        pdBirthday.setText(dateFormat.format(player.getBirthday()));
        pdBirthPlace.setText(player.getBirthPlace());
        String weight = player.getWeight() + " KG";
        pdWeight.setText(weight);
        String height = player.getHeight() + " M";
        pdHeight.setText(height);
        pdLastTeam.setText(player.getLastTeam());

        // Show the dialog with the player full information.
        dialog.show();
    }
}
