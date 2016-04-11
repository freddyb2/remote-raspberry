package com.fbo.remoteraspberry.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.fbo.remoteraspberry.R;
import com.fbo.remoteraspberry.data.LinkBundle;
import com.fbo.remoteraspberry.internet.GenericWebAsyncTask;
import com.fbo.remoteraspberry.internet.NopSustainable;
import com.fbo.remoteraspberry.internet.Sustainable;
import com.fbo.remoteraspberry.ui.AlertDialogUtils;
import com.fbo.remoteraspberry.util.ResourceUtils;
import com.fbo.remoteraspberry.util.TypeConvertionUtils;
import com.fbo.services.core.model.Bounds;
import com.fbo.services.core.model.ConstraintKind;
import com.fbo.services.core.model.GenericParameter;
import com.fbo.services.core.model.ParameterValue;
import com.fbo.services.core.resource.LinksResource;
import com.fbo.services.core.resource.ParameterResource;

import org.springframework.hateoas.Link;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class ServiceActivity extends Activity {

    public static final String PREVIOUS_HREF_EXTRA_ID = "PREVIOUS_HREF_EXTRA_ID";
    public static final String REL_LINKS_EXTRA_ID = "REL_LINKS_EXTRA_ID";
    public static final String HREF_LINKS_EXTRA_ID = "HREF_LINKS_EXTRA_ID";
    public static final String TITLE_EXTRA_ID = "TITLE_EXTRA_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        if (savedInstanceState == null) {
            LinkListFragment linkListFragment = new LinkListFragment();
            linkListFragment.setArguments(getExtras(getIntent()));
            getFragmentManager().beginTransaction()
                    .add(R.id.service_container, linkListFragment)
                    .commit();
        }
        setTitle(getIntent().getStringExtra(TITLE_EXTRA_ID));
    }

    private static Bundle getExtras(Intent intent) {
        Bundle args = new Bundle();
        args.putStringArray(REL_LINKS_EXTRA_ID, intent.getStringArrayExtra(REL_LINKS_EXTRA_ID));
        args.putStringArray(HREF_LINKS_EXTRA_ID, intent.getStringArrayExtra(HREF_LINKS_EXTRA_ID));
        return args;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_service, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                if (!hasPreviousAndGoBack()) {
                    finish();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean hasPreviousAndGoBack() {
        String previousHref = getIntent().getStringExtra(PREVIOUS_HREF_EXTRA_ID);
        if (previousHref != null) {
            followToLinkList(previousHref, this, (Sustainable) getFragmentManager().findFragmentById(R.id.service_container));
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (!hasPreviousAndGoBack()) {
            super.onBackPressed();
        }
        ;
    }

    public static void followToLinkList(final String uri, final Activity activity, Sustainable sustainable) {
        final RestTemplate restTemplate = buildRestTemplate();
        GenericWebAsyncTask<String, LinksResource> longTask = new GenericWebAsyncTask<String, LinksResource>(sustainable, activity, uri) {
            @Override
            protected LinksResource remoteServiceAccess(String input) {
                return restTemplate.getForObject(input, LinksResource.class);
            }

            @Override
            protected void treatResult(LinksResource output) {
                ServiceActivity.launch(activity, output);
            }
        };
        longTask.execute();
    }

    public static void followToEditor(final String uri, final Activity activity, Sustainable sustainable) {
        final RestTemplate restTemplate = buildRestTemplate();
        GenericWebAsyncTask<String, ParameterResource> longTask = new GenericWebAsyncTask<String, ParameterResource>(sustainable, activity, uri) {
            @Override
            protected ParameterResource remoteServiceAccess(String input) {
                return restTemplate.getForObject(uri, ParameterResource.class);
            }

            @Override
            protected void treatResult(ParameterResource output) {
                ParameterResource output2 = output;
                GenericParameter genericParameter = output.getContent();
                ConstraintKind kind = genericParameter.getConstraintKind();
                final String putRef = output.getLink(Link.REL_SELF).getHref();
                switch (kind) {
                    case NONE:
                        GenericParameter<String> tParameter = genericParameter;
                        editStringParameter(tParameter.getObjectValue(), putRef);
                        break;
                    case INT_BOUNDS:
                        GenericParameter<Integer> iParameter = genericParameter;
                        editIntParameter(activity, iParameter, putRef);
                        break;
                    case ENUM:
                        GenericParameter<String> eParameter = genericParameter;
                        editEnumParameter(activity, eParameter, putRef);
                        break;
                }
            }
        };
        longTask.execute();
    }

    private static void editStringParameter(String value, String putRef) {

    }

    private static void editIntParameter(final Context context, final GenericParameter<Integer> eParameter, final String putRef) {
        AlertDialogUtils.showIntegerChoiceDialog(
                context,
                eParameter.getName(),
                eParameter.getBounds().getMin(),
                eParameter.getBounds().getMax(),
                new Integer(eParameter.getObjectValue()),
                new AlertDialogUtils.Consumer<Integer>() {
            @Override
            public void consume(Integer value) {
                validEditParameter(context, putRef, value);
            }
        });
    }

    private static void editEnumParameter(final Context context, final GenericParameter<String> eParameter, final String putRef) {
        int selected = Arrays.asList(eParameter.getPossibleValues()).indexOf(eParameter.getObjectValue());
        final String[] choices = TypeConvertionUtils.toStringArray(eParameter.getPossibleValues());
        AlertDialogUtils.showSingleChoiceDialog(context, eParameter.getName(), choices, selected, new AlertDialogUtils.Consumer<Integer>() {
            @Override
            public void consume(Integer object) {
                validEditParameter(context, putRef, choices[object]);
            }
        });
    }

    private static <T> void validEditParameter(Context context, final String href, T value) {
        final RestTemplate restTemplate = buildRestTemplate();
        GenericWebAsyncTask<T, Void> longTask = new GenericWebAsyncTask<T, Void>(new NopSustainable(), context, value) {
            @Override
            protected Void remoteServiceAccess(T input) {
                restTemplate.put(href, new ParameterValue(input.toString()));
                return null;
            }

            @Override
            protected void treatResult(Void output) {
                // Do nothing
            }
        };
        longTask.execute();
    }


    private static void launch(Context context, LinksResource linksResource) {
        Intent intent = new Intent(context, ServiceActivity.class);
        LinkBundle displayedLinks = new LinkBundle(ResourceUtils.getDisplayedLinks(linksResource));
        intent.putExtra(REL_LINKS_EXTRA_ID, displayedLinks.getRels());
        intent.putExtra(HREF_LINKS_EXTRA_ID, displayedLinks.getHrefs());
        Link previous = ResourceUtils.getPreviousLink(linksResource);
        if (previous != null) {
            intent.putExtra(PREVIOUS_HREF_EXTRA_ID, previous.getHref());
        }
        intent.putExtra(TITLE_EXTRA_ID, linksResource.getContent());
        context.startActivity(intent);
    }

    private static RestTemplate buildRestTemplate() {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }


}
