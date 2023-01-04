package com.example.myapplication;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

    public class OptionsMenuActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater myMenuInflater = getMenuInflater();
        myMenuInflater.inflate(R.menu.menu_all, menu);

        return true;
}

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.explore:
                Toast.makeText(this, "explore was chosen", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.actionAdd:
                Intent intent = new Intent(getApplicationContext(), Crud.class);
                intent.putExtra("metodo", "agregar" );
                startActivity(intent);
                return true;
            case R.id.account:
                Toast.makeText(this, "account was chosen", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.my_places:
                Intent intent1 = new Intent(getApplicationContext(), FormActivity.class);
                startActivity(intent1);
                return true;
            case R.id.my_location:
                Toast.makeText(this, "my location was chosen", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
