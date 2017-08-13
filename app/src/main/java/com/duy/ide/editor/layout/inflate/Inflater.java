package com.duy.ide.editor.layout.inflate;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileReader;

/**
 * Created by Duy on 13-Aug-17.
 */

public class Inflater {
    private ViewGroup container;
    private TextView txtError;
    private LayoutInflater layoutInflater;

    public Inflater(ViewGroup container, TextView txtError) {
        this.container = container;
        this.layoutInflater = LayoutInflater.from(container.getContext());
        this.txtError = txtError;
    }

    public void inflate(File file) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = null;
            xpp = factory.newPullParser();
            xpp.setInput(new FileReader(file));
            layoutInflater.inflate(xpp, container);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            txtError.setText(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            txtError.setText(e.getMessage());
        }
    }
}
