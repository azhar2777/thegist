/*
 *
 */

package com.thegistapp.thegistapp.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import androidx.fragment.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.thegistapp.thegistapp.R;
import com.thegistapp.thegistapp.custom.CustomAsynkLoader;
import com.thegistapp.thegistapp.listener.VolleyCallback;
import com.thegistapp.thegistapp.model.UserClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Util {
	static int CONNECTIONTIMEOUT = 1000*120;
	static StringBuilder sb ;

	private static String USERCLASS = "USERCLASS";
	private static String TAG = "UtilTAG";
	static CustomAsynkLoader mDialog;


	// To check Internet Connection
	public static boolean isInternetAvailable(Context context) {

		ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conManager.getActiveNetworkInfo();
		return !((i == null) || (!i.isConnected()) || (!i.isAvailable()));
	}

	public static String postMethodWay_json(String hostName, String data, Context mContext) throws IOException {
		HttpURLConnection urlConnection = null;

		URL url = new URL(hostName);
		sb = new StringBuilder();
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod("POST");
			urlConnection.setUseCaches(false);
			urlConnection.setConnectTimeout(CONNECTIONTIMEOUT);
			urlConnection.setReadTimeout(CONNECTIONTIMEOUT);
			urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			
			urlConnection.connect();
			OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
			out.write(data);
			out.close();

			int HttpResult = urlConnection.getResponseCode();
			Log.v("HttpResult", "http status"+urlConnection.getResponseCode());
			//Log.v("Check Status", "http Message"+urlConnection.getResponseMessage());
			
			//String response	=	urlConnection.getInputStream();
			
			if (HttpResult == 200) {
				InputStream in = urlConnection.getInputStream();
				InputStreamReader is = new InputStreamReader(in);
				BufferedReader br = new BufferedReader(is);
				String read;
				//Log.v("Check Status1", "http Message"+read.toString());

				while ((read = br.readLine()) != null) {
					// System.out.println(read);
					if(read != null){
						//Log.v("Check Status2", "http Message"+read.toString());
						sb.append(read).append("\n");
						
						read = br.readLine();
					}
					else{
						//Log.v("Check Status2 else", "http Message"+br.readLine());
					}
					

				}
				// the entity
				
				//Log.v("Check Status3", "http Message"+sb.toString());
				return sb.toString();

			}


			else {
				Log.v("ErrorConnectingServer", "Error while connecting server");
				if
				(String.valueOf(urlConnection.getResponseCode()).startsWith("4")) {
					showMessageWithOk(mContext,
							"No internet!\nPlease check your internet connection.");
				}
				else if(String.valueOf(urlConnection.getResponseCode()).startsWith("5")) {
					showMessageWithOk(mContext,
							 "Server not responding!\nPlease try again later.");
				}
				else{
					showMessageWithOk(mContext,
							"Server not responding!\nPlease try again later.");
				}
				System.out.println(" Response code is:: " +
						urlConnection.getResponseCode());
				sb.append(urlConnection.getResponseCode());
				System.out.println(" Response is:: " +urlConnection.getResponseMessage());
				return sb.toString();

			}
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		return sb.toString(); 

	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static String postMethodWay_formEncode(String hostName, String data, Context mContext) throws IOException {
		HttpURLConnection urlConnection = null;

		URL url = new URL(hostName);
		sb = new StringBuilder();
		try {
			byte[] postData = data.getBytes( StandardCharsets.UTF_8 );
			int postDataLength = postData.length;
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod("POST");
			urlConnection.setConnectTimeout(CONNECTIONTIMEOUT);
			urlConnection.setReadTimeout(CONNECTIONTIMEOUT);
			urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			urlConnection.setRequestProperty("charset", "utf-8");
			urlConnection.setRequestProperty("Content-Length", Integer.toString(postDataLength ));
			urlConnection.setUseCaches(false);

			urlConnection.connect();
			OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
			out.write(data);
			out.close();

			int HttpResult = urlConnection.getResponseCode();
			//Log.v("Check Status", "http status"+urlConnection.getResponseCode());
			//Log.v("Check Status", "http Message"+urlConnection.getResponseMessage());

			//String response	=	urlConnection.getInputStream();

			if (HttpResult == 200) {
				InputStream in = urlConnection.getInputStream();
				InputStreamReader is = new InputStreamReader(in);
				BufferedReader br = new BufferedReader(is);
				String read;
				//Log.v("Check Status1", "http Message"+read.toString());

				while ((read = br.readLine()) != null) {
					// System.out.println(read);
					if(read != null){
						//Log.v("Check Status2", "http Message"+read.toString());
						sb.append(read).append("\n");

						read = br.readLine();
					}
					else{
						//Log.v("Check Status2 else", "http Message"+br.readLine());
					}


				}
				// the entity

				//Log.v("Check Status3", "http Message"+sb.toString());
				return sb.toString();

			}


			else {
				if
						(String.valueOf(urlConnection.getResponseCode()).startsWith("4")) {
					showMessageWithOk(mContext,
							"No internet!\nPlease check your internet connection.");
				}
				else if(String.valueOf(urlConnection.getResponseCode()).startsWith("5")) {
					showMessageWithOk(mContext,
							"Server not responding!\nPlease try again later.");
				}
				System.out.println(" Response code is:: " +
						urlConnection.getResponseCode());
				sb.append(urlConnection.getResponseCode());
				System.out.println(" Response is:: " +urlConnection.getResponseMessage());
				return sb.toString();

			}
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		return sb.toString();

	}

	/*
	 * public static String postMethodWay_json(String hostName, String data,
	 * Context mContext) throws IOException {
	 * 
	 * Log.v("hostname", ""+hostName);
	 * 
	 * sb = new StringBuilder(); HttpClient client = getNewHttpClient();
	 * HttpConnectionParams.setConnectionTimeout(client.getParams(),
	 * CONNECTIONTIMEOUT); HttpConnectionParams
	 * .setSoTimeout(client.getParams(), CONNECTIONTIMEOUT); HttpResponse
	 * response;
	 * 
	 * try { HttpPost post = new HttpPost(hostName);
	 * 
	 * StringEntity se = new StringEntity(data); se.setContentType(new
	 * BasicHeader(HTTP.CONTENT_TYPE, "application/json")); post.setEntity(se);
	 * response = client.execute(post); int statuscode =
	 * response.getStatusLine().getStatusCode();
	 * 
	 * 
	 * if (statuscode == 200) {
	 * 
	 * if (response != null) { InputStream in =
	 * response.getEntity().getContent(); // InputStream in = InputStreamReader
	 * is = new InputStreamReader(in);
	 * 
	 * BufferedReader br = new BufferedReader(is); String read = br.readLine();
	 * 
	 * while (read != null) { // System.out.println(read); sb.append(read); read
	 * = br.readLine();
	 * 
	 * } // the entity
	 * 
	 * return sb.toString(); } } else { if
	 * (String.valueOf(statuscode).startsWith("4")) {
	 * showMessageWithOk(mContext,
	 * "No internet!\nPlease check your internet connection."); } else if
	 * (String.valueOf(statuscode).startsWith("5")) {
	 * showMessageWithOk(mContext,
	 * "Server not responding!\nPlease try again later."); }
	 * System.out.println(" Response code is:: " +
	 * response.getStatusLine().getStatusCode()); sb.append(statuscode); return
	 * sb.toString(); }
	 * 
	 * } catch (Exception e) { e.printStackTrace();
	 * sb.append(e.getLocalizedMessage()); Log.v("response server",
	 * ""+sb.toString()); showMessageWithOk( mContext,
	 * "Could not connect at the moment. Please check your internet connection and try again."
	 * ); }
	 * 
	 * return sb.toString(); }
	 */

	/** Hide Soft Keyboard **/
	public static void hideSoftKeyboard(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
	}

	// Show Error Dialog
	public static void showErrorNetworkDialog(final Context context,
			final String message) {
		try {
			((Activity) context).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(context, message, Toast.LENGTH_LONG).show();
				}
			});
		} catch (Exception e) {
			try {
				((FragmentActivity) context).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(context, message, Toast.LENGTH_LONG)
								.show();
					}
				});
			} catch (Exception e2) {
				try {
					((Activity) context.getApplicationContext())
							.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									Toast.makeText(context, message,
											Toast.LENGTH_LONG).show();
								}
							});
				} catch (Exception e3) {
					Toast.makeText(context.getApplicationContext(), message,
							Toast.LENGTH_LONG).show();

				}
			}

		}
	}

	public static void showMessageWithOk(final Context mContext,
			final String message) {
		((Activity) mContext).runOnUiThread(new Runnable() {

			public void run() {
				final AlertDialog.Builder alert = new AlertDialog.Builder(
						mContext);
				alert.setTitle(R.string.app_name);

				alert.setMessage(message);
				alert.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						});
				alert.show();
			}
		});
	}

	public static void showCallBackMessageWithOkCancel(final Context mContext,
			final String message, final AlertDialogCallBack callBack) {
		((Activity) mContext).runOnUiThread(new Runnable() {

			public void run() {
				final AlertDialog.Builder alert = new AlertDialog.Builder(
						mContext);
				alert.setTitle(R.string.app_name);
				alert.setCancelable(false);
				alert.setMessage(message);
				alert.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								callBack.onSubmit();
							}
						});
				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								callBack.onCancel();
							}
						});
				alert.show();
			}
		});
	}


	public static void showCallBackMessageWithOkCancelButton(final Context mContext,
													   final String message, final String positiveButtonText, final String negativeButtonText, final AlertDialogCallBack callBack) {
		((Activity) mContext).runOnUiThread(new Runnable() {

			public void run() {
				final AlertDialog.Builder alert = new AlertDialog.Builder(
						mContext);
				alert.setTitle(R.string.app_name);
				alert.setCancelable(false);
				alert.setMessage(message);
				alert.setPositiveButton(""+positiveButtonText,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
												int whichButton) {
								callBack.onSubmit();
							}
						});
				alert.setNegativeButton(""+negativeButtonText,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int whichButton) {
								callBack.onCancel();
							}
						});
				alert.show();
			}
		});
	}

	public static void showMessageWithOkCallback(
			final Context mContext, final String message,
			final AlertDialogCallBack callBack) {
		((Activity) mContext).runOnUiThread(new Runnable() {

			public void run() {
				final AlertDialog.Builder alert = new AlertDialog.Builder(
						mContext);
				alert.setTitle(R.string.app_name);
				alert.setCancelable(false);
				alert.setMessage(message);
				alert.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								callBack.onSubmit();
							}
						});

				alert.show();
			}
		});
	}

	/** Get the current date and Time **/
	@SuppressLint("SimpleDateFormat")
	public static String getDateTime() {
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date());
		return date;
	}

	
	/* ===================== Function to retry ================ */

	public static void showCallBackMessageWithOkRetry(final Context mContext,
			final String message, final AlertDialogCallBack callBack) {
		((Activity) mContext).runOnUiThread(new Runnable() {

			public void run() {
				final AlertDialog.Builder alert = new AlertDialog.Builder(
						mContext);
				alert.setTitle(R.string.app_name);
				alert.setCancelable(false);
				alert.setMessage(message);
				alert.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								callBack.onSubmit();
							}
						});
				alert.setNegativeButton("Retry",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								callBack.onRetry();
							}
						});
				alert.show();
			}
		});
	}

	// ------------------------------------------------

	// Saving UserClass details
	public static void saveUserClass(final Context mContext, UserClass userClass) {
		SharedPreferences IMISPrefs = mContext.getSharedPreferences(
				"CleanerAppPrefs", Context.MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor = IMISPrefs.edit();
		try {
			prefsEditor.putString(USERCLASS,
					ObjectSerializer.serialize(userClass));
			Log.v("Pref", ""+ ObjectSerializer.serialize(userClass));
		} catch (IOException e) {
			e.printStackTrace();
		}
		prefsEditor.commit();
	}

	// Fetching UserClass details
	public static UserClass fetchUserClass(final Context mContext) {
		SharedPreferences IMISPrefs = mContext.getSharedPreferences(
				"CleanerAppPrefs", Context.MODE_PRIVATE);
		UserClass userClass = null;
		String serializeOrg = IMISPrefs.getString(USERCLASS, null);
		try {
			if (serializeOrg != null) {
				userClass = (UserClass) ObjectSerializer
						.deserialize(serializeOrg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return userClass;
	}


	public static String postWithVolley(String hostName, String data, final Context mContext, final VolleyCallback volleyCallback){





		sb = new StringBuilder();

		try {
			RequestQueue queue = Volley.newRequestQueue(mContext);
			String URL = hostName;
			final String requestBody = data;

			StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {



					//sb.append(response).append("\n");
					volleyCallback.onSuccess(response);
				}

			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Log.e("onErrorResponse", error.toString());

				}
			}) {
				@Override
				public String getBodyContentType() {
					return "application/json; charset=utf-8";
				}

				@Override
				public byte[] getBody() throws AuthFailureError {
					try {
						return requestBody == null ? null : requestBody.getBytes("utf-8");
					} catch (UnsupportedEncodingException uee) {
						VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
						return null;
					}
				}

				@Override
				protected Response<String> parseNetworkResponse(NetworkResponse response) {
					String responseString = "";
					if (response != null) {
						responseString = String.valueOf(response.statusCode);

						// can get more details such as response.headers
					}
					//return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
					Log.i(TAG, sb.toString());
					return super.parseNetworkResponse(response);
				}
			};

			queue.add(stringRequest);



		} catch (Exception e) {

		}
		Log.i(TAG, sb.toString());
		return	sb.toString();
	}


}
