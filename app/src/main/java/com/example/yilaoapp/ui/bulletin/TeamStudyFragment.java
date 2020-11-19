package com.example.yilaoapp.ui.bulletin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yilaoapp.R;
import com.example.yilaoapp.databinding.FragmentTeamStudyBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeamStudyFragment extends Fragment {

    public TeamStudyFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentTeamStudyBinding binding;
    private List<Team> teamList = new ArrayList<>();
    private BullentinViewModel viewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_team_study,container,false);
        viewModel = ViewModelProviders.of(requireActivity()).get(BullentinViewModel.class);
        binding.setData(viewModel);
        binding.setLifecycleOwner(requireActivity());
        initTeams();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.teamRecyclerview.setLayoutManager(layoutManager);
        TeamAdapter adapter = new TeamAdapter(teamList);
        binding.teamRecyclerview.setAdapter(adapter);

        //RecyclerView中没有item的监听事件，需要自己在适配器中写一个监听事件的接口。参数根据自定义
        adapter.setOnItemClickListener(new TeamAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, Team data) {
                new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull android.os.Message msg) {
                        //Toast.makeText(getActivity(),"我是item",Toast.LENGTH_SHORT).show();
                        NavController controller = Navigation.findNavController(view);
                        controller.navigate(R.id.action_bullentinFragment_to_teamDetailFragment);
                        viewModel.setTeam(data);
                        return false;
                    }
                }).sendEmptyMessageDelayed(0, 500);
            }
        });

        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_share_tools, container, false);
    }
    private void initTeams() {
        for (int i = 0; i < 2; i++) {
            Team t1 = new Team("数学建模竞赛","有计算机学院的朋友们想一起参加下学期" +
                    "的数模竞赛吗，这里有两名数科的同学，曾获数模奖项，有比赛经验","下午 6:00", R.drawable.head1);
            teamList.add(t1);
            Team t2 = new Team("大创","有小伙伴想组大创吗，这里有一名心理学院、一名光电学院"+
                    "的同学，已有导师，有丰富的项目经验","上午 12:00", R.drawable.head2);
            teamList.add(t2);
            Team t3 = new Team("打球","周六有小伙伴想一起去借场练习网球吗",
                    "下午14:00", R.drawable.head3);
            teamList.add(t3);
        }
    }
}