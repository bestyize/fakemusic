package com.yize.fakemusic.download;



import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yize.fakemusic.R;

import java.sql.Date;
import java.util.List;

public class DownloadListAdapter extends RecyclerView.Adapter<DownloadListAdapter.ViewHolder> {
    private List<DownloadInfo> downloadInfoList;
    private Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(context==null){
            context=viewGroup.getContext();
        }
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_download_list,viewGroup,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        DownloadInfo downloadInfo=downloadInfoList.get(i);
        viewHolder.tv_file_name.setText(downloadInfo.getFileName());
        viewHolder.tv_file_size.setText(String.valueOf(downloadInfo.getFileLength()));
        viewHolder.tv_file_time.setText(new Date(downloadInfo.getLastModifiedTime()).toString());
        viewHolder.btn_download_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"暂停",Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.btn_cancel_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadInfoList.remove(i);
                notifyItemRemoved(i);
                Toast.makeText(context,"取消",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return downloadInfoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_file_name,tv_file_time,tv_file_size;
        Button btn_download_pause,btn_cancel_download;

        public ViewHolder(View view) {
            super(view);
            tv_file_name=(TextView)view.findViewById(R.id.tv_file_name);
            tv_file_time=(TextView)view.findViewById(R.id.tv_file_time);
            tv_file_size=(TextView)view.findViewById(R.id.tv_file_size);
            btn_download_pause=(Button)view.findViewById(R.id.btn_pause_download);
            btn_cancel_download=(Button)view.findViewById(R.id.btn_cancel_download);
        }
    }


    public DownloadListAdapter(List<DownloadInfo> downloadInfoList){
        this.downloadInfoList=downloadInfoList;

    }

    /**
     * 向下载列表中添加任务
     * @param downloadInfo
     */
    public void addTaskToDownloadList(DownloadInfo downloadInfo){
        int position=downloadInfoList.size();
        downloadInfoList.add(position,downloadInfo);
        notifyItemInserted(position);
        noticeUser("任务已添加");
    }

    /**
     * 向下载列表中添加任务
     * @param position
     * @param downloadInfo
     */
    public void addTaskToDownloadList(int position,DownloadInfo downloadInfo){
        if(position>downloadInfoList.size()){
            noticeUser("插入位置错误");
            return;
        }
        downloadInfoList.add(position,downloadInfo);
        notifyItemInserted(position);
        noticeUser("任务已添加");
    }

    /**
     * 删除任务
     * @param position
     */
    public void removeTaskFromList(int position){
        downloadInfoList.remove(position);
        notifyDataSetChanged();
        noticeUser("任务已删除");
    }


    /**
     * 提醒用户
     * @param notice
     */
    protected void noticeUser(String notice){
        Toast.makeText(context,notice,Toast.LENGTH_SHORT).show();
    }







}
