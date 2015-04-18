/**
 * Copyright (c) 2014-present, Facebook, Inc. All rights reserved.
 *
 * You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 * copy, modify, and distribute this software in source code or binary form for use
 * in connection with the web services and APIs provided by Facebook.
 *
 * As with any software that integrates with the Facebook platform, your use of
 * this software is subject to the Facebook Developer Principles and Policies
 * [http://developers.facebook.com/policy/]. This copyright notice shall be
 * included in all copies or substantial portions of the software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.facebook.samples.messenger.send;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.messenger.MessengerThreadParams;
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Main Activity for sample.
 */
public class MainActivity extends Activity {

  // This is the request code that the SDK uses for startActivityForResult. See the code below
  // that references it. Messenger currently doesn't return any data back to the calling
  // application.
  private static final int REQUEST_CODE_SHARE_TO_MESSENGER = 1;

  private Toolbar mToolbar;
  private View mMessengerButton;
  private View mGetLinkButton;
  private MessengerThreadParams mThreadParams;
  private boolean mPicking;
    private Map<String,String> mParams;
//    private String magicUrl;


  String tipJSON(String message, String text_amount) {
    return "{ 'message' : message, 'text_amount': text_amount }";
  }



    public void getTipLinkClient() {
//http://requestb.in/prh364pr
        ChangeTipClient.post("prh364pr", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String string) {
                // Pull out the first event on the public timeline
             Log.i("I", string);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                // Pull out the first event on the public timeline
                Log.i("I", jsonArray.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                // Pull out the first event on the public timeline
                Log.i("I", jsonObject.toString());
            }
        });

    }

    public String getTipLinkDefaultHTTP()   throws Exception{

        InputStream inputStream = null;
        String result = "";
        //

        DefaultHttpClient httpclient = new DefaultHttpClient();

        HttpPost httpost = new HttpPost("https://api.changetip.com/v2/tip-url/?access_token=5G9nix6pXVSMuxctOQxta3NTjn09aY");
//        HttpPost httpost = new HttpPost("http://requestb.in/107ysyc1");
//        http://requestb.in/107ysyc1
//        mParams = new HashMap<String,String>();
//        mParams.put("message","tip");
//        mParams.put("text_amount","$1");
//        JSONObject jsonObject = new JSONObject(mParams);
//        HttpParams httpParams = new BasicHttpParams();
//        httpParams.setParameter("message", "Tip");
//        httpParams.setParameter("text_amount", "$1");
//        httpost.setParams(httpParams);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("message", "here"));
        nameValuePairs.add(new BasicNameValuePair("text_amount", "$1"));
        httpost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//        RequestParams params =  new RequestParams();
//        params.put("message","here");
//        httpost.setParams(params);
//        httpost.setParams();

//        String json = jsonObject.toString();

        //passes the results to a string builde
//        StringEntity se = new StringEntity(json);

        //sets the post request as the resulting string
//        httpost.setEntity(se);

        //sets a request header so the page receving the request
        //will know what to do with it
//        httpost.setHeader("Accept", "application/json");
        httpost.setHeader("Content-type", "application/x-www-form-urlencoded");

        //Handles what is returned from the page
        HttpResponse httpResponse = httpclient.execute(httpost);

        inputStream = httpResponse.getEntity().getContent();

        result = convertInputStringToString(inputStream);
        Log.i("I", result);
//       return result;
        JSONObject mainObject = new JSONObject(result);

        String magicURL = mainObject.getString("magic_url");
        return magicURL;

    }

    private static String convertInputStringToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result ="";
        while ((line = bufferedReader.readLine()) != null )
            result += line;

        inputStream.close();
        return result;

    }

    private static JSONObject getJsonObjectFromMap(Map params) throws JSONException {

        //all the passed parameters from the post request
        //iterator used to loop through all the parameters
        //passed in the post request
        Iterator iter = params.entrySet().iterator();

        //Stores JSON
        JSONObject holder = new JSONObject();

        //using the earlier example your first entry would get email
        //and the inner while would get the value which would be 'foo@bar.com'
        //{ fan: { email : 'foo@bar.com' } }

        //While there is another entry
        while (iter.hasNext())
        {
            //gets an entry in the params
            Map.Entry pairs = (Map.Entry)iter.next();

            //creates a key for Map
            String key = (String)pairs.getKey();

            //Create a new map
            Map m = (Map)pairs.getValue();

            //object for storing Json
            JSONObject data = new JSONObject();

            //gets the value
            Iterator iter2 = m.entrySet().iterator();
            while (iter2.hasNext())
            {
                Map.Entry pairs2 = (Map.Entry)iter2.next();
                data.put((String)pairs2.getKey(), (String)pairs2.getValue());
            }

            //puts email and 'foo@bar.com'  together in map
            holder.put(key, data);
        }
        return holder;
    }


    public void getTipLink() {
        String url ="https://api.changetip.com";
        String requestBin = "http://requestb.in/107ysyc1";

        RequestParams params = new RequestParams();
        params.put("message","Tip");
        params.put("text_amount","$1");

//        JSONObject jsonParams = new JSONObject();
//        jsonParams.put("message", "Tip");
//        jsonParams.put("text_amount", "$1");
//        StringEntity entity = new StringEntity(jsonParams.toString());

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(requestBin, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                Log.i("I", response.toString());


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("I", responseBody.toString());
            }
        });



    }
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.main_activity);
    mToolbar = (Toolbar) findViewById(R.id.toolbar);
    mMessengerButton = findViewById(R.id.messenger_send_button);

    // mToolbar.setTitle(R.string.app_name);
    findViewById(R.id.getLinkButton).setOnClickListener(new View.OnClickListener()
    {

        public void onClick(View v) {
            // Do stuff
            Log.i("IGOR", "Button clicked");

            try {
                new HttpAsyncTask().execute();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.i("IGOR", "Got link");

            // Set ChangeTip URL
            // EditText editor = (EditText)findViewById(R.id.editText);
            // editor.setTextColor(Color.BLACK);
            // editor.setText("http://tip.me/once/VCsD-d2NyZs7G");

//            TextView textView = (TextView) findViewById(R.id.textView2);
//            textView.setText("http://tip.me/once/VCsD-d2NyZs7G");
//            textView.setTextColor(Color.BLACK);

            HttpClient httpClient = new DefaultHttpClient();

        }
    });

    // If we received Intent.ACTION_PICK from Messenger, we were launched from a composer shortcut
    // or the reply flow.
    Intent intent = getIntent();
    if (Intent.ACTION_PICK.equals(intent.getAction())) {
      mThreadParams = MessengerUtils.getMessengerThreadParamsForIntent(intent);
      mPicking = true;

      // Note, if mThreadParams is non-null, it means the activity was launched from Messenger.
      // It will contain the metadata associated with the original content, if there was content.
    }

    mMessengerButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onMessengerButtonClicked();
        }
    });
  }


  private void onMessengerButtonClicked() {
    // The URI can reference a file://, content://, or android.resource. Here we use
    // android.resource for sample purposes.
    Uri uri =
        Uri.parse("android.resource://com.facebook.samples.messenger.send/" + R.drawable.qr);

    // Create the parameters for what we want to send to Messenger.
    ShareToMessengerParams shareToMessengerParams =
        ShareToMessengerParams.newBuilder(uri, "image/jpeg")
            .setMetaData("{ \"caption\" : \"test\" }")
            .build();

    if (mPicking) {
      // If we were launched from Messenger, we call MessengerUtils.finishShareToMessenger to return
      // the content to Messenger.
      MessengerUtils.finishShareToMessenger(this, shareToMessengerParams);
    } else {
      // Otherwise, we were launched directly (for example, user clicked the launcher icon). We
      // initiate the broadcast flow in Messenger. If Messenger is not installed or Messenger needs
      // to be upgraded, this will direct the user to the play store.
      MessengerUtils.shareToMessenger(
          this,
          REQUEST_CODE_SHARE_TO_MESSENGER,
          shareToMessengerParams);
    }
  }

    private class HttpAsyncTask extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... unused) {
            String magicUrl = "";

            try {
                magicUrl = getTipLinkDefaultHTTP();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return magicUrl;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            TextView textView = (TextView) findViewById(R.id.textView2);
            textView.setText(result);
            textView.setTextColor(Color.BLACK);


            Toast.makeText(getBaseContext(), "You've generated tip. Send it!", Toast.LENGTH_LONG).show();
        }
    }
}
