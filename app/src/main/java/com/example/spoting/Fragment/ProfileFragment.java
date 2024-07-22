package com.example.spoting.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.spoting.FirstAuthActivity;
import com.example.spoting.R;
import com.example.spoting.SaveSharedPreference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);
        Context context = getContext();
        TextView userNameView = rootView.findViewById(R.id.user_name);

        userNameView.setText(SaveSharedPreference.getUserName(context));
        return rootView;

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 프로필 화면의 로그아웃 버튼을 찾아냅니다.
        TextView profileLogoutButton = view.findViewById(R.id.profilefragment_logout);

        // 로그아웃 버튼을 클릭했을 때의 이벤트 처리
        profileLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(); // 로그아웃 메소드 호출
            }
        });
    }
    private void logout() {
        // SharedPreferences에서 사용자 정보를 제거합니다.
        SaveSharedPreference.clearUserInfo(getContext());

        // 자동 로그인 체크 부분을 수정하여, 로그인 화면으로 이동하도록 변경합니다.
        Intent intent = new Intent(getContext(), FirstAuthActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}