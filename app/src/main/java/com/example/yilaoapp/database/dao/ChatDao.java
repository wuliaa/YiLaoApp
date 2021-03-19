package com.example.yilaoapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.yilaoapp.bean.Mess;
import com.example.yilaoapp.bean.chat_user;

import java.util.List;

@Dao
public interface ChatDao {
    @Query("SELECT * FROM chat_table order by id")
    LiveData<List<Mess>> getAll();

    @Query("SELECT * FROM chat_table order by id")
    List<Mess> getAll2();

    @Insert
    void insert(Mess mess);

    @Delete
    void delete(Mess mess);

    @Query("SELECT * FROM chat_table WHERE id =:ID")
    Mess hasItem(int ID);
}

