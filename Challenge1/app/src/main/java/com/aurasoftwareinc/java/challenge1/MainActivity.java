package com.aurasoftwareinc.java.challenge1;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONObject;

public class MainActivity extends Activity {

    private TextView text1;
    private TextView text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createLayout();

        setTitle("Put Your Name Here...");
    }

    private void createLayout() {
        FrameLayout topFrame = new FrameLayout(this);
        topFrame.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        setContentView(topFrame);

        LinearLayout contentFrame = new LinearLayout(this);
        contentFrame.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        contentFrame.setOrientation(LinearLayout.VERTICAL);

        topFrame.addView(contentFrame);

        ScrollView scroll1 = new ScrollView(this);
        scroll1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0.5f));
        scroll1.setBackgroundColor(0xffccffcc);

        contentFrame.addView(scroll1);

        text1 = new TextView(this);
        text1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        scroll1.addView(text1);

        ScrollView scroll2 = new ScrollView(this);
        scroll2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0.5f));
        scroll2.setBackgroundColor(0xffccccff);

        contentFrame.addView(scroll2);

        text2 = new TextView(this);
        text2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        scroll2.addView(text2);

        LinearLayout buttonFrame = new LinearLayout(this);
        buttonFrame.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        buttonFrame.setOrientation(LinearLayout.HORIZONTAL);
        buttonFrame.setGravity(Gravity.CENTER_HORIZONTAL);
        buttonFrame.setBackgroundColor(0x88888888);
        buttonFrame.setPadding(10, 10, 10, 10);

        contentFrame.addView(buttonFrame);

        String text = "Test It";

        TextView testButton = new TextView(this);
        testButton.setText(text);
        testButton.setTextSize(36f);
        testButton.setBackgroundColor(0xffffffff);
        testButton.setPadding(20, 20, 20, 20);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testIt();
            }
        });

        buttonFrame.addView(testButton);
    }

    private void testIt() {
        try {
            //
            // Create instance and populate with test values.
            //

            SubclassTypes subclassTypes1 = new SubclassTypes();
            subclassTypes1.populateTestData();

            //
            // Marshal into JSON and display pretty
            // printed JSON string in text view 1.
            //

            JSONObject marshal1 = subclassTypes1.marshalJSON();
            text1.setText(marshal1.toString(2));

            //
            // Unmarshal from JSON into empty test class.
            //

            SubclassTypes subclassTypes2 = new SubclassTypes();
            subclassTypes2.unmarshalJSON(marshal1);

            //
            // Marshal unmarshalled class into JSON and display
            // pretty printed JSON string in text view 2.
            //

            JSONObject marshal2 = subclassTypes2.marshalJSON();
            text2.setText(marshal2.toString(2));

            //
            // Both text views should now contain identical content.
            //
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
