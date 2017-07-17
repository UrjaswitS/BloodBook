package app.com.project.urjaswitk.bloodbook.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import app.com.project.urjaswitk.bloodbook.R;
import app.com.project.urjaswitk.bloodbook.models.BloodBanks;

/**
 * Created by UrJasWitK on 12-Jul-17.
 */

public class BloodBankAdapter extends ArrayAdapter<BloodBanks> {

    public BloodBankAdapter(@NonNull Context context, @NonNull List<BloodBanks> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView = convertView;
        if(listView==null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        BloodBanks bloods = getItem(position);
        TextView name = (TextView) listView.findViewById(R.id.name);
        TextView add = (TextView) listView.findViewById(R.id.address);
        name.setText(bloods.getName());
        add.setText(bloods.getAdd());

        return listView;
    }
}