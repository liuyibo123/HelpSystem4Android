package com.upc.help_system.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.upc.help_system.R;

public class ConversitionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversition);
        EaseConversationListFragment conversationListFragment = new EaseConversationListFragment();
        conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {

            @Override
            public void onListItemClicked(EMConversation conversation) {
                startActivity(new Intent(ConversitionActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.getLastMessage().getUserName()));
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.conversition_fragment, conversationListFragment).commit();
    }

}
