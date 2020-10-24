package com.example.plotxl;

import android.content.Context;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Primitive;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class DatabaseAccess {
    private static final String COGNITO_POOL_ID = "us-east-1:ed011ccf-b5aa-4f0d-8413-f21c1eb4abc7";
    private static final Regions MY_REGION = Regions.US_EAST_1;
    Table dbTable;
    AmazonDynamoDBClient dbClient;
    Context context;
    CognitoCachingCredentialsProvider credentialsProvider;
    private static volatile DatabaseAccess instance;

    public  Document getMemoById(String noteId) {
         return dbTable.getItem(new Primitive(noteId));
    }

    public DatabaseAccess(Context context){
        this.context=context;
        credentialsProvider = new CognitoCachingCredentialsProvider(
               context, COGNITO_POOL_ID, MY_REGION);

        // Create a connection to DynamoDB
        dbClient = new AmazonDynamoDBClient(credentialsProvider);

        // Create a table reference
        dbTable = Table.loadTable(dbClient, "dataPlot");
    }

    public static synchronized DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }


    public void delete(Document memo) {
        dbTable.deleteItem(
                memo.get("userId").asPrimitive(),   // The Partition Key
                memo.get("noteId").asPrimitive());  // The Hash Key
    }


    public void create(Document memo) {
        if (!memo.containsKey("userId")) {
            memo.put("userId", credentialsProvider.getCachedIdentityId());
        }
        if (!memo.containsKey("noteId")) {
            memo.put("noteId", UUID.randomUUID().toString());
        }
        if (!memo.containsKey("creationDate")) {
            memo.put("creationDate", System.currentTimeMillis());
        }
        dbTable.putItem(memo);
    }

    public List<Document> getAllMemos() {
        return dbTable.query(new Primitive(credentialsProvider.getCachedIdentityId())).getAllResults();
    }



//    public GetItemResult getItem(String value_no)
//    {
//
//        Map<String, AttributeValue> key = new HashMap<String, AttributeValue>();
//        key.put("value_no",);
//
//
//        GetItemRequest getItemRequest = new GetItemRequest("dataPlot",key);
//        GetItemResult getItemResult = dbClient.getItem(getItemRequest);
//
//// name is a string, so its stored value will be in the S field
//        Log.d("", "value1 = '" + getItemResult.getItem().get("name").getS() + "'");
//        return getItemResult;
//
//    }

}
