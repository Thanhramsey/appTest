package com.vnpt.staffhddtpos58;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vnpt.common.ModelEvent;
import com.vnpt.staffhddtpos58.fragment.BaseFragment;
import com.vnpt.webservice.AppServices;

public class StatictisFragment extends BaseFragment {
    public static String TAG = StatictisFragment.class.getName();

    TextView txtCount;
    Button btnXem;
    EditText edtUser;

    AppServices appServices;

    public StatictisFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appServices = new AppServices(getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_statictis, container, false);
        init(layout);

        setValueForMembers();
        setEventForMembers();
        return layout;
    }

    @Override
    protected void init(View layout) {
        txtCount = layout.findViewById(R.id.txtCount);
        btnXem = layout.findViewById(R.id.btnXem);
        edtUser = layout.findViewById(R.id.edtUser);
    }

    @Override
    protected void setValueForMembers() {
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void setEventForMembers() {
        btnXem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = edtUser.getText().toString().trim();
                if (!TextUtils.isEmpty(user)) {
                    ((MainActivity) getActivity()).showProccessbar(true);
                    new GetListInvTask().execute(user);
                } else {
                    Toast.makeText(getContext(), "Vui lòng nhập tên User", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void handleModelViewEvent(ModelEvent modelEvent) {

    }

    @Override
    public void handleErrorModelViewEvent(ModelEvent modelEvent) {

        ((MainActivity) getActivity()).showProccessbar(false);

    }

    class GetListInvTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {
            return appServices.getTotalInvCurrentDate(getContext(), strings[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            ((MainActivity) getActivity()).showProccessbar(false);
            txtCount.setText(integer + "");

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            ((MainActivity) getActivity()).showProccessbar(false);
        }
    }
}
