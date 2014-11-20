An Android ```WebViewClient``` that allows you inject your own JavaScript into
```WebViews``` on the client-side.

**Get started**<br/>
Put any scripts you have in ```assets/```*\<dir_name\>*```/```*\<filename\>* <br/>
e.g 

    assets/scripts/my_script.js

Have a WebView with JavaScript enabled and a WebChromeClient to delegate to...

    WebView webview = [....]; // initialize
    webview.getSettings().setJavaScriptEnabled(true);
    webview.setWebChromeClient(new WebChromeClient());
    
Load up your scripts

    File script = AssetsUtil.getAssetFile(context, "scripts", "my_script.js"); // Catch IOExceptions!

Add them to your injector

    WebViewInjectorClient client = WebViewInjectorClient();
    client.appendScripts(script);
    
Add your injector to the ```WebView```

    webview.setWebViewClient(client);
    
Do whatever!

    webview.loadUrl("http://www.example.com");
    
**What's the license?**<br/>
Apache v2
   
**This is terrible**<br/>
That's not a question.
    
**A note from the author:** <br/>
By using this, you're conceivably exposing yourself to XSS vulnerabilities.

Use your judgment.<br/>
Don't risk your customers data and privacy.<br/>
Eat your veggies.
