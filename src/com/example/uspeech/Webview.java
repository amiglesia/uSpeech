package com.example.uspeech;



import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Webview extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web);
		
		String url = "http://192.168.10.50/thesis/";
		WebView webView = (WebView) this.findViewById(R.id.webView1);
		
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(url);
		
		webView.setWebViewClient(new WebViewClient() {
		    public boolean shouldOverrideUrlLoading(WebView view, String url){
		        // do your handling codes here, which url is the requested url
		        // probably you need to open that url rather than redirect:
		        view.loadUrl(url);
		        return false; // then it is not handled by default action
		   }
		    
		    @Override
		    public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
		        try {
		            webView.stopLoading();
		        } catch (Exception e) {
		        }
		        try {
		            webView.clearView();
		        } catch (Exception e) {
		        }
		        if (webView.canGoBack()) {
		            webView.goBack();
		        }
		        webView.loadUrl("file:///android_asset/www/error.html");
		        super.onReceivedError(webView, errorCode, description, failingUrl);
		    }
		    
		  
		    
		});
	}

}
