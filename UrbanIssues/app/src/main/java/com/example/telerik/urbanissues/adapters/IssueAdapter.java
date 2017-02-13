package com.example.telerik.urbanissues.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.telerik.urbanissues.R;
import com.example.telerik.urbanissues.models.BaseViewModel;
import com.example.telerik.urbanissues.models.Issue;
import com.telerik.everlive.sdk.core.model.system.User;
import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;


public class IssueAdapter extends ArrayAdapter<Issue> {
    private Context context;
    private int layoutResourceId;
    private List<Issue> issues;

    public IssueAdapter(Context context, int layoutResourceId, List<Issue> issues) {
        super(context, layoutResourceId, issues);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.issues = issues;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        IssueHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new IssueHolder();
            //holder.issueImage = (ImageView) row.findViewById(R.id.issue_image);
            holder.issueText = (TextView) row.findViewById(R.id.issue_content);
            //holder.username = (TextView) row.findViewById(R.id.issue_username);
            holder.issueCreateDate = (TextView) row.findViewById(R.id.issue_createDate);

            row.setTag(holder);
        } else {
            holder = (IssueHolder) row.getTag();
        }

        Issue issue = this.issues.get(position);
        if (issue != null) {
            holder.issueText.setText(issue.getDescription());
            DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
            holder.issueCreateDate.setText(dateFormat.format(issue.getCreatedAt()).toUpperCase());
            /*if (issue.getCreatedBy() != null) {
                BaseViewModel.myAppTest.workWith().users().getById(issue.getCreatedBy()).
                        executeAsync(new MyRequestResultCallbackAction(holder, ((Activity) context).getCurrentFocus()));
            } else {
                holder.username.setText("Anonymous");
            }
*/
        }

        return row;
    }

    static class IssueHolder {
        //private ImageView issueImage;
        private TextView issueText;
        //private TextView username;
        private TextView issueCreateDate;
    }

    class MyRequestResultCallbackAction extends RequestResultCallbackAction {
        private IssueHolder issueHolder;
        private View parentView;

        MyRequestResultCallbackAction(IssueHolder issueHolder, View parentView) {
            this.issueHolder = issueHolder;
            this.parentView = parentView;
        }

        @Override
        public void invoke(RequestResult requestResult) {
            /*if (requestResult.getSuccess()) {
                final User user = (User) requestResult.getValue();
                BaseViewModel.getInstance().addUser(user);
                final String userName = user.getDisplayName();
                this.parentView.post(new Runnable() {
                    @Override
                    public void run() {
                      issueHolder.username.setText(userName);
                    }
                });
            }*/
        }
    }
}
