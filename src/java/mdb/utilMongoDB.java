/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdb;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import db.UserHaveProperty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name = "mongoDb")
@ApplicationScoped
public class utilMongoDB {
    private DBCollection presentationData;
    private MongoClient mongoClient;
    private DB db;

    public DBCollection getPresentationData() {
        if(presentationData == null) {
            mongoClient = new MongoClient();
            db = mongoClient.getDB("fidbac");
            presentationData = db.getCollection("presentationData");
        }
        return presentationData;
    }
    
    public void insertFeeling(int presentationId, int userId, int feelingId, int previousFeelingId, List<UserHaveProperty> propertires) {
        List<BasicDBObject> props = new ArrayList<>();
        for (UserHaveProperty propertire : propertires) {
            BasicDBObject prop = new BasicDBObject("propertireId", propertire.getProperty().getPropertyId())
                    .append("propertireValue", propertire.getValue());
            props.add(prop);
        }
        BasicDBObject feeling = 
                new BasicDBObject("presentationId", presentationId)
                .append("userId", userId)
                .append("feelingId", feelingId)
                .append("previousFeelingId", previousFeelingId)
                .append("propertires", props)
                .append("time", new Date().getTime());
        
        getPresentationData().insert(feeling);
    }
    
    public List<SimpleFeeling> getFeelingsForUser(int presentationId, int userId) {
        return getFeeling(presentationId, userId, false, true);
    }
     
    public List<SimpleFeeling> getFeelingsForPresentation(int presentationId) {
        return getFeeling(presentationId, -1, true, false);
    }
    
    public List<SimpleFeeling> getFeeling(int presentationId, int userId, boolean getPropertires, boolean getUserId) {
        ArrayList<SimpleFeeling> listFeeling = new ArrayList<>();
        BasicDBObject query = new BasicDBObject("presentationId", presentationId);
        
        if(getUserId) {
            query.append("userId", userId);
        }
        
        try (DBCursor cursor = getPresentationData().find(query)) {
            cursor.sort(new BasicDBObject("time", 1));
            while (cursor.hasNext()) {
                DBObject feeling = cursor.next();
                SimpleFeeling sFeeling = new SimpleFeeling(presentationId,(int) feeling.get("userId"), (int) feeling.get("feelingId"), (int) feeling.get("previousFeelingId"), (long) feeling.get("time"));
                if(getPropertires) {
                    sFeeling.setPropertires(new ArrayList<SimpleProperty>());
                    for (BasicDBObject prop : (List<BasicDBObject>) feeling.get("propertires")) {
                        sFeeling.getPropertires().add(new SimpleProperty(prop.getInt("propertireId"), prop.getString("propertireValue")));
                    }
                }
                listFeeling.add(sFeeling);
            }
        }
        
        return listFeeling;
    }
    
    public void removePresentation(int presentationId) {
        BasicDBObject remove = new BasicDBObject();
        remove.put("presentationId", presentationId);
        getPresentationData().remove(remove);
    }
    
    public List<SimpleFeeling> getLoadDataForPresentation(int presentationId) {
        return getFeelingsForPresentation(presentationId);
    }
}
