package it.developing.ico2k2.playscounter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class BaseActivity extends Activity
{
    @RequiresApi(11)
    protected void setBackButtonEnabled(boolean enabled)
    {
        getActionBar().setHomeButtonEnabled(enabled);
        getActionBar().setDisplayHomeAsUpEnabled(enabled);
    }
}
