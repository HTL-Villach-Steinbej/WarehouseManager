package Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import Misc.Item;
import com.example.warehousemanager.R;

import java.util.ArrayList;

public class RemoveItemActivity extends AppCompatActivity {

    private ArrayList<Item> items;
    private ArrayAdapter<Item> adapter;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getIntent().getExtras();
        Intent intent = getIntent();
        setContentView(R.layout.activity_remove_item);
        items = (ArrayList<Item>) intent.getSerializableExtra("items");

        listView=findViewById(R.id.listview);
        adapter = new ArrayAdapter<Item>(this,
                android.R.layout.simple_list_item_1,
                items);
        listView.setAdapter(adapter);
    }
}