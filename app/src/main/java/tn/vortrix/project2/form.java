package tn.vortrix.project2;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class form extends AppCompatActivity {
    EditText time, place, name;
    Button add, view, update, delete;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        myDb = new DatabaseHelper(this);
        time = findViewById(R.id.time);
        place = findViewById(R.id.place);
        name = findViewById(R.id.name);
        add = findViewById(R.id.send);
        view = findViewById(R.id.view);
        update = findViewById(R.id.update);
        delete = findViewById(R.id.delete);
        time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(form.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();


            }
        });
        addData();
        viewAll();
        editTask();
        deleteTask();

    }

    public void addData() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean inserted = myDb.insertData(name.getText().toString(), place.getText().toString(), time.getText().toString());
                if (inserted == true) {
                    Toast.makeText(form.this, "Task added", Toast.LENGTH_LONG);
                } else {
                    Toast.makeText(form.this, "Error occurred while adding task.", Toast.LENGTH_LONG);
                }
            }
        });
    }

    public void viewAll() {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDb.getAllData();
                if (res.getCount() == 0) {
                    showMessage("Error", "No Data Available");
                    return;
                }

                StringBuffer stringBuffer = new StringBuffer();
                while (res.moveToNext()) {
                    stringBuffer.append("Name :" + res.getString(0) + "\n");
                    stringBuffer.append("Place :" + res.getString(1) + "\n");
                    stringBuffer.append("Time :" + res.getString(2) + "\n\n");


                }
                showMessage("Tasks", stringBuffer.toString());
            }
        });
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true)
                .setTitle(title)
                .setMessage(message)
                .show();
    }

    public void editTask() {
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean updated = myDb.updateData(name.getText().toString(), place.getText().toString(), time.getText().toString());
                if (updated) {
                    Toast.makeText(form.this, "Task updated", Toast.LENGTH_LONG);
                } else {
                    Toast.makeText(form.this, "Error occurred while updating task.", Toast.LENGTH_LONG);
                }
            }
        });
    }

    public void deleteTask() {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int deleted = myDb.deleteData(name.getText().toString());
                if (deleted != 0) {
                    Toast.makeText(form.this, "Task deleted", Toast.LENGTH_LONG);
                } else {
                    Toast.makeText(form.this, "Error occurred while deleting task.", Toast.LENGTH_LONG);
                }
            }
        });
    }
}
