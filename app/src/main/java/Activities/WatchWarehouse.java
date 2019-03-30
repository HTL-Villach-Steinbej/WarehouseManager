package Activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.warehousemanager.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;
import androidx.appcompat.widget.AppCompatButton;

public class WatchWarehouse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_warehouse);

        final GridLayout gridLayout = (GridLayout)findViewById(R.id.gridLayout);

        Button addPasswordButton = (Button)findViewById(R.id.btnAddRegal);
        addPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getApplicationContext();
                int childCount = gridLayout.getChildCount();

                AppCompatButton btnRegal = new AppCompatButton(context);
                btnRegal.setText("Regal" + (childCount + 1));
                gridLayout.addView(btnRegal, childCount);

            }
        });
    }
}
