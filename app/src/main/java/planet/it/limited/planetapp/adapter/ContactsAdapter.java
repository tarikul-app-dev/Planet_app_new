package planet.it.limited.planetapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import planet.it.limited.planetapp.R;
import planet.it.limited.planetapp.model.ContactModel;


/**
 * Created by Tarikul on 6/7/2018.
 */

public class ContactsAdapter extends BaseAdapter {
    private ArrayList<ContactModel> mcontacts;
    private ArrayList<ContactModel> contactList = new ArrayList<ContactModel>();

    private LayoutInflater mInflater;
    Context mContext;



    public ContactsAdapter(ArrayList<ContactModel> contact, Context context) {
        this.mcontacts = contact;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
       // mcontacts = new ArrayList<>();

    }


    @Override
    public int getCount() {

        return mcontacts.size();
    }

    @Override
    public ContactModel getItem(int position) {
        try {
            if (mcontacts != null) {
                return mcontacts.get(position);
            } else {
                return null;
            }
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position,  View convertView,  ViewGroup parent) {

        final ViewHolder holder;
        final ContactModel contact = getItem(position);

        LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = vi.inflate(R.layout.contact_list_item, null);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



            String phone = contact.getContactNumber();
            String name = contact.getUserName();

            holder.phoneNumber.setText(phone);
            holder.name.setText(name);


        return convertView;
    }





    // /////////// //
    // ViewHolder //
    // ///////// //
    private ViewHolder createViewHolder(View v) {
        ViewHolder holder = new ViewHolder();
        holder.phoneNumber = (TextView) v.findViewById(R.id.txv_user_number);
        holder.name = (TextView) v.findViewById(R.id.txv_user_name);
        return holder;
    }
    private   class ViewHolder {
        TextView phoneNumber,name;

    }




}
