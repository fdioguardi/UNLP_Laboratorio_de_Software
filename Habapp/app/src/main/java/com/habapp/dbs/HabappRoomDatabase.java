package com.habapp.dbs;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.habapp.daos.FarmDao;
import com.habapp.daos.PlotDao;
import com.habapp.daos.SackDao;
import com.habapp.daos.VegetableDao;
import com.habapp.daos.VisitDao;
import com.habapp.models.Farm;
import com.habapp.models.Plot;
import com.habapp.models.Sack;
import com.habapp.models.Vegetable;
import com.habapp.models.Visit;
import com.habapp.models.relations.PlotVegetableCrossRef;
import com.habapp.models.relations.SackVegetableCrossRef;
import com.habapp.models.relations.VisitAvailableVegetableCrossRef;

import java.time.Month;
import java.util.concurrent.ExecutorService;

@Database(entities = {Vegetable.class, Plot.class, PlotVegetableCrossRef.class, Farm.class, Sack.class, SackVegetableCrossRef.class, Visit.class, VisitAvailableVegetableCrossRef.class}, version = 1)
public abstract class HabappRoomDatabase extends RoomDatabase {

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            java.util.concurrent.Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static volatile HabappRoomDatabase INSTANCE;

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @SuppressLint("NewApi")
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more vegetables, just add them.
                VegetableDao dao = INSTANCE.vegetableDao();
                dao.deleteAll();

                dao.insert(
                        new Vegetable("Lechuga", "Hoja verde", Month.NOVEMBER, Month.DECEMBER),
                        new Vegetable("Tomate", "Fruta roja", Month.NOVEMBER, Month.DECEMBER)
                );
            });
        }
    };

    public static HabappRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (HabappRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    HabappRoomDatabase.class, "habapp_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract VegetableDao vegetableDao();

    public abstract PlotDao plotDao();

    public abstract FarmDao farmDao();

    public abstract SackDao sackDao();

    public abstract VisitDao visitDao();
}
