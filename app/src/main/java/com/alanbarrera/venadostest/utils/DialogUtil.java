package com.alanbarrera.venadostest.utils;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.alanbarrera.venadostest.R;
import com.alanbarrera.venadostest.models.Player;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class DialogUtil
{
    public static void showPlayerDetails(Context context, Player player)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.player_details);

        // Get views
        CircleImageView pdImage = dialog.findViewById(R.id.pd_image);
        TextView pdName = dialog.findViewById(R.id.pd_name);
        TextView pdPosition = dialog.findViewById(R.id.pd_position);
        TextView pdNumber = dialog.findViewById(R.id.pd_number);
        TextView pdBirthday = dialog.findViewById(R.id.pd_birthday);
        TextView pdBirthPlace = dialog.findViewById(R.id.pd_birth_place);
        TextView pdWeight = dialog.findViewById(R.id.pd_weight);
        TextView pdHeight = dialog.findViewById(R.id.pd_height);
        TextView pdLastTeam = dialog.findViewById(R.id.pd_last_team);

        // Set views properties
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

        dialog.show();
    }
}
