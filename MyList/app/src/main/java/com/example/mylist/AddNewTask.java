package com.example.mylist;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mylist.databinding.ActivityMainBinding;
import com.example.mylist.databinding.AddTaskLayoutBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.awt.font.TextAttribute;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

//class for the bottom dialog option to enter tasks
public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "AddNewTask";

    AddTaskLayoutBinding binding;

    private String dueDate = " ";

    private Context context;

    //firebase
    private FirebaseFirestore firestore;


    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //view binding
        binding = AddTaskLayoutBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firestore = FirebaseFirestore.getInstance();

        binding.etTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals(""))
                {
                    binding.btnSave.setEnabled(false);
                }
                else
                {
                    binding.btnSave.setEnabled(true);
                    binding.btnSave.setTextColor(getResources().getColor(R.color.secondary));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.tvSetDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int MONTH = calendar.get(Calendar.MONTH);
                int YEAR = calendar.get(Calendar.YEAR);
                int DAY = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;

                        dueDate = dayOfMonth+"/"+month+"/"+year;

                        binding.tvSetDueDate.setText(dueDate);
                    }
            }, YEAR, MONTH, DAY);
                datePickerDialog.show();
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = binding.etTaskText.getText().toString();

                if(task.isEmpty()){
                    Toast.makeText(context,"No task entered",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Map<String,Object> taskMap = new HashMap<>();
                    taskMap.put("task",task);
                    taskMap.put("due",dueDate);
                    taskMap.put("status",0);

                    firestore.collection("task").add(taskMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(context,"Task saved",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(context,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                dismiss();
            }
        });
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListener){
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }
    }
}
