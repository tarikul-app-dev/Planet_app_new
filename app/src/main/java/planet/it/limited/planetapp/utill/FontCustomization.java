package planet.it.limited.planetapp.utill;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Tarikul on 4/12/2018.
 */

public class FontCustomization {
    public Typeface TexgyreHerosRegular;
    public Typeface merlin;
    Context context;

    public FontCustomization(Context mContext){
        this.context = mContext;
        this.TexgyreHerosRegular = Typeface.createFromAsset(context.getAssets(),"texgyreheros-regular.otf") ;
        this.merlin = Typeface.createFromAsset(context.getAssets(),"merlin.ttf") ;

    }
    public Typeface getTexgyreHerosRegular(){
        return TexgyreHerosRegular;
    }
    public Typeface getMerlin(){
        return merlin;
    }
}
