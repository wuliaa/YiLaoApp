package com.example.yilaoapp.chat.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.yilaoapp.R;
import com.example.yilaoapp.bean.Uuid;
import com.example.yilaoapp.bean.chat_task;
import com.example.yilaoapp.chat.adapter.ChatAdapter;
import com.example.yilaoapp.chat.bean.AudioMsgBody;
import com.example.yilaoapp.chat.bean.FileMsgBody;
import com.example.yilaoapp.chat.bean.ImageMsgBody;
import com.example.yilaoapp.chat.bean.Message;
import com.example.yilaoapp.chat.bean.MsgSendStatus;
import com.example.yilaoapp.chat.bean.MsgType;
import com.example.yilaoapp.chat.bean.TextMsgBody;
import com.example.yilaoapp.chat.bean.VideoMsgBody;
import com.example.yilaoapp.chat.util.ChatUiHelper;
import com.example.yilaoapp.chat.util.FileUtils;
import com.example.yilaoapp.chat.util.LogUtil;
import com.example.yilaoapp.chat.util.PictureFileUtil;
import com.example.yilaoapp.chat.widget.MediaManager;
import com.example.yilaoapp.chat.widget.RecordButton;
import com.example.yilaoapp.chat.widget.StateButton;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.service.chat_service;
import com.example.yilaoapp.utils.PhotoOperation;
import com.example.yilaoapp.utils.SavePhoto;
import com.kongzue.dialog.interfaces.OnMenuItemClickListener;
import com.kongzue.dialog.v3.BottomMenu;
import com.kongzue.dialog.v3.TipDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.llContent)
    LinearLayout mLlContent;
    @BindView(R.id.rv_chat_list)
    RecyclerView mRvChat;
    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.bottom_layout)
    RelativeLayout mRlBottomLayout;//表情,添加底部布局
    @BindView(R.id.ivAdd)
    ImageView mIvAdd;
    @BindView(R.id.ivEmo)
    ImageView mIvEmo;
    @BindView(R.id.btn_send)
    StateButton mBtnSend;//发送按钮
    @BindView(R.id.ivAudio)
    ImageView mIvAudio;//录音图片
    @BindView(R.id.btnAudio)
    RecordButton mBtnAudio;//录音按钮
    @BindView(R.id.rlEmotion)
    LinearLayout mLlEmotion;//表情布局
    @BindView(R.id.llAdd)
    LinearLayout mLlAdd;//添加布局
    @BindView(R.id.swipe_chat)
    SwipeRefreshLayout mSwipeRefresh;//下拉刷新
    @BindView(R.id.common_toolbar_title)
    TextView title;
    private ChatAdapter mAdapter;
    public static final String mSenderId = "right";
    public static final String mTargetId = "left";
    public static final int REQUEST_CODE_IMAGE = 0000;
    public static final int REQUEST_CODE_VEDIO = 1111;
    public static final int REQUEST_CODE_FILE = 2222;
    Bundle bundle;
    String mob;
    String nick1;
    String uuid1;
    BigInteger phone;
    SharedPreferences pre;
    String mobile;
    String uuid2;
    String token;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ImageView back = findViewById(R.id.chatback);
        back.setOnClickListener(v -> onBackPressed());
        initContent();
        initPeople();
    }

    public void initPeople() {
        Intent intent = getIntent();
        bundle = intent.getBundleExtra("bundle");
        mob = bundle.getString("mobile");
        nick1 = bundle.getString("id_name");
        title.setText(nick1);
        uuid1 = bundle.getString("uuid");
        phone = new BigInteger(mob);
        pre = getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        mobile = pre.getString("mobile", "");
        uuid2 = pre.getString("id_photo", "");
        token = pre.getString("token", "");
    }

    private ImageView ivAudio;
    private ImageView photo;
    PopupWindow popupDialog;

    protected void initContent() {
        ButterKnife.bind(this);
        mAdapter = new ChatAdapter(this, new ArrayList<Message>());
        LinearLayoutManager mLinearLayout = new LinearLayoutManager(this);
        mRvChat.setLayoutManager(mLinearLayout);
        mRvChat.setAdapter(mAdapter);
        mSwipeRefresh.setOnRefreshListener(this);
        initChatUi();
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                final boolean isSend = Objects.requireNonNull(mAdapter.getItem(position)).getSenderId().equals(ChatActivity.mSenderId);
                switch (view.getId()) {
                    case R.id.bivPic:
                        ImageMsgBody imageMsgBody = (ImageMsgBody) mAdapter.getItem(position).getBody();
                        showDialog(getApplicationContext(),
                                imageMsgBody.getThumbUrl());
                        break;
                    case R.id.rlAudio:
                        if (ivAudio != null) {
                            if (isSend) {
                                ivAudio.setBackgroundResource(R.mipmap.audio_animation_list_right_3);
                            } else {
                                ivAudio.setBackgroundResource(R.mipmap.audio_animation_list_left_3);
                            }
                            ivAudio = null;
                            MediaManager.reset();
                        } else {
                            ivAudio = view.findViewById(R.id.ivAudio);
                            MediaManager.reset();
                            if (isSend) {
                                ivAudio.setBackgroundResource(R.drawable.audio_animation_right_list);
                            } else {
                                ivAudio.setBackgroundResource(R.drawable.audio_animation_left_list);
                            }
                            AnimationDrawable drawable = (AnimationDrawable) ivAudio.getBackground();
                            drawable.start();
                            MediaManager.playSound(ChatActivity.this, ((AudioMsgBody) mAdapter.getData().get(position).getBody()).getLocalPath(), mp -> {
                                if (isSend) {
                                    ivAudio.setBackgroundResource(R.mipmap.audio_animation_list_right_3);
                                } else {
                                    ivAudio.setBackgroundResource(R.mipmap.audio_animation_list_left_3);
                                }

                                MediaManager.release();
                            });
                        }
                        break;
                }


            }


        });

    }

    @Override
    public void onRefresh() {
        //下拉刷新模拟获取历史消息
        List<Message> mReceiveMsgList = new ArrayList<Message>();
        //构建文本消息
        Message mMessgaeText = getBaseReceiveMessage(MsgType.TEXT);
        TextMsgBody mTextMsgBody = new TextMsgBody();
        mTextMsgBody.setMessage("收到的消息");
        mMessgaeText.setBody(mTextMsgBody);
        mReceiveMsgList.add(mMessgaeText);
        //构建图片消息
        Message mMessgaeImage = getBaseReceiveMessage(MsgType.IMAGE);
        ImageMsgBody mImageMsgBody = new ImageMsgBody();
        mImageMsgBody.setThumbUrl("https://c-ssl.duitang.com/uploads/item/201208/30/20120830173930_PBfJE.thumb.700_0.jpeg");
        mMessgaeImage.setBody(mImageMsgBody);
        mReceiveMsgList.add(mMessgaeImage);
        //构建文件消息
        Message mMessgaeFile = getBaseReceiveMessage(MsgType.FILE);
        FileMsgBody mFileMsgBody = new FileMsgBody();
        mFileMsgBody.setDisplayName("收到的文件");
        mFileMsgBody.setSize(12);
        mMessgaeFile.setBody(mFileMsgBody);
        mReceiveMsgList.add(mMessgaeFile);
        mAdapter.addData(0, mReceiveMsgList);
        mSwipeRefresh.setRefreshing(false);
    }


    private void initChatUi() {
        //mBtnAudio
        final ChatUiHelper mUiHelper = ChatUiHelper.with(this);
        mUiHelper.bindContentLayout(mLlContent)
                .bindttToSendButton(mBtnSend)
                .bindEditText(mEtContent)
                .bindBottomLayout(mRlBottomLayout)
                .bindEmojiLayout(mLlEmotion)
                .bindAddLayout(mLlAdd)
                .bindToAddButton(mIvAdd)
                .bindToEmojiButton(mIvEmo)
                .bindAudioBtn(mBtnAudio)
                .bindAudioIv(mIvAudio)
                .bindEmojiData();
        //底部布局弹出,聊天列表上滑
        mRvChat.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom) {
                mRvChat.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mAdapter.getItemCount() > 0) {
                            mRvChat.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                        }
                    }
                });
            }
        });
        //点击空白区域关闭键盘
        mRvChat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mUiHelper.hideBottomLayout(false);
                mUiHelper.hideSoftInput();
                mEtContent.clearFocus();
                mIvEmo.setImageResource(R.mipmap.ic_emoji);
                return false;
            }
        });
        //
        ((RecordButton) mBtnAudio).setOnFinishedRecordListener((RecordButton.OnFinishedRecordListener) (audioPath, time) -> {
            LogUtil.d("录音结束回调");
            File file = new File(audioPath);
            if (file.exists()) {
                sendAudioMessage(audioPath, time);
            }
        });

    }

    @OnClick({R.id.btn_send, R.id.rlPhoto})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                sendTextMsg(mEtContent.getText().toString());
                mEtContent.setText("");
                break;
            case R.id.rlPhoto:
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent, 200);//打开系统相册
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK && null != data) {
            if (Build.VERSION.SDK_INT >= 19) {
                handleImageOnKitkat(data);
            } else {
                handleImageBeforeKitkat(data);
            }
        }
    }

    @TargetApi(19)
    private void handleImageOnKitkat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content:" +
                        "//downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是File类型的uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);//根据图片路径显示图片
    }

    private void handleImageBeforeKitkat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过uri和selection来获取真实的图片路径
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            byte[] ba = null;
            PhotoOperation Operation = new PhotoOperation();
            try {
                ba = Operation.Path2ByteArray(imagePath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.d("PhotoFIle", "onClick: 打不开文件");
            }
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            sendImageMessage(imagePath);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }


    //文本消息
    private void sendTextMsg(String hello) {
        final Message mMessgae = getBaseSendMessage(MsgType.TEXT);
        TextMsgBody mTextMsgBody = new TextMsgBody();
        mTextMsgBody.setMessage(hello);
        mMessgae.setBody(mTextMsgBody);

        chat_service chat = new RetrofitUser().get(getApplicationContext()).create(chat_service.class);
        Call<ResponseBody> chat_back = chat.send_message(mobile, token, "df3b72a07a0a4fa1854a48b543690eab", new chat_task(hello, phone));
        chat_back.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //开始发送
                mAdapter.addData(mMessgae);
                //模拟两秒后发送成功
                updateMsg(mMessgae);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        //模拟两秒后发送成功
    }


    //图片消息
    private void sendImageMessage(String path) {
        final Message mMessgae = getBaseSendMessage(MsgType.IMAGE);
        ImageMsgBody mImageMsgBody = new ImageMsgBody();
        mImageMsgBody.setThumbUrl(path);
        mMessgae.setBody(mImageMsgBody);
        //开始发送
        mAdapter.addData(mMessgae);
        //模拟两秒后发送成功
        updateMsg(mMessgae);
    }


    //视频消息
    private void sendVedioMessage(final LocalMedia media) {
        final Message mMessgae = getBaseSendMessage(MsgType.VIDEO);
        //生成缩略图路径
        String vedioPath = media.getPath();
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(vedioPath);
        Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime();
        String imgname = System.currentTimeMillis() + ".jpg";
        String urlpath = Environment.getExternalStorageDirectory() + "/" + imgname;
        File f = new File(urlpath);
        try {
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            LogUtil.d("视频缩略图路径获取失败：" + e.toString());
            e.printStackTrace();
        }
        VideoMsgBody mImageMsgBody = new VideoMsgBody();
        mImageMsgBody.setExtra(urlpath);
        mMessgae.setBody(mImageMsgBody);
        //开始发送
        mAdapter.addData(mMessgae);
        //模拟两秒后发送成功
        updateMsg(mMessgae);

    }

    //文件消息
    private void sendFileMessage(String from, String to, final String path) {
        final Message mMessgae = getBaseSendMessage(MsgType.FILE);
        FileMsgBody mFileMsgBody = new FileMsgBody();
        mFileMsgBody.setLocalPath(path);
        mFileMsgBody.setDisplayName(FileUtils.getFileName(path));
        mFileMsgBody.setSize(FileUtils.getFileLength(path));
        mMessgae.setBody(mFileMsgBody);
        //开始发送
        mAdapter.addData(mMessgae);
        //模拟两秒后发送成功
        updateMsg(mMessgae);

    }

    //语音消息
    private void sendAudioMessage(final String path, int time) {
        final Message mMessgae = getBaseSendMessage(MsgType.AUDIO);
        AudioMsgBody mFileMsgBody = new AudioMsgBody();
        mFileMsgBody.setLocalPath(path);
        mFileMsgBody.setDuration(time);
        mMessgae.setBody(mFileMsgBody);
        //开始发送
        mAdapter.addData(mMessgae);
        //模拟两秒后发送成功
        updateMsg(mMessgae);
    }


    private Message getBaseSendMessage(MsgType msgType) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://api.yilao.tk:15000/v1.0/users/")
                .append(mobile)
                .append("/resources/")
                .append(uuid2);
        String url = stringBuilder.toString();
        Message mMessgae = new Message();
        mMessgae.setUuid(url);
        mMessgae.setSenderId(mSenderId);
        mMessgae.setTargetId(mTargetId);
        mMessgae.setSentTime(System.currentTimeMillis());
        mMessgae.setSentStatus(MsgSendStatus.SENDING);
        mMessgae.setMsgType(msgType);
        return mMessgae;
    }


    private Message getBaseReceiveMessage(MsgType msgType) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://api.yilao.tk:15000/v1.0/users/")
                .append(mob)
                .append("/resources/")
                .append(uuid1);
        String url = stringBuilder.toString();
        Message mMessgae = new Message();
        mMessgae.setUuid(url);
        mMessgae.setSenderId(mTargetId);
        mMessgae.setTargetId(mSenderId);
        mMessgae.setSentTime(System.currentTimeMillis());
        mMessgae.setSentStatus(MsgSendStatus.SENDING);
        mMessgae.setMsgType(msgType);
        return mMessgae;
    }

    private void updateMsg(final Message mMessgae) {
        mRvChat.scrollToPosition(mAdapter.getItemCount() - 1);
        //模拟2秒后发送成功
        new Handler().postDelayed(() -> {
            int position = 0;
            mMessgae.setSentStatus(MsgSendStatus.SENT);
            //更新单个子条目
            for (int i = 0; i < mAdapter.getData().size(); i++) {
                Message mAdapterMessage = mAdapter.getData().get(i);
                if (mMessgae.getUuid().equals(mAdapterMessage.getUuid())) {
                    position = i;
                }
            }
            mAdapter.notifyItemChanged(position);
        }, 1000);

    }

    /**
     * 弹出图片放大框
     *
     * @param url 图片路径
     */
    public void showDialog(Context mContext, String url) {
        if (isFinishing()) {
            return;
        }
        try { //容错
            if (popupDialog != null) {
                popupDialog.dismiss();
            }
            View popView = getLayoutInflater().inflate(R.layout.dialog_image, null);
            WindowManager wm = (WindowManager) this
                    .getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            //glide 加载图片
            ImageView iv_dialog_image = (ImageView) popView.findViewById(R.id.iv_dialog_image);
            Glide.with(mContext)
                    .asBitmap()
                    .load(url)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                            int bitwidth = bitmap.getWidth();
                            int bitheight = bitmap.getHeight();
                            LogUtil.d("width " + bitwidth);
                            LogUtil.d("height " + bitheight);
                            Glide.with(mContext)
                                    .load(url)
                                    .override(width, width * bitheight / bitwidth)
                                    .into(iv_dialog_image);
                        }
                    });
            popupDialog = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            popupDialog.getContentView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popupDialog.isShowing()) {
                        popupDialog.dismiss();
                    }
                }
            });
            ImageView download = (ImageView) popView.findViewById(R.id.download);
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Glide.with(mContext)
                            .asBitmap()
                            .load(url)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                                    SavePhoto savePhoto=new SavePhoto(mContext);
                                    savePhoto.saveImageToGallery(bitmap);
                                }
                            });
                    TipDialog.show(ChatActivity.this, "下载成功", TipDialog.TYPE.SUCCESS);
                }
            });
            popupDialog.setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
            popupDialog.setOutsideTouchable(true);//外部点击消失
            popupDialog.setFocusable(true); // 这个很重要

            if (popupDialog != null && !popupDialog.isShowing()) {
                //popupDialog.setAnimationStyle(R.style.dialog_anim);
                popupDialog.showAtLocation(getWindow().getDecorView(),
                        Gravity.CENTER, 0, 0);
                popupDialog.setFocusable(true);
            }
        } catch (Exception e) {
            //showAtLocation
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText
            // （特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，
     * 来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v instanceof EditText) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            // 点击EditText的事件，忽略它。
            return !(event.getX() > left) || !(event.getX() < right)
                    || !(event.getY() > top) || !(event.getY() < bottom);
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，
        // 第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert im != null;
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}