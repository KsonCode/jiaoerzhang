package com.jiaoerzhang.platform.adapter.user;import android.content.Context;import android.support.annotation.Nullable;import com.chad.library.adapter.base.BaseQuickAdapter;import com.chad.library.adapter.base.BaseViewHolder;import com.jiaoerzhang.platform.R;import com.jiaoerzhang.platform.entity.user.MessageEntity;import com.jiaoerzhang.platform.ui.activity.user.MessageActivity;import com.jiaoerzhang.platform.utils.TimeUtils;import java.text.SimpleDateFormat;import java.util.Date;import java.util.List;public class MessagesAdapter extends BaseQuickAdapter<MessageEntity.Content, BaseViewHolder> {    private Context context;    public MessagesAdapter(Context context, int layoutResId, @Nullable List<MessageEntity.Content> data) {        super(layoutResId, data);        this.context = context;    }    @Override    protected void convert(BaseViewHolder helper, final MessageEntity.Content item) {        helper.setText(R.id.msgName,item.getContent()+"");        helper.setText(R.id.msgTime, TimeUtils.getDate(item.getEventTime()+"","MM/dd/yyyy"));    }}