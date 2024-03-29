package com.example.test_music.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test_music.MainActivity;
import com.example.test_music.R;
import com.example.test_music.dao.SqlUtils;
import com.example.test_music.utils.SongList;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;

public class HomeFragment extends Fragment {


    private HomeViewModel homeViewModel;
    RecyclerView recyclerView;
    SongList songList;

    SongListAdapt songListAdapt;
    MainActivity mainActivity;
    AlertDialog.Builder builder;
    SqlUtils sql;
    int choice = -1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        sql = new SqlUtils(getActivity(), "Songlist.db", null, 1);

        recyclerView = root.findViewById(R.id.recycle_songname);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        mainActivity = (MainActivity) getActivity();
        songList = mainActivity.getSongList();
        songListAdapt = new SongListAdapt(songList.getSongList());
        recyclerView.setAdapter(songListAdapt);


        return root;

    }

//    //接收Evenbus传递来的信息
//    @Subscribe
//    public void onEvent(ArrayList<String> list) {
//        songList=list;
//
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//    }

    public class SongListAdapt extends RecyclerView.Adapter<SongListAdapt.ViewHolder> {
        ArrayList<String> list;

        public SongListAdapt(ArrayList<String> list) {
            this.list = list;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView text_list;
            Button bt_up;
            Button bt_down;
            Button bt_add;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                text_list = itemView.findViewById(R.id.list_text);
                bt_up = itemView.findViewById(R.id.button_up);
                bt_down = itemView.findViewById(R.id.button_down);
                bt_add = itemView.findViewById(R.id.button_collection);
                text_list.setClickable(true);

            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);
            ViewHolder holder = new ViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            final String songpath = songList.getSongList().get(position);
//            System.out.println("songNAME========" + songList.getSongList().get(position));
            String songName = songpath.replace("/storage/emulated/0/netease/cloudmusic/Music/", "");
            songName = songName.replace(".mp3", "");
            songName = songName.replace(".flac", "");
            holder.text_list.setText(songName);
            //点击播放歌曲
            holder.text_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainActivity.songList.changIndex(position);
                    String songName = mainActivity.songList.getIndexSongName();
                    mainActivity.play(songpath);
                    mainActivity.mediaPlayer.start();
                    mainActivity.bt_play_stop.setBackgroundResource(R.drawable.player_pause);
                }
            });
            //歌曲向上移动
            holder.bt_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainActivity.songList.moveSong(songList.getIndex(), songList.getIndex() - 1);
                    recyclerView.setAdapter(songListAdapt);
                }
            });

            //歌曲向下移动
            holder.bt_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainActivity.songList.moveSong(songList.getIndex(), songList.getIndex() + 1);
                    recyclerView.setAdapter(songListAdapt);
                }
            });


            //添加歌单
            holder.bt_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    final int[] choice = {-1};
                    ArrayList<String> arraylist = mainActivity.getList();
//                    final Object items = arraylist.toArray();
                    final String[] items = new String[arraylist.size()];
                    for (int i = 0; i < arraylist.size(); i++) {
                        System.out.println("所有的歌单名称:" + arraylist.get(i));
                        items[i] = arraylist.get(i);
                    }

//                    final String[] name = new String[1];

//                    builder = new AlertDialog.Builder(getActivity()).setIcon(R.mipmap.ic_launcher).setTitle("选择歌单")
//                            .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    choice[0] = i;
////
//                                    Toast.makeText(getActivity(), "你选择了" + items[choice[0]], Toast.LENGTH_LONG).show();
//                                }
//                            }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    if (choice[0] != -1) {
//                                        System.out.println("你点击保存了" + items[choice[0]] + " 选项为:" + choice[0]);
//                                        name[0] = items[choice[0]];
////                                        sql.insertValue(items[choice[0]], songpath);
//                                        Toast.makeText(getActivity(), "你选择了" + items[choice[0]], Toast.LENGTH_LONG).show();
//                                    }
//                                }
//                            });
//                    builder.create().show();

//                    final String[] items = {"单选1", "单选2", "单选3", "单选4", "单选5", "单选6"};
                    final int[] choice = {-1};
                    builder = new AlertDialog.Builder(getActivity()).setIcon(R.mipmap.ic_launcher).setTitle("单选列表")
                            .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    System.out.println("现在songlist是这个"+items[i]);
                                    choice[0] = i;
                                }
                            }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (choice[0] != -1) {
                                        System.out.println("选择的歌单为:"+items[choice[0]]);
                                        sql.insertValue(items[choice[0]], songpath);
                                        Toast.makeText(getContext(), "你选择了" + items[choice[0]], Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    builder.create().show();

//                    sql.insertValue(name[0], songpath);
//                    System.out.println("好像歌曲存进去了");
                }
            });


        }

        @Override
        public int getItemCount() {
            return songList.getSongList().size();
        }


    }
}
