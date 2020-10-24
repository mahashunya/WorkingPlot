package com.example.plotxl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseAccess AccessTable;
    Context context;
    Button plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context=context;
        plot=  (Button)  findViewById(R.id.button) ;

        plot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetAllItemsAsyncTask get=new GetAllItemsAsyncTask();
                 get.execute();

            }
        });



        // Create a new credentials provider


    }
    private class GetAllItemsAsyncTask extends AsyncTask<Void, Void, List<Document>> {
        @Override
        protected List<Document> doInBackground(Void... params) {
            List<Document> documents;
//            Document d1;
//            GetItemResult g1;
            DatabaseAccess databaseAccess =new DatabaseAccess(MainActivity.this);
            documents=databaseAccess.getAllMemos();
//            d1=databaseAccess.getMemoById("value0");
            for(int i=0;i<10;i++)
            {
                documents.add(databaseAccess.getMemoById("value"+i));
            }
//
//            System.out.println(d1.get("value").asString());

            GraphView graph = (GraphView) findViewById(R.id.graph);
            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {

                    new DataPoint(0,Integer.parseInt(documents.get(0).get("value").asString())),
                    new DataPoint(1, Integer.parseInt(documents.get(1).get("value").asString())),
                    new DataPoint(2, Integer.parseInt(documents.get(2).get("value").asString())),
                    new DataPoint(3, Integer.parseInt(documents.get(3).get("value").asString())),
                    new DataPoint(4, Integer.parseInt(documents.get(4).get("value").asString())),
                    new DataPoint(5,Integer.parseInt(documents.get(5).get("value").asString())),
                    new DataPoint(6, Integer.parseInt(documents.get(6).get("value").asString())),
                    new DataPoint(7, Integer.parseInt(documents.get(7).get("value").asString())),
                    new DataPoint(8, Integer.parseInt(documents.get(8).get("value").asString())),
                    new DataPoint(9, Integer.parseInt(documents.get(9).get("value").asString()))
            });

            graph.addSeries(series);


            return documents;
        }
        protected void onPostExecute(List<Document> documents) {

        }

    }
    private class CreateItemAsyncTask extends AsyncTask<Document, Void, Void> {
        @Override
        protected Void doInBackground(Document... documents) {
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(MainActivity.this);
            databaseAccess.create(documents[0]);
            return null;
        }

    }
}