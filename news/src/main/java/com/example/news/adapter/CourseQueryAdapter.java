package com.example.news.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.news.R;
import com.example.news.bean.CourseQueryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/25     18:53
 * QQ:         1981367757
 */

public class CourseQueryAdapter extends BaseAdapter{
    private List<CourseQueryBean.KbListBean>  courseList=new ArrayList<>();




    public void refreshData(List<CourseQueryBean.KbListBean> listBeans){
        courseList.clear();
        courseList.addAll(listBeans);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 48;
    }

    @Override
    public Object getItem(int i) {
        return courseList.get(i);
    }


    private String getSignFromPosition(int position) {
        switch (position /8) {
            case 0:
                return "1\n2";
            case 1:
                return "3\n4";
            case 2:
                return "5\n6";
            case 3:
                return "7\n8";
            case 4:
                return "9\n10";
            case 5:
                return "11\n12";
        }
        return null;
    }



//     case 0:
//             return "08:00\n1\n2\n09:35";
//            case 1:
//                    return "10:05\n3\n4\n11:40";
//            case 2:
//                    return "14:00\n5\n6\n15:35";
//            case 3:
//                    return "16:00\n7\n8\n17:35";
//            case 4:
//                    return "19:00\n9\n10\n20:35";
//            case 5:
//                    return "21:05\n11\n12\n22:40";



    List<CourseQueryBean.KbListBean> getRealItem(int position) {
        List<CourseQueryBean.KbListBean> result=null;
        if (courseList != null) {
            result=new ArrayList<>();
            int newPosition=position%8;
            for (CourseQueryBean.KbListBean classBean :
                    courseList) {
//                第几节   1-2
//                xqjmc  星期一
                String xqjmc=classBean.getXqjmc();
                int number=getNumber(xqjmc);
                if (number == newPosition) {
                    int start=(position/8)*2+1;
                    String jcor=classBean.getJcor();
                    String[] str=jcor.split("-");
                    int resultStart=Integer.parseInt(str[0]);
                    int end=Integer.parseInt(str[1]);
                    if (resultStart<=start&& end >= start +1) {
                        result.add(classBean);
                    }
                }
            }
        }
        return result;
    }

    private int getNumber(String xqjmc) {
        if (xqjmc != null) {
            switch (xqjmc){
                case "星期一":
                    return 1;
                case "星期二":
                    return 2;
                case "星期三":
                    return 3;
                case "星期四":
                    return 4;
                case "星期五":
                    return 5;
                case "星期六":
                    return 6;
                case "星期天":
                    return 7;
                    default:
                        return 7;
            }
        }
        return 7;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder=null;
        if (view == null) {
            holder=new Holder();
            View newView= LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_activity_course_query,viewGroup,false);
            holder.content=newView.findViewById(R.id.tv_item_activity_course_query_content);
            view=newView;
            view.setTag(holder);
        }else {
            holder= (Holder) view.getTag();
        }
        if (i%8== 0) {
            holder.content.setText(getSignFromPosition(i));
        }else {
            List<CourseQueryBean.KbListBean> listBeans=getRealItem(i);
            StringBuilder result=new StringBuilder();
            if (listBeans != null) {
                int size=listBeans.size();
                for (int j = 0; j < size; j++) {
                    CourseQueryBean.KbListBean bean=listBeans.get(j);
                    if (j != 0) {
                        result.append("\n");
                    }
                    result.append(bean.getKcmc())
                            .append("@").append(bean.getCdmc());
                }
            }
            holder.content.setText(result.toString());
        }
        return view;
    }




    private static class Holder{
        TextView content;
    }
}
