package com.test.cmt.butternifetest.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.test.cmt.butternifetest.converter.Converters;
import com.test.cmt.butternifetest.dao.LocationDao;
import com.test.cmt.butternifetest.entity.LocationEntity;

@Database(entities = {LocationEntity.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppRoomDatabase extends RoomDatabase {

    public abstract LocationDao locationDao();

    private static AppRoomDatabase INSTANCE;

    public static AppRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext()
                        , AppRoomDatabase.class
                        , "location_database")
                    .addCallback(appRoomDatabaseCallback)
                    .build();
        }

        return INSTANCE;
    }

    private static RoomDatabase.Callback appRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final LocationDao mDao;

        PopulateDbAsync(AppRoomDatabase db) {
            mDao = db.locationDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            LocationEntity locationEntity = new LocationEntity();
            locationEntity.setPlaceName("MMHash Co., Ltd.");
            locationEntity.setLatLon("22.135256, 95.122832");
            //mDao.insert(locationEntity);
            return null;
        }
    }
}
