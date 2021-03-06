package saher.y.toprssfeeds;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Saher on 11/20/2015.
 */
public class RssDataParser {
    String xmlTitle = null;
    String xmlLine = null;
    String xmlDescribtion = null;
    String xmlPubData = null;
    String xmlImg = null;
    private static final String ns = null;
    ArrayList<Item> items = new ArrayList<Item>();

    public ArrayList parse(InputStream inputStream) throws XmlPullParserException, IOException {
        try {
            XmlPullParser xmlParser = Xml.newPullParser();
            xmlParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlParser.setInput(inputStream, null);
            xmlParser.nextTag();
            readFeed(xmlParser);
            Log.d("RssDataParser", "Array: " + items.toString());
            return items;
        } finally {
            inputStream.close();
        }
    }

    public static class Item {
        public final String title;
        public final String link;
        public final String description;
        public final String pubDate;
        public final String thumbnail;

        public Item(String title, String link, String description, String pubDate, String img) {
            this.title = title;
            this.link = link;
            this.description = description;
            this.pubDate = pubDate;
            this.thumbnail = img;
        }
    }

    private void readFeed(XmlPullParser xmlParser) throws XmlPullParserException, IOException {
        xmlParser.require(XmlPullParser.START_TAG, ns, "rss");
        while (xmlParser.next() != XmlPullParser.END_TAG) {
            if (xmlParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tagName = xmlParser.getName();
            if (tagName.equals("channel")) {
                readChannel(xmlParser);
            } else {
                skip(xmlParser);
            }
        }
    }

    private void readChannel(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException{
        xmlPullParser.require(XmlPullParser.START_TAG, ns, "channel");
        while (xmlPullParser.next() != XmlPullParser.END_TAG) {
            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tagName = xmlPullParser.getName();
            if (tagName.equals("image")) {
                readImage(xmlPullParser);
            }
            else if (tagName.equals("item")) {
                readItems(xmlPullParser);
            }
            else {
                skip(xmlPullParser);
            }
        }
    }

    private void readImage(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, ns, "image");
        while (xmlPullParser.next() != XmlPullParser.END_TAG) {
            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tagName = xmlPullParser.getName();
            if (tagName.equals("url")) {
                xmlImg = readString(xmlPullParser, "url");
            }
            else {
                skip(xmlPullParser);
            }
        }
    }

    private void readItems(XmlPullParser xmlParser) throws XmlPullParserException, IOException {
        xmlParser.require(XmlPullParser.START_TAG, ns, "item");
        while (xmlParser.next() != XmlPullParser.END_TAG) {
            if (xmlParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xmlParser.getName();
            if (name.equals("title")) {
                xmlTitle = readString(xmlParser, "title");
            } else if (name.equals("link")) {
                xmlLine = readString(xmlParser, "link");
            } else if (name.equals("description")) {
                xmlDescribtion = readString(xmlParser, "description");
            } else if (name.equals("pubData")) {
                xmlPubData = readString(xmlParser, "pubData");
            } else if (name.equals("url")) {
                xmlImg = readString(xmlParser, "url");
            } else {
                skip(xmlParser);
            }
        }
        items.add(new Item(xmlTitle, xmlLine, xmlDescribtion, xmlPubData, xmlImg));
    }

    private String readString(XmlPullParser xmlParser, String tag) throws XmlPullParserException, IOException {
        xmlParser.require(XmlPullParser.START_TAG, ns, tag);
        String title = readText(xmlParser);
        xmlParser.require(XmlPullParser.END_TAG, ns, tag);
        return title;
    }

    private String readText(XmlPullParser xmlParser) throws XmlPullParserException, IOException {
        String result = "";
        if (xmlParser.next() == XmlPullParser.TEXT) {
            result = xmlParser.getText();
            xmlParser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser xmlParser) throws XmlPullParserException, IOException {
        if (xmlParser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (xmlParser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}

