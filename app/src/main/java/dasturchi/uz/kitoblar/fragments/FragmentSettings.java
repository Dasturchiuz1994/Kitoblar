package dasturchi.uz.kitoblar.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import dasturchi.uz.kitoblar.R;
import dasturchi.uz.kitoblar.database.MyDatabase;
import dasturchi.uz.kitoblar.functions.Internet;

/**
 * Created by Ahmadjon on 18.01.17.
 */

public class FragmentSettings extends MyFragment implements View.OnClickListener {

    EditText shriftEditText;
    Button saveButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        title="Sozlamalar";
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_settings , container , false);

        saveButton = (Button)parentView.findViewById(R.id.button_save);
        saveButton.setOnClickListener(this);


        shriftEditText = (EditText)parentView.findViewById(R.id.edit_text_shrift);

        shriftEditText.setText(new MyDatabase(activity()).getShrift() + "");

        return parentView;
    }

    @Override
    public boolean back() {
        activity().backFragment();
        return true;
    }

    @Override
    public void update() {

    }

    public static FragmentSettings newInstance() {

        Bundle args = new Bundle();

        FragmentSettings fragment = new FragmentSettings();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        if(view == saveButton){
            try{
                int shrift = Integer.valueOf(shriftEditText.getText().toString());

                new MyDatabase(activity()).setShrift(shrift);

                back();
            }catch (Exception e){
                new MyDatabase(activity()).setShrift(20);

                back();

                e.printStackTrace();
            }

        }
    }
}
