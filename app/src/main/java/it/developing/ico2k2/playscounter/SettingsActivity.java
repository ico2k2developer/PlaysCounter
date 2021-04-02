package it.developing.ico2k2.playscounter;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsActivity extends BaseActivity
{
    /*public static final String ARGUMENT_PREFERENCE = "preference";
    public static final String ARGUMENT_INDEX = "index";
    public static final String LICENSE_APACHE_2_0 = "apache 2.0";

    private final PreferenceHeader[] headers =
            {
                    new PreferenceHeader(R.string.key_mediafile,R.attr.ic_file,true,R.drawable.ic_file_material_dark,false),
                    new PreferenceHeader(R.string.key_theme,R.attr.ic_image,true,R.drawable.ic_image_material_dark,false),
                    new PreferenceHeader(R.string.key_advanced,R.attr.ic_settings,true,R.drawable.ic_settings_material_dark,false),
                    new PreferenceHeader(R.string.key_licenses,R.attr.ic_license,true,R.drawable.ic_license_material_dark,false),
                    new PreferenceHeader(R.string.key_about,R.attr.ic_info,true,R.drawable.ic_info_material_dark,false),
            };


    private RecyclerView list;
    private PreferenceAdapter adapter;

    protected class PreferenceHeader
    {
        int icon1,icon2,title;
        boolean attr1,attr2;

        public PreferenceHeader(int titleRes,int icon1Res,boolean isIcon1Attr,int icon2Res,boolean isIcon2Attr)
        {
            title = titleRes;
            icon1 = icon1Res;
            icon2 = icon2Res;
            attr1 = isIcon1Attr;
            attr2 = isIcon2Attr;
        }

        public PreferenceHeader(int titleRes,int iconRes,boolean isIconAttr)
        {
            this(titleRes,iconRes,isIconAttr,iconRes,isIconAttr);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ListView list = new ListView(this);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            LinearLayout layout = new LinearLayout(this);
            layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
            layout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight = 1;
            list.setLayoutParams(params);
            FrameLayout frameLayout = new FrameLayout(this);
            params.weight = 2;
            frameLayout.setLayoutParams(params);
            layout.addView(list);
            layout.addView(frameLayout);
            setContentView(layout);
        }
        else
        {
            list.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
            setContentView(list);
        }
        setBackButtonEnabled(true);
        adapter = new PreferenceAdapter(headers);
        adapter.setOnPreferenceHeaderClickListener(new OnPreferenceHeaderClickListener(){
            @Override
            public void onPreferenceHeaderClick(int position){
                showHeader(headers[position].title,position);
            }
        });
        list.setAdapter(adapter);
        handleIntent(getIntent());
        getMainSharedPreferences().edit().putInt(getString(R.string.key_notification_tint),getColorPrimary()).apply();
    }

    @Override
    public void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent)
    {
        if(onIsMultiPane())
        {
            Bundle extras = intent.getExtras();
            int index = 0;
            if(extras != null)
            {
                if(extras.containsKey(ARGUMENT_INDEX))
                {
                    index = extras.getInt(ARGUMENT_INDEX);
                }
            }
            Log.d(getClass().getSimpleName(),"Settings: " + index);
            View v = list.getChildAt(index);
            if(v == null)
            {
                final int i = index;
                list.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
                    @Override
                    public void onGlobalLayout(){
                        list.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        list.getChildAt(i).performClick();
                    }
                });
            }
            else
            {
                v.performClick();
            }
        }
    }

    protected void showHeader(boolean multipane,int headerTitle,int position)
    {
        if(multipane)
        {
            showFragment(this,headerTitle,position);
            adapter.setSelected(position);
            adapter.notifyDataSetChanged();
        }
        else
        {
            Intent intent = new Intent(this,SettingsSecondaryActivity.class);
            intent.putExtra(ARGUMENT_PREFERENCE,headerTitle);
            intent.putExtra(ARGUMENT_INDEX,position);
            startActivity(intent);
        }
    }

    protected void showHeader(int headerTitle,int position)
    {
        showHeader(onIsMultiPane(),headerTitle,position);
    }

    protected static void showFragment(BaseActivity activity,int headerTitle,int position)
    {
        Fragment fragment;
        Bundle bundle = new Bundle();
        bundle.putInt(ARGUMENT_PREFERENCE,headerTitle);
        bundle.putInt(ARGUMENT_INDEX,position);
        FragmentFactory factory = activity.getSupportFragmentManager().getFragmentFactory();
        String className;
        switch(headerTitle)
        {
            case R.string.key_mediafile:
            case R.string.key_advanced:
            case R.string.key_licenses:
            {
                className = SettingsPreferenceFragment.class.getName();
                break;
            }
            default:
            {
                className = SettingsFragment.class.getName();
            }
        }
        fragment = activity.getSupportFragmentManager().getFragmentFactory().instantiate(activity.getClassLoader(),className);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.fade_out,android.R.anim.fade_in,android.R.anim.slide_out_right);
        transaction.replace(R.id.settings_frame,fragment);
        transaction.commit();
        activity.setTitle(headerTitle);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        protected TextView textView;
        protected ViewHolder(TextView v) {
            super(v);
            textView = v;
        }

        public void bind(final int position,final OnPreferenceHeaderClickListener listener)
        {
            textView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(listener != null)
                        listener.onPreferenceHeaderClick(position);
                }
            });
        }
    }

    public interface OnPreferenceHeaderClickListener
    {
        void onPreferenceHeaderClick(int position);
    }

    protected class PreferenceAdapter extends SimpleListAdapter
    {
        private final PreferenceHeader[] list;
        private final int margin;
        private final int background;
        private final int backgroundSelected;
        private int selected = -1;
        private final int textColor;
        private final int textColorSelected;
        private OnPreferenceHeaderClickListener listener;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder

        // Provide a suitable constructor (depends on the kind of dataset)
        public PreferenceAdapter(PreferenceHeader[] headers)
        {
            super(headers.length,SettingsActivity.this.getLayoutInflater(),android.R.layout.simple_list_item_1,);
            list = headers;
            margin = getResources().getDimensionPixelSize(R.dimen.default_small_margin);
            TypedValue value = new TypedValue();
            getTheme().resolveAttribute(android.R.attr.selectableItemBackground,value,true);
            background = value.resourceId;
            getTheme().resolveAttribute(android.R.attr.textColorPrimary,value,true);
            textColor = ContextCompat.getColor(SettingsActivity.this,value.resourceId);
            textColorSelected = ContextCompat.getColor(SettingsActivity.this,android.R.color.white);
            backgroundSelected = getColorAccent();
        }

        protected void setOnPreferenceHeaderClickListener(OnPreferenceHeaderClickListener clickListener)
        {
            listener = clickListener;
        }

        protected void setSelected(int position)
        {
            selected = position;
        }

        public int getSelected()
        {
            return selected;
        }

        // Create new views (invoked by the layout manager)
        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
            // create a new view
            TextView v = (TextView)LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1,parent,false);
            v.setCompoundDrawablePadding(margin);
            v.setClickable(true);
            v.setFocusable(true);
            return new ViewHolder(v);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder,int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.bind(position,listener);
            holder.textView.setText(list[position].title);
            Drawable drawable;
            if(position == selected)
            {
                holder.textView.setBackgroundColor(backgroundSelected);
                holder.textView.setTextColor(textColorSelected);
                if(list[position].attr2)
                {
                    TypedValue typedValue = new TypedValue();
                    getTheme().resolveAttribute(list[position].icon2,typedValue,true);
                    drawable = ContextCompat.getDrawable(SettingsActivity.this,typedValue.resourceId);
                }
                else
                    drawable = ContextCompat.getDrawable(SettingsActivity.this,list[position].icon2);
            }
            else
            {
                holder.textView.setBackgroundResource(background);
                holder.textView.setTextColor(textColor);
                if(list[position].attr1)
                {
                    TypedValue typedValue = new TypedValue();
                    getTheme().resolveAttribute(list[position].icon1,typedValue,true);
                    drawable = ContextCompat.getDrawable(SettingsActivity.this,typedValue.resourceId);
                }
                else
                    drawable = ContextCompat.getDrawable(SettingsActivity.this,list[position].icon1);
            }
            holder.textView.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
            //holder.textView.setCompoundDrawablePadding();

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return list.length;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        boolean result = true;
        switch(item.getItemId())
        {
            case android.R.id.home:
            {
                onBackPressed();
                break;
            }
            default:
            {
                result = super.onOptionsItemSelected(item);
            }
        }
        return result;
    }

    public static boolean onIsMultiPane(Configuration configuration){
        return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public boolean onIsMultiPane(){
        return onIsMultiPane(getResources().getConfiguration());
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle)
    {
        int a = adapter.getSelected();
        if(a >= 0)
            bundle.putInt(ARGUMENT_INDEX,a);
        super.onSaveInstanceState(bundle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle bundle)
    {
        super.onRestoreInstanceState(bundle);
        if(bundle.containsKey(ARGUMENT_INDEX))
        {
            int a = bundle.getInt(ARGUMENT_INDEX);
            showHeader(headers[a].title,a);
        }
        int a = adapter.getSelected();
        if(a >= 0)
            bundle.putInt(ARGUMENT_INDEX,a);
    }

    public static class SettingsSecondaryActivity extends BaseActivity
    {
        private int position = 0;

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_settings_secondary);
            setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            handleIntent(getIntent());
        }

        @Override
        public void onNewIntent(Intent intent)
        {
            super.onNewIntent(intent);
            handleIntent(intent);
        }

        protected void handleIntent(Intent intent)
        {
            int headerTitle = R.string.settings;
            Bundle extras = intent.getExtras();
            if(extras != null)
            {
                if(extras.containsKey(ARGUMENT_PREFERENCE))
                    headerTitle = extras.getInt(ARGUMENT_PREFERENCE);
                if(extras.containsKey(ARGUMENT_INDEX))
                    position = extras.getInt(ARGUMENT_INDEX);
            }
            showFragment(this,headerTitle,position);
        }

        @Override
        public void onStart() {
            super.onStart();
            if(onIsMultiPane(getResources().getConfiguration()))
            {
                Intent intent = new Intent(this,SettingsActivity.class);
                intent.putExtra(ARGUMENT_INDEX,position);
                startActivity(intent);
                finish();
            }
        }
    }

    public static class SettingsFragment extends Fragment
    {
        private static final String LIST_TITLE = "list title";

        private int preference = 0,preferenceIndex = -1;

        public SettingsFragment()
        {
            super();
        }

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
            Bundle arguments = getArguments();
            if(arguments != null)
            {
                if(arguments.containsKey(ARGUMENT_PREFERENCE))
                {
                    preference = arguments.getInt(ARGUMENT_PREFERENCE);
                }
                if(arguments.containsKey(ARGUMENT_INDEX))
                {
                    preferenceIndex = arguments.getInt(ARGUMENT_INDEX);
                }
            }
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container,Bundle savedInstance)
        {
            int layout = 0;
            switch(preference)
            {
                case R.string.key_theme:
                {
                    layout = R.layout.preference_theme;
                    break;
                }
                case R.string.key_about:
                {
                    layout = R.layout.preference_about;
                    break;
                }
            }
            View result;
            if(layout == 0)
                result = new View(getActivity());
            else
                result = inflater.inflate(layout,container,false);
            return result;
        }

        @Override
        public void onViewCreated(@NonNull final View view,@Nullable Bundle savedInstanceState)
        {
            View parent = (View)view.getParent();
            if(parent != null)
                parent.setPadding(0,0,0,0);
            switch(preference)
            {
                case R.string.key_theme:
                {
                    final SharedPreferences prefs = ((BaseActivity)getActivity()).getMainSharedPreferences();
                    final AppCompatSpinner themeSpinner = view.findViewById(R.id.theme_spinner);
                    final ArrayList<String> items = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.themes)));
                    final ArrayList<Integer> themes = new ArrayList<>(items.size());
                    int a;
                    for(a = 0; a < items.size(); a++)
                    {
                        String item = items.get(a);
                        try
                        {
                            themes.add(a,Utils.getThemeFromName(item));
                        }
                        catch(Exception e)
                        {
                            //themes.add(a,-1);
                            items.remove(item);
                        }
                    }
                    SimpleAdapter adapter = new SimpleAdapter(getActivity(),adapterMapsFromAdapterList(items,LIST_TITLE),android.R.layout.simple_list_item_1,new String[]{LIST_TITLE},new int[]{android.R.id.text1});
                    themeSpinner.setAdapter(adapter);
                    themeSpinner.setSelection(themes.indexOf(prefs.getInt(getString(R.string.key_theme),0)));
                    themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                        @Override
                        public void onItemSelected(AdapterView<?> parent,View v,int position,long id){
                            int currentTheme = prefs.getInt(getString(R.string.key_theme),0);
                            if(currentTheme == themes.get(position))
                                view.findViewById(R.id.theme_apply).setVisibility(View.GONE);
                            else
                                view.findViewById(R.id.theme_apply).setVisibility(View.VISIBLE);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent){

                        }
                    });
                    view.findViewById(R.id.theme_apply).setVisibility(View.GONE);
                    view.findViewById(R.id.theme_apply).setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            prefs.edit().putInt(getString(R.string.key_theme),themes.get(themeSpinner.getSelectedItemPosition())).apply();
                            Intent intent = new Intent(getActivity(),getActivity().getClass());
                            intent.putExtra(ARGUMENT_PREFERENCE,preference);
                            intent.putExtra(ARGUMENT_INDEX,preferenceIndex);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    });
                    break;
                }
                case R.string.key_about:
                {
                    Button playStore = view.findViewById(R.id.about_play_store);
                    Button share = view.findViewById(R.id.about_share);
                    PackageManager manager = getActivity().getPackageManager();
                    String name;
                    try
                    {
                        name = manager.getApplicationInfo(getString(R.string.google_play_store_package),0).loadLabel(manager).toString();
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                        name = "Google Play";
                    }
                    playStore.setText(name);
                    playStore.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            Intent playPage = new Intent(Intent.ACTION_VIEW);
                            playPage.setData(Uri.parse(getString(R.string.google_play_store_url)));
                            startActivity(playPage);
                        }
                    });
                    share.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            Intent send = new Intent(Intent.ACTION_SEND);
                            send.putExtra(Intent.EXTRA_TEXT,getString(R.string.app_about_share,getString(R.string.app_name),getString(R.string.google_play_store_url)));
                            send.setType("text/*");
                            startActivity(send);
                        }
                    });
                    break;
                }
            }
        }
    }

    public static class SettingsPreferenceFragment extends PreferenceFragmentCompat
    {
        private int preference = 0;

        public SettingsPreferenceFragment()
        {
            super();
        }

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
            Bundle arguments = getArguments();
            if(arguments != null)
            {
                if(arguments.containsKey(ARGUMENT_PREFERENCE))
                {
                    preference = arguments.getInt(ARGUMENT_PREFERENCE);
                }
            }
            switch(preference)
            {
                case R.string.key_mediafile:
                {
                    addPreferencesFromResource(R.xml.mediafile_settings);
                    break;
                }
                case R.string.key_advanced:
                {
                    addPreferencesFromResource(R.xml.advanced_settings);
                    break;
                }
                case R.string.key_licenses:
                {
                    addPreferencesFromResource(R.xml.preference_licenses);
                    break;
                }
            }
        }



        @Override
        public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
        {
            switch(preference)
            {
                case R.string.key_advanced:
                {
                    getPreferenceScreen().findPreference(getString(R.string.key_delete_all))
                            .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
                            {
                                @Override
                                public boolean onPreferenceClick(Preference preference)
                                {
                                    ConfirmDialog dialog = new ConfirmDialog(getActivity());
                                    dialog.setTitleAndIcon(preference);
                                    dialog.setCancelable(true);
                                    dialog.setNegativeButton(android.R.string.cancel);
                                    dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ((BaseActivity)getActivity()).getMainSharedPreferences().edit().clear().apply();
                                            getActivity().onBackPressed();
                                        }
                                    });
                                    dialog.show();
                                    return true;
                                }
                            });
                    getPreferenceScreen().findPreference(getString(R.string.key_prefs_data))
                            .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
                            {
                                @Override
                                public boolean onPreferenceClick(Preference preference)
                                {
                                    startActivity(new Intent(getActivity(),PrefsViewActivity.class));
                                    return true;
                                }
                            });
                    break;
                }
                case R.string.key_licenses:
                {
                    int a;
                    for(a = 0; a < getPreferenceScreen().getPreferenceCount(); a++)
                    {
                        getPreferenceScreen().getPreference(a).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                            @Override
                            public boolean onPreferenceClick(Preference preference){
                                DefaultDialog dialog = new DefaultDialog(getActivity());
                                switch(preference.getKey())
                                {
                                    case LICENSE_APACHE_2_0:
                                    {
                                        dialog.setMessage(getString(R.string.apache_2_0_license));
                                        break;
                                    }
                                    default:
                                    {
                                        dialog.setMessage(getString(R.string.missing_license));
                                    }
                                }
                                dialog.show();
                                return false;
                            }
                        });
                    }
                    break;
                }
            }
            return super.onCreateView(inflater,container,savedInstanceState);
        }

        public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
        {
            setPreferenceScreen(getPreferenceScreen());
        }

    }*/
}
