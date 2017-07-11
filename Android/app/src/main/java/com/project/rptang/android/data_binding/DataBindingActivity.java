package com.project.rptang.android.data_binding;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.project.rptang.android.R;
import com.project.rptang.android.databinding.ActivityDataBindingBinding;

public class DataBindingActivity extends AppCompatActivity {

    private ActivityDataBindingBinding binding;
    private Employee employee = new Employee("A","B");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_data_binding);
        binding.setEmployee(employee);
        binding.setPresenter(new Presenter());
    }

    public class Employee{
        public String firstName;
        public String lastName;

        public Employee(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
    }

    public class Presenter{
        public void onTextChanged(CharSequence s,int start,int before,int count){
            employee.setFirstName(s.toString());
            binding.setEmployee(employee);
        }

        public void onClick(View view){
            Toast.makeText(DataBindingActivity.this,"点到了",Toast.LENGTH_SHORT).show();
        }

        public void onClickListenerEmployee(Employee employee){
            Toast.makeText(DataBindingActivity.this,employee.getLastName(),Toast.LENGTH_SHORT).show();
        }
    }
}
