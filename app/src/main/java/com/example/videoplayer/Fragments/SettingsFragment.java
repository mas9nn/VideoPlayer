package com.example.videoplayer.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.videoplayer.R;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SettingsFragment extends Fragment {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private TextView forward, backward, seconds_forward, seconds_backward, clear_history;
    private int back, frw;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, null);
        pref = Objects.requireNonNull(getActivity()).getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        forward = v.findViewById(R.id.forward_peremotka);
        backward = v.findViewById(R.id.backforw_peremotka);
        seconds_backward = v.findViewById(R.id.seconds_backford);
        seconds_forward = v.findViewById(R.id.seconds_forward);
        clear_history = v.findViewById(R.id.clear_history);





        seconds_backward.setText(pref.getInt("backward", 5) + "");
        seconds_forward.setText(pref.getInt("forward", 5) + "");

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_peremotka);
                RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
                frw = pref.getInt("forwardId", -789);
                if (frw != -789) {
                    radioGroup.check(frw);
                }
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {

                        RadioButton btn = radioGroup.findViewById(i);
                        seconds_forward.setText(btn.getText());
                        switch (i) {
                            case R.id.five:
                                editor.putInt("forward", 5);
                                editor.putInt("forwardId", i);
                                editor.commit();
                                dialog.dismiss();
                                break;
                            case R.id.ten:
                                editor.putInt("forward", 10);
                                editor.putInt("forwardId", i);
                                editor.commit();
                                dialog.dismiss();
                                break;
                            case R.id.fifteen:
                                editor.putInt("forward", 15);
                                editor.putInt("forwardId", i);
                                editor.commit();
                                dialog.dismiss();
                                break;
                            case R.id.twenty:
                                editor.putInt("forward", 20);
                                editor.putInt("forwardId", i);
                                editor.commit();
                                dialog.dismiss();
                                break;
                            case R.id.therty:
                                editor.putInt("forward", 30);
                                editor.putInt("forwardId", i);
                                editor.commit();
                                dialog.dismiss();
                                break;
                            case R.id.sixty:
                                editor.putInt("forward", 60);
                                editor.putInt("forwardId", i);
                                editor.commit();
                                dialog.dismiss();
                                break;
                        }
                    }
                });
                dialog.show();
            }
        });
        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_peremotka);
                RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
                back = pref.getInt("backwardId", -789);
                if (back != -789) {
                    radioGroup.check(back);
                }
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {

                        RadioButton btn = radioGroup.findViewById(i);
                        seconds_backward.setText(btn.getText());
                        switch (i) {
                            case R.id.five:
                                editor.putInt("backward", 5);
                                editor.putInt("backwardId", i);
                                editor.commit();
                                dialog.dismiss();
                                break;
                            case R.id.ten:
                                editor.putInt("backward", 10);
                                editor.putInt("backwardId", i);
                                editor.commit();
                                dialog.dismiss();
                                break;
                            case R.id.fifteen:
                                editor.putInt("backward", 15);
                                editor.putInt("backwardId", i);
                                editor.commit();
                                dialog.dismiss();
                                break;
                            case R.id.twenty:
                                editor.putInt("backward", 20);
                                editor.putInt("backwardId", i);
                                editor.commit();
                                dialog.dismiss();
                                break;
                            case R.id.therty:
                                editor.putInt("backward", 30);
                                editor.putInt("backwardId", i);
                                editor.commit();
                                dialog.dismiss();
                                break;
                            case R.id.sixty:
                                editor.putInt("backward", 60);
                                editor.putInt("backwardId", i);
                                editor.commit();
                                dialog.dismiss();
                                break;
                        }
                    }
                });
                dialog.show();
            }
        });


        clear_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext(),R.style.AlertDialog).create();
                alertDialog.setMessage("Вы уверены, что хотите очистить историю поиска?");

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                editor.putString("set",null);
                                editor.commit();
                                alertDialog.dismiss();
                            }
                        });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "ОТМЕНА", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}