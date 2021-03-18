package com.example.yilaoapp.chat.activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.yilaoapp.bean.Mess;
import com.example.yilaoapp.database.chat.ChatDataBase;
import com.example.yilaoapp.database.dao.ChatDao;

import java.util.List;

import static com.example.yilaoapp.MyApplication.getContext;

public class ChatViewModel extends ViewModel {
    ChatDataBase chatDataBase= ChatDataBase.getDatabase(getContext());
    ChatDao chatDao= chatDataBase.getChatDao();

    public LiveData<List<Mess>> getChatList(){
        return chatDao.getAll();
    }

}
