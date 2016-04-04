package com.weiqianghu.usedbook.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tb.emoji.Emoji;
import com.tb.emoji.EmojiUtil;
import com.tb.emoji.FaceFragment;
import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.ChatMessageModel;
import com.weiqianghu.usedbook.model.entity.UserBean;
import com.weiqianghu.usedbook.presenter.adapter.ChatMessageAdapter;
import com.weiqianghu.usedbook.util.Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.newim.listener.ObseverListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public class ChatActivity extends AppCompatActivity implements FaceFragment.OnEmojiClickListener, ObseverListener {

    private TextView mTvTopBarText;
    private ImageView mIvTopBarLeftBtn;

    private BmobIMConversation c;

    private Button mChatSendBtn;
    private EditText mMessageEt;
    private UserBean chatUser = new UserBean();

    private List<ChatMessageModel> mData = new ArrayList<>();
    private ChatMessageAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwiperefreshLayout;

    private UserBean currentUser = null;

    private FragmentTransaction transaction;
    private FaceFragment faceFragment;
    private ImageView mEmoji;
    private boolean isSoftInputVisble = false;

    private boolean initData = false;
    private static final int LIMIT = 30;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initChat();
        initView();
        initTopBar();
        initData();
    }

    private void initData() {
        mSwiperefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                initData = true;
                mSwiperefreshLayout.setRefreshing(true);
                queryMessage(null, c, LIMIT);
            }
        });
    }

    private void initChat() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("c");
            if (bundle != null) {
                BmobIMConversation tc = (BmobIMConversation) bundle.getSerializable("c");
                c = BmobIMConversation.obtain(BmobIMClient.getInstance(), tc);
                chatUser = bundle.getParcelable(Constant.DATA);
            }
        }
    }

    private void initView() {
        Click click = new Click();

        mChatSendBtn = (Button) findViewById(R.id.btn_chat_send);
        mChatSendBtn.setOnClickListener(click);
        mMessageEt = (EditText) findViewById(R.id.et_message);
        mMessageEt.setOnClickListener(click);

        mSwiperefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ChatMessageAdapter(mData, R.layout.item_comment, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mSwiperefreshLayout.setColorSchemeResources(R.color.mainColor);
        mSwiperefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BmobIMMessage message;
                if (mData.size() > 0) {
                    message = mData.get(0).getMessage();
                    queryMessage(message, c, LIMIT);
                }
            }
        });

        faceFragment = FaceFragment.Instance();
        mEmoji = (ImageView) findViewById(R.id.emoji);
        mEmoji.setOnClickListener(click);

        final View activityRootView = findViewById(R.id.activityRoot);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
                    isSoftInputVisble = true;
                } else {
                    isSoftInputVisble = false;
                }
            }
        });
    }

    private void initTopBar() {
        mTvTopBarText = (TextView) findViewById(R.id.tv_top_bar_text);
        mTvTopBarText.setText(chatUser.getShop().getShopName());

        Click click = new Click();
        mIvTopBarLeftBtn = (ImageView) findViewById(R.id.top_bar_left_button);
        mIvTopBarLeftBtn.setImageResource(R.mipmap.back);
        mIvTopBarLeftBtn.setOnClickListener(click);
    }


    class Click implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.top_bar_left_button:
                    finish();
                    break;
                case R.id.btn_chat_send:
                    sendMessage();
                    break;
                case R.id.emoji:
                    selectEmoji();
                    break;
                case R.id.et_message:
                    if (faceFragment.isAdded() && !faceFragment.isHidden()) {
                        hideEmoji();
                    }
                    break;
            }
        }
    }

    private void sendMessage() {
        if (c == null) {
            return;
        }
        String msgText = mMessageEt.getText().toString().trim();
        if (TextUtils.isEmpty(msgText)) {
            return;
        }

        BmobIMTextMessage msg = new BmobIMTextMessage();
        msg.setContent(msgText);

        c.sendMessage(msg, new MessageSendListener() {
            @Override
            public void onStart(BmobIMMessage msg) {
                super.onStart(msg);

                mMessageEt.setText("");

                if (currentUser == null) {
                    currentUser = BmobUser.getCurrentUser(ChatActivity.this, UserBean.class);
                }

                ChatMessageModel model = new ChatMessageModel();
                model.setType(Constant.MESSAGE_SEND);
                model.setToImg(currentUser.getImg());
                model.setFromImg(chatUser.getImg());
                model.setMessage(msg);
                mData.add(model);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.smoothScrollToPosition(mData.size() - 1);
            }

            @Override
            public void done(BmobIMMessage msg, BmobException e) {

                if (e != null) {
                    Toast.makeText(ChatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isSoftInputVisble) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onEmojiClick(Emoji emoji) {
        if (emoji != null) {
            StringBuffer editString = new StringBuffer(mMessageEt.getText());
            int index = mMessageEt.getSelectionStart();
            if (index < 0) {
                editString.append(emoji.getContent());
            } else {
                editString.insert(index, emoji.getContent());
            }

            EmojiUtil.displayTextView(mMessageEt, editString.toString(), ChatActivity.this);
            mMessageEt.setSelection(index + emoji.getContent().length());
        }
    }

    @Override
    public void onEmojiDelete() {
        int index = mMessageEt.getSelectionStart();
        int length = EmojiUtil.deleteEmoji(mMessageEt, index);
        String str = mMessageEt.getText().toString();
        EmojiUtil.displayTextView(mMessageEt, str, ChatActivity.this);
        mMessageEt.setSelection(index - length);
    }

    public void selectEmoji() {
        transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!faceFragment.isAdded()) {
            transaction.add(R.id.emoji_container, faceFragment).commit();
            mEmoji.setImageResource(R.mipmap.emoji_open);
            hideInput();
        } else if (faceFragment.isHidden()) {
            transaction.show(faceFragment).commit();
            mEmoji.setImageResource(R.mipmap.emoji_open);
            hideInput();
        } else {
            transaction.hide(faceFragment).commit();
            mEmoji.setImageResource(R.mipmap.emoji);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (faceFragment.isAdded() && !faceFragment.isHidden()) {
                hideEmoji();
            } else {
                onBackPressed();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void hideEmoji() {
        transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.hide(faceFragment).commit();
        mEmoji.setImageResource(R.mipmap.emoji);
    }

    public void queryMessage(BmobIMMessage msg, BmobIMConversation c, final int limit) {
        c.queryMessages(msg, limit, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                if (e == null) {
                    if (null != list && list.size() > 0) {
                        if (currentUser == null) {
                            currentUser = UserBean.getCurrentUser(ChatActivity.this, UserBean.class);
                        }
                        List<ChatMessageModel> temoList = new ArrayList<ChatMessageModel>();
                        ;
                        for (ChatMessageModel model : mData) {
                            temoList.add(model);
                        }
                        mData.clear();

                        for (int i = 0, length = list.size(); i < length; i++) {
                            ChatMessageModel model = new ChatMessageModel();
                            model.setMessage(list.get(i));
                            model.setFromImg(chatUser.getImg());
                            model.setToImg(currentUser.getImg());
                            if (list.get(i).getFromId().equals(currentUser.getObjectId())) {
                                model.setType(Constant.MESSAGE_SEND);
                            } else {
                                model.setType(Constant.MESSAGE_RECEIVE);
                            }

                            mData.add(model);
                        }
                        mData.addAll(temoList);
                        mAdapter.notifyDataSetChanged();
                        if (initData) {
                            mRecyclerView.smoothScrollToPosition(mData.size() - 1);
                            initData = false;
                        }
                    }
                } else {
                    Toast.makeText(ChatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                mSwiperefreshLayout.setRefreshing(false);
            }
        });
    }

    private void addMessage2Chat(MessageEvent event) {
        BmobIMMessage msg = event.getMessage();

        if (c != null && event != null && c.getConversationId().equals(event.getConversation().getConversationId()) //如果是当前会话的消息
                && !msg.isTransient()) {//并且不为暂态消息
            ChatMessageModel model = new ChatMessageModel();
            model.setMessage(event.getMessage());
            model.setFromImg(chatUser.getImg());
            model.setToImg(currentUser.getImg());
            model.setType(Constant.MESSAGE_RECEIVE);
            mData.add(model);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.smoothScrollToPosition(mData.size() - 1);
        } else {
            Intent pendingIntent = new Intent(ChatActivity.this, MessageListActivity.class);

            BmobIMMessage message = event.getMessage();
            BmobIMUserInfo info = event.getFromUserInfo();
            //这里可以是应用图标，也可以将聊天头像转成bitmap
            Bitmap largetIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
            BmobNotificationManager.getInstance(ChatActivity.this).showNotification(largetIcon, info.getName(), message.getContent(),
                    "您有一条新消息", pendingIntent);
        }
    }

    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        addMessage2Chat(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BmobNotificationManager.getInstance(this).addObserver(this);
        addUnReadMessage();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onPause() {
        //取消通知栏监听
        BmobNotificationManager.getInstance(this).removeObserver(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        c.updateLocalCache();
        super.onDestroy();
    }

    private void addUnReadMessage() {
        List<MessageEvent> cache = BmobNotificationManager.getInstance(this).getNotificationCacheList();
        if (cache.size() > 0) {
            int size = cache.size();
            for (int i = 0; i < size; i++) {
                MessageEvent event = cache.get(i);
                addMessage2Chat(event);
            }
        }
        //mRecyclerView.smoothScrollToPosition(mData.size() - 1);
    }
}
