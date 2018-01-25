package com.bosqueprotector.espol.bosqueprotectorservicios.Utils;

/**
 * Created by joset on 23/01/2018.
 */

import android.content.Context;

public class Identifiers {

    public static String ID_APPLICATION;

    public static void setIdApplication(Context context){
        ID_APPLICATION = Installation.id(context);
    }




}
