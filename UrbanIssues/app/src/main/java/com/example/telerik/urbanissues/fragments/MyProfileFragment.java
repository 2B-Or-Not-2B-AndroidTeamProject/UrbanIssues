package com.example.telerik.urbanissues.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.telerik.urbanissues.R;
import com.example.telerik.urbanissues.models.BaseViewModel;
import com.example.telerik.urbanissues.models.ImageKind;
import com.example.telerik.urbanissues.models.MyUser;
import com.example.telerik.urbanissues.tasks.BitmapDownloadTask;
import com.telerik.everlive.sdk.core.EverliveApp;
import com.telerik.everlive.sdk.core.model.system.User;
import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;

import static com.example.telerik.urbanissues.activities.MainActivity.urbanIssuesApp;

public class MyProfileFragment extends Fragment implements View.OnClickListener{

    private Button updatePictureButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_my_profile, container, false);

        this.updatePictureButton = (Button) rootView.findViewById(R.id.profile_update_picture_btn);
        this.updatePictureButton.setOnClickListener(this);

        this.updateUI(rootView);
        return rootView;
    }

    private void updateUI(View view){

        final MyUser loggedUser = BaseViewModel.getInstance().getLoggedUser();
        if (loggedUser == null) {
           urbanIssuesApp.workWith().
                    users(MyUser.class).
                    getMe().
                    executeAsync(new RequestResultCallbackAction<MyUser>() {
                        @Override
                        public void invoke(RequestResult<MyUser> requestResult) {
                            if (requestResult.getSuccess()) {
                                MyUser loggedUser = requestResult.getValue();
                                BaseViewModel.getInstance().setLoggedUser(loggedUser);

                                MyProfileFragment.this.updatePictureButton.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        MyProfileFragment.this.updatePictureButton.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        }
                    });
        } else {
            this.updatePictureButton.post(new Runnable() {
                @Override
                public void run() {
                    updatePictureButton.setVisibility(View.VISIBLE);
                }
            });
        }
        BitmapDownloadTask task = new BitmapDownloadTask(view.getContext(), (ImageView) view.findViewById(R.id.profile_picture), ImageKind.User);
        task.execute(loggedUser.getPictureId() != null ? loggedUser.getPictureId().toString() : null);

        ((TextView) view.findViewById(R.id.profile_username)).setText(loggedUser.getUsername());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_update_picture_btn : {

            }
        }
    }
}
