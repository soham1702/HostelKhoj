package com.example.hostelkhoj;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    RecyclerView recycler;
    Adapter adapter;
    ArrayList<String> hnames,onames,types;
    Query query;
    FirestoreRecyclerOptions<HostelModel> hostels;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference user = db.collection("users");
    private LayoutInflater layoutInflater;
    //FirestoreRecyclerAdapter adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        setUpRecycler();
       /* query= FirebaseFirestore.getInstance().collection("users").limit(50);
        hostels=new FirestoreRecyclerOptions.Builder<HostelModel>().setQuery(query,HostelModel.class).build();
        adapter1=new FirestoreRecyclerAdapter<HostelModel, Adapter.ViewHolder>(hostels) {
            @Override
            protected void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position, @NonNull HostelModel model) {
                String hname=model.getHostelname();
                Log.d("TAG","hostel name:"+hname);
                holder.hostelname.setText(hname);

                String oname=model.getOwnername();
                holder.ownername.setText(oname);

                String type=model.type;
                holder.hosteltype.setText(type);
            }


            @NonNull
            @Override
            public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.custom_layout, parent, false);
                return new Adapter.ViewHolder(view);
            }
        };
        recycler=findViewById(R.id.recyclerview);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter1);


       /* hnames=new ArrayList<>();
        onames=new ArrayList<>();
        types=new ArrayList<>();

        hnames.add("SOHAM HOSTEL");
        hnames.add("SHREYAS HOSTEL");
        hnames.add("ATHARVA HOSTEL");
        hnames.add("SAYALI HOSTEL");
        hnames.add("SHREYA HOSTEL");

        onames.add("soham");
        onames.add("shreyas");
        onames.add("atharva");
        onames.add("sayali");
        onames.add("shreya");

        types.add("boys");
        types.add("boys");
        types.add("girls");
        types.add("coed");
        types.add("coed");

        adapter=new Adapter(this,hnames,onames,types);
        recycler.setAdapter(adapter);

        */
    }

    private void setUpRecycler() {

        query=user;
        hostels= new FirestoreRecyclerOptions.Builder<HostelModel>()
                .setQuery(query, HostelModel.class)
                .build();
        adapter = new Adapter(hostels);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot snapshot, int position) {
                HostelModel hostel = snapshot.toObject(HostelModel.class);
                String id = snapshot.getId();
                String path = snapshot.getReference().getPath();
                Log.d("TAG","path= "+path);
                Intent i= new Intent(HomePage.this,DetailsActivity.class);
                i.putExtra("user",id);
                i.putExtra("path",path);
                startActivity(i);

                //Toast.makeText(HomePage.this, "Position: " + position + " ID: " + id+ " path : "+path, Toast.LENGTH_SHORT).show();

            }


        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
