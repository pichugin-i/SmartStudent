package com.example.studentapp.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.studentapp.MainActivity;
import com.example.studentapp.R;
import com.example.studentapp.adapters.SubjectAdapter;
import com.example.studentapp.adapters.SubjectPlanAdapter;
import com.example.studentapp.al.PlanToDay;
import com.example.studentapp.al.PlanToSub;
import com.example.studentapp.databinding.FragmentCalendarBinding;
import com.example.studentapp.db.ApiInterface;
import com.example.studentapp.db.ServiceBuilder;
import com.example.studentapp.db.Subjects;
import com.example.studentapp.db.Users;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CalendarFragment extends Fragment {

    FragmentCalendarBinding binding;
    ApiInterface apiInterface;
    LocalDate localDate;
    SubjectPlanAdapter.OnItemClick itemClick;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.updateDBTime();
        localDate = LocalDate.now();
        itemClick = new SubjectPlanAdapter.OnItemClick() {
            @Override
            public void onClickPlanItem(PlanToSub subject, int position) {
                CalendarFragmentDirections.ActionCalendarFragmentToAnswerQuestionFragment action = CalendarFragmentDirections.actionCalendarFragmentToAnswerQuestionFragment(subject.getSub().getNameOfSubme());
                Navigation.findNavController(getView()).navigate(action);

            }
        };
        getSubjs();


        binding.chooseDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView,  int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                localDate =  myCalendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                getSubjs();
            }
        });
    }

    private boolean isNetworkWorking(){
        try {
            ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if (manager != null){
                networkInfo = manager.getActiveNetworkInfo();
            }
            return networkInfo != null && networkInfo.isConnected();
        }catch (NullPointerException ex){
            return false;
        }
    }

    private void getSubjs(){
        ArrayList<PlanToSub> subjs =  MainActivity.myDBManager.getFromDB();

        ArrayList<PlanToSub> subToDayCalendar = new ArrayList<>();

        for (int i = 0; i < subjs.size();i++){
            for (PlanToDay p: subjs.get(i).getFuturePlan()) {
                if(p.getDate().isEqual(localDate)) subToDayCalendar.add(subjs.get(i));
            }
            for (PlanToDay p: subjs.get(i).getLastPlan()) {
                if(p.getDate().isEqual(localDate)) subToDayCalendar.add(subjs.get(i));
            }
            if(subjs.get(i).getDateOfExams().isEqual(localDate))  subToDayCalendar.add(subjs.get(i));
        }
        binding.listSub.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.listSub.setHasFixedSize(true);
        binding.listSub.setAdapter(new SubjectPlanAdapter(getContext(), subToDayCalendar, localDate, itemClick));
        if(subjs.size() == 0) {
            binding.textPlanNull.setVisibility(View.VISIBLE);
        } else {
            binding.textPlanNull.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_calendar, container, false);
        Paper.init(getContext());
        apiInterface = ServiceBuilder.buildRequest().create(ApiInterface.class);
        return binding.getRoot();
    }
}