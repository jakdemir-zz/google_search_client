package edu.google.ajaxsearch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchUnit {

    // {"responseData": {
    // "results": [
    // {
    // "GsearchResultClass": "GwebSearch",
    // "unescapedUrl": "http://en.wikipedia.org/wiki/Paris_Hilton",
    // "url": "http://en.wikipedia.org/wiki/Paris_Hilton",
    // "visibleUrl": "en.wikipedia.org",
    // "cacheUrl":
    // "http://www.google.com/search?q\u003dcache:TwrPfhd22hYJ:en.wikipedia.org",
    // "title":
    // "\u003cb\u003eParis Hilton\u003c/b\u003e - Wikipedia, the free encyclopedia",
    // "titleNoFormatting": "Paris Hilton - Wikipedia, the free encyclopedia",
    // "content": "\[1\] In 2006, she released her debut album..."
    // },
    // {
    // "GsearchResultClass": "GwebSearch",
    // "unescapedUrl": "http://www.imdb.com/name/nm0385296/",
    // "url": "http://www.imdb.com/name/nm0385296/",
    // "visibleUrl": "www.imdb.com",
    // "cacheUrl":
    // "http://www.google.com/search?q\u003dcache:1i34KkqnsooJ:www.imdb.com",
    // "title": "\u003cb\u003eParis Hilton\u003c/b\u003e",
    // "titleNoFormatting": "Paris Hilton",
    // "content":
    // "Self: Zoolander. Socialite \u003cb\u003eParis Hilton\u003c/b\u003e..."
    // },
    // ...
    // ],
    // "cursor": {
    // "pages": [
    // { "start": "0", "label": 1 },
    // { "start": "4", "label": 2 },
    // { "start": "8", "label": 3 },
    // { "start": "12","label": 4 }
    // ],E
    // "estimatedResultCount": "59600000",
    // "currentPageIndex": 0,
    // "moreResultsUrl":
    // "http://www.google.com/search?oe\u003dutf8\u0026ie\u003dutf8..."
    // }
    // }
    // , "responseDetails": null, "responseStatus": 200}

    private JSONObject getSearchResponse(String searchText) throws Exception {

	URL url = new URL("http://ajax.googleapis.com/ajax/services/search/web?v=1.0&lr=lang_tr&q=" + searchText);
	// URL url = new URL(Constants.AJAX_SEARCH_URL + "v=1.0&lr=lang_tr&q=" +
	// searchText + "+site:searchinthissite.com");
	URLConnection connection = url.openConnection();
	connection.addRequestProperty("Referer", "http://www.mysite.com/index.html");

	String line;
	StringBuilder builder = new StringBuilder();
	BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	while ((line = reader.readLine()) != null) {
	    builder.append(line);
	}

	JSONObject json = new JSONObject(builder.toString());
	return json;
    }

    private boolean searchTerms(String terms) throws Exception, JSONException {
	String encodedTerms = URLEncoder.encode(terms);
	JSONObject json = getSearchResponse(encodedTerms);
	// System.out.println(json.toString(3));
	JSONObject responseData = json.getJSONObject("responseData");
	JSONArray jsonArray = responseData.getJSONArray("results");
	if ((jsonArray != null) && (!jsonArray.isNull(0))) {
	    JSONObject result0 = (JSONObject) jsonArray.get(0);
	    String url = (String) result0.get("url");
	    System.out.println("Term : " + terms);
	    System.out.println(url);
	    return true;
	} else {
	    System.out.println("Not FOUND : " + terms);
	    return false;
	}
    }

    public static void main(String[] args) {
	SearchUnit unit = new SearchUnit();
	String keywords = "Jak Akdemir";
	try {
	    unit.searchTerms(keywords);
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
}